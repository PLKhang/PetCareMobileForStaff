package com.petcare.staff.data.model.mapper;

import com.petcare.staff.data.model.api.order.CreateOrderRequest;
import com.petcare.staff.data.model.api.order.OrderItemRequest;
import com.petcare.staff.data.model.api.order.OrderItemResponse;
import com.petcare.staff.data.model.api.order.OrderResponse;
import com.petcare.staff.data.model.api.order.OrderStatus;
import com.petcare.staff.data.model.api.order.UpdateOrderStatusRequest;
import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.data.model.ui.Product;
import com.petcare.staff.utils.DateTime;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    public static Order toOrder(OrderResponse response) {
        DateTime order_created = DateTime.parse(response.getCreated_at());
        DateTime order_updated = DateTime.parse(response.getUpdated_at());
        List<Product> products = toProductList(response.getItems());
        return new Order(
                String.valueOf(response.getId()),
                String.valueOf(response.getCustomer_id()),
                String.valueOf(response.getBranch_id()),
                String.valueOf(response.getAppointment_id()),
                response.getTotal_price(),
                order_created,
                order_updated,
                products
        );
    }

    public static List<Order> toOrdersList (List<OrderResponse> responses) {
        List<Order> orders = new ArrayList<>();
        for (OrderResponse response: responses) {
            orders.add(toOrder(response));
        }

        return orders;
    }

    public static List<Product> toProductList(List<OrderItemResponse> responses) {
        List<Product> products = new ArrayList<>();
        for (OrderItemResponse product : responses) {
            products.add(new Product(
                    String.valueOf(product.getProduct_id()),
                    product.getProduct_type(),
                    product.getQuantity()
            ));
        }
        return products;
    }

    public static OrderItemRequest toOrderItemRequest(Product product) {
        return new OrderItemRequest(
                Integer.parseInt(product.getId()),
                product.getType(),
                product.getQuantity(),
                product.getPrice(),
                product.getName()
        );
    }

    public static List<OrderItemRequest> toOrderItemRequestList(List<Product> productList) {
        List<OrderItemRequest> items = new ArrayList<>();
        for (Product product : productList) {
            items.add(toOrderItemRequest(product));
        }
        return items;
    }

    public static CreateOrderRequest toCreateOrderRequest(Order order) {
        return new CreateOrderRequest(
                Integer.parseInt(order.getCustomer_id()),
                Integer.parseInt(order.getBranch_id()),
                Integer.parseInt(order.getAppointment_id()),
                toOrderItemRequestList(order.getProducts())
        );
    }

    public static UpdateOrderStatusRequest toUpdateOrderStatusRequest(OrderStatus status, String order_id) {
        return new UpdateOrderStatusRequest(
                order_id,
                status
        );
    }
}
