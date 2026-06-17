package org.example.has.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Thông tin hồ sơ bệnh nhân chi tiết")
public class PatientResponse {

    @Schema(description = "ID duy nhất của bệnh nhân trong hệ thống. Dùng làm khóa ngoại để tham chiếu trong các lịch hẹn khám, hồ sơ khám.", example = "1")
    private Long id;

    @Schema(description = "Mã số hồ sơ duy nhất của bệnh nhân", example = "PAT001")
    private String code;

    @Schema(description = "Họ và tên đầy đủ của bệnh nhân", example = "Trần Thị Vy")
    private String fullName;

    @Schema(description = "Ngày tháng năm sinh (định dạng yyyy-MM-dd)", example = "1995-08-12")
    private LocalDate dateOfBirth;

    @Schema(description = "Giới tính (MALE, FEMALE, OTHER)", example = "FEMALE")
    private Gender gender;

    @Schema(description = "Số điện thoại di động liên lạc", example = "0912345678")
    private String phone;

    @Schema(description = "Địa chỉ thường trú hiện tại", example = "123 Nguyễn Trãi, Thanh Xuân, Hà Nội")
    private String address;
}
