package org.example.has.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    @NotNull(message = "Bệnh nhân không được để trống")
    private Long patientId;

    @NotNull(message = "Bác sĩ không được để trống")
    private Long doctorId;

    @NotNull(message = "Ngày khám không được để trống")
    private LocalDate appointmentDate;

    @NotNull(message = "Giờ khám không được để trống")
    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", example = "14:30", description = "Giờ khám (định dạng HH:mm)")
    private LocalTime appointmentTime;

    private String reason;
}
