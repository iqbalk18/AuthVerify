package com.signup.auth.authentication2.activities.service;

import com.signup.auth.authentication2.activities.entity.ScheduledDeparture;
import com.signup.auth.authentication2.activities.model.ScheduledDepartureRequest;
import com.signup.auth.authentication2.activities.model.ScheduledDepartureResponse;
import com.signup.auth.authentication2.activities.repository.ScheduledDepartureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        scheduledDeparture.setDestination(request.getDestination());
        scheduledDeparture.setDepartureDateTime(request.getDepartureDate() + "T" + request.getDepartureTime());
        scheduledDeparture.setEstimatedDepartureDateTime(request.getEstimatedDepartureDate() + "T" + request.getEstimatedDepartureTime());
        scheduledDeparture.setActualDepartureDateTime(request.getActualDepartureDate() + "T" + request.getActualDepartureTime());
        scheduledDeparture.setCancelDateTime(request.getCancelDate() + "T" + request.getCancelTime());
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
                .destination(departure.getDestination())
                .departureDate(departure.getDepartureDateTime().substring(0, 10))
                .departureTime(departure.getDepartureDateTime().substring(11))
                .estimatedDepartureDate(departure.getEstimatedDepartureDateTime().substring(0, 10))
                .estimatedDepartureTime(departure.getEstimatedDepartureDateTime().substring(11))
                .actualDepartureDate(departure.getActualDepartureDateTime().substring(0, 10))
                .actualDepartureTime(departure.getActualDepartureDateTime().substring(11))
                .cancelDate(departure.getCancelDateTime().substring(0, 10))
                .cancelTime(departure.getCancelDateTime().substring(11))
                .build();

        requestData.add(request);

        return requestData;
    }
}
