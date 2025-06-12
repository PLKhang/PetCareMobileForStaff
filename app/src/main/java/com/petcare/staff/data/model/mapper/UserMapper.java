package com.petcare.staff.data.model.mapper;

import com.petcare.staff.data.model.api.user.ChangeInfoRequest;
import com.petcare.staff.data.model.api.user.ChangeInfoResponse;
import com.petcare.staff.data.model.api.user.ChangePasswordRequest;
import com.petcare.staff.data.model.api.user.ChangePasswordResponse;
import com.petcare.staff.data.model.api.user.CreateCustomerRequest;
import com.petcare.staff.data.model.api.user.CustomerResponse;
import com.petcare.staff.data.model.api.user.UserByIdResponse;
import com.petcare.staff.data.model.api.user.UserResponse;
import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.data.model.ui.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static User toUser(UserResponse response) {
        return new User(
                String.valueOf(response.getId()),
                response.getName(),
                response.getEmail(),
                response.getAddress(),
                response.getPhoneNumber(),
                String.valueOf(response.getBranchId())
        );
    }
    public static User fromUserByIdResponseToUser(UserByIdResponse response) {
        return new User (
            String.valueOf(response.getId()),
                response.getName(),
                response.getEmail(),
                response.getAddress(),
                response.getPhoneNumber(),
                "-1"
        );
    }
    public static Customer toCustomer(CustomerResponse response) {
        return new Customer(
                String.valueOf(response.getUserId()),
                response.getName(),
                response.getEmail(),
                response.getAddress(),
                response.getPhoneNumber()
        );
    }

    public static List<Customer> toListCustomer(List<CustomerResponse> responseList) {
        List<Customer> customers = new ArrayList<>();
        for (CustomerResponse response : responseList) {
            customers.add(toCustomer(response));
        }
        return customers;
    }

    public static ChangeInfoRequest toChangeInfoRequest(User user){
        return new ChangeInfoRequest(
                user.getAddress(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber()
        );
    }

    public static ChangePasswordRequest toChangePasswordRequest(String oldPassword, String newPassword) {
        return new ChangePasswordRequest(
                oldPassword,
                newPassword
        );
    }

    public static CreateCustomerRequest toCreateCustomerRequest(Customer record) {
        return new CreateCustomerRequest(
                record.getEmail(),
                record.getName(),
                record.getPhoneNumber()
        );
    }
}
