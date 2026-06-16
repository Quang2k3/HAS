package org.example.has.service;

import lombok.RequiredArgsConstructor;
import org.example.has.domain.entity.Department;
import org.example.has.domain.enums.AppointmentStatus;
import org.example.has.dto.response.AppointmentResponse;
import org.example.has.dto.response.StatisticsResponse;
import org.example.has.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for system reports and statistics.
 */
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    /**
 * Gets system overview statistics.
 *
 * @return system overview statistics
 */
    public StatisticsResponse getOverview() {
        Map<String, Long> doctorsByDepartment = new LinkedHashMap<>();
        List<Department> departments = departmentRepository.findAll();
        for (Department dept : departments) {
            long count = doctorRepository.countByDepartmentId(dept.getId());
            doctorsByDepartment.put(dept.getName(), count);
        }

        return StatisticsResponse.builder()
                .totalPatients(patientRepository.count())
                .totalDoctors(doctorRepository.count())
                .totalDepartments(departmentRepository.count())
                .totalAppointments(appointmentRepository.count())
                .scheduledAppointments(
                        appointmentRepository.countByStatus(AppointmentStatus.SCHEDULED))
                .completedAppointments(
                        appointmentRepository.countByStatus(AppointmentStatus.COMPLETED))
                .cancelledAppointments(
                        appointmentRepository.countByStatus(AppointmentStatus.CANCELLED))
                .doctorsByDepartment(doctorsByDepartment)
                .build();
    }

    public long getAppointmentCountByDate(LocalDate date) {
        return appointmentRepository.countByAppointmentDate(date);
    }

    /**
 * Gets appointments list of a doctor.
 *
 * @param doctorId the doctor ID
 * @return list of appointments
 */
    public List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(apt -> AppointmentResponse.builder()
                        .id(apt.getId())
                        .code(apt.getCode())
                        .appointmentDate(apt.getAppointmentDate())
                        .appointmentTime(apt.getAppointmentTime())
                        .status(apt.getStatus())
                        .reason(apt.getReason())
                        .patientId(apt.getPatient().getId())
                        .patientName(apt.getPatient().getFullName())
                        .doctorId(apt.getDoctor().getId())
                        .doctorName(apt.getDoctor().getFullName())
                        .build())
                .toList();
    }
}
