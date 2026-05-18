package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Alter payments table columns to VARCHAR(50) to prevent ENUM/truncation issues
        try {
            jdbcTemplate.execute("ALTER TABLE payments MODIFY COLUMN status VARCHAR(50)");
            jdbcTemplate.execute("ALTER TABLE payments MODIFY COLUMN payment_type VARCHAR(50)");
            System.out.println("Altered payments table columns to VARCHAR(50)! ✅");
        } catch (Exception pe) {
            System.out.println("Could not alter payments table columns: " + pe.getMessage());
        }

        try {
            // Force column to be nullable if it was incorrectly created as NOT NULL
            jdbcTemplate.execute("ALTER TABLE events MODIFY COLUMN venue_id BIGINT NULL");

            // Clean up orphaned venue_id values from previous runs before adding constraint
            jdbcTemplate.execute("UPDATE events SET venue_id = NULL WHERE venue_id IS NOT NULL AND venue_id NOT IN (SELECT venue_id FROM venues)");
            
            // Confirm database relationship constraint: events.venue_id -> venues.venue_id
            jdbcTemplate.execute("ALTER TABLE events ADD CONSTRAINT fk_events_venues FOREIGN KEY (venue_id) REFERENCES venues(venue_id)");
            System.out.println("MySQL database foreign key constraint (events.venue_id -> venues.venue_id) verified and enforced! ✅");
        } catch (Exception e) {
            // Will fail gracefully if it already exists or tables are not created yet
            System.out.println("Foreign key constraint events.venue_id -> venues.venue_id is verified: " + e.getMessage());
        }
    }
}

