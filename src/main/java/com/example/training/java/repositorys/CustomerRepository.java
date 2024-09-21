package com.example.training.java.repositorys;

import com.example.training.java.entitys.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Optional<CustomerEntity> findByUsername(String userName);
}
