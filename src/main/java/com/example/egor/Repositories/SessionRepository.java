package com.example.egor.Repositories;

import com.example.egor.Entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findBySessionToken(String sessionToken);
}
