-- Inserting sample data into the currencies table
INSERT INTO currencies (currency_id, currency) VALUES
                                                   (1, 'USD'),
                                                   (2, 'EUR'),
                                                   (3, 'GBP');

-- Inserting sample data into the exchange_rates table
INSERT INTO exchange_rates (exchange_rate_id, currency_id, rate) VALUES
                                                                     (1, 1, 1.0),
                                                                     (2, 2, 0.85),
                                                                     (3, 3, 0.73);

-- Sample data for users
INSERT INTO users (username, password, email, phone, status, creation_date, role_type) VALUES
                                                                                           ('user1', 'password1', 'user1@example.com', '1234567890', 'ACTIVE', NOW(), 'REGULAR'),
                                                                                           ('user2', 'password2', 'user2@example.com', '2345678901', 'ACTIVE', NOW(), 'REGULAR'),
                                                                                           ('user3', 'password3', 'user3@example.com', '3456789012', 'ACTIVE', NOW(), 'REGULAR'),
                                                                                           ('user4', 'password4', 'user4@example.com', '4567890123', 'ACTIVE', NOW(), 'REGULAR'),
                                                                                           ('user5', 'password5', 'user5@example.com', '5678901234', 'ACTIVE', NOW(), 'REGULAR'),
                                                                                           ('user6', 'password6', 'user6@example.com', '6789012345', 'ACTIVE', NOW(), 'REGULAR'),
                                                                                           ('user7', 'password7', 'user7@example.com', '7890123456', 'ACTIVE', NOW(), 'REGULAR'),
                                                                                           ('user8', 'password8', 'user8@example.com', '8901234567', 'ACTIVE', NOW(), 'REGULAR'),
                                                                                           ('user9', 'password9', 'user9@example.com', '9012345678', 'ACTIVE', NOW(), 'REGULAR'),
                                                                                           ('user10', 'password10', 'user10@example.com', '0123456789', 'ACTIVE', NOW(), 'REGULAR'),
                                                                                           ('user11', 'password11', 'user11@example.com', '9876543210', 'ACTIVE', NOW(), 'REGULAR');

-- Sample data for cards
INSERT INTO cards (card_number, card_holder, expiration_date, card_csv, user_id) VALUES
                                                                                     ('1111222233334444', 'John Doe', '12/25', '123', 1),
                                                                                     ('2222333344445555', 'Jane Smith', '06/27', '456', 2),
                                                                                     ('3333444455556666', 'Alice Wonderland', '09/26', '789', 3),
                                                                                     ('4444555566667777', 'Bob Miller', '03/28', '321', 4),
                                                                                     ('5555666677778888', 'David Jones', '11/25', '654', 5),
                                                                                     ('6666777788889999', 'Emily Davis', '02/24', '987', 6),
                                                                                     ('7777888899990000', 'Frank White', '07/26', '654', 7),
                                                                                     ('8888999900001111', 'Grace Taylor', '10/25', '321', 8),
                                                                                     ('9999000011112222', 'Harry Anderson', '05/27', '789', 9),
                                                                                     ('1234123412341234', 'Sarah Johnson', '08/28', '456', 10),
                                                                                     ('5678567856785678', 'Michael Brown', '01/23', '123', 11);

