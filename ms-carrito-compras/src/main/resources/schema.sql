CREATE TABLE IF NOT EXISTS CUSTOMER(
    idCustomer SERIAL PRIMARY KEY,
    sdi VARCHAR(40),
    firstName VARCHAR(80),
    lastName VARCHAR(90),
    phoneNumber VARCHAR(20),
    jobType VARCHAR(40),
    email VARCHAR(255),
    country VARCHAR(166),
    city VARCHAR(155),
    address VARCHAR(155)
);

CREATE TABLE IF NOT EXISTS SUPPLIER(
    idsupplier SERIAL PRIMARY KEY,
    name VARCHAR(80),
    jobType VARCHAR(40),
    country VARCHAR(166),
    city VARCHAR(155),
    address VARCHAR(155)
);

CREATE TABLE IF NOT EXISTS PRODUCT(
    idProduct SERIAL PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(190),
    department VARCHAR(90),
    material VARCHAR(90),
    imageUrl VARCHAR(255),
    price NUMERIC,
    stock NUMERIC
);

CREATE TABLE IF NOT EXISTS SHOPPINGCART(
    idShoppingCart SERIAL PRIMARY KEY,
    idcustomer SERIAL,
    creationdate TIMESTAMP,
    totalbuy NUMERIC,
    shippingcost NUMERIC,
    tax NUMERIC
);

CREATE TABLE IF NOT EXISTS DETAILCART(
    idDetailCart SERIAL PRIMARY KEY,
    idShoppingCart SERIAL,
    idProduct SERIAL,
    quantity NUMERIC,
    totalprice NUMERIC
);

CREATE TABLE IF NOT EXISTS ORDERSALE(
    idordersale SERIAL PRIMARY KEY,
    idcustomer SERIAL,
    idshoppingcart SERIAL,
    orderdate TIMESTAMP,
    totalbuy NUMERIC,
    orderstatus VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS ORDERBUY(
    idorderbuy SERIAL PRIMARY KEY,
    idsupplier SERIAL,
    orderdate TIMESTAMP,
    orderstatus VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS ITEMORDER(
    idItemOrder SERIAL PRIMARY KEY,
    idOrderBuy SERIAL,
    idProduct SERIAL,
    quantity NUMERIC,
    totalprice NUMERIC
);