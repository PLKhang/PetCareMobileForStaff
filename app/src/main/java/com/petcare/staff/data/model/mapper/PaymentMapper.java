package com.petcare.staff.data.model.mapper;

import com.petcare.staff.data.model.api.payment.CancelPaymentRequest;
import com.petcare.staff.data.model.api.payment.CreatePaymentRequest;
import com.petcare.staff.data.model.api.payment.CreatePaymentUrlRequest;
import com.petcare.staff.data.model.api.payment.PaymentMethod;
import com.petcare.staff.data.model.api.payment.PaymentResponse;
import com.petcare.staff.data.model.api.payment.PaymentStatus;
import com.petcare.staff.data.model.api.payment.UpdatePaymentAmountRequest;
import com.petcare.staff.data.model.api.payment.UpdatePaymentMethodRequest;
import com.petcare.staff.data.model.api.payment.UpdatePaymentStatusRequest;
import com.petcare.staff.data.model.ui.Bill;

public class PaymentMapper {
    public static Bill toBill(PaymentResponse response) {
        return new Bill(
                String.valueOf(response.getPayment_id()),
                String.valueOf(response.getOrder_id()),
                String.valueOf(response.getAppointment_id()),
                response.getAmount(),
                response.getMethod(),
                response.getStatus(),
                response.getDescription()
        );
    }

    public static CreatePaymentRequest toCreatePaymentRequest(Bill bill) {
        return new CreatePaymentRequest(
                bill.getAmount(),
                Integer.parseInt(bill.getAppointmentId()),
                bill.getDescription(),
                bill.getPaymentMethod(),
                Integer.parseInt(bill.getOrderId())
        );
    }

    public static CreatePaymentUrlRequest toCreatePaymentUrlRequest(Bill bill) {
        return new CreatePaymentUrlRequest(
                bill.getAmount(),
                bill.getDescription(),
                String.valueOf(bill.getId())
        );
    }

    public static CancelPaymentRequest toCancelPaymentRequest(String id, String reason) {
        return new CancelPaymentRequest(
                reason,
                id
        );
    }

    public static UpdatePaymentStatusRequest toUpdatePaymentStatusRequest(String id, PaymentStatus status) {
        return new UpdatePaymentStatusRequest(
                id,
                status
        );
    }

    public static UpdatePaymentMethodRequest toUpdatePaymentMethodRequest(String id, PaymentMethod method) {
        return new UpdatePaymentMethodRequest(
                method,
                id
        );
    }

    public static UpdatePaymentAmountRequest toCreateUpdatePaymentAmountRequest(String id, float amount) {
        return new UpdatePaymentAmountRequest(
                amount,
                id
        );
    }
}
