package com.petcare.staff.ui.appointment.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.petcare.staff.data.model.ui.Appointment;
import com.petcare.staff.data.model.ui.Service;

public class SharedAppointmentViewModel extends ViewModel {
    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<String> selectedTime = new MutableLiveData<>();
    private final MutableLiveData<String> address = new MutableLiveData<>();
    private final MutableLiveData<Float> total = new MutableLiveData<Float>();
    private MutableLiveData<Appointment> appointmentLiveData = new MutableLiveData<>();

    public LiveData<Appointment> getAppointmentLiveData() {
        return appointmentLiveData;
    }

    public void setAppointmentLiveData(Appointment appointment) {
        this.appointmentLiveData.setValue(appointment);
    }

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

    public void resetAppointmentLiveData(){
        this.appointmentLiveData.setValue(null);
    }
}
