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

ALTER TABLE keyword
    ADD CONSTRAINT FK_KEYWORD_ON_PROPERTY FOREIGN KEY (property_id) REFERENCES property (id);

ALTER TABLE member_consultation
DROP
COLUMN medium;

ALTER TABLE member_consultation
DROP
COLUMN status;

ALTER TABLE member_consultation
    ADD medium VARCHAR(255) NOT NULL;

ALTER TABLE property
DROP
COLUMN property_type;

ALTER TABLE property
DROP
COLUMN sales_type;

ALTER TABLE property
    ADD property_type VARCHAR(255) NOT NULL;

ALTER TABLE `admin`
DROP
COLUMN `role`;

ALTER TABLE `admin`
DROP
COLUMN status;

ALTER TABLE `admin`
    ADD `role` VARCHAR(255) NULL;

ALTER TABLE member
DROP
COLUMN `role`;

ALTER TABLE member
    ADD `role` VARCHAR(255) NULL;

ALTER TABLE property
    ADD sales_type VARCHAR(255) NOT NULL;

ALTER TABLE `admin`
    ADD status VARCHAR(255) NULL;

ALTER TABLE member_consultation
    ADD status VARCHAR(255) NOT NULL;

ALTER TABLE admin_consultation
DROP
COLUMN tier;

ALTER TABLE admin_consultation
    ADD tier VARCHAR(255) NULL;

ALTER TABLE file
DROP
COLUMN type;

ALTER TABLE file
    ADD type VARCHAR(255) NOT NULL;