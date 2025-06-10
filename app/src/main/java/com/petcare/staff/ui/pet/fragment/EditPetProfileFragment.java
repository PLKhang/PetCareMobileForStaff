package com.petcare.staff.ui.pet.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.ui.pet.viewmodel.PetDetailViewModel;
import com.petcare.staff.ui.pet.viewmodel.SelectedPetViewModel;

public class EditPetProfileFragment extends Fragment {
    private PetDetailViewModel viewModel;
    private Pet currentPet;
    private ImageView petImage;
    private TextView petName, petSpecies, petBirth;
    private EditText etWeight, etColor, etIdentityMark;
    private Button btnCancel, btnUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_pet_profile, container, false);
        initViews(view);

        SelectedPetViewModel temp = new ViewModelProvider(requireActivity()).get(SelectedPetViewModel.class);
        temp.getSelectedPet().observe(getViewLifecycleOwner(), pet -> {
            if (pet != null ) currentPet = pet;
            populatePetInfo(currentPet);
        });

        btnUpdate.setOnClickListener(v -> handleUpdate());
        btnCancel.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateWithoutBackStack(R.id.petDetailFragment, null);
        });

        viewModel = new ViewModelProvider(requireActivity()).get(PetDetailViewModel.class);

        return view;
    }

    private void handleUpdate() {
        String color = etColor.getText().toString().trim();
        String identityMark = etIdentityMark.getText().toString().trim();

        float newWeight = 0f;
        String weightStr = etWeight.getText().toString().trim();
        if (!weightStr.isEmpty()) {
            newWeight = Float.parseFloat(weightStr);
        }
        else {
            Toast.makeText(getContext(), "Enter current weight", Toast.LENGTH_SHORT).show();
            return;
        }

        Pet newInfo = new Pet(
                currentPet.getId(),
                currentPet.getName(),
                currentPet.getOwnerId(),
                currentPet.getBirth(),
                color,
                identityMark,
                currentPet.getSpecies(),
                newWeight
        );

        viewModel.changeInfo(newInfo, new RepositoryCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "Update info SUCCESSFULLY!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Fail to update info: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populatePetInfo(Pet currentPet) {
        petImage.setImageResource(R.drawable.ic_animal);
        petName.setText(currentPet.getName());
        petBirth.setText(currentPet.getBirth().toString());
        petSpecies.setText(currentPet.getSpecies());
        etColor.setText(currentPet.getColor());
        etIdentityMark.setText(currentPet.getIdentityMark());
        etWeight.setText(String.valueOf(currentPet.getWeight()));
    }

    private void initViews(View view) {
        petImage = view.findViewById(R.id.petImage);
        petName = view.findViewById(R.id.petName);
        petSpecies = view.findViewById(R.id.petSpecies);
        petBirth = view.findViewById(R.id.petBirth);
        etWeight = view.findViewById(R.id.etWeight);
        etColor = view.findViewById(R.id.etColor);
        etIdentityMark = view.findViewById(R.id.etIdentityMark);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnUpdate = view.findViewById(R.id.btnUpdate);
    }
}