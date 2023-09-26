package com.nk.reservation;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.nk.reservation.entity.ApplicationUser;
import com.nk.reservation.entity.ReservationTypes;
import com.nk.reservation.entity.Role;
import com.nk.reservation.repository.ReservationTypesRepository;
import com.nk.reservation.repository.RoleRepository;
import com.nk.reservation.repository.UserRepository;


@SpringBootApplication
@EnableDiscoveryClient
public class ReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationApplication.class, args);
	}
	

	
	@Bean
	CommandLineRunner run(RoleRepository roleRepository,
						 UserRepository userRepository,
						 PasswordEncoder passwordEncoder,
						 ReservationTypesRepository reservationTypesRepository){
		return args -> {
			if(reservationTypesRepository.findAll().size() > 0){
				return;
			}else{
				reservationTypesRepository.save(new ReservationTypes(1,"flight"));
				reservationTypesRepository.save(new ReservationTypes(2,"train"));
				reservationTypesRepository.save(new ReservationTypes(3,"bus"));
				reservationTypesRepository.save(new ReservationTypes(4,"cab"));
				reservationTypesRepository.save(new ReservationTypes(5,"hotel"));
			}
			
			if(roleRepository.findByAuthority("admin").isPresent()){
				return;
			}

			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));

			Set <Role> roles = new HashSet<>();
			roles.add(adminRole);

			ApplicationUser admin = new ApplicationUser("admin",passwordEncoder.encode("admin"),roles);
			userRepository.save(admin);
		};
	}

}
