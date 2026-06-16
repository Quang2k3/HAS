package org.example.has.repository;

import org.example.has.domain.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    Optional<MedicalRecord> findByCode(String code);

    Optional<MedicalRecord> findByAppointmentId(Long appointmentId);

    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.appointment.patient.id = :patientId ORDER BY mr.createdAt DESC")
    List<MedicalRecord> findByPatientId(@Param("patientId") Long patientId);

    boolean existsByCode(String code);
}
