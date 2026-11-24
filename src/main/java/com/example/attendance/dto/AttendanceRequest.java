package com.example.attendance.dto;
//hello
import com.example.attendance.entity.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AttendanceRequest(
        @NotNull
        Long employeeId,
        @NotNull
        LocalDate date,
        @NotNull
        AttendanceStatus status) {
}
