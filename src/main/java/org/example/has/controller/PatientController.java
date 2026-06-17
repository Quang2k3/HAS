package org.example.has.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.has.dto.request.PatientRequest;
import org.example.has.dto.response.PatientResponse;
import org.example.has.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing patient profiles.
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "4. Patient Management", description = "API quản lý thông tin bệnh nhân. Hỗ trợ tra cứu hồ sơ, tìm kiếm danh sách bệnh nhân cho bác sĩ và quản trị viên, và chỉnh sửa thông tin cá nhân.")
public class PatientController {

    private final PatientService patientService;

    /**
     * Gets a list of all patients.
     *
     * @return the list of patients
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Operation(
            summary = "Lấy danh sách tất cả bệnh nhân (ADMIN, DOCTOR)",
            description = "Trả về danh sách toàn bộ bệnh nhân đã đăng ký trên hệ thống kèm thông tin ngày sinh, giới tính, số điện thoại, địa chỉ. Yêu cầu quyền ADMIN hoặc DOCTOR."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện (Yêu cầu ADMIN/DOCTOR)")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<List<PatientResponse>>> getAll() {
        List<PatientResponse> patients = patientService.findAll();
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", patients));
    }

    /**
     * Gets patient details by ID.
     *
     * @param id the patient ID
     * @return the patient details
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @Operation(
            summary = "Xem thông tin chi tiết bệnh nhân theo ID",
            description = "Trả về thông tin chi tiết của bệnh nhân dựa trên ID. Yêu cầu quyền ADMIN, DOCTOR hoặc chính bệnh nhân sở hữu hồ sơ."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin bệnh nhân thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bệnh nhân với ID cung cấp"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập hồ sơ này")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<PatientResponse>> getById(
            @PathVariable @Parameter(description = "ID duy nhất của bệnh nhân trong hệ thống. Lấy từ API danh sách bệnh nhân `GET /api/patients` hoặc kết quả đăng ký tài khoản.", example = "1") Long id) {
        PatientResponse patient = patientService.findById(id);
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", patient));
    }

    /**
     * Searches patients by name containing keyword.
     *
     * @param keyword the search keyword
     * @return list of matching patients
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Operation(
            summary = "Tìm kiếm bệnh nhân theo tên (ADMIN, DOCTOR)",
            description = "Tìm bệnh nhân theo từ khóa họ tên. API này phục vụ cho việc bác sĩ hoặc nhân viên y tế tìm hồ sơ bệnh nhân để lên lịch hẹn khám hoặc lập bệnh án."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm kiếm thành công")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<List<PatientResponse>>> search(
            @RequestParam @Parameter(description = "Từ khóa tìm kiếm theo tên bệnh nhân.", example = "Vy") String keyword) {
        List<PatientResponse> patients = patientService.search(keyword);
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", patients));
    }

    /**
     * Creates a new patient. Requires ADMIN role.
     *
     * @param request the patient request data
     * @return the created patient details
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Tạo mới hồ sơ bệnh nhân (ADMIN)",
            description = "Tạo mới một hồ sơ bệnh nhân. Yêu cầu quyền ADMIN. Lưu ý: Bệnh nhân cũng có thể tự tạo tài khoản thông qua API đăng ký công khai `POST /api/auth/register`."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo bệnh nhân thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc mã bệnh nhân (code) đã tồn tại"),
            @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện hành động này (Yêu cầu ADMIN)")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<PatientResponse>> create(
            @Valid @RequestBody PatientRequest request) {
        PatientResponse patient = patientService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(org.example.has.dto.response.ApiResponse.success("Tạo bệnh nhân thành công", patient));
    }

    /**
     * Updates patient details. Requires ADMIN role or matching user ID.
     *
     * @param id the patient ID to update
     * @param request the updated patient data
     * @return the updated patient details
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    @Operation(
            summary = "Cập nhật hồ sơ bệnh nhân (ADMIN, PATIENT)",
            description = "Cập nhật các thông tin cá nhân của bệnh nhân như họ tên, ngày sinh, giới tính, số điện thoại, địa chỉ. Yêu cầu quyền ADMIN hoặc chính bệnh nhân sở hữu tài khoản."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy hồ sơ bệnh nhân cần cập nhật"),
            @ApiResponse(responseCode = "403", description = "Không có quyền cập nhật hồ sơ này")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<PatientResponse>> update(
            @PathVariable @Parameter(description = "ID của bệnh nhân cần chỉnh sửa. Lấy từ API danh sách bệnh nhân `GET /api/patients`.", example = "1") Long id,
            @Valid @RequestBody PatientRequest request) {
        PatientResponse patient = patientService.update(id, request);
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Cập nhật bệnh nhân thành công", patient));
    }

    /**
     * Deletes a patient by ID. Requires ADMIN role.
     *
     * @param id the patient ID to delete
     * @return success message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Xóa thông tin bệnh nhân (ADMIN)",
            description = "Xóa hoàn toàn hồ sơ bệnh nhân khỏi hệ thống dựa trên ID. Yêu cầu quyền ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Xóa bệnh nhân thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bệnh nhân cần xóa"),
            @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện hành động này (Yêu cầu ADMIN)")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<Void>> delete(
            @PathVariable @Parameter(description = "ID của bệnh nhân cần xóa. Lấy từ API danh sách bệnh nhân `GET /api/patients`.", example = "1") Long id) {
        patientService.delete(id);
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Xóa bệnh nhân thành công", null));
    }
}
