package org.example.has.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.has.domain.enums.Gender;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponse {

    private Long id;

    private String code;

    private String fullName;

    private LocalDate dateOfBirth;

    private Gender gender;

    private String phone;

    private String address;
}
