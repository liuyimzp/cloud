--4.0.0-SNAPSHOT码表0/1标志位调整 2017-2-15 update ByMinusZero In 2018-10-18
--update AA10 set AAE120 = '2' where AAE120 = '0';
 --update AA10 set AAE120 = '0' where AAE120 = '1';
--update AA10 set AAE120 = '1' where AAE120 = '2';
commit;
--4.0.0-SNAPSHOT 升级 调整yab003/yab139长度 2017-04-25
alter table taorg modify  YAB003 varchar2(20);
alter table taorg modify  YAB139 varchar2(20);

alter table aa10 modify  AAA102 VARCHAR2(20);
alter table aa10 modify  YAB003 VARCHAR2(20);

alter table TAYAB003LEVELMG modify  PYAB003 VARCHAR2(20);
alter table TAYAB003LEVELMG modify  YAB003 VARCHAR2(20);

alter table TAYAB139MG modify  YAB139 VARCHAR2(20);
alter table TAYAB139MG modify  YAB003 VARCHAR2(20);

alter table taadminyab139scope modify  YAB139 VARCHAR2(20);
--4.0.0-SNAPSHOT页面还原ucm方式新增字段-oracle 2017-05-31
drop table TAPAGEREVIEW;
create table TAPAGEREVIEW
(
  page_id     NUMBER(20) not null,
  function_id NUMBER(20),
  create_time DATE,
  context     BLOB,
  data        BLOB,
  olddata     BLOB,
  otherdata   CLOB,
  userinfo    BLOB,
  storetype   VARCHAR2(20),
  doc_id      VARCHAR2(20)
);
-- Add comments to the columns
comment on column TAPAGEREVIEW.page_id
  is 'id';
comment on column TAPAGEREVIEW.function_id
  is '页面功能id';
comment on column TAPAGEREVIEW.create_time
  is '创建时间';
comment on column TAPAGEREVIEW.context
  is '页面内容';
comment on column TAPAGEREVIEW.data
  is '页面新数据';
comment on column TAPAGEREVIEW.olddata
  is '页面老数据';
comment on column TAPAGEREVIEW.otherdata
  is '其它数据';
comment on column TAPAGEREVIEW.userinfo
  is '人员信息';
comment on column TAPAGEREVIEW.storetype
  is '存储方式';
comment on column TAPAGEREVIEW.doc_id
  is 'ucm存储标志';
-- Create/Recreate primary, unique and foreign key constraints
alter table TAPAGEREVIEW
  add constraint 主键 primary key (PAGE_ID)
  using index;
-- 4.0.1-SNAPSHOT版本需要执行的sql 2017年9月1日10:18:38
ALTER TABLE TALIMITRATE ADD maxcount NUMBER(10);
comment on column TALIMITRATE.maxcount
  is '允许访问的最大并发量';
ALTER TABLE taposition ADD  isdeveloper varchar2(1);
UPDATE  taposition set isdeveloper = '1' where positionid=1;
UPDATE  taposition set POSITIONTYPE='2' where positionid=1;
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE)
VALUES (280, 2, '超级管理员配置', 'org/developer/developerMg.do', '1/2/280', '银海软件/权限管理/字段授权', 'tree-folder-new', '', '', '', '1', '1', '1', '', 6, '01', 3, '1', '1', '', 'sysmg', '1', null, '0', '0');
COMMIT ;
-- 4.0.1-SNAPSHOT版本需要执行的sql 2017年9月25日 修改日志记录表 
-- Add/modify columns 
alter table TASERVEREXCEPTIONLOG modify id VARCHAR2(50);
alter table TASERVEREXCEPTIONLOG add port varchar2(10);
-- Add comments to the columns 
comment on column TASERVEREXCEPTIONLOG.port
  is '服务器端口';
