package org.example.has.service;

import lombok.RequiredArgsConstructor;
import org.example.has.domain.entity.Department;
import org.example.has.domain.entity.Doctor;
import org.example.has.domain.entity.User;
import org.example.has.domain.enums.Role;
import org.example.has.dto.request.DoctorRequest;
import org.example.has.dto.response.DoctorResponse;
import org.example.has.exception.DuplicateResourceException;
import org.example.has.exception.ResourceNotFoundException;
import org.example.has.repository.DoctorRepository;
import org.example.has.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing doctors.
 */
@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Finds all doctors.
     *
     * @return list of doctors
     */
    public List<DoctorResponse> findAll() {
        return doctorRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Finds doctor by ID.
     *
     * @param id the doctor ID
     * @return the doctor details
     */
    public DoctorResponse findById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bác sĩ", "id", id));
        return toResponse(doctor);
    }

    /**
     * Finds doctors belonging to a department.
     *
     * @param departmentId the department ID
     * @return list of doctors belonging to the department
     */
    public List<DoctorResponse> findByDepartment(Long departmentId) {
        return doctorRepository.findByDepartmentId(departmentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Searches doctors by keyword.
     *
     * @param keyword the search keyword
     * @return list of matching doctors
     */
    public List<DoctorResponse> search(String keyword) {
        return doctorRepository.findByFullNameContainingIgnoreCase(keyword).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new doctor.
     *
     * @param request the doctor request data
     * @return the created doctor
     */
    @Transactional
    public DoctorResponse create(DoctorRequest request) {
        if (doctorRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Bác sĩ", "code", request.getCode());
        }

        User user = null;
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new DuplicateResourceException("User", "username", request.getUsername());
            }
            user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .fullName(request.getFullName())
                    .role(Role.DOCTOR)
                    .enabled(true)
                    .build();
            user = userRepository.save(user);
        }

        Doctor doctor = Doctor.builder()
                .code(request.getCode())
                .fullName(request.getFullName())
                .gender(request.getGender())
                .phone(request.getPhone())
                .email(request.getEmail())
                .specialty(request.getSpecialty())
                .user(user)
                .build();

        doctor = doctorRepository.save(doctor);
        return toResponse(doctor);
    }

    /**
     * Updates doctor details.
     *
     * @param id      the doctor ID to update
     * @param request the updated doctor data
     * @return the updated doctor
     */
    @Transactional
    public DoctorResponse update(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bác sĩ", "id", id));

        doctor.setFullName(request.getFullName());
        doctor.setGender(request.getGender());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());
        doctor.setSpecialty(request.getSpecialty());

        doctor = doctorRepository.save(doctor);
        return toResponse(doctor);
    }

    /**
     * Deletes doctor by ID.
     *
     * @param id the doctor ID to delete
     */
    @Transactional
    public void delete(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bác sĩ", "id", id);
        }
        doctorRepository.deleteById(id);
    }

    private DoctorResponse toResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .code(doctor.getCode())
                .fullName(doctor.getFullName())
                .gender(doctor.getGender())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .specialty(doctor.getSpecialty())
                .departmentId(doctor.getDepartment() != null ? doctor.getDepartment().getId() : null)
                .departmentName(doctor.getDepartment() != null ? doctor.getDepartment().getName() : null)
                .build();
    }
}
