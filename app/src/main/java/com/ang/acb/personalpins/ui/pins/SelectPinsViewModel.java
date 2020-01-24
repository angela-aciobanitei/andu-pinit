package com.ang.acb.personalpins.ui.pins;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.personalpins.R;
import com.ang.acb.personalpins.data.entity.Pin;
import com.ang.acb.personalpins.data.repository.BoardRepository;
import com.ang.acb.personalpins.data.repository.PinRepository;
import com.ang.acb.personalpins.utils.SnackbarMessage;

import java.util.List;

import javax.inject.Inject;

public class SelectPinsViewModel extends ViewModel {

    private BoardRepository boardRepository;
    private PinRepository pinRepository;
    private final MutableLiveData<Long> boardId = new MutableLiveData<>();
    private final SnackbarMessage snackbarMessage = new SnackbarMessage();
    private LiveData<List<Pin>> pinsForBoard;
    private LiveData<List<Pin>> allPins;

    @Inject
    public SelectPinsViewModel(BoardRepository boardRepository, PinRepository pinRepository) {
        this.boardRepository = boardRepository;
        this.pinRepository = pinRepository;
    }

    public void setBoardId(long id) {
        boardId.setValue(id);
    }

    public LiveData<List<Pin>> getPinsForBoard() {
        if (pinsForBoard == null) {
            pinsForBoard = Transformations.switchMap(boardId, id ->
                    boardRepository.getPinsForBoard(id));
        }
        return  pinsForBoard;
    }

    public LiveData<List<Pin>> getAllPins() {
        if (allPins == null) {
            allPins = pinRepository.getAllPins();
        }
        return allPins;
    }

    public SnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }

    public void onPinChecked(long boardId, long pinId) {
        pinRepository.insertBoardPin(boardId, pinId);
        snackbarMessage.setValue(R.string.pin_added_to_board);
    }

    public void onPinUnchecked(long boardId, long pinId) {
        pinRepository.deleteBoardPin(boardId, pinId);
        snackbarMessage.setValue(R.string.pin_removed_from_board);
    }
}
