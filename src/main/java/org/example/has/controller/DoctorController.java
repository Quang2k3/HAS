package org.example.has.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.has.dto.request.DoctorRequest;
import org.example.has.dto.response.DoctorResponse;
import org.example.has.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing doctor profiles.
 */
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "3. Doctor Management", description = "API quản lý hồ sơ bác sĩ. Cho phép tra cứu danh sách bác sĩ, tìm kiếm bác sĩ công khai và quản trị thêm/sửa/xóa thông tin bác sĩ đi kèm tài khoản hệ thống (yêu cầu ADMIN).")
public class DoctorController {

        private final DoctorService doctorService;

        /**
         * Gets a list of all doctors.
         *
         * @return the list of doctors
         */
        @GetMapping
        @Operation(summary = "Lấy danh sách tất cả bác sĩ", description = "Trả về danh sách tất cả các bác sĩ hiện có trong hệ thống cùng thông tin chi tiết như chuyên khoa, số điện thoại, email và khoa trực thuộc.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lấy danh sách bác sĩ thành công")
        })
        public ResponseEntity<org.example.has.dto.response.ApiResponse<List<DoctorResponse>>> getAll() {
                List<DoctorResponse> doctors = doctorService.findAll();
                return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", doctors));
        }

        /**
         * Gets doctor details by ID.
         *
         * @param id the doctor ID
         * @return the doctor details
         */
        @GetMapping("/{id}")
        @Operation(summary = "Xem chi tiết thông tin bác sĩ theo ID", description = "Trả về thông tin chi tiết của một bác sĩ cụ thể dựa trên ID hệ thống tự sinh.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Tìm thấy bác sĩ thành công"),
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy bác sĩ với ID đã cung cấp")
        })
        public ResponseEntity<org.example.has.dto.response.ApiResponse<DoctorResponse>> getById(
                        @PathVariable @Parameter(description = "ID duy nhất của bác sĩ. Lấy từ API danh sách bác sĩ `GET /api/doctors`.", example = "1") Long id) {
                DoctorResponse doctor = doctorService.findById(id);
                return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", doctor));
        }

        /**
         * Gets doctors belonging to a specific department.
         *
         * @param departmentId the department ID
         * @return list of doctors belonging to the department
         */
        @GetMapping("/department/{departmentId}")
        @Operation(summary = "Lấy danh sách bác sĩ theo khoa", description = "Lọc danh sách các bác sĩ thuộc về khoa khám bệnh được chỉ định bởi ID khoa.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lấy danh sách bác sĩ theo khoa thành công"),
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy khoa khám bệnh tương ứng")
        })
        public ResponseEntity<org.example.has.dto.response.ApiResponse<List<DoctorResponse>>> getByDepartment(
                        @PathVariable @Parameter(description = "ID của khoa khám bệnh cần lọc bác sĩ. Lấy từ API danh sách khoa `GET /api/departments`.", example = "2") Long departmentId) {
                List<DoctorResponse> doctors = doctorService.findByDepartment(departmentId);
                return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", doctors));
        }

        /**
         * Searches doctors by name containing keyword.
         *
         * @param keyword the search keyword
         * @return list of matching doctors
         */
        @GetMapping("/search")
        @Operation(summary = "Tìm kiếm bác sĩ theo tên", description = "Tìm kiếm danh sách bác sĩ có họ tên chứa từ khóa tìm kiếm (không phân biệt chữ hoa, chữ thường).")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Tìm kiếm thành công")
        })
        public ResponseEntity<org.example.has.dto.response.ApiResponse<List<DoctorResponse>>> search(
                        @RequestParam @Parameter(description = "Từ khóa tìm kiếm theo tên bác sĩ (ví dụ: 'Minh', 'Lan')", example = "Minh") String keyword) {
                List<DoctorResponse> doctors = doctorService.search(keyword);
                return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Thành công", doctors));
        }

        /**
         * Creates a new doctor. Requires ADMIN role.
         *
         * @param request the doctor request data
         * @return the created doctor details
         */
        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Thêm mới bác sĩ (ADMIN)", description = "Thêm một bác sĩ mới vào hệ thống, đồng thời tự động tạo tài khoản đăng nhập đi kèm với vai trò DOCTOR (nếu cung cấp thông tin username/password). Yêu cầu quyền ADMIN.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Tạo bác sĩ thành công"),
                        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc mã bác sĩ/tên đăng nhập đã tồn tại trên hệ thống"),
                        @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện hành động này (Yêu cầu ADMIN)")
        })
        public ResponseEntity<org.example.has.dto.response.ApiResponse<DoctorResponse>> create(
                        @Valid @RequestBody DoctorRequest request) {
                DoctorResponse doctor = doctorService.create(request);
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(org.example.has.dto.response.ApiResponse.success("Tạo bác sĩ thành công",
                                                doctor));
        }

        /**
         * Updates doctor details. Requires ADMIN role.
         *
         * @param id      the doctor ID to update
         * @param request the updated doctor data
         * @return the updated doctor details
         */
        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Cập nhật thông tin bác sĩ (ADMIN)", description = "Cập nhật các thông tin cơ bản của bác sĩ như họ tên, giới tính, số điện thoại, email và chuyên khoa theo ID. Yêu cầu quyền ADMIN.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy bác sĩ cần cập nhật"),
                        @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện hành động này (Yêu cầu ADMIN)")
        })
        public ResponseEntity<org.example.has.dto.response.ApiResponse<DoctorResponse>> update(
                        @PathVariable @Parameter(description = "ID của bác sĩ cần cập nhật. Lấy từ danh sách bác sĩ `GET /api/doctors`.", example = "1") Long id,
                        @Valid @RequestBody DoctorRequest request) {
                DoctorResponse doctor = doctorService.update(id, request);
                return ResponseEntity.ok(
                                org.example.has.dto.response.ApiResponse.success("Cập nhật bác sĩ thành công", doctor));
        }

        /**
         * Deletes a doctor by ID. Requires ADMIN role.
         *
         * @param id the doctor ID to delete
         * @return success message
         */
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Xóa thông tin bác sĩ (ADMIN)", description = "Xóa hoàn toàn bác sĩ khỏi hệ thống dựa trên ID. Yêu cầu quyền ADMIN.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Xóa bác sĩ thành công"),
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy bác sĩ cần xóa"),
                        @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện hành động này (Yêu cầu ADMIN)")
        })
        public ResponseEntity<org.example.has.dto.response.ApiResponse<Void>> delete(
                        @PathVariable @Parameter(description = "ID của bác sĩ cần xóa. Lấy từ danh sách bác sĩ `GET /api/doctors`.", example = "1") Long id) {
                doctorService.delete(id);
                return ResponseEntity
                                .ok(org.example.has.dto.response.ApiResponse.success("Xóa bác sĩ thành công", null));
        }
}
