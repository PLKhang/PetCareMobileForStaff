package com.petcare.staff.ui.home.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.petcare.staff.data.model.ui.Branch;
import com.petcare.staff.data.repository.ProductRepository;

public class BranchViewModel extends AndroidViewModel {
    private final ProductRepository repository;
    private MutableLiveData<Branch> branchLiveData = new MutableLiveData<>();
    public BranchViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ProductRepository(application.getApplicationContext());
    }
    public void loadBranch(String branchId) {
        LiveData<Branch> liveData = repository.getBranchInfo(branchId);

        Observer<Branch> observer = new Observer<Branch>() {
            @Override
            public void onChanged(Branch branch) {
                branchLiveData.setValue(branch);
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
    }

    public LiveData<Branch> getBranchInfo() {
        return branchLiveData;
    }

    public LiveData<Branch> getBranchInfoById(String branchId) {
        return repository.getBranchInfo(branchId);
    }
}
