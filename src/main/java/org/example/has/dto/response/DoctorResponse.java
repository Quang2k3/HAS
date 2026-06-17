package org.example.has.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.has.domain.enums.Gender;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Thông tin phản hồi chi tiết về bác sĩ")
public class DoctorResponse {

    @Schema(description = "ID duy nhất của bác sĩ trong hệ thống. Dùng để truyền vào các API đặt lịch khám, thống kê.", example = "1")
    private Long id;

    @Schema(description = "Mã số duy nhất của bác sĩ", example = "DOC001")
    private String code;

    @Schema(description = "Họ và tên đầy đủ của bác sĩ", example = "Nguyễn Văn Minh")
    private String fullName;

    @Schema(description = "Giới tính của bác sĩ (MALE, FEMALE, OTHER)", example = "MALE")
    private Gender gender;

    @Schema(description = "Số điện thoại di động liên hệ", example = "0911223344")
    private String phone;

    @Schema(description = "Địa chỉ email liên hệ", example = "minh.nv@hospital.com")
    private String email;

    @Schema(description = "Chuyên khoa chuyên sâu của bác sĩ", example = "Nội khoa tổng quát")
    private String specialty;

    @Schema(description = "Tên khoa khám bệnh mà bác sĩ trực thuộc", example = "Nội tổng quát")
    private String departmentName;

    @Schema(description = "ID khoa khám bệnh mà bác sĩ trực thuộc. Lấy từ danh sách khoa `GET /api/departments`.", example = "1")
    private Long departmentId;
}
