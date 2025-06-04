package com.perepalacin.notification_service.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class UserDetailsDto {
    private String email;
    private UUID userId;
}
