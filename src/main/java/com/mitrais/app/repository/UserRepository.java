package com.mitrais.app.repository;

import com.mitrais.app.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
