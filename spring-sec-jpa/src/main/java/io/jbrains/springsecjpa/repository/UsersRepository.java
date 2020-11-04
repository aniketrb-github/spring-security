package io.jbrains.springsecjpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.jbrains.springsecjpa.model.Users;

/**
 * A repository to interact with database for table: user
 *
 * @author Aniket Bharsakale
 */
public interface UsersRepository extends JpaRepository<Users, Integer> {

	Optional<Users> findByName(String userName);
	
}
