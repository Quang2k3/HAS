package org.example.has.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Số liệu thống kê và báo cáo tổng quan hệ thống")
public class StatisticsResponse {

    @Schema(description = "Tổng số lượng bệnh nhân đã đăng ký trên hệ thống", example = "150")
    private long totalPatients;

    @Schema(description = "Tổng số lượng bác sĩ trong bệnh viện", example = "25")
    private long totalDoctors;

    @Schema(description = "Tổng số khoa khám bệnh", example = "5")
    private long totalDepartments;

    @Schema(description = "Tổng số lượng lịch hẹn khám đã được tạo", example = "1200")
    private long totalAppointments;

    @Schema(description = "Số lượng lịch hẹn đang ở trạng thái chờ khám (SCHEDULED)", example = "45")
    private long scheduledAppointments;

    @Schema(description = "Số lượng ca khám đã hoàn thành (COMPLETED)", example = "1050")
    private long completedAppointments;

    @Schema(description = "Số lượng lịch hẹn đã bị hủy (CANCELLED)", example = "105")
    private long cancelledAppointments;

    @Schema(description = "Bản đồ phân bổ số lượng bác sĩ theo tên từng khoa", example = "{\"Nội tổng quát\": 5, \"Tim mạch\": 4}")
    private Map<String, Long> doctorsByDepartment;
}
