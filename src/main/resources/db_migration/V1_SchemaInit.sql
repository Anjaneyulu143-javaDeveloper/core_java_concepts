create sequence customer_id_seq start with 1 increment by 50;
create sequence product_id_seq start with 1 increment by 50;

create table customers
(
    customer_id int bigint DEFAULT nextval('customer_id_seq') not null,
    email varchar(223)  not null,
    name varchar(223) not null,

    primary key(customer_id)
);

create table products
(
    product_id int bigint DEFAULT nextval('product_id_seq') not null,
    name varchar(223) not null,
    description varchar(223),
    price numeric not null,
    disabled boolean default false,

    primary key(product_id)
);


