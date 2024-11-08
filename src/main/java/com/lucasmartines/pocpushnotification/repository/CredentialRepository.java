package com.lucasmartines.pocpushnotification.repository;
import com.lucasmartines.pocpushnotification.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CredentialRepository extends MongoRepository<Customer, String> {

    Optional<Customer> findByName(String name);

}
