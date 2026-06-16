package org.example.has.service;

import lombok.RequiredArgsConstructor;
import org.example.has.domain.entity.Appointment;
import org.example.has.domain.entity.MedicalRecord;
import org.example.has.domain.enums.AppointmentStatus;
import org.example.has.dto.request.MedicalRecordRequest;
import org.example.has.dto.response.MedicalRecordResponse;
import org.example.has.exception.AppointmentConflictException;
import org.example.has.exception.ResourceNotFoundException;
import org.example.has.repository.AppointmentRepository;
import org.example.has.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing medical records.
 */
@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;

    /**
 * Finds medical record by ID.
 *
 * @param id the medical record ID
 * @return the medical record details
 */
    public MedicalRecordResponse findById(Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hồ sơ khám", "id", id));
        return toResponse(record);
    }

    public MedicalRecordResponse findByAppointment(Long appointmentId) {
        MedicalRecord record = medicalRecordRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hồ sơ khám", "appointmentId", appointmentId));
        return toResponse(record);
    }

    public List<MedicalRecordResponse> findByPatient(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
 * Creates a new medical record for completed appointment.
 *
 * @param request the medical record request data
 * @return the created medical record
 */
    @Transactional
    public MedicalRecordResponse create(MedicalRecordRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lịch hẹn", "id", request.getAppointmentId()));

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new AppointmentConflictException(
                    "Chỉ có thể tạo hồ sơ khám cho lịch hẹn đã hoàn thành (COMPLETED). " +
                            "Trạng thái hiện tại: " + appointment.getStatus());
        }

        if (medicalRecordRepository.findByAppointmentId(appointment.getId()).isPresent()) {
            throw new AppointmentConflictException(
                    "Lịch hẹn này đã có hồ sơ khám bệnh");
        }

        String code = "MR" + System.currentTimeMillis();

        MedicalRecord record = MedicalRecord.builder()
                .code(code)
                .diagnosis(request.getDiagnosis())
                .result(request.getResult())
                .prescription(request.getPrescription())
                .notes(request.getNotes())
                .createdAt(LocalDateTime.now())
                .appointment(appointment)
                .build();

        record = medicalRecordRepository.save(record);
        return toResponse(record);
    }

    /**
 * Updates medical record details.
 *
 * @param id the medical record ID to update
 * @param request the updated medical record data
 * @return the updated medical record
 */
    @Transactional
    public MedicalRecordResponse update(Long id, MedicalRecordRequest request) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hồ sơ khám", "id", id));

        record.setDiagnosis(request.getDiagnosis());
        record.setResult(request.getResult());
        record.setPrescription(request.getPrescription());
        record.setNotes(request.getNotes());

        record = medicalRecordRepository.save(record);
        return toResponse(record);
    }

    private MedicalRecordResponse toResponse(MedicalRecord record) {
        Appointment appointment = record.getAppointment();
        return MedicalRecordResponse.builder()
                .id(record.getId())
                .code(record.getCode())
                .diagnosis(record.getDiagnosis())
                .result(record.getResult())
                .prescription(record.getPrescription())
                .notes(record.getNotes())
                .createdAt(record.getCreatedAt())
                .appointmentId(appointment.getId())
                .appointmentCode(appointment.getCode())
                .patientName(appointment.getPatient().getFullName())
                .doctorName(appointment.getDoctor().getFullName())
                .build();
    }
}
