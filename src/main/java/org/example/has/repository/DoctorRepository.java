package org.example.has.repository;

import org.example.has.domain.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByCode(String code);

    List<Doctor> findByDepartmentId(Long departmentId);

    List<Doctor> findByFullNameContainingIgnoreCase(String fullName);

    long countByDepartmentId(Long departmentId);

    Optional<Doctor> findByUserId(Long userId);

    boolean existsByCode(String code);
}
