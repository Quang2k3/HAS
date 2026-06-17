package org.example.has.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.has.domain.enums.Gender;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Yêu cầu tạo mới hoặc cập nhật thông tin bác sĩ")
public class DoctorRequest {

    @NotBlank(message = "Mã bác sĩ không được để trống")
    @Schema(description = "Mã số duy nhất của bác sĩ (ví dụ: DOC001, DOC002)", example = "DOC001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    @NotBlank(message = "Họ tên không được để trống")
    @Schema(description = "Họ và tên đầy đủ của bác sĩ", example = "Nguyễn Văn Minh", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullName;

    @Schema(description = "Giới tính của bác sĩ (MALE, FEMALE, OTHER)", example = "MALE")
    private Gender gender;

    @Schema(description = "Số điện thoại di động liên hệ", example = "0911223344")
    private String phone;

    @Email(message = "Email không hợp lệ")
    @Schema(description = "Địa chỉ email liên hệ và nhận thông báo công việc", example = "minh.nv@hospital.com")
    private String email;

    @Schema(description = "Chuyên khoa chuyên sâu của bác sĩ", example = "Nội khoa tổng quát")
    private String specialty;

    @Schema(description = "Tên tài khoản đăng nhập mong muốn để bác sĩ truy cập hệ thống. Nếu cung cấp, hệ thống sẽ tự động tạo User với vai trò DOCTOR.", example = "doctor_minh")
    private String username;

    @Schema(description = "Mật khẩu cho tài khoản đăng nhập mới (tối thiểu 6 ký tự). Cần điền nếu điền username.", example = "doctor123")
    private String password;
}
