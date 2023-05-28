package com.example.jesuitas.repository;

import java.util.Optional;

import com.example.jesuitas.models.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jesuitas.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findRoleByName(ERole name);
}
