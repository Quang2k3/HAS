package org.example.has.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Yêu cầu đặt lịch hẹn khám bệnh")
public class AppointmentRequest {

    @NotNull(message = "Bệnh nhân không được để trống")
    @Schema(description = "ID duy nhất của bệnh nhân. Lấy từ API Danh sách bệnh nhân (GET `/api/patients`) hoặc sau khi tạo bệnh nhân thành công.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long patientId;

    @NotNull(message = "Bác sĩ không được để trống")
    @Schema(description = "ID duy nhất của bác sĩ khám. Lấy từ API Danh sách bác sĩ (GET `/api/doctors`) hoặc Danh sách bác sĩ theo khoa.", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long doctorId;

    @NotNull(message = "Ngày khám không được để trống")
    @Schema(description = "Ngày hẹn khám bệnh (định dạng: yyyy-MM-dd)", example = "2026-06-18", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate appointmentDate;

    @NotNull(message = "Giờ khám không được để trống")
    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", example = "14:30", description = "Giờ hẹn khám bệnh cụ thể (định dạng HH:mm)", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime appointmentTime;

    @Schema(description = "Lý do khám bệnh hoặc các triệu chứng ban đầu của bệnh nhân", example = "Đau họng kéo dài, kèm sốt nhẹ")
    private String reason;
}
