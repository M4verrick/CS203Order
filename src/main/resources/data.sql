UPDATE payment_order_seq
SET next_val = 101;

INSERT INTO PAYMENT_ORDER (id, customer_id, purchase_request_id, event_id, payment_date_time, total_amount, event_name, event_category, event_artist,
event_banner_url, event_seat_map_url, event_location, event_start_time, event_end_time)
VALUES(0, "18b172d4-d66e-470d-991e-84bba61ca3f7", 0, 1, "2023-09-15T16:00:00+08:00", 150, "Music Of The Spheres World Tour", "concert", "Coldplay",
                 		"https://cs203.s3.ap-southeast-1.amazonaws.com/event-image/coldplay.png", "https://cs203.s3.ap-southeast-1.amazonaws.com/seat-map/SeatMap.jpg",
                 		"Singapore Indoor Stadium",  "2023-12-15T16:00:00+08:00",  "2023-09-20T16:00:00+08:00");

INSERT INTO PAYMENT_ORDER (id, customer_id, purchase_request_id, event_id, payment_date_time, total_amount, event_name, event_category, event_artist,
event_banner_url, event_seat_map_url, event_location, event_start_time, event_end_time)
VALUES(1, "18b172d4-d66e-470d-991e-84bba61ca3f7", 0, 3, "2023-09-15T16:00:00+08:00", 100, "MAYDAY NOWHERE Re: Live 2024", "concert", "Mayday",
                 		"https://cs203.s3.ap-southeast-1.amazonaws.com/event-image/mayday.jpg", "https://cs203.s3.ap-southeast-1.amazonaws.com/seat-map/SeatMap.jpg",
                 		"Singapore Indoor Stadium", "2023-10-06T16:00:00+08:00",  "2023-10-15T16:00:00+08:00");

INSERT INTO PAYMENT_ORDER (id, customer_id, purchase_request_id, event_id, payment_date_time, total_amount, event_name, event_category, event_artist,
event_banner_url, event_seat_map_url, event_location, event_start_time, event_end_time)
VALUES(2, "18b172d4-d66e-470d-991e-84bba61ca3f7", 0, 2, "2023-09-15T16:00:00+08:00", 50, "Eric Chou Odyssey", "concert", "Eric Chou",
                 		"https://cs203.s3.ap-southeast-1.amazonaws.com/event-image/eric.jpg", "https://cs203.s3.ap-southeast-1.amazonaws.com/seat-map/SeatMap.jpg",
                 		"Singapore Indoor Stadium", "2023-09-15T16:00:00+08:00",  "2023-09-23T16:00:00+08:00");


UPDATE order_item_seq
SET next_val = 101;

INSERT INTO ORDER_ITEM (id, ticket_type, quantity, price, payment_order_id, start_time, end_time)
VALUES(0, "CAT A", 1, 60.0, 0, "2023-12-20T12:00:00+08:00", "2023-12-21T16:00:00+08:00");

INSERT INTO ORDER_ITEM (id, ticket_type, quantity, price, payment_order_id, start_time, end_time)
VALUES(1, "CAT B", 1, 40.0, 0, "2023-12-21T12:00:00+08:00", "2023-12-21T16:00:00+08:00");

INSERT INTO ORDER_ITEM (id, ticket_type, quantity, price, payment_order_id, start_time, end_time)
VALUES(2, "CAT C", 2, 25.0, 0, "2023-12-21T12:00:00+08:00", "2023-12-21T16:00:00+08:00");

INSERT INTO ORDER_ITEM (id, ticket_type, quantity, price, payment_order_id, start_time, end_time)
VALUES(4, "CAT A", 2, 60.0, 1, "2023-10-21T12:00:00+08:00", "2023-10-21T16:00:00+08:00");

INSERT INTO ORDER_ITEM (id, ticket_type, quantity, price, payment_order_id, start_time, end_time)
VALUES(5, "CAT B", 2, 40.0, 1, "2023-12-30T12:00:00+08:00", "2023-12-30T16:00:00+08:00");

INSERT INTO ORDER_ITEM (id, ticket_type, quantity, price, payment_order_id, start_time, end_time)
VALUES(3, "CAT C", 2, 25.0, 2, "2023-10-21T12:00:00+08:00", "2023-10-21T16:00:00+08:00");
