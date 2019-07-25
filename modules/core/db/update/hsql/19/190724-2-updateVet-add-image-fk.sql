alter table PETCLINIC_VET add constraint FK_PETCLINIC_VET_IMAGE foreign key (IMAGE_ID) references SYS_FILE(ID);
create index IDX_PETCLINIC_VET_IMAGE on PETCLINIC_VET (IMAGE_ID);
