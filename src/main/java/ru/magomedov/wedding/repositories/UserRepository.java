package ru.magomedov.wedding.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.magomedov.wedding.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
   User findByEmail (String email);
}
