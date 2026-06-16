package org.example.has.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordResponse {

    private Long id;

    private String code;

    private String diagnosis;

    private String result;

    private String prescription;

    private String notes;

    private LocalDateTime createdAt;

    private Long appointmentId;

    private String appointmentCode;

    private String patientName;

    private String doctorName;
}
