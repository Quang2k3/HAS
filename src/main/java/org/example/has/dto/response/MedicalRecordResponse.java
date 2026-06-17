package org.example.has.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Thông tin chi tiết hồ sơ bệnh án trả về")
public class MedicalRecordResponse {

    @Schema(description = "ID duy nhất của hồ sơ bệnh án trong hệ thống", example = "1")
    private Long id;

    @Schema(description = "Mã số hồ sơ bệnh án duy nhất tự sinh (ví dụ: MR + timestamp hoặc MR001)", example = "MR001")
    private String code;

    @Schema(description = "Chẩn đoán bệnh lý của bác sĩ", example = "Tăng huyết áp độ 1, rối loạn lipid máu nhẹ")
    private String diagnosis;

    @Schema(description = "Kết quả khám lâm sàng, chỉ số đo đạc", example = "Huyết áp đo tại phòng khám 145/90 mmHg. Nhịp tim 82 lần/phút.")
    private String result;

    @Schema(description = "Đơn thuốc chi tiết", example = "1. Amlodipin 5mg - 30 viên (uống 1 viên vào buổi sáng sau ăn)\n2. Atorvastatin 10mg - 30 viên (uống 1 viên vào buổi tối trước khi đi ngủ)")
    private String prescription;

    @Schema(description = "Lời dặn dò tái khám của bác sĩ", example = "Ăn giảm muối. Tái khám sau 30 ngày hoặc khi có triệu chứng tức ngực.")
    private String notes;

    @Schema(description = "Thời gian lập hồ sơ bệnh án (yyyy-MM-dd HH:mm:ss)", example = "2026-06-16T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "ID của lịch hẹn khám tương ứng với bệnh án này. Tham chiếu tới API `GET /api/appointments`.", example = "2")
    private Long appointmentId;

    @Schema(description = "Mã của lịch hẹn khám tương ứng", example = "APP002")
    private String appointmentCode;

    @Schema(description = "Họ tên bệnh nhân", example = "Nguyễn Hùng Cường")
    private String patientName;

    @Schema(description = "Họ tên bác sĩ thực hiện khám và lập bệnh án", example = "Lê Thị Lan")
    private String doctorName;
}
