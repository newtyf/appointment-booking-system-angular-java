package com.monarca.appointments.repository;

import com.monarca.appointments.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByAppointmentId(Long appointmentId);
    List<Reminder> findBySent(Boolean sent);
}