-- Sample data for transactions
INSERT INTO external_transactions (type, user_id, card_id, timestamp, currency_id, amount) VALUES
                                                                                               ('DEPOSIT', 1, 1, NOW(), 1, 100),
                                                                                               ('DEPOSIT', 2, 2, NOW(), 1, 150),
                                                                                               ('WITHDRAWAL', 3, 3, NOW(), 1, 50),
                                                                                               ('DEPOSIT', 4, 4, NOW(), 1, 200),
                                                                                               ('WITHDRAWAL', 5, 5, NOW(), 1, 75),
                                                                                               ('DEPOSIT', 6, 6, NOW(), 1, 300),
                                                                                               ('DEPOSIT', 7, 7, NOW(), 1, 250),
                                                                                               ('WITHDRAWAL', 8, 8, NOW(), 1, 100),
                                                                                               ('DEPOSIT', 9, 9, NOW(), 1, 400),
                                                                                               ('WITHDRAWAL', 10, 10, NOW(), 1, 125),
                                                                                               ('DEPOSIT', 11, 11, NOW(), 1, 350),
                                                                                               ('DEPOSIT', 1, 1, NOW(), 1, 200),
                                                                                               ('DEPOSIT', 2, 2, NOW(), 1, 250),
                                                                                               ('WITHDRAWAL', 3, 3, NOW(), 1, 100),
                                                                                               ('DEPOSIT', 4, 4, NOW(), 1, 300),
                                                                                               ('WITHDRAWAL', 5, 5, NOW(), 1, 125),
                                                                                               ('DEPOSIT', 6, 6, NOW(), 1, 400),
                                                                                               ('DEPOSIT', 7, 7, NOW(), 1, 350),
                                                                                               ('WITHDRAWAL', 8, 8, NOW(), 1, 150),
                                                                                               ('DEPOSIT', 9, 9, NOW(), 1, 500);

INSERT INTO contacts_lists (member, owner) VALUES
                                               (2, 1), -- Jane Smith added to John Doe's contacts list
                                               (3, 1), -- Alice Wonderland added to John Doe's contacts list
                                               (4, 1), -- Bob Miller added to John Doe's contacts list
                                               (5, 1), -- David Jones added to John Doe's contacts list
                                               (1, 2), -- John Doe added to Jane Smith's contacts list
                                               (3, 2), -- Alice Wonderland added to Jane Smith's contacts list
                                               (4, 2), -- Bob Miller added to Jane Smith's contacts list
                                               (5, 2), -- David Jones added to Jane Smith's contacts list
                                               (1, 3), -- John Doe added to Alice Wonderland's contacts list
                                               (2, 3), -- Jane Smith added to Alice Wonderland's contacts list
                                               (4, 3); -- Bob Miller added to Alice Wonderland's contacts list


INSERT INTO spending_categories (name, creator_id) VALUES
                                                       ('Food', 1),       -- Created by user with ID 1
                                                       ('Transportation', 2), -- Created by user with ID 2
                                                       ('Entertainment', 3),  -- Created by user with ID 3
                                                       ('Shopping', 4),       -- Created by user with ID 4
                                                       ('Utilities', 5),      -- Created by user with ID 5
                                                       ('Healthcare', 6),     -- Created by user with ID 6
                                                       ('Education', 7),      -- Created by user with ID 7
                                                       ('Travel', 8),         -- Created by user with ID 8
                                                       ('Personal Care', 9),  -- Created by user with ID 9
                                                       ('Gifts', 10),         -- Created by user with ID 10
                                                       ('Miscellaneous', 11); -- Created by user with ID 11

INSERT INTO tokens (code, user_id, expiration_time, is_active) VALUES
                                                                   ('token_code_1', 1, '2024-03-20 12:00:00', 0),
                                                                   ('token_code_2', 2, '2024-03-20 12:00:00', 0),
                                                                   ('token_code_3', 3, '2024-03-20 12:00:00', 0),
                                                                   ('token_code_4', 4, '2024-03-20 12:00:00', 0),
                                                                   ('token_code_5', 5, '2024-03-20 12:00:00', 0),
                                                                   ('token_code_6', 6, '2024-03-20 12:00:00', 0),
                                                                   ('token_code_7', 7, '2024-03-20 12:00:00', 0),
                                                                   ('token_code_8', 8, '2024-03-20 12:00:00', 0),
                                                                   ('token_code_9', 9, '2024-03-20 12:00:00', 0),
                                                                   ('token_code_10', 10, '2024-03-20 12:00:00', 0),
                                                                   ('token_code_11', 11, '2024-03-20 12:00:00', 0);

