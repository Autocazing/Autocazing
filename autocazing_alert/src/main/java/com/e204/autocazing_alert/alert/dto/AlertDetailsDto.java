package com.e204.autocazing_alert.alert.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//알림(발주) 조회
public class AlertDetailsDto {

    private Integer alertId;
    private String content;
    private String loginId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean completed;
}

