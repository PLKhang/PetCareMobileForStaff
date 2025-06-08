package com.petcare.staff.ui.appointment.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedAppointmentViewModel extends ViewModel {
    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<String> selectedTime = new MutableLiveData<>();
    private final MutableLiveData<String> address = new MutableLiveData<>();
    private final MutableLiveData<Float> total = new MutableLiveData<Float>();

    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String date) {
        selectedDate.setValue(date);
    }

    public LiveData<String> getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String time) {
        selectedTime.setValue(time);
    }

    public LiveData<String> getAddress() {
        return address;
    }

    public void setAddress(String value) {
        address.setValue(value);
    }

    public MutableLiveData<Float> getTotal() {
        return total;
    }

    public void setTotal(float total)
    {
        this.total.setValue(total);
    }
}
