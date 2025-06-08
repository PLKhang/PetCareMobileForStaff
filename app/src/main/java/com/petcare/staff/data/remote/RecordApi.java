package com.petcare.staff.data.remote;

import com.petcare.staff.data.model.api.record.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RecordApi {

    // --- PET ROUTES ---
    @POST("api/v1/pets")
    Call<IdResponse> createPet(@Body CreatePetRequest request);

    @GET("api/v1/pets/{id}")
    Call<PetResponse> getPet(@Path("id") String petId);

    @PUT("api/v1/pets")
    Call<PetResponse> updatePet(@Body UpdatePetRequest request);

    @DELETE("api/v1/pets/{id}")
    Call<Void> deletePet(@Path("id") String petId);

    @GET("api/v1/pets/owner/{owner_id}")
    Call<List<PetResponse>> listPetsByOwner(@Path("owner_id") int ownerId);


    // --- EXAMINATION ROUTES ---
    @POST("api/v1/examinations")
    Call<IdResponse> createExamination(@Body CreateExaminationRequest request);

    @GET("api/v1/examinations/{id}")
    Call<ExaminationResponse> getExamination(@Path("id") String examId);

    @PUT("api/v1/examinations")
    Call<ExaminationResponse> updateExamination(@Body UpdateExaminationRequest request);

    @DELETE("api/v1/examinations/{id}")
    Call<Void> deleteExamination(@Path("id") String examId);

    @GET("api/v1/examinations/pet/{pet_id}")
    Call<List<ExaminationResponse>> listExaminationsByPet(@Path("pet_id") String petId);


    // --- VACCINATION ROUTES ---
    @POST("api/v1/vaccinations")
    Call<IdResponse> createVaccination(@Body CreateVaccinationRequest request);

    @GET("api/v1/vaccinations/{id}")
    Call<VaccinationResponse> getVaccination(@Path("id") String vaccinationId);

    @PUT("api/v1/vaccinations")
    Call<VaccinationResponse> updateVaccination(@Body UpdateVaccinationRequest request);

    @DELETE("api/v1/vaccinations/{id}")
    Call<Void> deleteVaccination(@Path("id") String vaccinationId);

    @GET("api/v1/vaccinations/pet/{pet_id}")
    Call<List<VaccinationResponse>> listVaccinationsByPet(@Path("pet_id") String petId);


    // --- PRESCRIPTION ROUTES ---
    @POST("api/v1/prescriptions")
    Call<IdResponse> createPrescription(@Body CreatePrescriptionRequest request);

    @GET("api/v1/prescriptions/{id}")
    Call<PrescriptionResponse> getPrescription(@Path("id") String prescriptionId);

    @PUT("api/v1/prescriptions")
    Call<PrescriptionResponse> updatePrescription(@Body UpdatePrescriptionRequest request);

    @DELETE("api/v1/prescriptions/{id}")
    Call<Void> deletePrescription(@Path("id") String prescriptionId);

    @GET("api/v1/prescriptions/examination/{examination_id}")
    Call<List<PrescriptionResponse>> listPrescriptionsByExamination(@Path("examination_id") int examinationId);
}
