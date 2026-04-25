CREATE TABLE SPRING_SESSION (
    PRIMARY_ID CHAR(36) NOT NULL,
    SESSION_ID CHAR(36) NOT NULL,
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    EXPIRY_TIME BIGINT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
    SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES BYTEA NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);

CREATE TABLE user_role(
    id SERIAL primary key,
    name varchar(25)
);

CREATE TABLE app_user(
      id BIGSERIAL primary key,
      role_id int references user_role(id),
      email varchar(30) unique,
      password varchar(128),
      name varchar(30),
      surname varchar(30)
);

CREATE TABLE form_template(
    id SERIAL primary key,
    title varchar(60),
    json text
);

CREATE TABLE sent_form_status(
    id SERIAL primary key,
    name varchar(25)
);

CREATE TABLE sent_form(
    id BIGSERIAL primary key,
    user_id int references app_user(id),
    form_template_id int references form_template(id),
    status_id int references sent_form_status(id),
    json text
);


INSERT INTO user_role VALUES
(0, 'admin'),
(1, 'supervisor'),
(2, 'worker'),
(3, 'student');

INSERT INTO app_user VALUES (0, 0, 'admin@gmail.com', '$2a$10$j9Hadn8DmGYa4P5Sw.JPBuEOkVx1/hypk7JDSxRE5Q3xrbH30d4c6', 'admin', 'kowalski');

INSERT INTO sent_form_status VALUES
(0, 'accepted'),
(1, 'pending'),
(2, 'declined');

INSERT INTO form_template VALUES
(0, 'Form 1', 'Form json description.'),
(1, 'Form 2', 'Form json description.'),
(2, 'Form 3', 'Form json description.');

INSERT INTO sent_form VALUES
(0, 0, 0, 0, 'Form json data.'),
(1, 0, 1, 0, 'Form json data.'),
(2, 0, 2, 1, 'Form json data.'),
(3, 0, 2, 2, 'Form json data.');


