package com.ang.acb.personalpins.ui.pins;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ang.acb.personalpins.data.entity.Pin;
import com.ang.acb.personalpins.data.repository.PinRepository;

import java.util.List;

import javax.inject.Inject;

public class FavoritePinsViewModel extends ViewModel {

    private PinRepository pinRepository;
    private LiveData<List<Pin>> favoritePins;

    @Inject
    public FavoritePinsViewModel(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    public LiveData<List<Pin>> getFavoritePins() {
        if (favoritePins == null) {
            favoritePins = pinRepository.getAllFavoritePins();
        }
        return favoritePins;
    }
}
