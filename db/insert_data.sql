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

-- Inserting sample data into the roles table
INSERT INTO roles (role_id, role_type) VALUES
                                           (1, 'ADMIN'),
                                           (2, 'REGULAR'),
                                           (3, 'BANNED');

-- Inserting sample data into the users table
INSERT INTO users (user_id, username, password, email, phone, picture, status, creation_date, role_type) VALUES
                                                                                                             (1, 'admin', 'admin123', 'admin@example.com', '123456789', NULL, 'ACTIVE', NOW(), 'ADMIN'),
                                                                                                             (2, 'user1', 'user123', 'user1@example.com', '987654321', NULL, 'ACTIVE', NOW(), 'REGULAR');

-- Inserting sample data into the cards table
INSERT INTO cards (card_id, card_number, card_holder, expiration_date, card_csv, isDeleted, user_id) VALUES
                                                                                                         (1, '1234567890123456', 'John Doe', '12/23', '123', 0, 1),
                                                                                                         (2, '9876543210987654', 'Jane Smith', '11/22', '456', 0, 2);

-- Inserting sample data into the spending_categories table
INSERT INTO spending_categories (spending_category_id, name, isDeleted, creator_id) VALUES
                                                                                        (1, 'Food', 0, 1),
                                                                                        (2, 'Transportation', 0, 1);

-- Inserting sample data into the wallets table
INSERT INTO wallets (wallet_id, balance, currency_id, user_id, isActive, isDeleted) VALUES
                                                                                        (1, 1000.00, 1, 1, 1, 0),
                                                                                        (2, 500.00, 2, 2, 1, 0);

-- Inserting sample data into the internal_transactions table
INSERT INTO internal_transactions (internal_transaction_id, type, sender_wallet_id, recipient_wallet_id, amount, timestamp, spending_category, currency) VALUES
                                                                                                                                                             (1, 'OUTGOING', 1, 2, 50.00, NOW(), 1, 1),
                                                                                                                                                             (2, 'INCOMING', 2, 1, 100.00, NOW(), 2, 2),
                                                                                                                                                             (3, 'OUTGOING', 1, 2, 75.00, NOW(), 1, 1),
                                                                                                                                                             (4, 'INCOMING', 2, 1, 50.00, NOW(), 2, 2),
                                                                                                                                                             (5, 'OUTGOING', 1, 2, 100.00, NOW(), 1, 1),
                                                                                                                                                             (6, 'INCOMING', 2, 1, 80.00, NOW(), 2, 2);

-- Inserting sample data into the joint_wallets table
INSERT INTO joint_wallets (joint_wallet_id, users_wallet, owner_id, currency, balance) VALUES
                                                                                           (1, 1, 1, 1, 1500.00),
                                                                                           (2, 2, 2, 2, 800.00);

-- Inserting sample data into the users_joint_wallets table
INSERT INTO users_joint_wallets (users_joint_wallets_id, member_id, joint_wallet_id) VALUES
                                                                                         (1, 2, 1),
                                                                                         (2, 1, 2);
