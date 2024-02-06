package com.parmeshwar.ecom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parmeshwar.ecom.entity.User;
import com.parmeshwar.ecom.enums.UserRole;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findFirstByEmail(String username);

	User findByRole(UserRole admin);

}
