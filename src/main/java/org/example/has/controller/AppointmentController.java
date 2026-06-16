package org.example.has.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.has.dto.request.AppointmentRequest;
import org.example.has.dto.response.ApiResponse;
import org.example.has.dto.response.AppointmentResponse;
import org.example.has.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for managing patient appointments.
 */
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "5. Appointment Management", description = "API for booking, cancelling, and completing appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
 * Gets all appointments (ADMIN).
 *
 * @return the list of appointments
 */
    @GetMapping
    @Operation(summary = "List of appointments",
            description = "Get all appointments (ADMIN)")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAll() {
        List<AppointmentResponse> appointments = appointmentService.findAll();
        return ResponseEntity.ok(ApiResponse.success("Thành công", appointments));
    }

    /**
 * Gets appointment details by ID.
 *
 * @param id the appointment ID
 * @return the appointment details
 */
    @GetMapping("/{id}")
    @Operation(summary = "Appointment details",
            description = "Get detailed appointment information by ID")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getById(@PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Thành công", appointment));
    }

    /**
 * Gets appointments of a patient.
 *
 * @param patientId the patient ID
 * @return list of appointments
 */
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Appointments by patient",
            description = "Get list of appointments of a patient")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getByPatient(
            @PathVariable Long patientId) {
        List<AppointmentResponse> appointments = appointmentService.findByPatient(patientId);
        return ResponseEntity.ok(ApiResponse.success("Thành công", appointments));
    }

    /**
 * Gets appointments of a doctor.
 *
 * @param doctorId the doctor ID
 * @return list of appointments
 */
    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Appointments by doctor",
            description = "Get list of appointments of a doctor")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getByDoctor(
            @PathVariable Long doctorId) {
        List<AppointmentResponse> appointments = appointmentService.findByDoctor(doctorId);
        return ResponseEntity.ok(ApiResponse.success("Thành công", appointments));
    }

    /**
 * Gets appointments in a day.
 *
 * @param date the checkup date (format: yyyy-MM-dd)
 * @return list of appointments
 */
    @GetMapping("/date")
    @Operation(summary = "Appointments by date",
            description = "Get list of appointments in a day (format: yyyy-MM-dd)")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AppointmentResponse> appointments = appointmentService.findByDate(date);
        return ResponseEntity.ok(ApiResponse.success("Thành công", appointments));
    }

    /**
 * Books a new appointment. System checks for schedule conflicts.
 *
 * @param request the appointment request data
 * @return the created appointment
 */
    @PostMapping
    @Operation(summary = "Book appointment",
            description = "Book a new appointment. System automatically checks for conflicts.")
    public ResponseEntity<ApiResponse<AppointmentResponse>> create(
            @Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse appointment = appointmentService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đặt lịch khám thành công", appointment));
    }

    /**
 * Cancels an appointment (only when status is SCHEDULED).
 *
 * @param id the appointment ID to cancel
 * @return the cancelled appointment
 */
    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel appointment",
            description = "Cancel appointment (only when status is SCHEDULED)")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancel(@PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.cancel(id);
        return ResponseEntity.ok(ApiResponse.success("Hủy lịch khám thành công", appointment));
    }

    /**
 * Marks an appointment as completed.
 *
 * @param id the appointment ID to complete
 * @return the completed appointment
 */
    @PutMapping("/{id}/complete")
    @Operation(summary = "Complete appointment",
            description = "Mark appointment as completed (DOCTOR, ADMIN)")
    public ResponseEntity<ApiResponse<AppointmentResponse>> complete(@PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.complete(id);
        return ResponseEntity.ok(ApiResponse.success("Hoàn thành lịch khám", appointment));
    }
}
