drop view IF EXISTS APPTREECODE;

drop table IF EXISTS TADATASOURCEPRO;

drop table IF EXISTS TAXQFIELD;

drop table IF EXISTS TAXQFIELDCOUNTWAY;

drop table IF EXISTS TAXQFIELDRELATION;

drop table IF EXISTS TAXQPLAN;

drop table IF EXISTS TAXQPLANFIELDCOUNT;

drop table IF EXISTS TAXQPLANFIELDGROUP;

drop table IF EXISTS TAXQPLANFIELDWHERE;

drop table IF EXISTS TAXQTHEME;

--==============================================================
-- Table: TADATASOURCEPRO
--==============================================================

create table TADATASOURCEPRO  (
  PROPERTYID           NUMERIC(20,0)                   not null,
  PROPERTYNAME         VARCHAR(60),
  VALUE              VARCHAR(10),
primary key (PROPERTYID)
      constraint PK_TADATASOURCEPRO
);

--==============================================================
-- Table: TAXQFIELD
--==============================================================

create table TAXQFIELD  (
  FIELDID              SERIAL8                  not null,
  THEMEID              NUMERIC(20,0)                   not null,
  FIELDNAME            VARCHAR(200),
  FIELDALIAS           VARCHAR(30),
  FIELDCOMMENT         VARCHAR(200),
  FIELDTYPE            VARCHAR(10),
  FIELDTREECODE        VARCHAR(60),
  FIELDISCONDITION     VARCHAR(2),
  FIELDDISPLAYTYPE     VARCHAR(2),
  FIELDISGROUP         VARCHAR(2),
  FIELDISCOUNT         VARCHAR(2),
  FIELDSORT            VARCHAR(255),
primary key (FIELDID)
      constraint PK_TAXQFIELD
);

--==============================================================
-- Table: TAXQFIELDCOUNTWAY
--==============================================================

create table TAXQFIELDCOUNTWAY  (
  COUNTWAYID           SERIAL8                   not null,
  FIELDID              NUMERIC(20,0)                   not null,
  COUNTWAY             VARCHAR(2),
primary key (COUNTWAYID)
      constraint PK_TAXQFIELDCOUNTWAY
);

--==============================================================
-- Table: TAXQFIELDRELATION
--==============================================================

create table TAXQFIELDRELATION  (
  FIELDRELAID          SERIAL8                   not null,
  FIELDID              NUMERIC(20,0)                   not null,
  FIELDRELATION        VARCHAR(2)                      not null,
primary key (FIELDRELAID)
      constraint PK_TAXQFIELDRELATION
);

--==============================================================
-- Table: TAXQPLAN
--==============================================================

create table TAXQPLAN  (
  PLANID               SERIAL8                  not null,
  THEMEID              NUMERIC(20,0)                   not null,
  PLANNAME             VARCHAR(200),
  PLANCREATERID        VARCHAR(100)                    not null,
  PLANDATE             DATETIME YEAR TO FRACTION(5)         not null,
primary key (PLANID)
      constraint PK_TAXQPLAN
);

--==============================================================
-- Table: TAXQPLANFIELDCOUNT
--==============================================================

create table TAXQPLANFIELDCOUNT  (
  COUNTID              SERIAL8                   not null,
  PLANID               NUMERIC(20,0)                   not null,
  SORT                 NUMERIC(4,0),
  FIELDID              NUMERIC(20,0)                   not null,
  COUNTWAY             VARCHAR(2),
primary key (COUNTID)
      constraint PK_TAXQPLANFIELDCOUNT
);

--==============================================================
-- Table: TAXQPLANFIELDGROUP
--==============================================================

create table TAXQPLANFIELDGROUP  (
  GROUPID              SERIAL8                  not null,
  PLANID               NUMERIC(20,0)                   not null,
  FIELDID              NUMERIC(20,0)                   not null,
  SORT                 NUMERIC(4,0),
  SORTTYPE             VARCHAR(2),
primary key (GROUPID)
      constraint PK_TAXQPLANFIELDGROUP
);

--==============================================================
-- Table: TAXQPLANFIELDWHERE
--==============================================================

create table TAXQPLANFIELDWHERE  (
  WHEREID              SERIAL8                   not null,
  PLANID               NUMERIC(20,0)                   not null,
  WHEREGROUP           NUMERIC(20,0)                   not null,
  FIELDID              NUMERIC(20,0)                   not null,
  FIELDRELATION        VARCHAR(2),
  INPUTVALUE           LVARCHAR(400),
  SORT                 NUMERIC(3,0),
primary key (WHEREID)
      constraint PK_TAXQPLANFIELDWHERE
);

