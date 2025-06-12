package com.petcare.staff.data.model.mapper;

import android.content.Context;
import android.util.Log;

import com.petcare.staff.data.model.api.appointment.AppointmentDetailResponse;
import com.petcare.staff.data.model.api.appointment.AppointmentResponse;
import com.petcare.staff.data.model.api.appointment.AppointmentServiceDetail;
import com.petcare.staff.data.model.api.appointment.AppointmentStatus;
import com.petcare.staff.data.model.api.appointment.CreateAppointmentRequest;
import com.petcare.staff.data.model.api.appointment.OrderItem;
import com.petcare.staff.data.model.api.appointment.ServiceResponse;
import com.petcare.staff.data.model.api.appointment.UpdateAppointmentEmployeeRequest;
import com.petcare.staff.data.model.api.appointment.UpdateAppointmentStatusRequest;
import com.petcare.staff.data.model.ui.Appointment;
import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.data.model.ui.Product;
import com.petcare.staff.data.model.ui.Service;
import com.petcare.staff.data.model.ui.SimplifiedAppointment;
import com.petcare.staff.utils.DateTime;
import com.petcare.staff.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentMapper {
    public static Service toService(ServiceResponse response) {
        Log.d("MAPPER", "Service id: " + response.getId() + "; Name: " + response.getName());
        return new Service(
                String.valueOf(response.getId()),
                response.getName(),
                response.getDescription(),
                response.getPrice(),
                response.getImage_url()
        );
    }

    public static List<Service> toServiceList(List<ServiceResponse> responses) {
        List<Service> services = new ArrayList<>();
        for (ServiceResponse response : responses) {
            services.add(toService(response));
        }
        return services;
    }

    public static SimplifiedAppointment toSimplifiedAppointment(AppointmentResponse response) {
        // Convert scheduled_time Iso String to DateTime using your utils.DateTime class
        DateTime time = DateTime.parse(response.getScheduled_time());

        return new SimplifiedAppointment(
                String.valueOf(response.getId()),
                String.valueOf(response.getCustomer_id()),
                String.valueOf(response.getEmployee_id()),
                String.valueOf(response.getBranch_id()),
                time,
                response.getStatus(),
                response.getNote(),
                response.getCustomer_address(),
                response.getTotal()
        );
    }

    public static List<SimplifiedAppointment> toSimplifiedAppointmentList(List<AppointmentResponse> responses) {
        List<SimplifiedAppointment> appointments = new ArrayList<>();
        for (AppointmentResponse appointment : responses) {
            appointments.add(toSimplifiedAppointment(appointment));
        }
        return appointments;
    }

    public static Appointment toAppointmentDetail(AppointmentDetailResponse response) {
        // Convert scheduled_time Iso String to DateTime using your utils.DateTime class
        DateTime time = DateTime.parse(response.getAppointment().getScheduled_time());
//        DateTime order_created = DateTime.parse(response.getOrder().getCreated_at());
//        DateTime order_updated = DateTime.parse(response.getOrder().getUpdated_at());

        // String to enum
        AppointmentStatus status = AppointmentStatus.valueOf(response.getAppointment().getStatus().toUpperCase());
        Order order = new Order();
        List<Product> products = new ArrayList<>();
        if (response.getOrder() != null) {
            for (OrderItem product : response.getOrder().getItems()) {
                products.add(new Product(
                        String.valueOf(product.getProduct_id()),
                        product.getProduct_type(),
                        product.getQuantity()));
            }

            order.setId(String.valueOf(response.getOrder().getId()));
            order.setCustomer_id(String.valueOf(response.getOrder().getCustomer_id()));
            order.setBranch_id(String.valueOf(response.getOrder().getBranch_id()));
            order.setAppointment_id(String.valueOf(response.getOrder().getAppointment_id()));
            order.setTotal_price(response.getOrder().getTotal_price());
            order.setCreated_at(DateTime.parse(response.getOrder().getCreated_at()));
            order.setUpdated_at(DateTime.parse(response.getOrder().getUpdated_at()));
            order.setProducts(products);
        }
        else {
            order = null;
        }

        List<Service> services = new ArrayList<>();
        for (AppointmentServiceDetail service : response.getDetails()) {
            services.add(new Service(
                    String.valueOf(service.getService_id()),
                    service.getQuantity()
            ));
        }


        return new Appointment(
                String.valueOf(response.getAppointment().getId()),
                String.valueOf(response.getAppointment().getEmployee_id()),
                order,
                response.getAppointment().getCustomer_address(),
                time,
                status,
                response.getAppointment().getNote(),
                response.getAppointment().getTotal(),
                services
        );
    }

    public static CreateAppointmentRequest toCreateAppointmentRequest(Appointment appointment) {
        List<CreateAppointmentRequest.ServiceItem> services = new ArrayList<>();
        for (Service service : appointment.getServices()) {
            services.add(new CreateAppointmentRequest.ServiceItem(
                    Integer.parseInt(service.getId()),
                    service.getQuantity()
            ));
        }
        return new CreateAppointmentRequest(
                Integer.parseInt(appointment.getCustomerId()),
                appointment.getCustomerAddress(),
                appointment.getScheduledTime().toIsoString(),
                services,
                appointment.getNote(),
                Integer.parseInt(appointment.getBranchId())
        );
    }

    public static UpdateAppointmentStatusRequest toUpdateAppointmentRequest(Appointment
                                                                                    appointment) {
        return new UpdateAppointmentStatusRequest(
                String.valueOf(appointment.getId()),
                appointment.getStatus()
        );
    }

    public static UpdateAppointmentEmployeeRequest toUpdateAppointmentEmployeeRequest
            (Appointment temp) {
        return new UpdateAppointmentEmployeeRequest(
                temp.getId(),
                temp.getEmployeeId()
        );
    }
}
