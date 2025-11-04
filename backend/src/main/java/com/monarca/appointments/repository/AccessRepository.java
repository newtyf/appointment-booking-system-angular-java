package com.monarca.appointments.repository;

import com.monarca.appointments.model.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessRepository extends JpaRepository<Access, Long> {
    List<Access> findByUserId(Long userId);
}
