package org.example.has.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Thông tin xác thực trả về sau khi đăng nhập/đăng ký thành công")
public class AuthResponse {

    @Schema(description = "Mã JWT token để truy cập các API bảo mật. Gửi ở Header dưới dạng 'Authorization: Bearer <token>'", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String token;

    @Builder.Default
    @Schema(description = "Loại token xác thực", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Tên tài khoản đăng nhập của người dùng", example = "admin")
    private String username;

    @Schema(description = "Họ tên đầy đủ của người dùng", example = "Quản trị viên hệ thống")
    private String fullName;

    @Schema(description = "Vai trò (Role) của người dùng trong hệ thống (ADMIN, DOCTOR, PATIENT)", example = "ADMIN")
    private String role;
}
