package com.example.training.java.repositorys;

import com.example.training.java.entitys.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
