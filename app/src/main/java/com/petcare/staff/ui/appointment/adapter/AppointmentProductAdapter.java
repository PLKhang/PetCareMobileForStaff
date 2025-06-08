package com.petcare.staff.ui.appointment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

// item_appointment_product
public class AppointmentProductAdapter extends RecyclerView.Adapter<AppointmentProductAdapter.ProductViewHolder> {

    private final List<Product> fullList = new ArrayList<>();
    private final List<Product> displayList = new ArrayList<>();
    private final OnQuantityChangeListener listener;
    private boolean isActiveButtons = true;
    private final int PAGE_SIZE = 3;

    public interface OnQuantityChangeListener {
        void onQuantityChanged(Product product, int newQuantity);
    }

    public AppointmentProductAdapter(Boolean active, OnQuantityChangeListener listener) {
        this.isActiveButtons = active;
        this.listener = listener;
    }

    public void setActiveButtons(boolean activeButtons) {
        isActiveButtons = activeButtons;
        notifyDataSetChanged();
    }

    public void setData(List<Product> products) {
        fullList.clear();
        fullList.addAll(products);

        displayList.clear();
        int end = Math.min(PAGE_SIZE, products.size());
        displayList.addAll(products.subList(0, end));
        notifyDataSetChanged();
    }

    public boolean showMore() {
        int start = displayList.size();
        int end = Math.min(start + PAGE_SIZE, fullList.size());

        if (start >= end) return false; // Nothing more to show

        displayList.addAll(fullList.subList(start, end));
        notifyItemRangeInserted(start, end - start);
        return displayList.size() < fullList.size();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(displayList.get(position));
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productDescription, productPrice;
        EditText itemQuantity;
        ImageButton btnIncrease, btnDecrease;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }

        void bind(Product product) {
            itemQuantity.setEnabled(isActiveButtons);
            btnIncrease.setEnabled(isActiveButtons);
            btnDecrease.setEnabled(isActiveButtons);

            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText(String.format("%.2f $", product.getPrice()));
            itemQuantity.setText(String.valueOf(product.getQuantity()));

            if (!product.getImgUrl().equals(productImage.getTag())) {
                Picasso.get()
                        .load(product.getImgUrl())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(productImage);
                productImage.setTag(product.getImgUrl());
            }

            btnIncrease.setOnClickListener(v -> {
                int newQty = product.getQuantity() + 1;
//                if (newQty <= product.getStock()) {
                    product.setQuantity(newQty);
                    itemQuantity.setText(String.valueOf(newQty));
                    if (listener != null) listener.onQuantityChanged(product, newQty);
//                }
            });

            btnDecrease.setOnClickListener(v -> {
                if (product.getQuantity() > 1) {
                    int newQty = product.getQuantity() - 1;
                    product.setQuantity(newQty);
                    itemQuantity.setText(String.valueOf(newQty));
                    if (listener != null) listener.onQuantityChanged(product, newQty);
                }
            });

            itemQuantity.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    try {
                        int value = Integer.parseInt(itemQuantity.getText().toString());
//                        value = Math.max(1, Math.min(value, product.getStock())); //check stock
                        product.setQuantity(value);
                        itemQuantity.setText(String.valueOf(value));
                        if (listener != null) listener.onQuantityChanged(product, value);
                    } catch (NumberFormatException e) {
                        itemQuantity.setText(String.valueOf(product.getQuantity()));
                    }
                }
            });
        }
    }
}

