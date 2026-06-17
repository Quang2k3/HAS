package org.example.has.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.has.dto.response.AppointmentResponse;
import org.example.has.dto.response.StatisticsResponse;
import org.example.has.service.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for hospital analytics and reporting.
 * (Administrator metrics and stats dashboard)
 */
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Tag(name = "6. Reports & Statistics", description = "API báo cáo và thống kê hệ thống. Cung cấp dữ liệu thống kê tổng quan (số lượng bệnh nhân, bác sĩ, lịch hẹn) dành riêng cho Administrator quản lý hệ thống.")
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * Gets system overview statistics. Requires ADMIN role.
     *
     * @return system overview statistics
     */
    @GetMapping("/overview")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Xem thống kê tổng quan hệ thống (ADMIN)",
            description = "Trả về tổng số lượng bệnh nhân, bác sĩ, khoa khám bệnh, lịch hẹn theo các trạng thái (Scheduled, Completed, Cancelled) và cơ cấu phân bổ bác sĩ theo từng khoa. API dành riêng cho vai trò ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy báo cáo tổng quan thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập (Yêu cầu ADMIN)")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<StatisticsResponse>> getOverview() {
        StatisticsResponse stats = statisticsService.getOverview();
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", stats));
    }

    /**
     * Gets appointments count of a specific date. Requires ADMIN role.
     *
     * @param date the checkup date (format: yyyy-MM-dd)
     * @return appointment count
     */
    @GetMapping("/date")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Đếm số lượng lịch hẹn khám theo ngày cụ thể (ADMIN)",
            description = "Trả về tổng số ca khám bệnh đã được lên lịch trong ngày chỉ định. Yêu cầu quyền ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy số lượng thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập (Yêu cầu ADMIN)")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<Long>> getAppointmentCountByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            @Parameter(description = "Ngày cần thống kê lịch hẹn (Định dạng: yyyy-MM-dd).", example = "2026-06-16") LocalDate date) {
        long count = statisticsService.getAppointmentCountByDate(date);
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", count));
    }

    /**
     * Gets appointments list of a doctor. Requires ADMIN or DOCTOR role.
     *
     * @param doctorId the doctor ID
     * @return list of appointments
     */
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Operation(
            summary = "Lấy danh sách lịch hẹn khám của một bác sĩ cụ thể (ADMIN, DOCTOR)",
            description = "Trả về tất cả các lịch hẹn của một bác sĩ dựa theo ID bác sĩ, phục vụ việc lập lịch trực hoặc xem hàng đợi bệnh nhân chờ khám."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách lịch hẹn thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bác sĩ")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<List<AppointmentResponse>>> getAppointmentsByDoctor(
            @PathVariable @Parameter(description = "ID của bác sĩ cần lấy danh sách lịch hẹn. Lấy từ danh sách bác sĩ `GET /api/doctors`.", example = "2") Long doctorId) {
        List<AppointmentResponse> appointments = statisticsService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", appointments));
    }
}
