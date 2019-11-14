drop table TAACCESSSYSTEM cascade constraints;

drop table TAACCESSAUTHORITY cascade constraints;

drop table TASERVICEDEFINITION cascade constraints;

create table TAACCESSSYSTEM
(
   SYSTEMKEY            VARCHAR2(30)         not null,
   SYSTEMNAME           VARCHAR2(200)        not null,
   constraint PK_TAACCESSSYSTEM primary key (SYSTEMKEY)
);

comment on table TAACCESSSYSTEM is
'接入系统';

comment on column TAACCESSSYSTEM.SYSTEMKEY is
'接入系统标识';

comment on column TAACCESSSYSTEM.SYSTEMNAME  is
'接入系统名称';



create table TAACCESSAUTHORITY 
(
   SYSTEMKEY            VARCHAR2(30)         not null,
   SERVICEKEY           VARCHAR2(200)        not null,
   CREATEUSER           INTEGER              not null,
   CREATETIME           DATE                 not null,
   constraint PK_TAACCESSAUTHORITY primary key (SYSTEMKEY, SERVICEKEY)           
);

comment on table TAACCESSAUTHORITY is
'接入系统授权';

comment on column TAACCESSAUTHORITY.SYSTEMKEY is
'接入系统标识';

comment on column TAACCESSAUTHORITY.SERVICEKEY  is
'服务标识';

comment on column TAACCESSAUTHORITY.CREATEUSER is
'操作用户';

comment on column TAACCESSAUTHORITY.CREATETIME is
'操作时间';

create table TASERVICEDEFINITION 
(
   SERVICEKEY           VARCHAR2(30)         not null,
   SERVICEMETADATA      VARCHAR2(20)         not null,
   SERVICENAME          VARCHAR2(200)        not null,
   BEANID               VARCHAR2(40),
   CONTENT              CLOB                 not null,
   AUTH                 VARCHAR2(2)          not null,
   constraint PK_TASERVICEDEFINITION primary key (SERVICEKEY)
);

comment on table TASERVICEDEFINITION is
'服务定义';

comment on column TASERVICEDEFINITION.SERVICEKEY is
'服务标识';

comment on column TASERVICEDEFINITION.SERVICEMETADATA is
'服务类型';

comment on column TASERVICEDEFINITION.SERVICENAME is
'服务名称';

comment on column TASERVICEDEFINITION.BEANID is
'Spring BeanId 或者DataSourceId';

comment on column TASERVICEDEFINITION.CONTENT is
'调用方法内容';

comment on column TASERVICEDEFINITION.AUTH is
'是否需要授权';


insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('SERVICE_METADATA', '服务类型', 'springbean', '本地bean', '9999', '0', null);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('SERVICE_METADATA', '服务类型', 'oraclesqlquery', 'Oracle SQL查询', '9999', '0', null);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('SERVICE_METADATA', '服务类型', 'oracleprocedure', 'Oracle 存储过程', '9999', '0', null);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('SERVICE_METADATA', '服务类型', 'mysqlsqlquery', 'MySQL SQL查询', '9999', '0', null);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('SERVICE_METADATA', '服务类型', 'mysqlprocedure', 'MySQL 存储过程', '9999', '0', null);

