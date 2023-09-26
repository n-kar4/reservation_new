package com.nk.reservation.dto;

import com.nk.reservation.entity.ApplicationUser;

public record LoginResponseDTO(
    ApplicationUser user,
    String token
) {
    
}
