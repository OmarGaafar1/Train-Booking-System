/*==============================================================*/
/* DBMS name:      Microsoft SQL Server 2017                    */
/* Created on:     5/16/2023 5:08:58 PM                         */
/*==============================================================*/


if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BOOKING') and o.name = 'FK_BOOKING_BOOKING_USER')
alter table BOOKING
   drop constraint FK_BOOKING_BOOKING_USER
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BOOKING') and o.name = 'FK_BOOKING_BOOKING2_TRIP')
alter table BOOKING
   drop constraint FK_BOOKING_BOOKING2_TRIP
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('BOOKING') and o.name = 'FK_BOOKING_BOOKING3_SEAT')
alter table BOOKING
   drop constraint FK_BOOKING_BOOKING3_SEAT
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('SEAT') and o.name = 'FK_SEAT_HAS1_TRAIN')
alter table SEAT
   drop constraint FK_SEAT_HAS1_TRAIN
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TRIP') and o.name = 'FK_TRIP_HAS2_TRAIN')
alter table TRIP
   drop constraint FK_TRIP_HAS2_TRAIN
go

if exists (select 1
            from  sysobjects
           where  id = object_id('ADMIN')
            and   type = 'U')
   drop table ADMIN
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('BOOKING')
            and   name  = 'BOOKING3_FK'
            and   indid > 0
            and   indid < 255)
   drop index BOOKING.BOOKING3_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('BOOKING')
            and   name  = 'BOOKING2_FK'
            and   indid > 0
            and   indid < 255)
   drop index BOOKING.BOOKING2_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('BOOKING')
            and   name  = 'BOOKING_FK'
            and   indid > 0
            and   indid < 255)
   drop index BOOKING.BOOKING_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('BOOKING')
            and   type = 'U')
   drop table BOOKING
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('SEAT')
            and   name  = 'HAS1_FK'
            and   indid > 0
            and   indid < 255)
   drop index SEAT.HAS1_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('SEAT')
            and   type = 'U')
   drop table SEAT
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TRAIN')
            and   type = 'U')
   drop table TRAIN
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('TRIP')
            and   name  = 'HAS2_FK'
            and   indid > 0
            and   indid < 255)
   drop index TRIP.HAS2_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TRIP')
            and   type = 'U')
   drop table TRIP
go

if exists (select 1
            from  sysobjects
           where  id = object_id('"USER"')
            and   type = 'U')
   drop table "USER"
go

/*==============================================================*/
/* Table: ADMIN                                                 */
/*==============================================================*/
create table ADMIN (
   ADMINID              bigint               not null,
   NAME                 varchar(20)          null,
   EMAIL                varchar(100)         not null,
   PASSWORD             varchar(20)          not null,
   constraint PK_ADMIN primary key (ADMINID)
)
go

/*==============================================================*/
/* Table: BOOKING                                               */
/*==============================================================*/
create table BOOKING (
   USERID               bigint               not null,
   TRIPID               bigint               not null,
   BOOKINGID            bigint               not null,
   constraint PK_BOOKING primary key (BOOKINGID)
)
go

/*==============================================================*/
/* Index: BOOKING_FK                                            */
/*==============================================================*/





/*==============================================================*/
/* Index: BOOKING2_FK                                           */
/*==============================================================*/





/*==============================================================*/
/* Index: BOOKING3_FK                                           */
/*==============================================================*/



/*==============================================================*/
/* Table: SEAT                                                 */
/*==============================================================*/
create table SEAT (
   SEATID               bigint               not null,
   TRAINID              bigint               null,
   BookingID            bigint               null,
   SEATNO               bigint               not null,
   constraint PK_SEAT primary key (SEATID)
)
go

/*==============================================================*/
/* Index: HAS1_FK                                               */
/*==============================================================*/




/*==============================================================*/
/* Table: TRAIN                                                 */
/*==============================================================*/
create table TRAIN (
   TRAINID              bigint               not null,
   NAME                 varchar(20)          not null,
   CAPACITY             int                  not null,
   constraint PK_TRAIN primary key (TRAINID)
)
go

/*==============================================================*/
/* Table: TRIP                                                  */
/*==============================================================*/
create table TRIP (
   TRIPID               bigint               not null,
   TRAINID              bigint               not null,
   DATE                 date                 not null,
   SOURCE               varchar(20)          not null,
   DESTINATION          varchar(20)          not null,
   ARRIVALTIME          time                 not null,
   DEPARTURETIME        time                 not null,
   constraint PK_TRIP primary key (TRIPID)
)


/*==============================================================*/
/* Table: "USER"                                                */
/*==============================================================*/
create table "USER" (
   USERID               bigint               not null,
   NAME                 varchar(20)          not null,
   EMAIL                varchar(100)         not null,
   PASSWORD             varchar(20)          not null,
   constraint PK_USER primary key (USERID)
)
go

alter table BOOKING
   add constraint FK_BOOKING_BOOKING_USER foreign key (USERID)
      references "USER" (USERID)
go

alter table BOOKING
   add constraint FK_BOOKING_BOOKING2_TRIP foreign key (TRIPID)
      references TRIP (TRIPID)
go

alter table Seat
   add constraint FK_BOOKING_SEAT foreign key (BookingID)
      references Booking (BookingID)
go

alter table SEAT
   add constraint FK_SEAT_HAS1_TRAIN foreign key (TRAINID)
      references TRAIN (TRAINID)
go

alter table TRIP
   add constraint FK_TRIP_HAS2_TRAIN foreign key (TRAINID)
      references TRAIN (TRAINID)
go

