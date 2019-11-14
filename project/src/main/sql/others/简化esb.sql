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
'����ϵͳ';

comment on column TAACCESSSYSTEM.SYSTEMKEY is
'����ϵͳ��ʶ';

comment on column TAACCESSSYSTEM.SYSTEMNAME  is
'����ϵͳ����';



create table TAACCESSAUTHORITY 
(
   SYSTEMKEY            VARCHAR2(30)         not null,
   SERVICEKEY           VARCHAR2(200)        not null,
   CREATEUSER           INTEGER              not null,
   CREATETIME           DATE                 not null,
   constraint PK_TAACCESSAUTHORITY primary key (SYSTEMKEY, SERVICEKEY)           
);

comment on table TAACCESSAUTHORITY is
'����ϵͳ��Ȩ';

comment on column TAACCESSAUTHORITY.SYSTEMKEY is
'����ϵͳ��ʶ';

comment on column TAACCESSAUTHORITY.SERVICEKEY  is
'�����ʶ';

comment on column TAACCESSAUTHORITY.CREATEUSER is
'�����û�';

comment on column TAACCESSAUTHORITY.CREATETIME is
'����ʱ��';

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
'������';

comment on column TASERVICEDEFINITION.SERVICEKEY is
'�����ʶ';

comment on column TASERVICEDEFINITION.SERVICEMETADATA is
'��������';

comment on column TASERVICEDEFINITION.SERVICENAME is
'��������';

comment on column TASERVICEDEFINITION.BEANID is
'Spring BeanId ����DataSourceId';

comment on column TASERVICEDEFINITION.CONTENT is
'���÷�������';

comment on column TASERVICEDEFINITION.AUTH is
'�Ƿ���Ҫ��Ȩ';


insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('SERVICE_METADATA', '��������', 'springbean', '����bean', '9999', '0', null);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('SERVICE_METADATA', '��������', 'oraclesqlquery', 'Oracle SQL��ѯ', '9999', '0', null);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('SERVICE_METADATA', '��������', 'oracleprocedure', 'Oracle �洢����', '9999', '0', null);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('SERVICE_METADATA', '��������', 'mysqlsqlquery', 'MySQL SQL��ѯ', '9999', '0', null);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('SERVICE_METADATA', '��������', 'mysqlprocedure', 'MySQL �洢����', '9999', '0', null);

