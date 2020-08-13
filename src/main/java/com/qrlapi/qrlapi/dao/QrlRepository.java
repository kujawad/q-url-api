package com.qrlapi.qrlapi.dao;

import com.qrlapi.qrlapi.model.Qrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QrlRepository extends JpaRepository<Qrl, UUID> {

    Qrl findByStamp(final String stamp);
}
