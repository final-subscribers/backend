CREATE SCHEMA IF NOT EXISTS cheongyak;
USE cheongyak;

CREATE TABLE `admin`
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime NULL,
    updated_at   datetime NULL,
    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    address      VARCHAR(255) NOT NULL,
    business     VARCHAR(255) NOT NULL,
    status       VARCHAR(255) NULL,
    `role`       VARCHAR(255) NULL,
    CONSTRAINT pk_admin PRIMARY KEY (id)
);

CREATE TABLE admin_consultation
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    created_at      datetime NULL,
    updated_at      datetime NULL,
    tier            VARCHAR(255) NULL,
    consult_message VARCHAR(255) NULL,
    consultant      VARCHAR(255) NULL,
    completed_at    date NULL,
    CONSTRAINT pk_admin_consultation PRIMARY KEY (id)
);

CREATE TABLE area
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    created_at       datetime NULL,
    updated_at       datetime NULL,
    square_meter     INT    NOT NULL,
    price            INT    NOT NULL,
    discount_percent INT NULL,
    discount_price   INT NULL,
    property_id      BIGINT NOT NULL,
    CONSTRAINT pk_area PRIMARY KEY (id)
);

CREATE TABLE file
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime NULL,
    updated_at  datetime NULL,
    admin_id    BIGINT NULL,
    property_id BIGINT NULL,
    name        VARCHAR(255) NOT NULL,
    link        VARCHAR(255) NOT NULL,
    type        VARCHAR(255) NOT NULL,
    CONSTRAINT pk_file PRIMARY KEY (id)
);

CREATE TABLE keyword
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at    datetime NULL,
    updated_at    datetime NULL,
    json_value    JSON   NOT NULL,
    name          VARCHAR(255) NULL,
    type          VARCHAR(255) NULL,
    is_searchable BIT(1) NOT NULL,
    property_id   BIGINT NOT NULL,
    CONSTRAINT pk_keyword PRIMARY KEY (id)
);

CREATE TABLE likes
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime NULL,
    updated_at  datetime NULL,
    member_id   BIGINT NOT NULL,
    property_id BIGINT NOT NULL,
    CONSTRAINT pk_likes PRIMARY KEY (id)
);

CREATE TABLE member
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime NULL,
    updated_at   datetime NULL,
    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    `role`       VARCHAR(255) NULL,
    CONSTRAINT pk_member PRIMARY KEY (id)
);

CREATE TABLE member_consultation
(
    id                    BIGINT AUTO_INCREMENT NOT NULL,
    created_at            datetime NULL,
    updated_at            datetime NULL,
    status                VARCHAR(255) NOT NULL,
    member_message        VARCHAR(255) NULL,
    preferred_at          date         NOT NULL,
    member_name           VARCHAR(255) NULL,
    phone_number          VARCHAR(255) NULL,
    medium                VARCHAR(255) NOT NULL,
    member_id             BIGINT NULL,
    property_id           BIGINT NULL,
    admin_consultation_id BIGINT NULL,
    CONSTRAINT pk_member_consultation PRIMARY KEY (id)
);

CREATE TABLE property
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    created_at       datetime NULL,
    updated_at       datetime NULL,
    image_url        VARCHAR(255) NOT NULL,
    name             VARCHAR(255) NOT NULL,
    constructor      VARCHAR(255) NOT NULL,
    area_addr        VARCHAR(255) NOT NULL,
    model_house_addr VARCHAR(255) NOT NULL,
    phone_number     VARCHAR(255) NOT NULL,
    contact_channel  VARCHAR(255) NULL,
    home_page        VARCHAR(255) NULL,
    like_count       INT          NOT NULL,
    start_date       date         NOT NULL,
    end_date         date         NOT NULL,
    property_type    VARCHAR(255) NOT NULL,
    sales_type       VARCHAR(255) NOT NULL,
    total_number     INT          NOT NULL,
    company_name     VARCHAR(255) NOT NULL,
    addr_do          VARCHAR(255) NOT NULL,
    addr_gu          VARCHAR(255) NOT NULL,
    addr_dong        VARCHAR(255) NOT NULL,
    building_name    VARCHAR(255) NOT NULL,
    price            INT          NOT NULL,
    discount_price   INT NULL,
    discount_percent INT NULL,
    admin_id         BIGINT       NOT NULL,
    CONSTRAINT pk_property PRIMARY KEY (id)
);

ALTER TABLE member_consultation
    ADD CONSTRAINT uc_member_consultation_admin_consultation UNIQUE (admin_consultation_id);

ALTER TABLE area
    ADD CONSTRAINT FK_AREA_ON_PROPERTY FOREIGN KEY (property_id) REFERENCES property (id);

ALTER TABLE file
    ADD CONSTRAINT FK_FILE_ON_ADMIN FOREIGN KEY (admin_id) REFERENCES `admin` (id);

ALTER TABLE file
    ADD CONSTRAINT FK_FILE_ON_PROPERTY FOREIGN KEY (property_id) REFERENCES property (id);

ALTER TABLE keyword
    ADD CONSTRAINT FK_KEYWORD_ON_PROPERTY FOREIGN KEY (property_id) REFERENCES property (id);

ALTER TABLE likes
    ADD CONSTRAINT FK_LIKES_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE likes
    ADD CONSTRAINT FK_LIKES_ON_PROPERTY FOREIGN KEY (property_id) REFERENCES property (id);

ALTER TABLE member_consultation
    ADD CONSTRAINT FK_MEMBER_CONSULTATION_ON_ADMIN_CONSULTATION FOREIGN KEY (admin_consultation_id) REFERENCES admin_consultation (id);

ALTER TABLE member_consultation
    ADD CONSTRAINT FK_MEMBER_CONSULTATION_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE member_consultation
    ADD CONSTRAINT FK_MEMBER_CONSULTATION_ON_PROPERTY FOREIGN KEY (property_id) REFERENCES property (id);

ALTER TABLE property
    ADD CONSTRAINT FK_PROPERTY_ON_ADMIN FOREIGN KEY (admin_id) REFERENCES `admin` (id);
