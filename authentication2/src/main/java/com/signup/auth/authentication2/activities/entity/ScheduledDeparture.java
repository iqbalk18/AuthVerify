package com.signup.auth.authentication2.activities.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedule_departure")
public class ScheduledDeparture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String travelNumber;
    private String travelStatus;
    private String remark;
    private String serviceType;
    private String destination;
    private String departureDateTime;
    private String estimatedDepartureDateTime;
    private String actualDepartureDateTime;
    private String cancelDateTime;
}
