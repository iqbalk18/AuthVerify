package com.signup.auth.authentication2.activities.service;

import com.signup.auth.authentication2.activities.entity.ScheduledDeparture;
import com.signup.auth.authentication2.activities.model.ScheduledDepartureRequest;
import com.signup.auth.authentication2.activities.model.ScheduledDepartureResponse;
import com.signup.auth.authentication2.activities.repository.ScheduledDepartureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduledDepartureService {
    @Autowired
    private ScheduledDepartureRepository scheduledDepartureRepository;

    public List<ScheduledDepartureResponse> getAllScheduledDepartures() {
        List<ScheduledDeparture> departures = scheduledDepartureRepository.findAll();
        return departures.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    public ScheduledDepartureResponse addScheduledDeparture(ScheduledDepartureRequest request) {
        ScheduledDeparture scheduledDeparture = mapRequestToEntity(request);
        ScheduledDeparture savedDeparture = scheduledDepartureRepository.save(scheduledDeparture);
        return mapEntityToResponse(savedDeparture);
    }

    private ScheduledDeparture mapRequestToEntity(ScheduledDepartureRequest request) {
        ScheduledDeparture scheduledDeparture = new ScheduledDeparture();
        scheduledDeparture.setTravelNumber(request.getTravelNumber());
        scheduledDeparture.setTravelStatus(request.getTravelStatus());
        scheduledDeparture.setRemark(request.getRemark());
        scheduledDeparture.setServiceType(request.getServiceType());
        scheduledDeparture.setDepartureDate(request.getDepartureDate());
        scheduledDeparture.setDestination(request.getDestination());
        scheduledDeparture.setDepartureTime(request.getDepartureTime());
        scheduledDeparture.setEstimatedDepartureTime(request.getEstimatedDepartureTime());
        scheduledDeparture.setActualDepartureTime(request.getActualDepartureTime());
        scheduledDeparture.setCancelTime(request.getCancelTime());
        return scheduledDeparture;
    }

    private ScheduledDepartureResponse mapEntityToResponse(ScheduledDeparture departure) {
        ScheduledDepartureResponse response = new ScheduledDepartureResponse();
        response.setStatus("Success");
        response.setMessage("Data retrieved successfully");

        List<ScheduledDepartureRequest> requestData = mapEntityToRequest(departure);
        response.setData(requestData);

        return response;
    }

    private List<ScheduledDepartureRequest> mapEntityToRequest(ScheduledDeparture departure) {
        List<ScheduledDepartureRequest> requestData = new ArrayList<>();

        ScheduledDepartureRequest request = ScheduledDepartureRequest.builder()
                .travelNumber(departure.getTravelNumber())
                .travelStatus(departure.getTravelStatus())
                .remark(departure.getRemark())
                .serviceType(departure.getServiceType())
                .departureDate(departure.getDepartureDate())
                .destination(departure.getDestination())
                .departureTime(departure.getDepartureTime())
                .estimatedDepartureTime(departure.getEstimatedDepartureTime())
                .actualDepartureTime(departure.getActualDepartureTime())
                .cancelTime(departure.getCancelTime())
                .build();

        requestData.add(request);

        return requestData;
    }
}
