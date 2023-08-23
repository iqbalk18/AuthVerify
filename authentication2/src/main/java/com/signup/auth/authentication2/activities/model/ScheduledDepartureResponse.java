package com.signup.auth.authentication2.activities.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledDepartureResponse {
    private String status;
    private String message;
    private List<ScheduledDepartureRequest> data;
}
