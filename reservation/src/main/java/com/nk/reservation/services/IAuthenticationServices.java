package com.nk.reservation.services;

import com.nk.reservation.dto.LoginResponseDTO;
import com.nk.reservation.entity.ApplicationUser;

public interface IAuthenticationServices {
    public ApplicationUser registerUser(String username, String password);
    public LoginResponseDTO login(String username, String password);
}
