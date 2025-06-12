package com.petcare.staff.ui.billing.adapter;

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
import com.petcare.staff.ui.common.adapter.BaseExpandableAdapter;
import com.squareup.picasso.Picasso;

public class ProductAdapter extends BaseExpandableAdapter<Product, ProductAdapter.ProductViewHolder> {
    public ProductAdapter() {
    }

    @Override
    protected void bind(ProductViewHolder holder, Product item) {
        holder.bind(item);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_product, parent, false);
        return new ProductViewHolder(view);
    }

    //item_bill_product
    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productDescription, productPrice, txtQuantity;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
        }

        public void bind(Product product) {
            txtQuantity.setText("x" + product.getQuantity());
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText(String.format("%.2f $", product.getPrice()));

            if (!product.getImgUrl().equals(productImage.getTag())) {
                Picasso.get()
                        .load(product.getImgUrl())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(productImage);
                productImage.setTag(product.getImgUrl());
            }
        }
    }
}
