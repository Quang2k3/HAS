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
@Schema(description = "Phản hồi chuẩn từ hệ thống REST API")
public class ApiResponse<T> {

    @Schema(description = "Trạng thái thực hiện yêu cầu (true: Thành công, false: Thất bại)", example = "true")
    private boolean success;

    @Schema(description = "Thông báo phản hồi hiển thị cho người dùng", example = "Thành công")
    private String message;

    @Schema(description = "Dữ liệu trả về (có thể là một đối tượng, danh sách hoặc null)")
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
