package org.example.has.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.has.dto.request.LoginRequest;
import org.example.has.dto.request.RegisterRequest;
import org.example.has.dto.response.AuthResponse;
import org.example.has.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller handling user authentication. Public endpoints (no JWT required).
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "1. Authentication", description = "API xác thực người dùng. Cung cấp các chức năng đăng ký tài khoản bệnh nhân và đăng nhập hệ thống để nhận JWT Token sử dụng cho các API bảo mật khác.")
public class AuthController {

    private final AuthService authService;

    /**
 * Registers a new patient account.
 *
 * @param request the registration details
 * @return AuthResponse containing JWT token and basic user info
 */
    @PostMapping("/register")
    @Operation(
            summary = "Đăng ký tài khoản bệnh nhân mới",
            description = "Cho phép bệnh nhân tự đăng ký tài khoản trên hệ thống. Sau khi đăng ký thành công, hệ thống tự động đăng nhập và trả về mã JWT token cùng thông tin cơ bản để bắt đầu sử dụng dịch vụ ngay lập tức."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Đăng ký tài khoản và tạo hồ sơ bệnh nhân thành công"),
            @ApiResponse(responseCode = "400", description = "Tên đăng nhập đã tồn tại hoặc dữ liệu nhập vào không hợp lệ (ví dụ: mật khẩu dưới 6 ký tự)")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(org.example.has.dto.response.ApiResponse.success("Đăng ký thành công", response));
    }

    /**
 * Authenticates username/password credentials.
 *
 * @param request the login credentials
 * @return AuthResponse containing JWT token (24h validity) and basic user info
 */
    @PostMapping("/login")
    @Operation(
            summary = "Đăng nhập hệ thống",
            description = "Xác thực tài khoản bằng username và password. Trả về mã JWT token có thời gian hết hạn là 24 giờ. Sử dụng token này điền vào phần Bearer Authentication của Swagger để gọi các API khác."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Đăng nhập thành công và cấp JWT token"),
            @ApiResponse(responseCode = "400", description = "Sai tài khoản hoặc mật khẩu hoặc tài khoản đã bị vô hiệu hóa")
    })
    public ResponseEntity<org.example.has.dto.response.ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(org.example.has.dto.response.ApiResponse.success("Đăng nhập thành công", response));
    }
}
