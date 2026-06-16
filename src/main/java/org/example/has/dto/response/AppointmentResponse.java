package org.example.has.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.has.domain.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {

    private Long id;

    private String code;

    private LocalDate appointmentDate;

    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", example = "14:30", description = "Giờ khám (định dạng HH:mm)")
    private LocalTime appointmentTime;

    private AppointmentStatus status;

    private String reason;

    private Long patientId;

    private String patientName;

    private Long doctorId;

    private String doctorName;

    private String departmentName;
}
