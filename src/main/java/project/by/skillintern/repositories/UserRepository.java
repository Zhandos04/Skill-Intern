package project.by.skillintern.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.by.skillintern.entities.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    User getUserById(Long id);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.isVerified = false AND u.codeSentAt < :expirationTime")
    void deleteExpiredUnverifiedUsers(@Param("expirationTime") LocalDateTime expirationTime);

}
