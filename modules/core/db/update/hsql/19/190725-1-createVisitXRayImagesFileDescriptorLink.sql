create table PETCLINIC_VISIT_X_RAY_IMAGES_FILE_DESCRIPTOR_LINK (
    VISIT_ID varchar(36) not null,
    FILE_DESCRIPTOR_ID varchar(36) not null,
    primary key (VISIT_ID, FILE_DESCRIPTOR_ID)
);
