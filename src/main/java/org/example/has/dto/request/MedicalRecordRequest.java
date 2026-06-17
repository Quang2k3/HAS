package org.example.has.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Yêu cầu tạo mới hoặc cập nhật hồ sơ bệnh án")
public class MedicalRecordRequest {

    @Schema(description = "ID của lịch hẹn khám đã khám xong (Trạng thái của lịch hẹn này phải là COMPLETED). Lấy từ API lịch hẹn `GET /api/appointments`.", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long appointmentId;

    @Schema(description = "Chẩn đoán bệnh của bác sĩ", example = "Tăng huyết áp độ 1, rối loạn lipid máu nhẹ")
    private String diagnosis;

    @Schema(description = "Kết quả khám lâm sàng, cận lâm sàng, xét nghiệm (nếu có)", example = "Huyết áp đo tại phòng khám 145/90 mmHg. Nhịp tim 82 lần/phút.")
    private String result;

    @Schema(description = "Đơn thuốc chi tiết (tên thuốc, số lượng, cách dùng)", example = "1. Amlodipin 5mg - 30 viên (uống 1 viên vào buổi sáng sau ăn)\n2. Atorvastatin 10mg - 30 viên (uống 1 viên vào buổi tối trước khi đi ngủ)")
    private String prescription;

    @Schema(description = "Lời dặn của bác sĩ và chế độ sinh hoạt, dinh dưỡng hẹn tái khám", example = "Ăn giảm muối. Tái khám sau 30 ngày hoặc khi có triệu chứng đau đầu.")
    private String notes;
}
