truncate table wallet CASCADE;
truncate table transaction CASCADE;

INSERT INTO wallet (id, user_id, balance, created_at, updated_at) VALUES
('10a5dbf7-0353-40d3-92a1-cc322485ea2c', '10a5dbf7-0353-40d3-85pd-cc322485ea2c', 3000, '2026-07-15T16:56:36.751971', '2026-07-15T16:56:36.751971'),
('65d7b3dd-a8fa-4224-9055-02da67ee8361', '10a5dbf7-0353-40d3-85pd-cc922485ea2c', 30000, '2026-07-15T16:56:36.751971', '2026-07-15T16:56:36.751971'),
('10a5dbf7-0353-40d3-92a1-cc322485eh2c', '10a5dbt5-0353-40d3-85pd-cc322485ea2c', 5000, '2026-07-15T16:56:36.751971', '2026-07-15T16:56:36.751971');

INSERT INTO transaction (id, wallet_id, type, reference, created_at) VALUES
('0dff6580-6adb-4521-aef7-7275f1bfc445', '10a5dbf7-0353-40d3-92a1-cc322485ea2c', 0, 'abc', '2026-07-15T16:56:36.751971'),
('ce45d941-c7e1-4959-8ef9-22e76eeb68f2', '10a5dbf7-0353-40d3-92a1-cc322485ea2c', 1, 'xyz', '2026-07-15T16:56:36.751971'),
('26abd9ac-00b5-4f39-81fd-8da54567f1c6', '10a5dbf7-0353-40d3-92a1-cc322485ea2c', 0, 'efg', '2026-07-15T16:56:36.751971'),
('eecca0a9-5030-43fc-8f73-63c2cc77dd2e', '10a5dbf7-0353-40d3-92a1-cc322485eh2c', 1, 'def', '2026-07-15T16:56:36.751971'),
('4febe283-7a2d-4292-9ab8-2fcb44d11158', '65d7b3dd-a8fa-4224-9055-02da67ee8361', 0, 'tur', '2026-07-15T16:56:36.751971');
