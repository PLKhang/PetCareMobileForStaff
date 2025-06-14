package com.petcare.staff.ui.common.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.data.model.ui.Product;
import com.petcare.staff.ui.billing.adapter.ProductAdapter;
import com.petcare.staff.utils.DateTime;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseExpandableAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected List<T> fullList = new ArrayList<>();
    protected List<T> displayList = new ArrayList<>();
    protected int displaySize = 3;

    public void setData(List<T> data) {
        this.fullList = data;
        this.displayList = new ArrayList<>(data.subList(0, Math.min(displaySize, data.size())));
        notifyDataSetChanged();
    }

    public boolean showMore() {
        int start = displayList.size();
        int end = Math.min(start + 3, fullList.size());
        if (start >= end) return false;

        displayList.addAll(fullList.subList(start, end));
        notifyItemRangeInserted(start, end - start);

        displaySize = displayList.size();
        return displaySize < fullList.size();
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        bind(holder, displayList.get(position));
    }

    protected abstract void bind(VH holder, T item);
}
