package com.petcare.staff.ui.billing.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.petcare.staff.data.model.ui.Product;
import com.petcare.staff.data.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class SharedProductViewModel extends AndroidViewModel {
    private final ProductRepository repository;
    private final MutableLiveData<List<Product>> allBranchProducts = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> branchAvailableProducts = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> selectedProducts = new MutableLiveData<>();
    private boolean clearedOnce = false;

    public SharedProductViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ProductRepository(application.getApplicationContext());
        loadAllProducts();

        //TO-DO
        selectedProducts.setValue(new ArrayList<>());
    }
    public void loadAllProducts() {
        LiveData<List<Product>> liveData = repository.getAllProducts();

        Observer<List<Product>> observer = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> productList) {
                allBranchProducts.setValue(productList);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public void loadAllProducts(String branchId) {
        LiveData<List<Product>> liveData = repository.getAllProducts(branchId);

        Observer<List<Product>> observer = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> productList) {
                branchAvailableProducts.setValue(productList);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public LiveData<List<Product>> getAllProducts() {
        return branchAvailableProducts;
    }

    public LiveData<List<Product>> getAllBranchProducts() {
        return allBranchProducts;
    }

    private Product findProductById(List<Product> list, Product product) {
        for (Product p : list) {
            if (p.getId().equals(product.getId()) && p.getType().equals(product.getType())) {
                Log.d("Debug_calculator", product.toString());
                return p;
            }
        }
        return null;
    }

    public void addSeletedProduct(Product product) {
        List<Product> current = selectedProducts.getValue();
        if (current == null) current = new ArrayList<>();

        Product existing = findProductById(current, product);
        if (existing != null) {
            current.remove(existing);
        } else {
            current.add(product);
        }

        selectedProducts.setValue(current);
    }

    public LiveData<List<Product>> getSelectedProducts() {
        return selectedProducts;
    }

    public void clearSelectedProducts() {
        if (!clearedOnce) {
            selectedProducts.setValue(new ArrayList<>());
            clearedOnce = true;
        }
    }

    public void resetClearFlag() {
        clearedOnce = false;
    }

    public void setSelectedProductsById(List<Product> selectedProduct) {
        List<Product> all = branchAvailableProducts.getValue();
        if (all == null) return;

        List<Product> selected = new ArrayList<>();

        for (Product pFromAppointment : selectedProduct) {
            for (Product p : all) {
                if (p.getId().equals(pFromAppointment.getId()) && p.getType().equals(pFromAppointment.getType())) {
                    p.setQuantity(pFromAppointment.getQuantity());
                    Product copy = new Product(p);

                    selected.add(copy);
                    Log.d("SetSelectedProduct", "Copy: " + copy.toString());
                    break;
                }
            }
        }
        selectedProducts.setValue(selected);
    }


    public float calculatePrice(List<Product> products) {
        float total = 0f;
        List<Product> current = getAllBranchProducts().getValue();
        for (Product p: current)
        {
            Log.d("Debug_calculator", "All product list: " + p);
        }
        if (current == null) current = new ArrayList<>();
        Log.d("Debug_calculator", "Calculate, product size: " + products.size());
        for(Product p: products){
            Log.d("Debug_calculator", "Object: " + p);
            Product existing = findProductById(current, p);
            total += p.getQuantity() * existing.getPrice();
        }


        return total;
    }
}
