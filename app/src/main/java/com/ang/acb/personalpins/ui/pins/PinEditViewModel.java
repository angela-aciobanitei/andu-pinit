package com.ang.acb.personalpins.ui.pins;

import androidx.lifecycle.ViewModel;

import com.ang.acb.personalpins.data.entity.Pin;
import com.ang.acb.personalpins.data.repository.PinRepository;

import javax.inject.Inject;

public class PinEditViewModel extends ViewModel {

    private PinRepository pinRepository;

    @Inject
    public PinEditViewModel(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    public void createPin(Pin pin) {
        pinRepository.insertPin(pin);
    }
}
