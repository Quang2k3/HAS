package org.example.has.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.has.domain.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Thông tin phản hồi chi tiết về lịch hẹn khám bệnh")
public class AppointmentResponse {

    @Schema(description = "ID duy nhất của lịch hẹn khám. Dùng để cập nhật trạng thái hoặc lập hồ sơ bệnh án.", example = "1")
    private Long id;

    @Schema(description = "Mã số định danh lịch hẹn duy nhất (tự động sinh ví dụ: APP001 hoặc APP + timestamp)", example = "APP001")
    private String code;

    @Schema(description = "Ngày hẹn khám bệnh (yyyy-MM-dd)", example = "2026-06-18")
    private LocalDate appointmentDate;

    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", example = "14:30", description = "Giờ hẹn khám bệnh (HH:mm)")
    private LocalTime appointmentTime;

    @Schema(description = "Trạng thái lịch hẹn khám (SCHEDULED: Đã lên lịch, COMPLETED: Đã khám xong, CANCELLED: Đã hủy)", example = "SCHEDULED")
    private AppointmentStatus status;

    @Schema(description = "Lý do khám bệnh hoặc triệu chứng", example = "Đau họng kéo dài, kèm sốt nhẹ")
    private String reason;

    @Schema(description = "ID của bệnh nhân đặt lịch. Tham chiếu tới API danh sách bệnh nhân `GET /api/patients`.", example = "1")
    private Long patientId;

    @Schema(description = "Họ tên đầy đủ của bệnh nhân khám", example = "Trần Thị Vy")
    private String patientName;

    @Schema(description = "ID của bác sĩ tiếp nhận khám. Tham chiếu tới API danh sách bác sĩ `GET /api/doctors`.", example = "2")
    private Long doctorId;

    @Schema(description = "Họ tên đầy đủ của bác sĩ khám", example = "Nguyễn Văn Minh")
    private String doctorName;

    @Schema(description = "Tên khoa khám bệnh của bác sĩ tiếp nhận", example = "Tai mũi họng")
    private String departmentName;
}
