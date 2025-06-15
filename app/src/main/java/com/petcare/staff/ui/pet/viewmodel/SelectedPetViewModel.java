package com.petcare.staff.ui.pet.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.petcare.staff.data.model.ui.Pet;

public class SelectedPetViewModel extends ViewModel {
    private final MutableLiveData<Pet> selectedPet = new MutableLiveData<>();

    public SelectedPetViewModel() {
    }

    public LiveData<Pet> getSelectedPet() {
        return selectedPet;
    }

    public void setSelectedPet(Pet pet) {
        this.selectedPet.setValue(pet);
    }

    public void clear() {
        this.selectedPet.setValue(null);
    }

}
