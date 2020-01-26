package com.ang.acb.personalpins.ui.boards;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ang.acb.personalpins.R;
import com.ang.acb.personalpins.data.entity.Pin;
import com.ang.acb.personalpins.databinding.FragmentBoardDetailsBinding;
import com.ang.acb.personalpins.ui.common.MainActivity;
import com.ang.acb.personalpins.ui.pins.PinsAdapter;
import com.ang.acb.personalpins.ui.widget.BoardWidgetProvider;
import com.ang.acb.personalpins.ui.widget.PreferencesUtils;
import com.ang.acb.personalpins.utils.GridMarginDecoration;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static androidx.navigation.fragment.NavHostFragment.findNavController;


public class BoardDetailsFragment extends Fragment {

    private FragmentBoardDetailsBinding binding;
    private BoardDetailsViewModel boardDetailsViewModel;
    private PinsAdapter pinsAdapter;
    private long boardId;
    private String boardTitle;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public BoardDetailsFragment() {}

    @Override
    public void onAttach(@NotNull Context context) {
        // When using Dagger with Fragments, inject as early as possible.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Extract arguments sent via Safe Args.
        if (getArguments() != null) {
            boardId = BoardDetailsFragmentArgs.fromBundle(getArguments()).getBoardId();
            boardTitle = BoardDetailsFragmentArgs.fromBundle(getArguments()).getBoardTitle();
        }

        // Report that this fragment would like to populate the options menu.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get an instance of the binding class.
        binding = FragmentBoardDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViewModel();
        initAdapter();
        observeBoard();
        observePins();
        onAddPins();
    }

    private void initViewModel() {
        boardDetailsViewModel = new ViewModelProvider(this, viewModelFactory)
                .get(BoardDetailsViewModel.class);
        boardDetailsViewModel.setBoardId(boardId);
    }

    private void initAdapter() {
        pinsAdapter = new PinsAdapter(this::onPinClick);
        binding.boardPins.rv.setAdapter(pinsAdapter);
        binding.boardPins.rv.setLayoutManager(new GridLayoutManager(
                getHostActivity(), getResources().getInteger(R.integer.columns_count)));
        binding.boardPins.rv.addItemDecoration(new GridMarginDecoration(
                getHostActivity(), R.dimen.grid_item_spacing));
    }

    private void onPinClick(Pin pin, ImageView sharedImage) {
        // On item click navigate to pin details fragment.
        BoardDetailsFragmentDirections.ActionBoardDetailsToPinDetails action =
                BoardDetailsFragmentDirections.actionBoardDetailsToPinDetails();
        action.setPinId(pin.getId());
        if (pin.getPhotoUri() != null && sharedImage != null) {
            // This is a photo pin.
            action.setIsPhoto(true);
            // Create the shared element transition extras.
            FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(sharedImage, sharedImage.getTransitionName())
                    .build();
            findNavController(this).navigate(action, extras);
        } else {
            // This is a video pin, there are no shared element transition extras.
            action.setIsPhoto(false);
            findNavController(this).navigate(action);
        }
    }

    private void observeBoard() {
        boardDetailsViewModel.getBoard().observe(getViewLifecycleOwner(), board -> {
            if (board != null) {
                ActionBar actionBar = getHostActivity().getSupportActionBar();
                if (actionBar != null) actionBar.setTitle(board.getTitle());
            }
        });
    }

    private void observePins() {
        boardDetailsViewModel.getPinsForBoard().observe(getViewLifecycleOwner(), pins -> {
            int boardPinsCount = (pins == null) ? 0 : pins.size();
            binding.setBoardPinsCount(boardPinsCount);

            if(boardPinsCount != 0) pinsAdapter.submitList(pins);
            else binding.boardPins.tv.setText(R.string.no_board_pins);

            binding.executePendingBindings();
        });
    }

    private void onAddPins() {
        binding.addPinsButton.setOnClickListener(view -> {
            // Navigate to select pins fragment and pass the board ID as bundle arg.
            BoardDetailsFragmentDirections.ActionBoardDetailsToSelectPins action =
                    BoardDetailsFragmentDirections.actionBoardDetailsToSelectPins();
            action.setBoardId(boardId);
            findNavController(this).navigate(action);
        });
    }

    private MainActivity getHostActivity(){
        return  (MainActivity) getActivity();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.board_details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_create_widget:
                addWidgetToHomeScreen(boardId, boardTitle);
                return true;
            case R.id.action_delete_board:
                deleteBoard(boardId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addWidgetToHomeScreen(long boardId, String boardTitle) {
        PreferencesUtils.setWidgetTitle(getContext(), boardTitle);
        PreferencesUtils.setWidgetBoardId(getContext(), boardId);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(getHostActivity(), BoardWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(
                appWidgetIds, R.id.widget_board_pins_lv);
        BoardWidgetProvider.updateRecipeWidget(
                getContext(), appWidgetManager, appWidgetIds);
    }

    private void deleteBoard(long boardId) {
        boardDetailsViewModel.deleteBoard(boardId);
        // Navigate back to pin list fragment.
        findNavController(this).popBackStack(R.id.boards, false);
    }
}
