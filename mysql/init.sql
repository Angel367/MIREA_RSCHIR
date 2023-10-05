CREATE DATABASE IF NOT EXISTS appDB;

USE appDB;

CREATE TABLE appDB.country (
    id int NOT NULL AUTO_INCREMENT UNIQUE,
    name varchar(256) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE appDB.city (
  id int NOT NULL AUTO_INCREMENT UNIQUE,
  name varchar(32) NOT NULL,
  population int NOT NULL,
  country_id int NOT NULL,
  created timestamp NOT NULL DEFAULT NOW(),
  modified timestamp NOT NULL on update current_timestamp() DEFAULT NOW(),
  PRIMARY KEY (id),
  FOREIGN KEY (country_id) REFERENCES country (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO appDB.country (name) VALUES ('Russia'),('USA'), ('China');

INSERT INTO appDB.city (name, population, country_id) VALUES
                                                          ('Moscow', 12692466, 1),
                                                          ('St. Petersburg', 5398064, 1),
                                                          ('New York', 8336817, 2),
                                                          ('Los Angeles', 39776830, 2),
                                                          ('Beijing', 21516000, 3),
                                                          ('Shanghai', 24183300, 3),
                                                          ('Sanya', 685350, 3),
                                                          ('Guangzhou', 13909000, 3),
                                                          ('Shenzhen', 13000000, 3),
                                                          ('Hong Kong', 7496981, 3);
