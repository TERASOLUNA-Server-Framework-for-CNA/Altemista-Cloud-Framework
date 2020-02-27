package com.mycompany.application.module.repository;

import org.springframework.stereotype.Repository;

import com.microsoft.azure.spring.data.documentdb.repository.DocumentDbRepository;
import com.mycompany.application.module.entity.User;

@Repository
public interface UserRepository extends DocumentDbRepository<User, String> {
}
