package com.petcare.staff.ui.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.ui.common.adapter.BaseExpandableAdapter;

public class PetAdapter extends BaseExpandableAdapter<Pet, PetAdapter.PetViewHolder> {

    private final OnPetInfoClickListener listener;

    public interface OnPetInfoClickListener {
        void onMoreInfoClick(Pet pet);
    }

    public PetAdapter(OnPetInfoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    protected void bind(PetViewHolder holder, Pet pet) {
        holder.bind(pet);
    }

    class PetViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, species, birth;
        Button btnMoreInfo;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.petImage);
            name = itemView.findViewById(R.id.petName);
            species = itemView.findViewById(R.id.petSpecies);
            birth = itemView.findViewById(R.id.petBirth);
            btnMoreInfo = itemView.findViewById(R.id.btnPetInfo);
        }

        public void bind(Pet pet) {
            image.setImageResource(R.drawable.ic_animal);
            name.setText("Name: " + pet.getName());
            species.setText("Species: " + pet.getSpecies());
            birth.setText("Birth: " + pet.getBirth().toString());

            btnMoreInfo.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMoreInfoClick(pet);
                }
            });
        }
    }
}

