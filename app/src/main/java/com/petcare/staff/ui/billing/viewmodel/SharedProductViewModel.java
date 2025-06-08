package com.petcare.staff.ui.billing.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.petcare.staff.data.model.ui.Product;
import com.petcare.staff.data.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class SharedProductViewModel extends AndroidViewModel {
    private final ProductRepository repository;
    private final MutableLiveData<List<Product>> products = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> selectedProducts = new MutableLiveData<>();
    private boolean clearedOnce = false;

    public SharedProductViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ProductRepository(application.getApplicationContext());
        loadAllProducts();

        //TO-DO
        selectedProducts.setValue(new ArrayList<>());
    }

    private void loadAllProducts() {
        LiveData<List<Product>> liveData = repository.getAllProducts();

        Observer<List<Product>> observer = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> productList) {
                products.setValue(productList);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public LiveData<List<Product>> getAllProducts() {
        return products;
    }

    public void addSeletedProduct(Product product) {
        List<Product> current = selectedProducts.getValue();
        boolean isExisted = false;
        for (Product p: current) {
            if (p.getId().equals(product.getId())){
                isExisted = true;
                break;
            }
        }
        if (!isExisted) {
            current.add(product);
            selectedProducts.setValue(current);
        } else {
            removeSeletedProduct(product);
        }
    }

    public void removeSeletedProduct(Product product) {
        List<Product> current = selectedProducts.getValue();
        boolean isExisted = false;
        for (Product p: current) {
            if (p.getId().equals(product.getId())){
                isExisted = true;
                break;
            }
        }
        if (isExisted) {
            current.remove(product);
            selectedProducts.setValue(current);
        }
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

    public void setSelectedProductsById(List<Product> selectedFromAppointment) {
        List<Product> all = products.getValue();
        if (all == null) return;

        List<Product> selected = new ArrayList<>();
        for (Product pFromAppointment : selectedFromAppointment) {
            for (Product p : all) {
                if (p.getId().equals(pFromAppointment.getId())) {
                    Product copy = new Product(p);
                    copy.setQuantity(pFromAppointment.getQuantity());
                    selected.add(copy);
                    break;
                }
            }
        }
        selectedProducts.setValue(selected);
    }


}
