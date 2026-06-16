package org.example.has.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordRequest {

    private Long appointmentId;

    private String diagnosis;

    private String result;

    private String prescription;

    private String notes;
}
