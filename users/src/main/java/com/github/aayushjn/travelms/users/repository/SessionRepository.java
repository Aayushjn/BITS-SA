package com.github.aayushjn.travelms.users.repository;

import com.github.aayushjn.travelms.users.exceptions.InvalidCredentialException;
import com.github.aayushjn.travelms.users.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    @Query("SELECT s FROM Session s WHERE s.user.id = ?1")
    Optional<Session> findByUserId(Integer userId) throws InvalidCredentialException;

    @Query("select s from Session s where s.authKey = ?1")
    Optional<Session> findByAuthKey(String key);
}
