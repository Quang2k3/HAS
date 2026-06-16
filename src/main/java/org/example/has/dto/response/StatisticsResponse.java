package org.example.has.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsResponse {

    private long totalPatients;

    private long totalDoctors;

    private long totalDepartments;

    private long totalAppointments;

    private long scheduledAppointments;

    private long completedAppointments;

    private long cancelledAppointments;

    private Map<String, Long> doctorsByDepartment;
}
