create sequence hibernate_sequence;

alter sequence hibernate_sequence owner to postgres;

create table if not exists users 
(
	id varchar(255) not null
		constraint users_pkey
			primary key,
	name varchar(255),
	roles integer not null,
	username varchar(255) not null
		constraint user_username_uk
			unique
);

alter table users owner to postgres;

create table if not exists credentials
(
	id varchar(255) not null
		constraint credentials_pkey
			primary key,
	password varchar(255) not null,
	roles integer not null,
	username varchar(255) not null
		constraint credentials_username_uk
			unique
);

alter table credentials owner to postgres;

create table if not exists applications
(
	id varchar(255) not null
		constraint applications_pkey
			primary key,
	name varchar(255)
);

alter table applications owner to postgres;

create table if not exists revision
(
	id bigint not null
		constraint revision_pkey
			primary key,
	modified_at timestamp,
	modified_by varchar(255)
);

alter table revision owner to postgres;

create table if not exists z_applications
(
	id varchar(255) not null,
	rev bigint not null
		constraint z_applications_revision_fk
			references revision,
	revtype smallint,
	name varchar(255),
	constraint z_applications_pkey
		primary key (id, rev)
);

alter table z_applications owner to postgres;

create table if not exists component
(
	id varchar(255) not null
		constraint component_pkey
			primary key,
	name varchar(255)
);

alter table component owner to postgres;

create table if not exists z_component
(
	id varchar(255) not null,
	rev bigint not null
		constraint z_component_revision_fk
			references revision,
	revtype smallint,
	name varchar(255),
	constraint z_component_pkey
		primary key (id, rev)
);

alter table z_component owner to postgres;

