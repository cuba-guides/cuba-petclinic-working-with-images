-- begin PETCLINIC_PET
create table PETCLINIC_PET (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    --
    IDENTIFICATION_NUMBER varchar(255) not null,
    BIRTH_DATE date,
    TYPE_ID varchar(36),
    OWNER_ID varchar(36),
    --
    primary key (ID)
)^
-- end PETCLINIC_PET
-- begin PETCLINIC_PET_TYPE
create table PETCLINIC_PET_TYPE (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    --
    primary key (ID)
)^
-- end PETCLINIC_PET_TYPE
-- begin PETCLINIC_OWNER
create table PETCLINIC_OWNER (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    FIRST_NAME varchar(255) not null,
    LAST_NAME varchar(255),
    --
    ADDRESS varchar(255) not null,
    CITY varchar(255) not null,
    EMAIL varchar(255),
    TELEPHONE varchar(255),
    --
    primary key (ID)
)^
-- end PETCLINIC_OWNER
-- begin PETCLINIC_VET
create table PETCLINIC_VET (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    FIRST_NAME varchar(255) not null,
    LAST_NAME varchar(255),
    --
    IMAGE_ID varchar(36),
    --
    primary key (ID)
)^
-- end PETCLINIC_VET
-- begin PETCLINIC_SPECIALTY
create table PETCLINIC_SPECIALTY (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    NAME varchar(255),
    --
    primary key (ID)
)^
-- end PETCLINIC_SPECIALTY
-- begin PETCLINIC_VISIT
create table PETCLINIC_VISIT (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    VISIT_DATE date not null,
    DESCRIPTION varchar(4000),
    PET_ID varchar(36) not null,
    TREATING_VET_ID varchar(36),
    --
    primary key (ID)
)^
-- end PETCLINIC_VISIT
-- begin PETCLINIC_VET_SPECIALTY_LINK
create table PETCLINIC_VET_SPECIALTY_LINK (
    VET_ID varchar(36) not null,
    SPECIALTY_ID varchar(36) not null,
    primary key (VET_ID, SPECIALTY_ID)
)^
-- end PETCLINIC_VET_SPECIALTY_LINK
-- begin PETCLINIC_VISIT_X_RAY_IMAGES_FILE_DESCRIPTOR_LINK
create table PETCLINIC_VISIT_X_RAY_IMAGES_FILE_DESCRIPTOR_LINK (
    VISIT_ID varchar(36) not null,
    FILE_DESCRIPTOR_ID varchar(36) not null,
    primary key (VISIT_ID, FILE_DESCRIPTOR_ID)
)^
-- end PETCLINIC_VISIT_X_RAY_IMAGES_FILE_DESCRIPTOR_LINK
