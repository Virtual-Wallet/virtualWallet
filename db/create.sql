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
    creation_date datetime                                                                     null,
    role_type     enum ('ADMIN', 'REGULAR', 'BANNED')                                          not null
);

create table cards
(
    card_id         int auto_increment
        primary key,
    card_number     varchar(16)          null,
    card_holder     varchar(50)          null,
    expiration_date varchar(5)           not null,
    card_csv        varchar(3)           not null,
    isDeleted       tinyint(1) default 0 not null,
    user_id         int                  null,
    constraint cards_users_user_id_fk
        foreign key (user_id) references users (user_id)
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

create table spending_categories
(
    spending_category_id int auto_increment
        primary key,
    name                 varchar(32)          null,
    isDeleted            tinyint(1) default 0 not null,
    creator_id           int                  null,
    constraint spending_categories_users_user_id_fk
        foreign key (creator_id) references users (user_id)
);

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
    balance     double     default 0 null,
    currency_id int                  null,
    user_id     int                  null,
    isActive    tinyint(1) default 1 null,
    isDeleted   tinyint(1) default 0 not null,
    constraint wallets_ibfk_1
        foreign key (currency_id) references currencies (currency_id),
    constraint wallets_ibfk_2
        foreign key (user_id) references users (user_id)
);

create table internal_transactions
(
    internal_transaction_id int auto_increment
        primary key,
    sender_wallet_id        int      null,
    recipient_wallet_id     int      null,
    amount                  double   null,
    timestamp               datetime null,
    spending_category       int      null,
    currency                int      null,
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
