package org.example.has.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.has.dto.request.AppointmentRequest;
import org.example.has.dto.response.AppointmentResponse;
import org.example.has.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for managing patient appointments.
 */
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "5. Appointment Management", description = "API quản lý đặt lịch hẹn khám bệnh. Hỗ trợ đặt lịch, hủy lịch, hoàn thành lịch khám và tra cứu lịch hẹn theo bệnh nhân, bác sĩ hoặc theo ngày.")
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * Gets all appointments (ADMIN).
     *
     * @return the list of appointments
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Danh sách toàn bộ lịch hẹn khám (ADMIN)",
            description = "Trả về danh sách tất cả các lịch hẹn khám có trên hệ thống từ trước tới nay. Yêu cầu quyền ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện hành động này (Yêu cầu ADMIN)")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<List<AppointmentResponse>>> getAll() {
        List<AppointmentResponse> appointments = appointmentService.findAll();
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", appointments));
    }

    /**
     * Gets appointment details by ID.
     *
     * @param id the appointment ID
     * @return the appointment details
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @Operation(
            summary = "Xem chi tiết lịch hẹn theo ID",
            description = "Trả về thông tin chi tiết một lịch hẹn cụ thể. Yêu cầu quyền ADMIN, DOCTOR hoặc chính PATIENT sở hữu lịch hẹn đó."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy chi tiết thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy lịch hẹn với ID đã cung cấp"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập thông tin lịch hẹn này")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<AppointmentResponse>> getById(
            @PathVariable @Parameter(description = "ID của lịch hẹn khám. Lấy từ các danh sách lịch hẹn hoặc kết quả đặt lịch.", example = "1") Long id) {
        AppointmentResponse appointment = appointmentService.findById(id);
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", appointment));
    }

    /**
     * Gets appointments of a patient.
     *
     * @param patientId the patient ID
     * @return list of appointments
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @Operation(
            summary = "Lấy danh sách lịch hẹn theo bệnh nhân",
            description = "Lấy lịch sử khám và các lịch hẹn sắp tới của một bệnh nhân cụ thể."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<List<AppointmentResponse>>> getByPatient(
            @PathVariable @Parameter(description = "ID bệnh nhân cần tra cứu lịch hẹn. Lấy từ API danh sách bệnh nhân `GET /api/patients`.", example = "2") Long patientId) {
        List<AppointmentResponse> appointments = appointmentService.findByPatient(patientId);
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", appointments));
    }

    /**
     * Gets appointments of a doctor.
     *
     * @param doctorId the doctor ID
     * @return list of appointments
     */
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Operation(
            summary = "Lấy danh sách lịch hẹn theo bác sĩ (ADMIN, DOCTOR)",
            description = "Lấy lịch hẹn khám sắp tới hoặc lịch sử các ca khám của một bác sĩ cụ thể."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<List<AppointmentResponse>>> getByDoctor(
            @PathVariable @Parameter(description = "ID bác sĩ cần xem lịch khám. Lấy từ API danh sách bác sĩ `GET /api/doctors`.", example = "2") Long doctorId) {
        List<AppointmentResponse> appointments = appointmentService.findByDoctor(doctorId);
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", appointments));
    }

    /**
     * Gets appointments in a day.
     *
     * @param date the checkup date (format: yyyy-MM-dd)
     * @return list of appointments
     */
    @GetMapping("/date")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Operation(
            summary = "Lấy danh sách lịch hẹn trong ngày (ADMIN, DOCTOR)",
            description = "Lấy tất cả lịch hẹn trong một ngày cụ thể (định dạng yyyy-MM-dd)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<List<AppointmentResponse>>> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            @Parameter(description = "Ngày khám cần lọc lịch hẹn (định dạng yyyy-MM-dd).", example = "2026-06-18") LocalDate date) {
        List<AppointmentResponse> appointments = appointmentService.findByDate(date);
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", appointments));
    }

    /**
     * Books a new appointment. System checks for schedule conflicts.
     *
     * @param request the appointment request data
     * @return the created appointment
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    @Operation(
            summary = "Đặt lịch hẹn khám bệnh mới",
            description = "Bệnh nhân tự đặt lịch hẹn khám hoặc Admin đặt lịch hộ. Hệ thống tự động kiểm tra xung đột thời gian (Bác sĩ không thể có 2 lịch hẹn trùng ngày, trùng giờ có cùng trạng thái hoạt động)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Đặt lịch hẹn thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc bác sĩ bị trùng lịch khám vào khung giờ này"),
            @ApiResponse(responseCode = "403", description = "Không có quyền đặt lịch (Yêu cầu ADMIN hoặc PATIENT)")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<AppointmentResponse>> create(
            @Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse appointment = appointmentService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(org.example.has.dto.response.ApiResponse.success("Đặt lịch khám thành công", appointment));
    }

    /**
     * Cancels an appointment (only when status is SCHEDULED).
     *
     * @param id the appointment ID to cancel
     * @return the cancelled appointment
     */
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    @Operation(
            summary = "Hủy lịch hẹn khám bệnh",
            description = "Hủy bỏ lịch khám đã hẹn trước đó. Chỉ có thể hủy lịch khi trạng thái hiện tại là SCHEDULED (Chưa khám). Yêu cầu quyền ADMIN hoặc chính bệnh nhân đã đặt lịch."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hủy lịch thành công"),
            @ApiResponse(responseCode = "400", description = "Lịch khám không ở trạng thái SCHEDULED nên không thể hủy"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy lịch khám cần hủy")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<AppointmentResponse>> cancel(
            @PathVariable @Parameter(description = "ID của lịch hẹn cần hủy. Lấy từ API lịch hẹn của bệnh nhân `GET /api/appointments/patient/{patientId}`.", example = "1") Long id) {
        AppointmentResponse appointment = appointmentService.cancel(id);
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Hủy lịch khám thành công", appointment));
    }

    /**
     * Marks an appointment as completed.
     *
     * @param id the appointment ID to complete
     * @return the completed appointment
     */
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Operation(
            summary = "Hoàn thành ca khám bệnh (DOCTOR, ADMIN)",
            description = "Đánh dấu lịch khám đã hoàn tất sau khi bác sĩ thực hiện khám xong cho bệnh nhân. Trạng thái lịch khám chuyển thành COMPLETED. Cho phép tiếp tục lập hồ sơ bệnh án."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hoàn thành ca khám thành công"),
            @ApiResponse(responseCode = "400", description = "Lịch khám đã bị hủy hoặc đã hoàn tất trước đó"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy lịch khám")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<AppointmentResponse>> complete(
            @PathVariable @Parameter(description = "ID của lịch hẹn khám cần hoàn thành. Lấy từ API danh sách lịch hẹn của bác sĩ `GET /api/appointments/doctor/{doctorId}`.", example = "2") Long id) {
        AppointmentResponse appointment = appointmentService.complete(id);
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Hoàn thành lịch khám", appointment));
    }
}
