package com.qurlapi.qurlapi.dao;

import com.qurlapi.qurlapi.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UrlRepository extends JpaRepository<Url, UUID> {

}
