package com.nk.reservation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nk.reservation.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer>{
    Optional<Role> findByAuthority(String authority);   
}
