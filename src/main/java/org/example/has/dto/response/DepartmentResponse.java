package org.example.has.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Thông tin phản hồi chi tiết về khoa khám bệnh")
public class DepartmentResponse {

    @Schema(description = "ID duy nhất của khoa trong cơ sở dữ liệu. Sử dụng làm khóa ngoại trong các thao tác lập hồ sơ bác sĩ, đặt lịch khám.", example = "1")
    private Long id;

    @Schema(description = "Mã viết tắt duy nhất của khoa", example = "DEP001")
    private String code;

    @Schema(description = "Tên đầy đủ của khoa khám bệnh", example = "Nội tổng quát")
    private String name;

    @Schema(description = "Mô tả chi tiết về khoa", example = "Khoa khám và điều trị các bệnh nội khoa tổng quát")
    private String description;

    @Schema(description = "Tổng số lượng bác sĩ trực thuộc khoa khám bệnh này", example = "5")
    private int doctorCount;
}
