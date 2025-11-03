package com.monarca.appointments.repository;

import com.monarca.appointments.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByClientId(Long clientId);
    
    List<Appointment> findByStylistId(Long stylistId);
    
    List<Appointment> findByStatusIn(List<String> statuses);
    
    @Query("SELECT a FROM Appointment a WHERE a.stylistId = :stylistId " +
           "AND a.status IN ('pending', 'confirmed') " +
           "AND ((a.date <= :endTime AND a.date >= :startTime))")
    List<Appointment> findConflictingAppointments(
        @Param("stylistId") Long stylistId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    List<Appointment> findByDateBetween(LocalDateTime start, LocalDateTime end);
    
    List<Appointment> findByStylistIdAndDateBetween(Long stylistId, LocalDateTime start, LocalDateTime end);
}
