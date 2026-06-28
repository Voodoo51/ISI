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
      name varchar(40),
      surname varchar(30)
);

CREATE TABLE oauth_provider(
    id SERIAL primary key,
    name varchar(30)
);

CREATE TABLE oauth_user(
      id BIGSERIAL primary key,
      user_id BIGINT references app_user(id),
      provider_id int references oauth_provider(id),
      provider_user_id VARCHAR(128), -- varchar bo rozne serwisy uzywaja roznych idkow
      token varchar(512),
      CONSTRAINT uq_oauth_provider_user UNIQUE(provider_id, provider_user_id)
);
-- potencjalnie dodac slownik do rodzaju konta (google, github) ale raczej tylko github

CREATE TABLE form_template(
    id SERIAL primary key,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    title varchar(60),
    form_fields JSONB
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
    sent_at TIMESTAMP NOT NULL DEFAULT NOW(),
    response text, -- this should rarely be used so i tihnk its fine
    form_data JSONB
);

CREATE TABLE payment_status(
    id SERIAL primary key,
    name varchar(20)
);

--dopisane //idk może trzeba dopisać fkey do form
CREATE TABLE payment(
      id BIGSERIAL primary key,
      user_id BIGINT references app_user(id),
      payment_status_id SERIAL references payment_status(id),
      order_id text,
      title varchar(40),
      description text,
      amount BIGINT
);

CREATE TABLE proposition(
    id BIGSERIAL primary key,
    user_id BIGINT references app_user(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    title varchar(50),
    description text
);

CREATE TABLE proposition_file(
    id BIGSERIAL primary key,
    proposition_id BIGINT references proposition(id),
    file_name varchar(255),
    file_size BIGINT,
    data BYTEA
);

CREATE TABLE proposition_message(
    id BIGSERIAL primary key,
    proposition_id BIGINT REFERENCES proposition(id),
    user_id BIGINT references app_user(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    message text
);

CREATE TABLE proposition_message_file(
    id BIGSERIAL primary key,
    proposition_message_id BIGINT references proposition_message(id),
    file_name varchar(255),
    file_size BIGINT,
    data BYTEA
);

INSERT INTO user_role VALUES
(0, 'admin'),
(1, 'supervisor'),
(2, 'worker'),
(3, 'student');

INSERT INTO app_user VALUES (0, 0, 'admin@gmail.com', '$2a$10$j9Hadn8DmGYa4P5Sw.JPBuEOkVx1/hypk7JDSxRE5Q3xrbH30d4c6', 'admin', 'kowalski');
INSERT INTO app_user VALUES (1, 1, 'supervisor@gmail.com', '$2a$10$j9Hadn8DmGYa4P5Sw.JPBuEOkVx1/hypk7JDSxRE5Q3xrbH30d4c6', 'supervisor', 'kowalski');
INSERT INTO app_user VALUES (2, 2, 'worker@gmail.com', '$2a$10$j9Hadn8DmGYa4P5Sw.JPBuEOkVx1/hypk7JDSxRE5Q3xrbH30d4c6', 'worker', 'kowalski');
INSERT INTO app_user VALUES (3, 3, 'student1@gmail.com', '$2a$10$j9Hadn8DmGYa4P5Sw.JPBuEOkVx1/hypk7JDSxRE5Q3xrbH30d4c6', 'Szymon', 'Błaszczyk');
INSERT INTO app_user VALUES (4, 3, 'student2@gmail.com', '$2a$10$j9Hadn8DmGYa4P5Sw.JPBuEOkVx1/hypk7JDSxRE5Q3xrbH30d4c6', 'Katarzyna', 'Stępień');
INSERT INTO app_user VALUES (5, 3, 'student3@gmail.com', '$2a$10$j9Hadn8DmGYa4P5Sw.JPBuEOkVx1/hypk7JDSxRE5Q3xrbH30d4c6', 'Cezary', 'Buła');
INSERT INTO app_user VALUES (6, 3, 'student4@gmail.com', '$2a$10$j9Hadn8DmGYa4P5Sw.JPBuEOkVx1/hypk7JDSxRE5Q3xrbH30d4c6', 'Szymon', 'Dziadek');
INSERT INTO app_user VALUES (7, 3, 'student5@gmail.com', '$2a$10$j9Hadn8DmGYa4P5Sw.JPBuEOkVx1/hypk7JDSxRE5Q3xrbH30d4c6', 'Joanna', 'Bombol');
INSERT INTO app_user VALUES (8, 3, 'student6@gmail.com', '$2a$10$j9Hadn8DmGYa4P5Sw.JPBuEOkVx1/hypk7JDSxRE5Q3xrbH30d4c6', 'Maksymilian', 'Sulecki');
INSERT INTO app_user VALUES (9, 3, 'student7@gmail.com', '$2a$10$j9Hadn8DmGYa4P5Sw.JPBuEOkVx1/hypk7JDSxRE5Q3xrbH30d4c6', 'Olivia', 'Pacoha');
INSERT INTO app_user VALUES (10, 3, 'student8@gmail.com', '$2a$10$j9Hadn8DmGYa4P5Sw.JPBuEOkVx1/hypk7JDSxRE5Q3xrbH30d4c6', 'student', 'kowalski');
INSERT INTO app_user VALUES (11, 3, 'student9@gmail.com', '$2a$10$j9Hadn8DmGYa4P5Sw.JPBuEOkVx1/hypk7JDSxRE5Q3xrbH30d4c6', 'student', 'kowalski');

INSERT INTO sent_form_status VALUES
(0, 'accepted'),
(1, 'pending'),
(2, 'declined'),
(3, 'not_sent'),
(4, 'in_need_of_update');

INSERT INTO form_template VALUES
(0, '2026-05-27 19:03:39.91878', 'Form 1', '[{"id": 1, "label": "Numer telefonu", "type": "phoneNumber", "placeholder": "570678420"}, {"id": 2, "label": "Email", "type": "email", "placeholder": "nazwa@gmail.com"}]'),
(1, '2026-05-27 19:04:38.857737', 'Form 2', '[{"id": 1, "label": "Wydział", "type": "none", "placeholder": "WEAIL"}, {"id": 2, "label": "Dowolny wpis", "type": "none", "placeholder": "Wpis"}]'),
(2, '2026-05-27 19:05:31.703989', 'Form 3', '[{"id": 1, "label": "PESEL", "type": "none", "placeholder": "0325102358"}, {"id": 2, "label": "Kierunek", "type": "none", "placeholder": "Informatyka"}]');

INSERT INTO oauth_provider VALUES
(0, 'github');

INSERT INTO payment_status VALUES
(0, 'unpaid'),
(1, 'pending'),
(2, 'paid'),
(3, 'canceled');

INSERT INTO payment VALUES
(0, 0, 0, null, 'Tytul 1', 'Opis 1', 10000),
(1, 0, 0, null, 'Tytul 2', 'Opis 2', 5000),
(2, 0, 0, null, 'Tytul 3', 'Opis 3', 1000);
