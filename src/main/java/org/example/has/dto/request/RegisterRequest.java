package org.example.has.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Yêu cầu đăng ký tài khoản bệnh nhân mới")
public class RegisterRequest {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3 đến 50 ký tự")
    @Schema(description = "Tên đăng nhập mong muốn (3-50 ký tự, duy nhất trong hệ thống)", example = "patient_vy", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    @Schema(description = "Mật khẩu đăng nhập tài khoản (tối thiểu 6 ký tự)", example = "patient123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    @Schema(description = "Họ và tên đầy đủ của bệnh nhân", example = "Trần Thị Vy", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fullName;

    @Schema(description = "Số điện thoại liên hệ của bệnh nhân", example = "0912345678")
    private String phone;

    @Schema(description = "Địa chỉ thường trú của bệnh nhân", example = "123 Nguyễn Trãi, Thanh Xuân, Hà Nội")
    private String address;
}
