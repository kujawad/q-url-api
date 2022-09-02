package com.qurlapi.qurlapi.dao;

import com.qurlapi.qurlapi.model.QUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QUrlRepository extends JpaRepository<QUrl, UUID> {
    Optional<QUrl> findByStamp(final String stamp);
}
