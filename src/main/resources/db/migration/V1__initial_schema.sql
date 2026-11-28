CREATE TABLE students (
    id INT NOT NULL AUTO_INCREMENT,
    address VARCHAR(255) NOT NULL,
    admission_date DATE NOT NULL,
    course VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    email VARCHAR(255) NOT NULL,
    gender VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT,
    enabled BIT NOT NULL,
    password VARCHAR(255),
    role VARCHAR(255),
    username VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB;
