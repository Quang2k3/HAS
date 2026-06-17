package org.example.has.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Yêu cầu tạo mới hoặc cập nhật khoa khám bệnh")
public class DepartmentRequest {

    @NotBlank(message = "Mã khoa không được để trống")
    @Schema(description = "Mã viết tắt duy nhất của khoa khám bệnh (ví dụ: DEP001, DEP002)", example = "DEP001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    @NotBlank(message = "Tên khoa không được để trống")
    @Schema(description = "Tên đầy đủ của khoa khám bệnh", example = "Nội tổng quát", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Mô tả sơ lược về chức năng và nhiệm vụ khám chữa bệnh của khoa", example = "Khoa khám và điều trị các bệnh nội khoa tổng quát")
    private String description;
}
