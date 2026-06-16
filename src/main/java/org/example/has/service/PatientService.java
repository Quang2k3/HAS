package org.example.has.service;

import lombok.RequiredArgsConstructor;
import org.example.has.domain.entity.Patient;
import org.example.has.dto.request.PatientRequest;
import org.example.has.dto.response.PatientResponse;
import org.example.has.exception.DuplicateResourceException;
import org.example.has.exception.ResourceNotFoundException;
import org.example.has.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing patients.
 */
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    /**
 * Finds all patients.
 *
 * @return list of patients
 */
    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
 * Finds patient by ID.
 *
 * @param id the patient ID
 * @return the patient details
 */
    public PatientResponse findById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bệnh nhân", "id", id));
        return toResponse(patient);
    }

    /**
 * Searches patients by keyword.
 *
 * @param keyword the search keyword
 * @return list of matching patients
 */
    public List<PatientResponse> search(String keyword) {
        return patientRepository.findByFullNameContainingIgnoreCase(keyword).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
 * Creates a new patient.
 *
 * @param request the patient request data
 * @return the created patient
 */
    @Transactional
    public PatientResponse create(PatientRequest request) {
        if (patientRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Bệnh nhân", "code", request.getCode());
        }

        Patient patient = Patient.builder()
                .code(request.getCode())
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();

        patient = patientRepository.save(patient);
        return toResponse(patient);
    }

    /**
 * Updates patient details.
 *
 * @param id the patient ID to update
 * @param request the updated patient data
 * @return the updated patient
 */
    @Transactional
    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bệnh nhân", "id", id));

        patient.setFullName(request.getFullName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setAddress(request.getAddress());

        patient = patientRepository.save(patient);
        return toResponse(patient);
    }

    /**
 * Deletes patient by ID.
 *
 * @param id the patient ID to delete
 */
    @Transactional
    public void delete(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bệnh nhân", "id", id);
        }
        patientRepository.deleteById(id);
    }

    private PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .code(patient.getCode())
                .fullName(patient.getFullName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .address(patient.getAddress())
                .build();
    }
}
