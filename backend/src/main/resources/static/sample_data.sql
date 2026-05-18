-- SQL Sample Data for Ticket Type & Pricing Management
-- Database: event_ticket_booking

CREATE DATABASE IF NOT EXISTS event_ticket_booking;
USE event_ticket_booking;

-- Note: The 'tickets' table is automatically created by Hibernate (ddl-auto=update).
-- This script provides sample INSERT statements.

-- Sample VIP Ticket
INSERT INTO tickets (ticket_type, event_id, ticket_name, description, ticket_category, base_price, discount_percentage, service_fee, tax_percentage, final_price, total_quantity, sold_quantity, available_quantity, sale_start_date, sale_end_date, status, vip_benefits, lounge_access, complimentary_items, vip_service_fee, created_at)
VALUES ('VIP', 101, 'Platinum VIP Pass', 'Exclusive access to the front row and backstage', 'VIP', 500.0, 10.0, 20.0, 15.0, 515.0, 50, 10, 40, '2026-05-01 10:00:00', '2026-06-01 22:00:00', 'ACTIVE', 'Backstage access, Meet & Greet', 1, 'Signed Poster, Drink Vouchers', 50.0, NOW());

-- Sample Standard Ticket
INSERT INTO tickets (ticket_type, event_id, ticket_name, description, ticket_category, base_price, discount_percentage, service_fee, tax_percentage, final_price, total_quantity, sold_quantity, available_quantity, sale_start_date, sale_end_date, status, standard_service_fee, seat_preference_allowed, created_at)
VALUES ('STANDARD', 101, 'General Admission', 'Standard entry to the event', 'STANDARD', 100.0, 0.0, 10.0, 5.0, 115.0, 500, 250, 250, '2026-05-01 10:00:00', '2026-06-01 22:00:00', 'ACTIVE', 5.0, 1, NOW());

-- Sample Early Bird Ticket
INSERT INTO tickets (ticket_type, event_id, ticket_name, description, ticket_category, base_price, discount_percentage, service_fee, tax_percentage, final_price, total_quantity, sold_quantity, available_quantity, sale_start_date, sale_end_date, status, early_bird_discount_percentage, offer_end_date, created_at)
VALUES ('EARLY_BIRD', 102, 'Early Bird Special', 'Discounted tickets for early buyers', 'EARLY_BIRD', 80.0, 5.0, 5.0, 5.0, 81.0, 100, 100, 0, '2026-04-01 10:00:00', '2026-05-01 10:00:00', 'SOLD_OUT', 15.0, '2026-05-01 10:00:00', NOW());

-- Sample Student Ticket (Stored as Standard)
INSERT INTO tickets (ticket_type, event_id, ticket_name, description, ticket_category, base_price, discount_percentage, service_fee, tax_percentage, final_price, total_quantity, sold_quantity, available_quantity, sale_start_date, sale_end_date, status, standard_service_fee, seat_preference_allowed, created_at)
VALUES ('STANDARD', 103, 'Student Discounted', 'Valid student ID required at entry', 'STUDENT', 50.0, 20.0, 5.0, 5.0, 47.5, 200, 45, 155, '2026-05-10 09:00:00', '2026-06-15 18:00:00', 'ACTIVE', 0.0, 0, NOW());
