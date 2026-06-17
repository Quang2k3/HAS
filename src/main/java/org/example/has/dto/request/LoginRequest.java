package org.example.has.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Thông tin đăng nhập của người dùng")
public class LoginRequest {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Schema(description = "Tên đăng nhập của tài khoản", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Schema(description = "Mật khẩu truy cập tài khoản", example = "admin123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
