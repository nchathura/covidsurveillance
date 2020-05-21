create table globalcovidone
(
    date      date not null
        primary key,
    confirmed int  not null,
    recovered int  not null,
    deaths    int  not null
);

create table globalcovidtwo
(
    date      date not null,
    confirmed int  not null,
    recovered int  not null,
    deaths    int  not null
);

create table hospitalinformation
(
    id               varchar(50)  not null
        primary key,
    HospitalName     varchar(525) not null,
    city             varchar(225) not null,
    district         varchar(225) not null,
    capacity         int          not null,
    director         varchar(225) not null,
    directorContact  varchar(11)  not null,
    hospitalContact1 varchar(11)  not null,
    hospitalContact2 varchar(11)  null,
    hospitalFax      varchar(11)  not null,
    hospitalEmail    varchar(300) not null
);

create table qcinformation
(
    id          varchar(200) not null
        primary key,
    qcName      varchar(200) not null,
    district    varchar(200) not null,
    city        varchar(200) not null,
    capacity    int          not null,
    head        varchar(200) not null,
    headContact varchar(11)  not null,
    qcContact   varchar(11)  not null,
    qcContact2  varchar(11)  not null
);

create table userinformation
(
    name     varchar(200) not null,
    contact  varchar(11)  not null,
    userName varchar(200) not null,
    email    varchar(200) not null,
    password varchar(200) not null,
    userRole varchar(200) not null,
    constraint userInformation_userName_uindex
        unique (userName)
);

alter table userinformation
    add primary key (userName);

create table user_hospital
(
    username varchar(200) not null,
    hospital varchar(200) null,
    constraint user_hospital_username_uindex
        unique (username),
    constraint user_hospital_userinformation__fk
        foreign key (username) references userinformation (userName)
            on update cascade on delete cascade
);

alter table user_hospital
    add primary key (username);

create table user_qccenter
(
    username varchar(200) not null,
    qcenter  varchar(200) not null,
    constraint user_qccenter_username_uindex
        unique (username),
    constraint user_qccenter_userinformation__fk
        foreign key (username) references userinformation (userName)
);


