package org.example.has.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.has.domain.enums.Gender;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequest {

    @NotBlank(message = "Mã bác sĩ không được để trống")
    private String code;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    private Gender gender;

    private String phone;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String specialty;

    private String username;

    private String password;
}
