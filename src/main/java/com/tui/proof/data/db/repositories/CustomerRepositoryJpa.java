package com.tui.proof.data.db.repositories;

import java.util.Optional;

import com.tui.proof.data.db.entities.CustomerData;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepositoryJpa extends JpaRepository<CustomerData, Long> {

    Optional<CustomerData> findByEmail(String email);
}
