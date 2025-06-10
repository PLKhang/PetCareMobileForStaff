package com.petcare.staff.data.model.mapper;

import com.petcare.staff.data.model.api.record.CreateExaminationRequest;
import com.petcare.staff.data.model.api.record.CreatePetRequest;
import com.petcare.staff.data.model.api.record.CreatePrescriptionRequest;
import com.petcare.staff.data.model.api.record.CreateVaccinationRequest;
import com.petcare.staff.data.model.api.record.ExaminationResponse;
import com.petcare.staff.data.model.api.record.MedicationResponse;
import com.petcare.staff.data.model.api.record.PetResponse;
import com.petcare.staff.data.model.api.record.PrescriptionResponse;
import com.petcare.staff.data.model.api.record.UpdateExaminationRequest;
import com.petcare.staff.data.model.api.record.UpdatePetRequest;
import com.petcare.staff.data.model.api.record.UpdatePrescriptionRequest;
import com.petcare.staff.data.model.api.record.UpdateVaccinationRequest;
import com.petcare.staff.data.model.api.record.VaccinationResponse;

import com.petcare.staff.data.model.ui.MedicalRecord;
import com.petcare.staff.data.model.ui.Medication;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.data.model.ui.Prescription;
import com.petcare.staff.data.model.ui.VaccineRecord;

import com.petcare.staff.utils.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordMapper {
    public static Pet toPet(PetResponse response) {
        DateTime birth = DateTime.parse(response.getDob());
        return new Pet(
                response.getId(),
                response.getName(),
                response.getOwner_id(),
                birth,
                response.getColor(),
                response.getIdentity_mark(),
                response.getSpecies(),
                response.getWeight()
        );
    }

    public static List<Pet> toPetList(List<PetResponse> responses) {
        List<Pet> petList = new ArrayList<>();
        for (PetResponse response : responses) {
            petList.add(toPet(response));
        }

        return petList;
    }

    public static Medication toMedication(MedicationResponse response) {
        DateTime startDate = DateTime.fromApiDateString(response.getStart_date());
        DateTime endDate = DateTime.fromApiDateString(response.getEnd_date());
        return new Medication(
                response.getName(),
                response.getDosage(),
                startDate,
                endDate
        );
    }

    public static List<Medication> toMedicationList(List<MedicationResponse> responses) {
        List<Medication> medicationList = new ArrayList<>();
        for (MedicationResponse response : responses) {
            medicationList.add(toMedication(response));
        }

        return medicationList;
    }

    public static Prescription toPrescription(PrescriptionResponse response) {
        return new Prescription(
                response.getId(),
                response.getExamination_id(),
                toMedicationList(response.getMedications())
        );
    }

    public static List<Prescription> toPrescriptionList(List<PrescriptionResponse> responses) {
        List<Prescription> prescriptionList = new ArrayList<>();
        for (PrescriptionResponse response : responses) {
            prescriptionList.add(toPrescription(response));
        }

        return prescriptionList;
    }

    public static MedicalRecord toMedicalRecord(ExaminationResponse response) {
        DateTime date = DateTime.fromApiDateString(response.getDate());
        return new MedicalRecord(
                response.getId(),
                response.getPet_id(),
                response.getVet_id(),
                response.getDiagnosis(),
                date,
                response.getNotes()
        );
    }

    public static List<MedicalRecord> toMedicalRecordList(List<ExaminationResponse> responses) {
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        for (ExaminationResponse response : responses) {
            medicalRecordList.add(toMedicalRecord(response));
        }

        return medicalRecordList;
    }

    public static VaccineRecord toVaccineRecord(VaccinationResponse response) {
        DateTime date = DateTime.fromApiDateString(response.getDate());
        DateTime nextDose = DateTime.fromApiDateString(response.getNext_dose());
        return new VaccineRecord(
                response.getId(),
                response.getPet_id(),
                response.getVet_id(),
                response.getVaccine_name(),
                date,
                nextDose
        );
    }

    public static List<VaccineRecord> toVaccineRecordList(List<VaccinationResponse> responses) {
        List<VaccineRecord> vaccineRecordList = new ArrayList<>();
        for (VaccinationResponse response : responses) {
            vaccineRecordList.add(toVaccineRecord(response));
        }

        return vaccineRecordList;
    }

    public static CreatePetRequest toCreatePetRequest(Pet pet) {
        return new CreatePetRequest(
                pet.getColor(),
                pet.getBirth().toIsoString(),
                pet.getIdentityMark(),
                pet.getName(),
                pet.getOwnerId(),
                pet.getSpecies(),
                pet.getWeight()
        );
    }

    public static UpdatePetRequest toUpdatePetRequest(Pet pet) {
        return new UpdatePetRequest(
                pet.getColor(),
                pet.getBirth().toIsoString(),
                pet.getId(),
                pet.getIdentityMark(),
                pet.getName(),
                pet.getOwnerId(),
                pet.getSpecies(),
                pet.getWeight()
        );
    }

    public static CreateExaminationRequest toCreateExaminationRequest(MedicalRecord medicalRecord) {
        return new CreateExaminationRequest(
                medicalRecord.getDate().toApiDateString(),
                medicalRecord.getDiagnosis(),
                medicalRecord.getNotes(),
                medicalRecord.getPetId(),
                medicalRecord.getVetId()
        );
    }

    public static UpdateExaminationRequest toUpdateExaminationRequest(MedicalRecord medicalRecord) {
        return new UpdateExaminationRequest(
                medicalRecord.getDate().toIsoString(),
                medicalRecord.getDiagnosis(),
                medicalRecord.getId(),
                medicalRecord.getNotes(),
                medicalRecord.getPetId(),
                medicalRecord.getVetId()
        );
    }
    public static CreateVaccinationRequest toCreateVaccinationRequest(VaccineRecord vaccineRecord) {
        return new CreateVaccinationRequest(
                vaccineRecord.getDate().toApiDateString(),
                vaccineRecord.getNextDose().toApiDateString(),
                vaccineRecord.getPetId(),
                vaccineRecord.getVaccineName(),
                vaccineRecord.getVetId()
        );
    }
    public static UpdateVaccinationRequest toUpdateVaccinationRequest(VaccineRecord vaccineRecord) {
        return new UpdateVaccinationRequest(
                vaccineRecord.getDate().toIsoString(),
                vaccineRecord.getId(),
                vaccineRecord.getNextDose().toIsoString(),
                vaccineRecord.getPetId(),
                vaccineRecord.getVaccineName(),
                vaccineRecord.getVetId()
        );
    }
    public static MedicationResponse toMedicationRequest(Medication medication) {
        return new MedicationResponse(
                medication.getDosage(),
                medication.getEndDate().toApiDateString(),
                medication.getId(),
                medication.getName(),
                medication.getStartDate().toApiDateString()
        );
    }

    public static List<MedicationResponse> toMedicationRequestList(List<Medication> medicationList) {
        List<MedicationResponse> medicationResponseList = new ArrayList<>();
        for (Medication medication: medicationList) {
            medicationResponseList.add(toMedicationRequest(medication));
        }

        return medicationResponseList;
    }
    public static CreatePrescriptionRequest toCreatePrescriptionRequest(Prescription prescription) {
        return new CreatePrescriptionRequest(
                prescription.getExaminationId(),
                toMedicationRequestList(prescription.getMedicationList())
        );
    }
    public static UpdatePrescriptionRequest toUpdatePrescriptionRequest(Prescription prescription) {
        return new UpdatePrescriptionRequest(
                prescription.getExaminationId(),
                prescription.getId(),
                toMedicationRequestList(prescription.getMedicationList())
        );
    }
}
