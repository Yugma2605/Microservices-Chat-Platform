package com.example.api_gateway.repository;

import com.example.api_gateway.entity.SecretKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecretKeyRepository extends JpaRepository<SecretKeys, Long> {
    //    @Query("SELECT s FROM SecretKeys s WHERE s.keyName = :keyName")
    @Query("SELECT s FROM SecretKeys s WHERE s.keyName = :keyName")
    Optional<SecretKeys> findByKeyName(@Param("keyName") String keyName);
}
