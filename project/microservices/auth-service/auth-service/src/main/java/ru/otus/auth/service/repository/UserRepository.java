package ru.otus.auth.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.auth.service.model.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLastNameAndFirstNameAndEmail(String lastName, String firstName, String email);

    User findByEmail(String email);

    User findByUsername(String username);
}