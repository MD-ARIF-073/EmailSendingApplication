package com.example.userservice.repository;

import com.example.userservice.domain.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConfirmationRepo extends JpaRepository<Confirmation, Long> {

    Confirmation findByToken(String token);

}
