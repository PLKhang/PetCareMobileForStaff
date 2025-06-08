package com.petcare.staff.ui.billing.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Product;
import com.petcare.staff.data.model.ui.Service;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// item_product_selection
public class ProductSelectorAdapter extends RecyclerView.Adapter<ProductSelectorAdapter.ProductViewHolder> {
    private List<Product> allProducts;
    private List<Product> displayList;
    private List<Product> selectedProducts;
    private final OnAddProductClickListener listener;

    public void filter(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            displayList = new ArrayList<>(allProducts);
        } else {
            String lowerKeyword = keyword.toLowerCase();
            displayList = new ArrayList<>();
            for (Product p : allProducts) {
                if (p.getName().toLowerCase().contains(lowerKeyword) ||
                        p.getDescription().toLowerCase().contains(lowerKeyword)) {
                    displayList.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

    public interface OnAddProductClickListener {
        void onAddClick(Product product);
    }

    public ProductSelectorAdapter(OnAddProductClickListener listener) {
        this.listener = listener;
        this.allProducts = new ArrayList<>();
        this.selectedProducts = new ArrayList<>();
        this.displayList = new ArrayList<>();
    }

    public void setProducts(List<Product> products) {
        this.allProducts = products;
        this.displayList = new ArrayList<>(products);
        notifyDataSetChanged();
    }

    public void setSelectedProducts(List<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_selection, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = displayList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, price;
        ImageView image;
        Button btnAdd;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            description = itemView.findViewById(R.id.productDescription);
            price = itemView.findViewById(R.id.productPrice);
            image = itemView.findViewById(R.id.productImage);
            btnAdd = itemView.findViewById(R.id.btnAddproduct);
        }

        void bind(Product product) {
            name.setText(product.getName());
            Log.d("Debug_recycler", name.getText().toString());
            description.setText(product.getDescription());
            price.setText(String.format("%.2f $", product.getPrice()));
            if (!product.getImgUrl().equals(image.getTag())) {
                Picasso.get()
                        .load(product.getImgUrl())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(image);
            }
            boolean isExited = false;
            for (Product p: selectedProducts)
            {
                if (p.getId().equals(product.getId())){
                    isExited = true;
                    break;
                }
            }
            btnAdd.setText(isExited ? "REMOVE" : "ADD");
            btnAdd.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddClick(product);
                    boolean existed = false;
                    for (Product p: selectedProducts)
                    {
                        if (p.getId().equals(product.getId())){
                            existed = true;
                            break;
                        }
                    }
                    btnAdd.setText(existed ? "REMOVE" : "ADD");
                }
            });
        }
    }
}
