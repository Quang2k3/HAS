package org.example.has.service;

import lombok.RequiredArgsConstructor;
import org.example.has.domain.entity.Appointment;
import org.example.has.domain.entity.Doctor;
import org.example.has.domain.entity.Patient;
import org.example.has.domain.enums.AppointmentStatus;
import org.example.has.dto.request.AppointmentRequest;
import org.example.has.dto.response.AppointmentResponse;
import org.example.has.exception.AppointmentConflictException;
import org.example.has.exception.ResourceNotFoundException;
import org.example.has.repository.AppointmentRepository;
import org.example.has.repository.DoctorRepository;
import org.example.has.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

/**
 * Service for managing appointments.
 */
@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    /**
 * Finds all appointments.
 *
 * @return list of appointments
 */
    public List<AppointmentResponse> findAll() {
        return appointmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
 * Finds appointment by ID.
 *
 * @param id the appointment ID
 * @return the appointment details
 */
    public AppointmentResponse findById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lịch hẹn", "id", id));
        return toResponse(appointment);
    }

    public List<AppointmentResponse> findByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> findByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
 * Finds appointments by date.
 *
 * @param date the checkup date
 * @return list of appointments
 */
    public List<AppointmentResponse> findByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
 * Books a new appointment. Checks for schedule conflicts.
 *
 * @param request the appointment request data
 * @return the created appointment
 */
    @Transactional
    public AppointmentResponse create(AppointmentRequest request) {

        // Không cho đặt lịch trong quá khứ
        LocalDateTime appointmentDateTime =
                LocalDateTime.of(
                        request.getAppointmentDate(),
                        request.getAppointmentTime());

        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            throw new AppointmentConflictException(
                    "Không được đặt lịch khám trong quá khứ");
        }
        // Không cho đặt lịch quá 3 tháng
        if (request.getAppointmentDate().isAfter(
                LocalDate.now().plusMonths(3))) {

            throw new AppointmentConflictException(
                    "Chỉ được đặt lịch trong vòng 3 tháng tới");
        }

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bệnh nhân", "id", request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bác sĩ", "id", request.getDoctorId()));
// Kiểm tra bệnh nhân có lịch trùng giờ hay không
        boolean patientConflict = appointmentRepository
                .existsByPatientIdAndAppointmentDateAndAppointmentTimeAndStatus(
                        request.getPatientId(),
                        request.getAppointmentDate(),
                        request.getAppointmentTime(),
                        AppointmentStatus.SCHEDULED);

        if (patientConflict) {
            throw new AppointmentConflictException(
                    "Bệnh nhân đã có lịch hẹn vào thời gian này");
        }
        boolean isConflict = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatus(
                        request.getDoctorId(),
                        request.getAppointmentDate(),
                        request.getAppointmentTime(),
                        AppointmentStatus.SCHEDULED);

        if (isConflict) {
            throw new AppointmentConflictException(
                    String.format("Bác sĩ %s đã có lịch hẹn vào %s lúc %s",
                            doctor.getFullName(),
                            request.getAppointmentDate(),
                            request.getAppointmentTime()));
        }

        String code = "APT" + System.currentTimeMillis();

        Appointment appointment = Appointment.builder()
                .code(code)
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .status(AppointmentStatus.SCHEDULED)
                .reason(request.getReason())
                .build();

        appointment = appointmentRepository.save(appointment);
        return toResponse(appointment);
    }

    /**
 * Cancels an appointment (only when status is SCHEDULED).
 *
 * @param id the appointment ID to cancel
 * @return the cancelled appointment
 */
    @Transactional
    public AppointmentResponse cancel(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lịch hẹn", "id", id));
// Không hủy lịch đã qua
        if (appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new AppointmentConflictException(
                    "Không thể hủy lịch khám đã qua");
        }
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new AppointmentConflictException(
                    "Chỉ có thể hủy lịch hẹn đang ở trạng thái SCHEDULED. " +
                            "Trạng thái hiện tại: " + appointment.getStatus());
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment = appointmentRepository.save(appointment);
        return toResponse(appointment);
    }

    /**
 * Completes an appointment.
 *
 * @param id the appointment ID to complete
 * @return the completed appointment
 */
    @Transactional
    public AppointmentResponse complete(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lịch hẹn", "id", id));
        // Không cho hoàn thành lịch khám khi chưa đến ngày khám
        if (appointment.getAppointmentDate().isAfter(LocalDate.now())) {
            throw new AppointmentConflictException(
                    "Không thể hoàn thành lịch khám khi chưa đến ngày khám");
        }

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new AppointmentConflictException(
                    "Chỉ có thể hoàn thành lịch hẹn đang ở trạng thái SCHEDULED. " +
                            "Trạng thái hiện tại: " + appointment.getStatus());
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment = appointmentRepository.save(appointment);
        return toResponse(appointment);
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .code(appointment.getCode())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .reason(appointment.getReason())
                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getFullName())
                .doctorId(appointment.getDoctor().getId())
                .doctorName(appointment.getDoctor().getFullName())
                .departmentName(appointment.getDoctor().getDepartment() != null
                        ? appointment.getDoctor().getDepartment().getName() : null)
                .build();
    }
}
