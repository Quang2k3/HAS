package org.example.has.service;

import lombok.RequiredArgsConstructor;
import org.example.has.domain.entity.Department;
import org.example.has.dto.request.DepartmentRequest;
import org.example.has.dto.response.DepartmentResponse;
import org.example.has.exception.DuplicateResourceException;
import org.example.has.exception.ResourceNotFoundException;
import org.example.has.repository.DepartmentRepository;
import org.example.has.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing departments.
 */
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;

    /**
 * Finds all departments.
 *
 * @return list of departments
 */
    public List<DepartmentResponse> findAll() {
        return departmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
 * Finds department by ID.
 *
 * @param id the department ID
 * @return the department details
 */
    public DepartmentResponse findById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khoa", "id", id));
        return toResponse(department);
    }

    public DepartmentResponse findByCode(String code) {
        Department department = departmentRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Khoa", "code", code));
        return toResponse(department);
    }

    /**
 * Searches departments by keyword.
 *
 * @param keyword the search keyword
 * @return list of matching departments
 */
    public List<DepartmentResponse> search(String keyword) {
        return departmentRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
 * Creates a new department.
 *
 * @param request the department request data
 * @return the created department
 */
    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Khoa", "code", request.getCode());
        }

        Department department = Department.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .build();

        department = departmentRepository.save(department);
        return toResponse(department);
    }

    /**
 * Updates department details.
 *
 * @param id the ID of the department to update
 * @param request the updated department data
 * @return the updated department
 */
    @Transactional
    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khoa", "id", id));

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        department = departmentRepository.save(department);
        return toResponse(department);
    }

    /**
 * Deletes department by ID.
 *
 * @param id the department ID to delete
 */
    @Transactional
    public void delete(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Khoa", "id", id);
        }
        departmentRepository.deleteById(id);
    }

    private DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .code(department.getCode())
                .name(department.getName())
                .description(department.getDescription())
                .doctorCount((int) doctorRepository.countByDepartmentId(department.getId()))
                .build();
    }
}
