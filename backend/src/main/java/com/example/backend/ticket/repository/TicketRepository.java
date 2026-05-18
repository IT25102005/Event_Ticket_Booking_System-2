package com.example.backend.ticket.repository;

import com.example.backend.ticket.model.TicketCategory;
import com.example.backend.ticket.model.TicketStatus;
import com.example.backend.ticket.model.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<TicketType, Long> {
    
    List<TicketType> findByEventId(Long eventId);
    
    boolean existsByEventIdAndTicketCategory(Long eventId, TicketCategory category);

    @Query("SELECT t FROM TicketType t WHERE " +
           "(:keyword IS NULL OR LOWER(t.ticketName) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:eventId IS NULL OR t.eventId = :eventId) AND " +
           "(:category IS NULL OR t.ticketCategory = :category) AND " +
           "(:status IS NULL OR t.status = :status)")
    List<TicketType> searchTickets(
            @Param("keyword") String keyword,
            @Param("eventId") Long eventId,
            @Param("category") TicketCategory category,
            @Param("status") TicketStatus status
    );
}
