create table category (
    id bigint not null auto_increment,
    description varchar(255),
    primary key (id)
) engine=InnoDB;

create table image (
    id bigint not null auto_increment,
    file_name varchar(255) not null,
    thumbnail_file_name varchar(255) not null,
    product_id bigint,
    primary key (id)
) engine=InnoDB;

create table inquiry (
    id bigint not null auto_increment,
    created_on datetime(6),
    email varchar(255) not null,
    message_body longtext not null,
    product_id bigint,
    primary key (id)
) engine=InnoDB;

create table product (
    id bigint not null auto_increment,
    amount_in_stock integer,
    created_at timestamp,
    description longtext,
    name varchar(255),
    phone_number varchar(255),
    price integer,
    category_id bigint,
    user_id bigint,
    primary key (id)
) engine=InnoDB;

create table product_unique_addresses (
    product_id bigint not null,
    unique_addresses varchar(255)
) engine=InnoDB;

create table transaction (
    id bigint not null auto_increment,
    time datetime(6),
    buyer_id bigint,
    product_id bigint,
    seller_id bigint,
    primary key (id)
) engine=InnoDB;

create table user (
    id bigint not null auto_increment,
    email varchar(255),
    is_account_non_expired bit not null,
    is_account_non_locked bit not null,
    is_credentials_non_expired bit not null,
    is_enabled bit not null,
    password varchar(255) not null,
    user_role varchar(255),
    username varchar(255) not null,
    primary key (id)
) engine=InnoDB;

alter table image
    add constraint image_product_fk
    foreign key (product_id)
    references product (id);

alter table inquiry
    add constraint inquiry_product_fk
    foreign key (product_id)
    references product (id);

alter table product
    add constraint product_category_fk
    foreign key (category_id)
    references category (id);

alter table product
    add constraint product_user_fk
    foreign key (user_id)
    references user (id);

alter table product_unique_addresses
    add constraint product_addresses_product_fk
    foreign key (product_id)
    references product (id);

alter table transaction
    add constraint transaction_buyer_fk
    foreign key (buyer_id)
    references user (id);

alter table transaction
    add constraint transaction_product_fk
    foreign key (product_id)
    references product (id);

alter table transaction
    add constraint transaction_seller_fk
    foreign key (seller_id)
    references user (id);

