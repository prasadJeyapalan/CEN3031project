DELETE FROM linen;
DELETE FROM orders;
DELETE FROM cart;
DELETE FROM customer;
DELETE FROM sqlite_sequence;

INSERT INTO customer ('customer_ID', 'name', 'address')
VALUES
	("VAC", "Vacasa", "40 Hill Ave NW, FL 32548"),
	("PBR", "Perdido Beach Resort", "27200 Perdido Beach Blvd, AL, 36561"),
	("ABC", "AlphaBet Condos", "123 Main Str, NY 12345")
;

INSERT INTO cart ('cart_id', 'location', 'tare_weight', 'state')
VALUES
	("V-1000", "VAC", 100, "empty"),
	("V-1001", "ABC", 100, "outbound"),
	("V-1002", "VAC", 100, "sorted"),
	("V-1003", "VAC", 100, "packaged")
;

INSERT INTO orders ('customer_ID', 'date_shipped_in', 'weight')
VALUES
	("ABC", "01/01/1970", "3000"),
	("PBR", "01/01/1970", "5034")
;

INSERT INTO linen
VALUES
	("B Towel", "2"),
	("K Set", "4.1")
;