INSERT INTO users_cards (user_id, card_id) VALUES
                                               (1, 1),  -- John Doe's card
                                               (2, 2),  -- Jane Smith's card
                                               (3, 3),  -- Alice Wonderland's card
                                               (4, 4),  -- Bob Miller's card
                                               (5, 5),  -- David Jones's card
                                               (6, 6),  -- Emily Davis's card
                                               (7, 7),  -- Frank White's card
                                               (8, 8),  -- Grace Taylor's card
                                               (9, 9),  -- Harry Anderson's card
                                               (10, 10),-- Sarah Johnson's card
                                               (11, 11);-- Michael Brown's card

INSERT INTO wallets (balance, currency_id, user_id, isActive) VALUES
                                                                  (1000, 1, 1, 1),  -- User 1's wallet with $1000 balance
                                                                  (1500, 1, 2, 1),  -- User 2's wallet with $1500 balance
                                                                  (2000, 1, 3, 1),  -- User 3's wallet with $2000 balance
                                                                  (2500, 1, 4, 1),  -- User 4's wallet with $2500 balance
                                                                  (3000, 1, 5, 1),  -- User 5's wallet with $3000 balance
                                                                  (3500, 1, 6, 1),  -- User 6's wallet with $3500 balance
                                                                  (4000, 1, 7, 1),  -- User 7's wallet with $4000 balance
                                                                  (4500, 1, 8, 1),  -- User 8's wallet with $4500 balance
                                                                  (5000, 1, 9, 1),  -- User 9's wallet with $5000 balance
                                                                  (5500, 1, 10, 1), -- User 10's wallet with $5500 balance
                                                                  (6000, 1, 11, 1); -- User 11's wallet with $6000 balance

-- Sample data for internal transactions
INSERT INTO internal_transactions (sender_wallet_id, recipient_wallet_id, amount, timestamp, spending_category, currency) VALUES
                                                                                                                              (1, 2, 100, '2024-03-20 09:00:00', 1, 1),
                                                                                                                              (2, 3, 150, '2024-03-20 10:15:00', 2, 1),
                                                                                                                              (3, 4, 200, '2024-03-20 11:30:00', 3, 1),
                                                                                                                              (4, 5, 250, '2024-03-20 12:45:00', 4, 1),
                                                                                                                              (5, 6, 300, '2024-03-20 14:00:00', 5, 1),
                                                                                                                              (6, 7, 350, '2024-03-20 15:15:00', 6, 1),
                                                                                                                              (7, 8, 400, '2024-03-20 16:30:00', 7, 1),
                                                                                                                              (8, 9, 450, '2024-03-20 17:45:00', 8, 1),
                                                                                                                              (9, 10, 500, '2024-03-20 19:00:00', 9, 1),
                                                                                                                              (10, 11, 550, '2024-03-20 20:15:00', 10, 1),
                                                                                                                              (11, 1, 600, '2024-03-20 21:30:00', 11, 1),
                                                                                                                              (2, 1, 100, '2024-03-21 09:00:00', 1, 1),
                                                                                                                              (3, 2, 150, '2024-03-21 10:15:00', 2, 1),
                                                                                                                              (4, 3, 200, '2024-03-21 11:30:00', 3, 1),
                                                                                                                              (5, 4, 250, '2024-03-21 12:45:00', 4, 1),
                                                                                                                              (6, 5, 300, '2024-03-21 14:00:00', 5, 1),
                                                                                                                              (7, 6, 350, '2024-03-21 15:15:00', 6, 1),
                                                                                                                              (8, 7, 400, '2024-03-21 16:30:00', 7, 1),
                                                                                                                              (9, 8, 450, '2024-03-21 17:45:00', 8, 1),
                                                                                                                              (10, 9, 500, '2024-03-21 19:00:00', 9, 1),
                                                                                                                              (11, 10, 550, '2024-03-21 20:15:00', 10, 1);
