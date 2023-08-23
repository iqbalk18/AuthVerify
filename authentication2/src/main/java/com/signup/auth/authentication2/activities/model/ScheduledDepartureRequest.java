package com.signup.auth.authentication2.activities.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledDepartureRequest {
    private String travelNumber;
    private String travelStatus;
    private String remark;
    private String serviceType;
    private LocalDateTime departureDate;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime estimatedDepartureTime;
    private LocalDateTime actualDepartureTime;
    private LocalDateTime cancelTime;
}

