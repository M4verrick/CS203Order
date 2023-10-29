UPDATE payment_order_seq
SET next_val = 101;

INSERT INTO PAYMENT_ORDER (id, customer_id, purchase_request_id, event_id, payment_date_time, total_amount, event_name, event_category, event_artist,
event_banner_url, event_seat_map_url, event_location)
VALUES(0, "18b172d4-d66e-470d-991e-84bba61ca3f7", 0, 1, "2023-09-15T16:00:00+08:00", 150, "Music Of The Spheres World Tour", "concert", "Coldplay",
                 		"https://cs203.s3.ap-southeast-1.amazonaws.com/event-image/coldplay.png", "https://cs203.s3.ap-southeast-1.amazonaws.com/seat-map/SeatMap.jpg",
                 		"Singapore Indoor Stadium");


UPDATE order_item_seq
SET next_val = 101;

INSERT INTO ORDER_ITEM (id, ticket_type, quantity, price, payment_order_id)
VALUES(0, "CAT A", 1, 60.0, 0);
