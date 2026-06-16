package org.example.has.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.has.dto.request.PatientRequest;
import org.example.has.dto.response.ApiResponse;
import org.example.has.dto.response.PatientResponse;
import org.example.has.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing patient profiles.
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "4. Patient Management", description = "API for managing patient information")
public class PatientController {

    private final PatientService patientService;

    /**
 * Gets a list of all patients.
 *
 * @return the list of patients
 */
    @GetMapping
    @Operation(summary = "List of patients",
            description = "Get list of all patients (ADMIN, DOCTOR)")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> getAll() {
        List<PatientResponse> patients = patientService.findAll();
        return ResponseEntity.ok(ApiResponse.success("Thành công", patients));
    }

    /**
 * Gets patient details by ID.
 *
 * @param id the patient ID
 * @return the patient details
 */
    @GetMapping("/{id}")
    @Operation(summary = "Patient details",
            description = "Get detailed patient information by ID")
    public ResponseEntity<ApiResponse<PatientResponse>> getById(@PathVariable Long id) {
        PatientResponse patient = patientService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Thành công", patient));
    }

    /**
 * Searches patients by name containing keyword.
 *
 * @param keyword the search keyword
 * @return list of matching patients
 */
    @GetMapping("/search")
    @Operation(summary = "Search patients",
            description = "Search patients by name containing keyword")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> search(
            @RequestParam String keyword) {
        List<PatientResponse> patients = patientService.search(keyword);
        return ResponseEntity.ok(ApiResponse.success("Thành công", patients));
    }

    /**
 * Creates a new patient. Requires ADMIN role.
 *
 * @param request the patient request data
 * @return the created patient details
 */
    @PostMapping
    @Operation(summary = "Create patient",
            description = "Create a new patient (ADMIN)")
    public ResponseEntity<ApiResponse<PatientResponse>> create(
            @Valid @RequestBody PatientRequest request) {
        PatientResponse patient = patientService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo bệnh nhân thành công", patient));
    }

    /**
 * Updates patient details. Requires ADMIN role or matching user ID.
 *
 * @param id the patient ID to update
 * @param request the updated patient data
 * @return the updated patient details
 */
    @PutMapping("/{id}")
    @Operation(summary = "Update patient",
            description = "Update patient details (ADMIN or patient themselves)")
    public ResponseEntity<ApiResponse<PatientResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequest request) {
        PatientResponse patient = patientService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật bệnh nhân thành công", patient));
    }

    /**
 * Deletes a patient by ID. Requires ADMIN role.
 *
 * @param id the patient ID to delete
 * @return success message
 */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient", description = "Delete patient (ADMIN)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa bệnh nhân thành công", null));
    }
}
