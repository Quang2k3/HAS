package org.example.has.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.has.domain.enums.Gender;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Yêu cầu tạo mới hoặc cập nhật hồ sơ bệnh nhân")
public class PatientRequest {

    @NotBlank(message = "Mã bệnh nhân không được để trống")
    @Schema(description = "Mã duy nhất của bệnh nhân (ví dụ: PAT001, PAT002)", example = "PAT001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    @NotBlank(message = "Họ tên không được để trống")
    @Schema(description = "Họ và tên đầy đủ của bệnh nhân", example = "Trần Thị Vy", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullName;

    @Schema(description = "Ngày sinh của bệnh nhân (định dạng: yyyy-MM-dd)", example = "1995-08-12")
    private LocalDate dateOfBirth;

    @Schema(description = "Giới tính của bệnh nhân (MALE, FEMALE, OTHER)", example = "FEMALE")
    private Gender gender;

    @Schema(description = "Số điện thoại liên hệ di động", example = "0912345678")
    private String phone;

    @Schema(description = "Địa chỉ thường trú hiện tại", example = "123 Nguyễn Trãi, Thanh Xuân, Hà Nội")
    private String address;
}
