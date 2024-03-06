create table cards
(
    card_id         int auto_increment
        primary key,
    card_number     varchar(16)          null,
    card_holder     varchar(50)          null,
    expiration_date date                 not null,
    card_csv        int                  not null,
    isDeleted       tinyint(1) default 0 not null
);

create table currencies
(
    currency_id int auto_increment
        primary key,
    currency    varchar(3) null
);

create table exchange_rates
(
    exchange_rate_id int auto_increment
        primary key,
    currency_id      int    null,
    rate             double null,
    constraint exchange_rates_ibfk_1
        foreign key (currency_id) references currencies (currency_id)
);

create index currency_id
    on exchange_rates (currency_id);

create table roles
(
    role_id   int auto_increment
        primary key,
    role_type enum ('ADMIN', 'REGULAR', 'BANNED') null
);

create table spending_categories
(
    spending_category_id int auto_increment
        primary key,
    name                 varchar(32) null
);

create table users
(
    user_id       int auto_increment
        primary key,
    username      varchar(20)                                                                  null,
    password      varchar(20)                                                                  null,
    email         varchar(32)                                                                  null,
    phone         varchar(20)                                                                  null,
    picture       blob                                                                         null,
    status        enum ('PENDING_EMAIL', 'EMAIL_CONFIRMED', 'PENDING_ID', 'ACTIVE', 'BLOCKED') null,
    creation_date datetime                                                                     null
);

create table contacts_lists
(
    contact_list_id int auto_increment
        primary key,
    member          int null,
    owner           int null,
    constraint contacts_lists_users_user_id_fk
        foreign key (owner) references users (user_id),
    constraint contacts_lists_users_user_id_fk2
        foreign key (member) references users (user_id)
);

create table external_transactions
(
    external_transaction_id int auto_increment
        primary key,
    type                    enum ('DEPOSIT', 'WITHDRAWAL') null,
    user_id                 int                            null,
    card_id                 int                            null,
    timestamp               datetime                       null,
    currency_id             int                            null,
    amount                  double                         null,
    constraint external_transactions_currencies_currency_id_fk
        foreign key (currency_id) references currencies (currency_id),
    constraint external_transactions_ibfk_1
        foreign key (user_id) references users (user_id),
    constraint external_transactions_ibfk_2
        foreign key (card_id) references cards (card_id)
);

create index card_id
    on external_transactions (card_id);

create index user_id
    on external_transactions (user_id);

create table tokens
(
    token_id        int auto_increment
        primary key,
    code            varchar(20)          not null,
    user_id         int                  null,
    expiration_time datetime             null,
    is_active       tinyint(1) default 1 null,
    constraint tokens_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table users_cards
(
    users_cards_id int auto_increment
        primary key,
    user_id        int null,
    card_id        int null,
    constraint users_cards_ibfk_1
        foreign key (user_id) references users (user_id),
    constraint users_cards_ibfk_2
        foreign key (card_id) references cards (card_id)
);

create index card_id
    on users_cards (card_id);

create index user_id
    on users_cards (user_id);

create table users_roles
(
    user_id int null,
    role_id int null,
    constraint users_roles_roles_role_id_fk
        foreign key (role_id) references roles (role_id),
    constraint users_roles_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table verifications
(
    verification_id int auto_increment
        primary key,
    id_card         blob null,
    selfie          blob null,
    user_id         int  null,
    constraint verifications_ibfk_1
        foreign key (user_id) references users (user_id)
);

create index user_id
    on verifications (user_id);

create table wallets
(
    wallet_id   int auto_increment
        primary key,
    balance     double               null,
    currency_id int                  null,
    user_id     int                  null,
    isActive    tinyint(1) default 1 null,
    constraint wallets_ibfk_1
        foreign key (currency_id) references currencies (currency_id),
    constraint wallets_ibfk_2
        foreign key (user_id) references users (user_id)
);

create table internal_transactions
(
    internal_transaction_id int auto_increment
        primary key,
    type                    enum ('INCOMING', 'OUTGOING') null,
    sender_wallet_id        int                           null,
    recipient_wallet_id     int                           null,
    amount                  double                        null,
    timestamp               datetime                      null,
    spending_category       int                           null,
    currency                int                           null,
    constraint internal_transactions_ibfk_1
        foreign key (sender_wallet_id) references wallets (wallet_id),
    constraint internal_transactions_ibfk_2
        foreign key (recipient_wallet_id) references wallets (wallet_id),
    constraint internal_transactions_ibfk_3
        foreign key (spending_category) references spending_categories (spending_category_id),
    constraint internal_transactions_ibfk_4
        foreign key (currency) references currencies (currency_id)
);

create index currency
    on internal_transactions (currency);

create index recipient_wallet_id
    on internal_transactions (recipient_wallet_id);

create index sender_wallet_id
    on internal_transactions (sender_wallet_id);

create index spending_category
    on internal_transactions (spending_category);

create table joint_wallets
(
    joint_wallet_id int auto_increment
        primary key,
    users_wallet    int    null,
    owner_id        int    null,
    currency        int    null,
    balance         double null,
    constraint joint_wallets_ibfk_1
        foreign key (users_wallet) references wallets (wallet_id),
    constraint joint_wallets_ibfk_2
        foreign key (owner_id) references users (user_id),
    constraint joint_wallets_ibfk_3
        foreign key (currency) references currencies (currency_id)
);

create index currency
    on joint_wallets (currency);

create index owner_id
    on joint_wallets (owner_id);

create index users_wallet
    on joint_wallets (users_wallet);

create table users_joint_wallets
(
    users_joint_wallets_id int auto_increment
        primary key,
    member_id              int null,
    joint_wallet_id        int null,
    constraint users_joint_wallets_ibfk_1
        foreign key (member_id) references users (user_id),
    constraint users_joint_wallets_ibfk_2
        foreign key (joint_wallet_id) references joint_wallets (joint_wallet_id)
);

create index joint_wallet_id
    on users_joint_wallets (joint_wallet_id);

create index member_id
    on users_joint_wallets (member_id);

create index currency_id
    on wallets (currency_id);

create index user_id
    on wallets (user_id);

