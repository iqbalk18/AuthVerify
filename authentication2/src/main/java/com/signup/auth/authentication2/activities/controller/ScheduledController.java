package com.signup.auth.authentication2.activities.controller;

import com.signup.auth.authentication2.activities.model.ScheduledDepartureRequest;
import com.signup.auth.authentication2.activities.model.ScheduledDepartureResponse;
import com.signup.auth.authentication2.activities.service.ScheduledDepartureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scheduled")
@RequiredArgsConstructor
@CrossOrigin
public class ScheduledController {
    private final ScheduledDepartureService scheduledDepartureService;

    @GetMapping ("/display")
    public ResponseEntity<List<ScheduledDepartureResponse>> getAllScheduledDepartures() {
        List<ScheduledDepartureResponse> departures = scheduledDepartureService.getAllScheduledDepartures();
        return new ResponseEntity<>(departures, HttpStatus.OK);
    }

    @PostMapping("/departure")
    public ResponseEntity<ScheduledDepartureResponse> addScheduledDeparture(@RequestBody ScheduledDepartureRequest request) {
        ScheduledDepartureResponse response = scheduledDepartureService.addScheduledDeparture(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
