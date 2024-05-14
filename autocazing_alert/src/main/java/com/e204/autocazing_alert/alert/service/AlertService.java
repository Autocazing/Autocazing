package com.e204.autocazing_alert.alert.service;


import com.e204.autocazing_alert.alert.dto.AlertDetailsDto;
import com.e204.autocazing_alert.db.entity.AlertEntity;
import com.e204.autocazing_alert.db.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public List<AlertDetailsDto> findAllAlert(String loginId) {
        List<AlertEntity> alertEntities =alertRepository.findByLoginId(loginId);
        return alertEntities
                .stream()
                .map(this::convertToAlertDetailsDTO)
                .collect(Collectors.toList());
    }

    private AlertDetailsDto convertToAlertDetailsDTO(AlertEntity alert) {
        AlertDetailsDto alertDetails = new AlertDetailsDto();
        alertDetails.setAlertId(alert.getAlertId());
        alertDetails.setContent(alert.getContent());
        alertDetails.setCreatedAt(alert.getCreatedAt());
        alertDetails.setUpdatedAt(alert.getUpdatedAt());
        alertDetails.setCompleted(alert.getCompleted());
        alertDetails.setLoginId(alert.getLoginId());
        return alertDetails;
    }

}
