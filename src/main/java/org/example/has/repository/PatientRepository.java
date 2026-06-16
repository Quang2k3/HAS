package org.example.has.repository;

import org.example.has.domain.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByCode(String code);

    List<Patient> findByFullNameContainingIgnoreCase(String fullName);

    Optional<Patient> findByPhone(String phone);

    Optional<Patient> findByUserId(Long userId);

    boolean existsByCode(String code);
}