-- 4.1.0-RELEASE版本需要执行的sql 2017年11月21日
-- 日记输出级别 新增码表数据
insert into AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('LOGLEVEL', '日志等级', '0', 'OFF', '9999', '0', 0);
insert into AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('LOGLEVEL', '日志等级', '1', 'FATAL', '9999', '0', 0);
insert into AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('LOGLEVEL', '日志等级', '2', 'ERROR', '9999', '0', 0);
insert into AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('LOGLEVEL', '日志等级', '3', 'WARN', '9999', '0', 0);
insert into AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('LOGLEVEL', '日志等级', '4', 'INFO', '9999', '0', 0);
insert into AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('LOGLEVEL', '日志等级', '5', 'DEBUG', '9999', '0', 0);
insert into AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('LOGLEVEL', '日志等级', '6', 'TRACE', '9999', '0', 0);
insert into AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
values ('LOGLEVEL', '日志等级', '7', 'ALL', '9999', '0', 0);
-- 4.1.0-RELEASE版本需要执行的sql 2017年11月21日
-- 日记输出级别 添加菜单数据
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE)
values (290,4,'日志输出级别','logLevel/logLevelController.do','1/4/232732','银海软件/系统管理/日志输出级别','icon-036?#00fafa','','','','1','1','1','',9,'01',3,'1','1','','sysmg','0',null,'0','0');
--菜单图标更新
UPDATE TAMENU SET ICONSKIN = 'icon-073?#3413ed' WHERE MENUID = '1';
UPDATE TAMENU SET ICONSKIN = 'icon-004?#87bfe8' WHERE MENUID = '2';
UPDATE TAMENU SET ICONSKIN = 'icon-001?#6A6A6A' WHERE MENUID = '3';
UPDATE TAMENU SET ICONSKIN = 'icon-005?#E1785A' WHERE MENUID = '4';
UPDATE TAMENU SET ICONSKIN = 'icon-010?#E7B346' WHERE MENUID = '5';
UPDATE TAMENU SET ICONSKIN = 'icon-029?#88BBF1' WHERE MENUID = '6';
UPDATE TAMENU SET ICONSKIN = 'icon-011?#33475F' WHERE MENUID = '7';
UPDATE TAMENU SET ICONSKIN = 'icon-023?#B59BC9' WHERE MENUID = '8';
UPDATE TAMENU SET ICONSKIN = 'icon-014?#936BB2' WHERE MENUID = '9';
UPDATE TAMENU SET ICONSKIN = 'icon-057?#56ABE4' WHERE MENUID = '10';
UPDATE TAMENU SET ICONSKIN = 'icon-065?#E5937C' WHERE MENUID = '11';
UPDATE TAMENU SET ICONSKIN = 'icon-026?#B4D984' WHERE MENUID = '12';
UPDATE TAMENU SET ICONSKIN = 'icon-049?#56ABE4' WHERE MENUID = '13';
UPDATE TAMENU SET ICONSKIN = 'icon-035?#97CC52' WHERE MENUID = '14';
UPDATE TAMENU SET ICONSKIN = 'icon-060?#00BB9C' WHERE MENUID = '15';
UPDATE TAMENU SET ICONSKIN = 'icon-053?#deaf49' WHERE MENUID = '16';
UPDATE TAMENU SET ICONSKIN = 'icon-028?#B196C7' WHERE MENUID = '17';
UPDATE TAMENU SET ICONSKIN = 'icon-067?#E1785A' WHERE MENUID = '18';
UPDATE TAMENU SET ICONSKIN = 'icon-065?#deaf49' WHERE MENUID = '19';
UPDATE TAMENU SET ICONSKIN = 'icon-037?#deaf49' WHERE MENUID = '20';
UPDATE TAMENU SET ICONSKIN = 'icon-006?#E1785A' WHERE MENUID = '21';
UPDATE TAMENU SET ICONSKIN = 'icon-005?#E1785A' WHERE MENUID = '22';
UPDATE TAMENU SET ICONSKIN = 'icon-015?#97CC52' WHERE MENUID = '23';
UPDATE TAMENU SET ICONSKIN = 'icon-045?#33475F' WHERE MENUID = '24';
UPDATE TAMENU SET ICONSKIN = 'icon-048?#42dbd4' WHERE MENUID = '25';
UPDATE TAMENU SET ICONSKIN = 'icon-008?#a397e8' WHERE MENUID = '26';
UPDATE TAMENU SET ICONSKIN = 'icon-001?#6A6A6A' WHERE MENUID = '27';
UPDATE TAMENU SET ICONSKIN = 'icon-027?#00BB9C' WHERE MENUID = '28';
UPDATE TAMENU SET ICONSKIN = 'icon-017?#cf829d' WHERE MENUID = '29';
UPDATE TAMENU SET ICONSKIN = 'icon-071?#e37de3' WHERE MENUID = '30';
UPDATE TAMENU SET ICONSKIN = 'icon-050?#27d971' WHERE MENUID = '31';
UPDATE TAMENU SET ICONSKIN = 'icon-039?#db4b89' WHERE MENUID = '32';
UPDATE TAMENU SET ICONSKIN = 'icon-037?#8ABCF1' WHERE MENUID = '33';
UPDATE TAMENU SET ICONSKIN = 'icon-048?#97CC52' WHERE MENUID = '34';
UPDATE TAMENU SET ICONSKIN = 'icon-051?#4D69B5' WHERE MENUID = '35';
UPDATE TAMENU SET ICONSKIN = 'icon-009?#AFD67C' WHERE MENUID = '36';
UPDATE TAMENU SET ICONSKIN = 'icon-032?#E1785A' WHERE MENUID = '38';
UPDATE TAMENU SET ICONSKIN = 'icon-052?#86a9eb' WHERE MENUID = '100';
UPDATE TAMENU SET ICONSKIN = 'icon-024?#94C1F1' WHERE MENUID = '101';
UPDATE TAMENU SET ICONSKIN = 'icon-022?#e82774' WHERE MENUID = '280';
--4.1.0-RELEASE版本需要执行的sql 2018年3月21日
--集群server配置表变更
--删除原来SERVERADDRESS表
DROP TABLE SERVERADDRESS CASCADE CONSTRAINTS;
--创建新的集群配置表
CREATE TABLE CLUSTERCONFIG(
  CLUSTERID NUMBER(10,0) NOT NULL ,
  CLUSTERNAME VARCHAR2(200),
  CLUSTERAPP VARCHAR2(200),
  CLUSTERURL VARCHAR2(200) NOT NULL,
  CLUSTERDESC VARCHAR2(200)
);
COMMENT ON TABLE CLUSTERCONFIG IS '集群配置表';
COMMENT ON COLUMN CLUSTERCONFIG.CLUSTERID  IS '集群配置id';
COMMENT ON COLUMN CLUSTERCONFIG.CLUSTERNAME  IS '集群名称';
COMMENT ON COLUMN CLUSTERCONFIG.CLUSTERAPP  IS '应用名称';
COMMENT ON COLUMN CLUSTERCONFIG.CLUSTERURL  IS '服务URL';
COMMENT ON COLUMN CLUSTERCONFIG.CLUSTERDESC  IS '描述';
ALTER TABLE CLUSTERCONFIG  ADD CONSTRAINT PK_CLUSTERCONFIG PRIMARY KEY (CLUSTERID);
--新增集群配置功能菜单
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (37, 4, '集群server配置管理', 'clusterConfig/clusterConfigController.do', '1/4/37', '银海软件/开发管理/集群server配置管理', 'icon-049?#56ABE4', NULL, NULL, NULL, '1', '1', '1', NULL, 10, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
COMMIT;
/****Add By MinusZero In 2018/08/16****/
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (291, 2, '组织正副职管理', 'org/positionMgAction.do', '1/2/291', '银海软件/权限管理/组织正副职管理', 'icon-011?#09dceb', NULL, NULL, '', '1', '1', '1', NULL, 8, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', '', '1');

UPDATE TAMENU SET SECURITYPOLICY='2' WHERE (MENUID=21);
UPDATE TAMENU SET SECURITYPOLICY='2' WHERE (MENUID=22);
UPDATE TAMENU SET SECURITYPOLICY='2' WHERE (MENUID=23);
UPDATE TAMENU SET SECURITYPOLICY='2' WHERE (MENUID=24);
UPDATE TAMENU SET SECURITYPOLICY='2' WHERE (MENUID=25);
UPDATE TAMENU SET SECURITYPOLICY='2' WHERE (MENUID=26);
COMMIT;
/*End By MinusZero In 2018/08/16*/


/**Add By MinusZero In 2018/10/18 start ***/


-- Add comments to the table
comment on table TAONLINESTATLOG
  is '时点在线情况统计表';

-- Create table (创建登录日志统计表)
create table TALOGINSTATLOG
(
  statdate    varchar2(20) not null,
  pointintime varchar2(20) not null,
  loginnum    number(15) not null
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table
comment on table TALOGINSTATLOG
  is '时点登录人数分析';
-- Add comments to the columns
comment on column TALOGINSTATLOG.statdate
  is '统计日期';
comment on column TALOGINSTATLOG.pointintime
  is '统计时间点';
comment on column TALOGINSTATLOG.loginnum
  is '登录人数';
-- Create/Recreate primary, unique and foreign key constraints （创建在线人数统计任务）
alter table TALOGINSTATLOG
  add constraint taloginstatlog primary key (STATDATE, POINTINTIME);

begin
    DBMS_SCHEDULER.CREATE_JOB (
    job_name => 'TA_ONLINE_STAT_LOG',
    job_type => 'PLSQL_BLOCK',
    job_action => 'INSERT INTO TAONLINESTATLOG (
  SELECT
    TO_CHAR (SYSDATE, ''yyyy-MM-dd'') AS STATDATE,
    TO_CHAR (SYSDATE, ''HH24:mi'') AS POINTINTIME,
    COUNT (*) AS LOGINNUM
  FROM
    TAONLINELOG
);',
    start_date         =>  to_date('2018-09-26 12:00:00', 'yyyy-MM-dd HH24:mi:ss'),
    Repeat_Interval    =>  'FREQ=MINUTELY; INTERVAL=1',
    Enabled => True,
    Comments => '在线时点人数统计任务调度'
    );
End;
/
-- 创建登录人数统计任务

begin
    DBMS_SCHEDULER.CREATE_JOB (
    job_name => 'TA_LOGIN_STAT_LOG',
    job_type => 'PLSQL_BLOCK',
    job_action => 'INSERT INTO TALOGINSTATLOG (SELECT
  TO_CHAR (LOGINTIME, ''yyyy-MM-dd'') AS STATDATE,
  TO_CHAR (LOGINTIME, ''HH24:mi'') AS POINTINTIME,
  COUNT (*) AS LOGINNUM
FROM
  TAONLINELOG
WHERE
  (
    TO_CHAR (
      LOGINTIME,
     ''yyyy-MM-dd HH24:mi''
    ) = TO_CHAR (
          SYSDATE,
          ''yyyy-MM-dd HH24:mi''
        )
  )
AND LOGINTIME IS NOT NULL
GROUP BY
  LOGINTIME);',
    start_date         =>  to_date('2018-09-25 12:00:00', 'yyyy-MM-dd HH24:mi:ss'),
    Repeat_Interval    =>  'FREQ=MINUTELY; INTERVAL=1',
    Enabled => True,
    Comments => '登录时点人数统计任务'
    );
End;
/
DROP TABLE TAZKADDRESSMG CASCADE CONSTRAINTS;
create table TAZKADDRESSMG
(
  zkid            VARCHAR2(20) not NULL,
  zkaddress       VARCHAR2(200),
  appname         VARCHAR2(60),
  appnamespace    VARCHAR2(30)
);
comment on column TAZKADDRESSMG.zkid
  is 'zk注册中心id';
comment on column TAZKADDRESSMG.zkaddress
  is 'zk地址';
comment on column TAZKADDRESSMG.appname
  is '应用名称';
comment on column TAZKADDRESSMG.appnamespace
  is 'zk命名空间';
DROP TABLE JOB_BUSY_FREE_MG CASCADE CONSTRAINTS;
create table JOB_BUSY_FREE_MG
(
  jobstatus     VARCHAR(2),
  zkid          VARCHAR(20) not null,
  cron          VARCHAR(20),
  jobtype       VARCHAR(2),
  jobname       VARCHAR(20),
  ips           VARCHAR(20),
  ebfid         VARCHAR(20) not null,
  CONSTRAINT PK_JOB_BUSY_FREE_MG PRIMARY KEY (ebfid)
);
comment on table JOB_BUSY_FREE_MG
is '闲忙任务管理';
comment on column JOB_BUSY_FREE_MG.JOBSTATUS
is '任务状态';
comment on column JOB_BUSY_FREE_MG.ZKID
is '注册中心zkid';
comment on column JOB_BUSY_FREE_MG.CRON
is '闲忙时间点';
comment on column JOB_BUSY_FREE_MG.JOBTYPE
is '任务类型';
comment on column JOB_BUSY_FREE_MG.JOBNAME
is '任务名称';
comment on column JOB_BUSY_FREE_MG.IPS
is 'ip地址';
comment on column JOB_BUSY_FREE_MG.EBFID
is '闲忙任务id';
/**************end In 2018/10/18****************/
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (292, 4, 'sql热修改', 'modifySqlStatement/modifySqlStatementController.do', '1/4/292', '银海软件/系统管理/sql热修改', 'icon-011?#09dceb', NULL, NULL, '', '1', '1', '1', NULL, 8, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', '', '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (293, 3, 'elasticJob管理', null, '1/3/293', '银海软件/开发管理/elasticJob管理', 'icon-001?#cfaf11', null, null, null, '1', '1', '1', null, 7, '01', 3, '0', '1', null, 'sysmg', '0', null, '0', '0', null, '0');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (294, 293, 'zk管理', 'elasticJob/zookeeperCenterManagerController.do', '1/3/293/294', '银海软件/开发管理/elasticJob管理/zk管理', 'icon-005?', null, null, null, '1', '1', '1', null, 1, '01', 4, '1', '1', null, 'sysmg', '0', null, '0', '0', null, '0');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (295, 293, '闲忙job管理', 'elasticJob/freeBusyHourManagerController.do', '1/3/293/295', '银海软件/开发管理/elasticJob管理/闲忙job管理', 'icon-060?', null, null, null, '1', '1', '1', null, 2, '01', 4, '1', '1', null, 'sysmg', '0', null, '0', '0', null, '0');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (296, 293, 'job管理', 'elasticJob/elasticJobManagerConsoleController.do', '1/3/293/296', '银海软件/开发管理/elasticJob管理/job管理', 'icon-008?', null, null, null, '1', '1', '1', null, 0, '01', 4, '1', '1', null, 'sysmg', '0', null, '0', '0', null, '0');
commit ;