--==============================================================
-- Table: TAXQTHEME
--==============================================================

create table TAXQTHEME  (
  THEMEID              SERIAL8                  not null,
  DATASOURCEID         NUMERIC(20,0)                   not null,
  MENUID               NUMERIC(20,0)                   not null,
  THEMENAME            VARCHAR(200),
  THEMETABLE           VARCHAR(40),
  THEMEWHERE           VARCHAR(200),
  THEMEWHEREDESC       LVARCHAR(500),
  THEMECREATERID       NUMERIC(15,0)                   not null,
  THEMEDATE            DATETIME YEAR TO FRACTION(5)         not null,
primary key (THEMEID)
      constraint PK_TAXQTHEME
);



--==============================================================
-- View: APPTREECODE
--==============================================================
create view APPTREECODE
(id,pid,name,isparent,type,icon,iconskin,py)
as
(SELECT
  THEMEID AS ID,
  DATASOURCEID AS PID,
  THEMENAME AS NAME,
  '' AS isParent,
  'THEMETREE' AS TYPE,
  '' AS icon,
  'icon-028?#259c7a' AS iconskin,
  NULL::char AS py
  FROM TAXQTHEME
UNION ALL
SELECT
  DATASOURCEID AS ID,
  0 AS PID,
  DATASOURCENAME AS NAME,
  '' AS isParent,
  'THEMETREE' AS TYPE,
  '' AS icon,
  'icon-035?#259c8e' AS iconskin,
  null::char AS py
  FROM TADATASOURCE
UNION ALL
SELECT
  0 AS ID,
  null::numeric AS PID,
  '数据源' AS NAME,
  '' AS isParent,
  'THEMETREE' AS TYPE,
  '' AS icon,
  'icon-040?#35899c' AS iconskin,
  null::char AS py
  FROM sysmaster: sysshmvals);

insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQDATATYPE', '数据类型', '93', '日期型', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQDATATYPE', '数据类型', '3', '数字型', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQDATATYPE', '数据类型', '1', 'CHAR型', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQDATATYPE', '数据类型', '12', 'VARCHAR2型', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQDISPLAY', '查询统计项目值的展现形式', '11', '文本', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQDISPLAY', '查询统计项目值的展现形式', '12', '年月', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQDISPLAY', '查询统计项目值的展现形式', '13', '日期', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQDISPLAY', '查询统计项目值的展现形式', '14', '数字', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQORDERBY', '排序方式', '2', '降序', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQORDERBY', '排序方式', '1', '升序', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQDISPLAY', '查询统计项目值的展现形式', '22', '树', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQDISPLAY', '查询统计项目值的展现形式', '21', '代码值平铺', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQRELA', '支持的关系', '42', '不包含', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQCOUNT', '查询统计结果的计算方式', '6', '最小值', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQCOUNT', '查询统计结果的计算方式', '5', '最大值', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQCOUNT', '查询统计结果的计算方式', '4', '求平均', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQCOUNT', '查询统计结果的计算方式', '3', '求和', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQCOUNT', '查询统计结果的计算方式', '2', '去重后计数', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQCOUNT', '查询统计结果的计算方式', '1', '计数', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQRELA', '支持的关系', '41', '包含', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQRELA', '支持的关系', '32', '小于等于', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQRELA', '支持的关系', '31', '小于', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQRELA', '支持的关系', '22', '大于等于', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQRELA', '支持的关系', '21', '大于', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQRELA', '支持的关系', '12', '不等于', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('XQRELA', '支持的关系', '11', '等于', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('DBTYPE', '数据源类型', '1', 'Oracle', '9999', '0', '0');
insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('DBTYPE', '数据源类型', '2', 'MySQL', '9999', '0', '0');
INSERT INTO tamenu (menuid, pmenuid, menuname, url, menuidpath, menunamepath, iconskin, selectimage, reportid, accesstimeel, effective, securitypolicy, isdismultipos, quickcode, sortno, resourcetype, menulevel, isleaf, menutype, iscache, syspath, useyab003, typeflag, isaudite, consolemodule, customencoding, isfiledscontrol)
VALUES (400, 3, '通用查询', 'theme/themeController.do', '1/3/400', '银海软件/开发管理/通用查询', '', null, null, '', '1', '1', '1', null, 6, '01', 3, '0', '1', null, 'sysmg', '0', null, '0', '0', '', '0');