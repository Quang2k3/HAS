package org.example.has.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.has.domain.enums.Gender;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponse {

    private Long id;

    private String code;

    private String fullName;

    private Gender gender;

    private String phone;

    private String email;

    private String specialty;

    private String departmentName;

    private Long departmentId;
}
