package com.petcare.staff.ui.customer.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.ui.customer.CustomerSelectionMode;

public class SelectedCustomerViewModel extends ViewModel {
    private final MutableLiveData<Customer> selectedCustomer = new MutableLiveData<>();
    private CustomerSelectionMode selectionMode = CustomerSelectionMode.VIEW_DETAIL;


    //To-do: check đang mở để chọn khách hàng làm gì?
    /*
     *   case1: để chọn khách hàng xem chi tiết thông tin: toCustomerDetailFragment
     *   case2: để chọn khách hàng khi tạo hóa đơn: returnToCreateBillFragment
     *   case3: để chọn khách hàng khi tạo cuộc hẹn: returnToCreateAppointmentFragment
     *
     */

    public SelectedCustomerViewModel(){

    }
    public void resetSelectedCustomer() {
        selectedCustomer.setValue(null);
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer.getValue();
    }

    public void setSelectedCustomer(Customer customer) {
        this.selectedCustomer.setValue(customer);
    }

    public CustomerSelectionMode getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(CustomerSelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }
}
