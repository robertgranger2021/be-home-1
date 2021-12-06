DROP ALL OBJECTS;

CREATE TABLE invoice (
    id_invoice int NOT NULL AUTO_INCREMENT,
    customer_email varchar(70) NOT NULL,
    customer_name varchar(50) NOT NULL,
    description varchar(255) NOT NULL,
    due_date date NOT NULL,
    status varchar(10) NOT NULL,
    total decimal(10,2) NOT NULL,
    PRIMARY KEY (id_invoice)
);

CREATE TABLE line_item (
    id_line_item int NOT NULL AUTO_INCREMENT,
    invoice_id_invoice_fk int NOT NULL,
    line_item_description varchar(255) NOT NULL,
    cost decimal(10,2),
    PRIMARY KEY (id_line_item)
);