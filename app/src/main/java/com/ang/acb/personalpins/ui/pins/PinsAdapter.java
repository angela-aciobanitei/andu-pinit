package com.ang.acb.personalpins.ui.pins;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.personalpins.data.entitiy.Pin;
import com.ang.acb.personalpins.databinding.PinItemBinding;

import java.util.List;

public class PinsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Pin> pins;
    private PinClickCallback clickCallback;

    PinsAdapter(PinClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout and get an instance of the binding class.
        PinItemBinding itemBinding = PinItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),  parent, false);
        return new PinViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind item data
        Pin pin = pins.get(position);
        ((PinViewHolder) holder).bindTo(pin);

        // Handle item click events
        holder.itemView.setOnClickListener(v -> {
            if (pin != null && clickCallback != null) {
                clickCallback.onClick(pin);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pins == null ? 0 :  pins.size();
    }

    public void submitList(List<Pin> pins) {
        this.pins = pins;
        // Notify any registered observers that the data set has changed.
        notifyDataSetChanged();
    }

    public interface PinClickCallback {
        void onClick(Pin pin);
    }

    class PinViewHolder extends RecyclerView.ViewHolder {

        private PinItemBinding binding;

        // Required constructor matching super
        PinViewHolder(@NonNull PinItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        void bindTo(Pin pin) {
            // Bind data for this item.
            binding.setPin(pin);

            // Binding must be executed immediately.
            binding.executePendingBindings();
        }
    }
}