package com.signup.auth.authentication2.activities.repository;

import com.signup.auth.authentication2.activities.entity.ScheduledDeparture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledDepartureRepository extends JpaRepository<ScheduledDeparture, Long> {
}
