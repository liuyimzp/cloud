create table TAFILEIMPORT
(
  FILE_ID      VARCHAR2(50) not null,
  FILE_NAME      VARCHAR2(50) not null,
  UPLOADGUID   VARCHAR2(100) not null,
  USER_ID   NUMBER(10) not null,
  USER_NAME   VARCHAR2(50) not null,
  MENU_ID       NUMBER(10) ,
  PROGRESS     VARCHAR2(10) not null,
  STOREPROGRESS     VARCHAR2(50) ,
  IS_END  		VARCHAR2(1),
  ERRORINFO 	CLOB,
  BEGINDATE     TIMESTAMP(6),
  ENDDATE     TIMESTAMP(6)
);
comment on column TAFILEIMPORT.FILE_ID
  is '上传文件id';
comment on column TAFILEIMPORT.FILE_NAME
  is '上传文件名称';
comment on column TAFILEIMPORT.UPLOADGUID
  is '上传id';
comment on column TAFILEIMPORT.USER_ID
  is '上传人员id';
comment on column TAFILEIMPORT.USER_NAME
  is '上传人员名称';
comment on column TAFILEIMPORT.MENU_ID
  is '上传菜单id';
comment on column TAFILEIMPORT.PROGRESS
  is '处理进度';
comment on column TAFILEIMPORT.STOREPROGRESS
  is '存储进度';
comment on column TAFILEIMPORT.IS_END
  is '是否结束';
comment on column TAFILEIMPORT.ERRORINFO
  is '异常信息';
comment on column TAFILEIMPORT.BEGINDATE
  is '开始时间';
comment on column TAFILEIMPORT.ENDDATE
  is '结束时间';
alter table TAFILEIMPORT
  add constraint TAFILEIMPORT primary key (FILE_ID)
  using index;

create table TAFILEIMPORTCONFIG
(
  PREFIX      VARCHAR2(20) not null,
  DAO_NAME   VARCHAR2(20),
  FIELDS   VARCHAR2(200) not null,
  DOMAIN_CLASS   VARCHAR2(100),
  IMPORT_CLASS   VARCHAR2(100),
  SQL_STATEMENT_NAME     VARCHAR2(20) not null,
  BATCHNUM           NUMBER(10) default 100,
  TITLENUM           NUMBER(10) default 1
);
comment on column TAFILEIMPORTCONFIG.PREFIX
  is '上传文件前缀';
comment on column TAFILEIMPORTCONFIG.DAO_NAME
  is '导入使用dao';
comment on column TAFILEIMPORTCONFIG.FIELDS
  is '导入字段';
comment on column TAFILEIMPORTCONFIG.DOMAIN_CLASS
  is '数据对象';
comment on column TAFILEIMPORTCONFIG.IMPORT_CLASS
  is '自定义处理类';
comment on column TAFILEIMPORTCONFIG.SQL_STATEMENT_NAME
  is 'SQLID';
comment on column TAFILEIMPORTCONFIG.BATCHNUM
  is '批量执行数量';
comment on column TAFILEIMPORTCONFIG.TITLENUM
  is '表格表头行数';
alter table TAFILEIMPORTCONFIG
  add constraint TAFILEIMPORTCONFIG primary key (PREFIX)
  using index;

create table TAFILEIMPORTDATA
(
  FILE_ID      VARCHAR2(50) not null,
  FILE_NAME      VARCHAR2(50) not null,
  UPLOADGUID   VARCHAR2(100) not null,
  FILE_SIGN VARCHAR2(150) not null,
  FIELDS   VARCHAR2(200) not null,
  DOMAIN_CLASS   VARCHAR2(100) not null,
  FILE_PAGE NUMBER not null,
  DATA_LIST CLOB not null,
  DATA_SIGN NUMBER not null,
  ID  VARCHAR2(50) not null
);
comment on column TAFILEIMPORTDATA.FILE_ID
  is '文件ID';
comment on column TAFILEIMPORTDATA.FILE_NAME
  is '文件名称';
comment on column TAFILEIMPORTDATA.UPLOADGUID
  is '导入标识';
comment on column TAFILEIMPORTDATA.FILE_SIGN
  is '文件标识';
comment on column TAFILEIMPORTDATA.FIELDS
  is '字段';
comment on column TAFILEIMPORTDATA.DOMAIN_CLASS
  is '数据对象';
comment on column TAFILEIMPORTDATA.FILE_PAGE
  is '页码';
comment on column TAFILEIMPORTDATA.DATA_LIST
  is '数据';
comment on column TAFILEIMPORTDATA.DATA_SIGN
  is '数据标志';
create index FILESIGN on TAFILEIMPORTDATA (FILE_SIGN);