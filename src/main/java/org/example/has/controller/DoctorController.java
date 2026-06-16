package org.example.has.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.has.dto.request.DoctorRequest;
import org.example.has.dto.response.ApiResponse;
import org.example.has.dto.response.DoctorResponse;
import org.example.has.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing doctor profiles.
 */
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "3. Doctor Management", description = "API for managing doctor profiles")
public class DoctorController {

    private final DoctorService doctorService;

    /**
     * Gets a list of all doctors.
     *
     * @return the list of doctors
     */
    @GetMapping
    @Operation(summary = "List of doctors", description = "Get a list of all doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAll() {
        List<DoctorResponse> doctors = doctorService.findAll();
        return ResponseEntity.ok(ApiResponse.success("Thành công", doctors));
    }

    /**
     * Gets doctor details by ID.
     *
     * @param id the doctor ID
     * @return the doctor details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Doctor details", description = "Get detailed doctor information by ID")
    public ResponseEntity<ApiResponse<DoctorResponse>> getById(@PathVariable Long id) {
        DoctorResponse doctor = doctorService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Thành công", doctor));
    }

    /**
     * Gets doctors belonging to a specific department.
     *
     * @param departmentId the department ID
     * @return list of doctors belonging to the department
     */
    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Doctors by department", description = "Get list of doctors in a department")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getByDepartment(
            @PathVariable Long departmentId) {
        List<DoctorResponse> doctors = doctorService.findByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success("Thành công", doctors));
    }

    /**
     * Searches doctors by name containing keyword.
     *
     * @param keyword the search keyword
     * @return list of matching doctors
     */
    @GetMapping("/search")
    @Operation(summary = "Search doctors", description = "Search doctors by malee containing keyword")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> search(
            @RequestParam String keyword) {
        List<DoctorResponse> doctors = doctorService.search(keyword);
        return ResponseEntity.ok(ApiResponse.success("Thành công", doctors));
    }

    /**
     * Creates a new doctor. Requires ADMIN role.
     *
     * @param request the doctor request data
     * @return the created doctor details
     */
    @PostMapping
    @Operation(summary = "Create doctor", description = "Create a new doctor, optionally with login account (ADMIN)")
    public ResponseEntity<ApiResponse<DoctorResponse>> create(
            @Valid @RequestBody DoctorRequest request) {
        DoctorResponse doctor = doctorService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo bác sĩ thành công", doctor));
    }

    /**
     * Updates doctor details. Requires ADMIN role.
     *
     * @param id      the doctor ID to update
     * @param request the updated doctor data
     * @return the updated doctor details
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update doctor", description = "Update doctor details (ADMIN)")
    public ResponseEntity<ApiResponse<DoctorResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequest request) {
        DoctorResponse doctor = doctorService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật bác sĩ thành công", doctor));
    }

    /**
     * Deletes a doctor by ID. Requires ADMIN role.
     *
     * @param id the doctor ID to delete
     * @return success message
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete doctor", description = "Delete doctor (ADMIN)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa bác sĩ thành công", null));
    }
}
