ALTER TABLE TAORGMG
   DROP CONSTRAINT FK_REFERENCE_8;
ALTER TABLE TAORGMG
   DROP CONSTRAINT FK_REFERENCE_9;
ALTER TABLE TAORG
   DROP CONSTRAINT FK_REFERENCE_6;
ALTER TABLE TAPOSITION
   DROP CONSTRAINT FK_RELATIONSHIP_5;
ALTER TABLE TAPOSITIONAUTHRITY
   DROP CONSTRAINT FK_REFERENCE_7;
ALTER TABLE TAPOSITIONAUTHRITY
   DROP CONSTRAINT FK_RELATIONSHIP_11;
ALTER TABLE TAUSERPOSITION
   DROP CONSTRAINT FK_RELATIONSHIP_10;
ALTER TABLE TAUSERPOSITION
   DROP CONSTRAINT FK_RELATIONSHIP_9;
   
DROP view AA10A1;

DROP TABLE  AA10 CASCADE CONSTRAINTS;

DROP TABLE  TACONFIG CASCADE CONSTRAINTS;

DROP TABLE  TACONFIGSYSPATH CASCADE CONSTRAINTS;

DROP TABLE TAORGMG CASCADE CONSTRAINTS;

DROP TABLE TASHAREPOSITION CASCADE CONSTRAINTS;

DROP TABLE TAMANAGERMG CASCADE CONSTRAINTS;

DROP TABLE TAMENU CASCADE CONSTRAINTS;

DROP TABLE TAORG CASCADE CONSTRAINTS;

DROP TABLE TAORGOPLOG CASCADE CONSTRAINTS;

DROP TABLE TAPOSITION CASCADE CONSTRAINTS;

DROP TABLE TAPOSITIONAUTHRITY CASCADE CONSTRAINTS;

DROP TABLE TAUSER CASCADE CONSTRAINTS;

DROP TABLE TAUSERPOSITION CASCADE CONSTRAINTS;

DROP TABLE TAACCESSLOG CASCADE CONSTRAINTS;

DROP TABLE TAMENUPOSITIONYAB003 CASCADE CONSTRAINTS;

DROP TABLE TACOMMONMENU CASCADE CONSTRAINTS;

DROP TABLE TAYAB003SCOPE CASCADE CONSTRAINTS;

DROP TABLE CLUSTERCONFIG CASCADE CONSTRAINTS;

DROP TABLE TACONSOLEMODULE CASCADE CONSTRAINTS;

DROP TABLE TACONSOLEMODULELOCATION CASCADE CONSTRAINTS;

DROP TABLE TACONSOLEMODULEPRIVILEGE CASCADE CONSTRAINTS;

DROP TABLE TAONLINELOG CASCADE CONSTRAINTS;

DROP TABLE TALOGINHISTORYLOG CASCADE CONSTRAINTS;

DROP SEQUENCE HIBERNATE_SEQUENCE;

DROP SEQUENCE SEQ_DEFAULT;

DROP TABLE TASERVEREXCEPTIONLOG CASCADE CONSTRAINTS;

DROP TABLE SIGNRECORD CASCADE CONSTRAINTS;
DROP TABLE TEMPLATEMANAGER CASCADE CONSTRAINTS;
DROP TABLE TAMESSAGESTATE CASCADE CONSTRAINTS;
DROP TABLE TAFIELD CASCADE CONSTRAINTS;
DROP TABLE TAFIELDAUTHRITY CASCADE CONSTRAINTS;
DROP TABLE TAMESSAGE CASCADE CONSTRAINTS;
DROP TABLE TAMESSAGEATTACHMENT CASCADE CONSTRAINTS;
DROP TABLE TAZKADDRESSMG CASCADE CONSTRAINTS;
DROP TABLE JOB_BUSY_FREE_MG CASCADE CONSTRAINTS;
DROP SEQUENCE TEMPLATE_SEQUENCE;
DROP SEQUENCE SEQ_MESSAGE;

create sequence HIBERNATE_SEQUENCE
minvalue 100000
maxvalue 1000000000
start with 110000
increment by 1;

create sequence SEQ_DEFAULT
minvalue 100000
maxvalue 1000000000
start with 110000
increment by 1;


/*==============================================================*/
/* Table: TASERVEREXCEPTIONLOG                                                */
/*==============================================================*/
create table TASERVEREXCEPTIONLOG
(
  id            VARCHAR2(50) not NULL,
  ipaddress     VARCHAR2(255),
  port          VARCHAR2(10),
  type          VARCHAR2(255),
  content       BLOB,
  time          DATE,
  syspath       VARCHAR2(50),
  clientip      VARCHAR2(50),
  url           VARCHAR2(100),
  menuid        VARCHAR2(8),
  menuname      VARCHAR2(30),
  useragent     VARCHAR2(200),
  exceptiontype VARCHAR2(2),
  parameter     BLOB
);
-- Add comments to the table 
comment on table TASERVEREXCEPTIONLOG
  is '系统异常日志表';
-- Add comments to the columns 
comment on column TASERVEREXCEPTIONLOG.id
  is 'id';
comment on column TASERVEREXCEPTIONLOG.ipaddress
  is '服务器ip地址';
comment on column TASERVEREXCEPTIONLOG.port
  is '服务器端口';
comment on column TASERVEREXCEPTIONLOG.type
  is '异常类型';
comment on column TASERVEREXCEPTIONLOG.content
  is '异常内容';
comment on column TASERVEREXCEPTIONLOG.time
  is '报错时间';
comment on column TASERVEREXCEPTIONLOG.syspath
  is '系统标识';
comment on column TASERVEREXCEPTIONLOG.clientip
  is '客户端ip';
comment on column TASERVEREXCEPTIONLOG.url
  is '访问功能url';
comment on column TASERVEREXCEPTIONLOG.menuid
  is '菜单id';
comment on column TASERVEREXCEPTIONLOG.menuname
  is '菜单名称';
comment on column TASERVEREXCEPTIONLOG.useragent
  is '客户端环境';
comment on column TASERVEREXCEPTIONLOG.exceptiontype
  is '异常分类(1 系统异常  2业务异常)';
comment on column TASERVEREXCEPTIONLOG.parameter
  is '传入参数';


/*==============================================================*/
/* Table: AA10                                                  */
/*==============================================================*/
CREATE TABLE AA10
(
   AAA100               VARCHAR2(20) NOT NULL ,
   AAA101               VARCHAR2(50) NOT NULL ,
   AAA102               VARCHAR2(6) NOT NULL ,
   AAA103               VARCHAR2(50) NOT NULL ,
   YAB003               VARCHAR2(6) NOT NULL ,
   AAE120               VARCHAR2(6) NOT NULL ,
   VER                  NUMBER(10,0) ,
   constraint PK_AA10 primary key (AAA100, AAA102)
);

COMMENT ON TABLE AA10 IS 'AA10代码表';
COMMENT ON COLUMN AA10.AAA100 IS 'AAA100代码类别';
COMMENT ON COLUMN AA10.AAA101 IS 'AAA102代码值';
COMMENT ON COLUMN AA10.AAA102 IS 'AAA102代码值';
COMMENT ON COLUMN AA10.AAA103 IS 'AAA103代码名称';
COMMENT ON COLUMN AA10.YAB003 IS 'YAB003经办机构';
COMMENT ON COLUMN AA10.AAE120 IS 'AAE120注销标志';

/*==============================================================*/
/* Table: TACONFIG                                                  */
/*==============================================================*/
CREATE TABLE TACONFIG (
  CONFIGID NUMBER(10,0) NOT NULL,
  CONFIGNAME VARCHAR2(100) NOT NULL,
  CONFIGVALUE VARCHAR2(1024) ,
  CONFIGTYPE VARCHAR2(1024) ,
  CONFIGFLAG VARCHAR2(20) NOT NULL,
  CONFIGDESC VARCHAR2(200),
  CONSTRAINT PK_TACONFIG PRIMARY KEY (CONFIGID)
);
COMMENT ON TABLE TACONFIG IS '系统配置表';
COMMENT ON COLUMN TACONFIG.CONFIGID IS '配置项ID';
COMMENT ON COLUMN TACONFIG.CONFIGNAME IS '配置项名称';
COMMENT ON COLUMN TACONFIG.CONFIGVALUE IS '配置项内容';
COMMENT ON COLUMN TACONFIG.CONFIGTYPE IS '是否系统参数';
COMMENT ON COLUMN TACONFIG.CONFIGFLAG IS '系统标识';
COMMENT ON COLUMN TACONFIG.CONFIGDESC IS '参数说明';

/*==============================================================*/
/* Table: TACONFIGSYSPATH                                                */
/*==============================================================*/
CREATE TABLE TACONFIGSYSPATH (
  SERIALID NUMBER(10,0) NOT NULL,
  ID VARCHAR2(20) NOT NULL,
  NAME VARCHAR2(50) NOT NULL,
  URL VARCHAR2(100) NOT NULL,
  PY VARCHAR2(20) ,
  CURSYSTEM VARCHAR2(1) NOT NULL,
  PRIMARY KEY (SERIALID)
);
COMMENT ON TABLE TACONFIGSYSPATH IS '系统路径配置表';
COMMENT ON COLUMN TACONFIGSYSPATH.SERIALID IS '流水号';
COMMENT ON COLUMN TACONFIGSYSPATH.ID IS '系统ID';
COMMENT ON COLUMN TACONFIGSYSPATH.NAME IS '系统名称';
COMMENT ON COLUMN TACONFIGSYSPATH.URL IS '系统路径前缀';
COMMENT ON COLUMN TACONFIGSYSPATH.PY IS '拼音';
COMMENT ON COLUMN TACONFIGSYSPATH.CURSYSTEM IS '是否为当前系统';

/*==============================================================*/
/* Table: TAMENU                                                */
/*==============================================================*/
create table TAMENU
(
  menuid          NUMBER(10) not NULL,
  pmenuid         NUMBER(10) not NULL,
  menuname        VARCHAR2(60),
  url             VARCHAR2(100),
  menuidpath      VARCHAR2(1024),
  menunamepath    VARCHAR2(1024),
  iconskin        VARCHAR2(200),
  selectimage     VARCHAR2(200),
  reportid        VARCHAR2(50),
  accesstimeel    VARCHAR2(200),
  effective       CHAR(1) not NULL,
  securitypolicy  CHAR(1) not NULL,
  isdismultipos   CHAR(1) not NULL,
  quickcode       VARCHAR2(20),
  sortno          NUMBER,
  resourcetype    CHAR(2) not NULL,
  menulevel       NUMBER,
  isleaf          CHAR(1),
  menutype        CHAR(1),
  iscache         CHAR(1),
  syspath         VARCHAR2(20),
  useyab003       CHAR(1),
  typeflag        NUMBER(10),
  isaudite        VARCHAR2(1),
  consolemodule   CHAR(1) default 1,
  customencoding  VARCHAR2(20),
  isfiledscontrol CHAR(1) default 1,
  constraint PK_TAMENU primary key (MENUID)
);
-- Add comments to the table 
comment on table TAMENU
  is '功能菜单';
-- Add comments to the columns 
comment on column TAMENU.menuid
  is '菜单Id';
comment on column TAMENU.pmenuid
  is '父菜单id';
comment on column TAMENU.menuname
  is '功能地址';
comment on column TAMENU.url
  is '功能名称';
comment on column TAMENU.menuidpath
  is '菜单id路径';
comment on column TAMENU.menunamepath
  is '菜单名称路径';
comment on column TAMENU.iconskin
  is '选择前图片';
comment on column TAMENU.selectimage
  is '选择后图片';
comment on column TAMENU.reportid
  is '查询报表的报表id';
comment on column TAMENU.accesstimeel
  is '访问限制时间表达式';
comment on column TAMENU.effective
  is '有效标志';
comment on column TAMENU.securitypolicy
  is '安全策略';
comment on column TAMENU.isdismultipos
  is '是否显示多岗';
comment on column TAMENU.quickcode
  is '快捷访问码';
comment on column TAMENU.sortno
  is '排序号';
comment on column TAMENU.resourcetype
  is '权限类型（功能权限\按钮权限\表单只读\表单可编辑\表格列只读\表格列可编辑）';
comment on column TAMENU.menulevel
  is '菜单层级';
comment on column TAMENU.isleaf
  is '是否叶子节点';
comment on column TAMENU.menutype
  is '菜单类型';
comment on column TAMENU.iscache
  is '是否缓存';
comment on column TAMENU.syspath
  is '系统路径';
comment on column TAMENU.useyab003
  is '是否启用分中心数据权限';
comment on column TAMENU.typeflag
  is '类标识';
comment on column TAMENU.isaudite
  is '是否需要审核';
comment on column TAMENU.consolemodule
  is '是否为工作台模块';
comment on column TAMENU.customencoding
  is '自定义编码';
comment on column TAMENU.isfiledscontrol
  is '是否需要字段权限控制';

/*==============================================================*/
/* Table: TAORG                                                 */
/*==============================================================*/
CREATE TABLE TAORG
(
   ORGID                NUMBER(10,0) NOT NULL ,
   PORGID               NUMBER(10,0),
   ORGNAME              VARCHAR2(60) ,
   COSTOMNO             VARCHAR2(10) ,
   ORGIDPATH            VARCHAR2(1024) ,
   ORGNAMEPATH          VARCHAR2(1024) ,
   COSTOMNOPATH         VARCHAR2(1024) ,
   ORGTYPE              CHAR(2) ,
   SORT                 NUMBER,
   YAB003               VARCHAR2(6) ,
   DIMENSION            CHAR(2) ,
   CREATEUSER           NUMBER(10) NOT NULL ,
   CREATETIME           DATE NOT NULL ,
   EFFECTIVE            CHAR(1) NOT NULL,
   ORGLEVEL             NUMBER(10),
   ISLEAF               CHAR(1), 
   ORGMANAGER           NUMBER(10) ,
   DESTORY             	CHAR(1),
   TYPEFLAG             NUMBER(10),
   YAB139 		VARCHAR2(6),
   constraint PK_TAORG primary key (ORGID)
);
COMMENT ON TABLE TAORG IS
'组织视图';
COMMENT ON COLUMN TAORG.ORGID IS
'组织id';
COMMENT ON COLUMN TAORG.PORGID IS
'组织父id';
COMMENT ON COLUMN TAORG.ORGNAME IS
'组织名称';
COMMENT ON COLUMN TAORG.COSTOMNO IS
'自定义编码';
COMMENT ON COLUMN TAORG.ORGIDPATH IS
'组织id路径';
COMMENT ON COLUMN TAORG.ORGNAMEPATH IS
'组织名称路径';
COMMENT ON COLUMN TAORG.COSTOMNOPATH IS
'自定义编码路径';
COMMENT ON COLUMN TAORG.ORGTYPE IS
'组织类型（机构（集团、子公司）、部门、组）';
COMMENT ON COLUMN TAORG.SORT IS
'排序号';
COMMENT ON COLUMN TAORG.YAB003 IS
'经办机构';
COMMENT ON COLUMN TAORG.DIMENSION IS
'视图维度';
COMMENT ON COLUMN TAORG.CREATEUSER IS
'创建人';
COMMENT ON COLUMN TAORG.CREATETIME IS
'创建时间';
COMMENT ON COLUMN TAORG.EFFECTIVE IS
'有效性';
COMMENT ON COLUMN TAORG.ORGLEVEL IS
'组织层级';
COMMENT ON COLUMN TAORG.ISLEAF IS
'是否叶子节点';
COMMENT ON COLUMN TAORG.ORGMANAGER IS
'组织负责人（正职）';
COMMENT ON COLUMN TAORG.DESTORY IS
'是否销毁';
COMMENT ON COLUMN TAORG.TYPEFLAG IS
'类标识';
COMMENT ON COLUMN TAORG.YAB139 IS '数据区';

/*==============================================================*/
/* Table: TAORGOPLOG                                            */
/*==============================================================*/
CREATE TABLE TAORGOPLOG
(
   LOGID                NUMBER(10,0) NOT NULL ,
   BATCHNO              VARCHAR2(10) ,
   OPTYPE               CHAR(2) ,
   INFLUENCEBODYTYPE    CHAR(2) ,
   INFLUENCEBODY        VARCHAR2(10) ,
   OPBODY               CHAR(2) ,
   OPSUBJEKT            VARCHAR2(10),
   CHANGCONTENT         VARCHAR2(2048) ,
   OPTIME               DATE NOT NULL ,
   OPUSER               VARCHAR2(10) NOT NULL ,
   OPPOSITION           VARCHAR2(10) NOT NULL ,
   ISPERMISSION 	VARCHAR2(1),
   constraint PK_TAORGOPLOG primary key (LOGID)
);
COMMENT ON TABLE TAORGOPLOG IS
'组织及权限操作日志';
COMMENT ON COLUMN TAORGOPLOG.LOGID IS
'日志id';
COMMENT ON COLUMN TAORGOPLOG.BATCHNO IS
'操作批次号';
COMMENT ON COLUMN TAORGOPLOG.OPTYPE IS
'操作类型（新增、编辑、删除、授权、回收权限）';
COMMENT ON COLUMN TAORGOPLOG.INFLUENCEBODYTYPE IS
'影响主体类型（组织、人员、岗位、角色、权限资源）';
COMMENT ON COLUMN TAORGOPLOG.INFLUENCEBODY IS
'影响主体';
COMMENT ON COLUMN TAORGOPLOG.OPBODY IS
'操作主体类型（人员、组织、岗位、角色、权限资源）';
COMMENT ON COLUMN TAORGOPLOG.OPSUBJEKT IS
'操作主体';
COMMENT ON COLUMN TAORGOPLOG.CHANGCONTENT IS
'主体变更内容';
COMMENT ON COLUMN TAORGOPLOG.OPTIME IS
'经办时间';
COMMENT ON COLUMN TAORGOPLOG.OPUSER IS
'经办人';
COMMENT ON COLUMN TAORGOPLOG.OPPOSITION IS
'经办岗位';
COMMENT ON COLUMN TAORGOPLOG.ISPERMISSION IS '是否有权限';

/*==============================================================*/
/* Table: TAPOSITION                                            */
/*==============================================================*/
CREATE TABLE TAPOSITION
(
   POSITIONID           NUMBER(10,0) NOT NULL,
   ORGID                NUMBER(10,0) NOT NULL,
   POSITIONNAME         VARCHAR2(60) NOT NULL ,
   POSITIONTYPE         CHAR(1) NOT NULL ,
   CREATEPOSITIONID     NUMBER(10,0) NOT NULL,
   ORGIDPATH            VARCHAR2(1024),
   ORGNAMEPATH          VARCHAR2(1024),
   VALIDTIME            DATE ,
   CREATEUSER           NUMBER(10,0) NOT NULL ,
   CREATETIME           DATE NOT NULL,
   EFFECTIVE            CHAR(1) NOT NULL,
   ISADMIN              CHAR(1), 
   ISSHARE              CHAR(1),
   ISCOPY               CHAR(1),
   TYPEFLAG             NUMBER(10),
   POSITIONCATEGORY 	VARCHAR2(2),
   ISDEVELOPER 	VARCHAR2(1),
   constraint PK_TAPOSITION primary key (POSITIONID)
);
COMMENT ON TABLE TAPOSITION IS
'岗位';
COMMENT ON COLUMN TAPOSITION.POSITIONID IS
'岗位id';
COMMENT ON COLUMN TAPOSITION.ORGID IS
'组织id';
COMMENT ON COLUMN TAPOSITION.POSITIONNAME IS
'岗位名称';
COMMENT ON COLUMN TAPOSITION.POSITIONTYPE IS
'岗位类型（个人专属岗位/公有岗位）';
COMMENT ON COLUMN TAPOSITION.CREATEPOSITIONID IS
'创建人使用的岗位';
COMMENT ON COLUMN TAPOSITION.ORGIDPATH IS
'所在组织id路径';
COMMENT ON COLUMN TAPOSITION.ORGNAMEPATH IS
'所在组织名称路径';
COMMENT ON COLUMN TAPOSITION.VALIDTIME IS
'只针对委派生成的岗位';
COMMENT ON COLUMN TAPOSITION.CREATEUSER IS
'创建人';
COMMENT ON COLUMN TAPOSITION.CREATETIME IS
'创建时间';
COMMENT ON COLUMN TAPOSITION.EFFECTIVE IS
'有效标志';
COMMENT ON COLUMN TAPOSITION.ISADMIN IS
'是否管理员';
COMMENT ON COLUMN TAPOSITION.ISSHARE IS
'是否为共享岗位';
COMMENT ON COLUMN TAPOSITION.ISCOPY IS
'是否为复制岗位';
COMMENT ON COLUMN TAPOSITION.TYPEFLAG IS
'类标识';
COMMENT ON COLUMN TAPOSITION.POSITIONCATEGORY IS '岗位类别';
COMMENT ON COLUMN TAPOSITION.ISDEVELOPER IS '是否是超级管理员';

/*==============================================================*/
/* Table: TAPOSITIONAUTHRITY                                    */
/*==============================================================*/
CREATE TABLE TAPOSITIONAUTHRITY
(
   POSITIONID           NUMBER(10,0) NOT NULL ,
   MENUID               NUMBER(10,0) NOT NULL ,
   USEPERMISSION        CHAR(1),
   REPERMISSION         CHAR(1) ,
   REAUTHRITY           CHAR(1),
   CREATEUSER           NUMBER(10,0) NOT NULL ,
   CREATETIME           DATE NOT NULL ,
   EFFECTTIME           DATE , 
   AUDITEACCESSDATE 	DATE,
   AUDITEUSER 		NUMBER(10),
   AUDITSTATE 		VARCHAR2(1) DEFAULT 0,
   constraint PK_TAPOSITIONAUTHRITY primary key (POSITIONID, MENUID)
);
COMMENT ON TABLE TAPOSITIONAUTHRITY IS
'岗位权限表';
COMMENT ON COLUMN TAPOSITIONAUTHRITY.POSITIONID IS
'岗位id';
COMMENT ON COLUMN TAPOSITIONAUTHRITY.MENUID IS
'菜单Id';
COMMENT ON COLUMN TAPOSITIONAUTHRITY.USEPERMISSION IS
'使用权限';
COMMENT ON COLUMN TAPOSITIONAUTHRITY.REPERMISSION IS
'授权别人使用权限';
COMMENT ON COLUMN TAPOSITIONAUTHRITY.REAUTHRITY IS
'授权别人授权权限';
COMMENT ON COLUMN TAPOSITIONAUTHRITY.CREATEUSER IS
'创建人';
COMMENT ON COLUMN TAPOSITIONAUTHRITY.CREATETIME IS
'创建时间';
COMMENT ON COLUMN TAPOSITIONAUTHRITY.EFFECTTIME IS
'有效时间';
COMMENT ON COLUMN TAPOSITIONAUTHRITY.AUDITEACCESSDATE IS '审核通过时间';
COMMENT ON COLUMN TAPOSITIONAUTHRITY.AUDITEUSER IS '审核人';
COMMENT ON COLUMN TAPOSITIONAUTHRITY.AUDITSTATE IS '审核状态';

/*==============================================================*/
/* Table: TAUSER                                                */
/*==============================================================*/
CREATE TABLE TAUSER
(
   USERID               NUMBER(10,0) NOT NULL ,
   NAME                 VARCHAR2(60) NOT NULL ,
   SEX                  CHAR(1) ,
   LOGINID              VARCHAR2(20) NOT NULL ,
   PASSWORD             VARCHAR2(50) NOT NULL ,
   PASSWORDFAULTNUM     NUMBER ,
   PWDLASTMODIFYDATE    DATE ,
   ISLOCK               CHAR(1) ,
   SORT                 NUMBER ,
   EFFECTIVE            CHAR(1) NOT NULL ,
   TEL                  VARCHAR2(15) ,
   AUTHMODE             VARCHAR2(2) ,
   CREATEUSER           NUMBER(10,0) ,
   CREATETIME           DATE NOT NULL ,
   DIRECTORGID        NUMBER(10,0) NOT NULL,
   DESTORY				CHAR(1),
   TYPEFLAG        NUMBER(10,0) ,
   constraint PK_TAUSER primary key (USERID)
);
COMMENT ON TABLE TAUSER IS
'人员';
COMMENT ON COLUMN TAUSER.USERID IS
'人员id';
COMMENT ON COLUMN TAUSER.NAME IS
'姓名';
COMMENT ON COLUMN TAUSER.SEX IS
'性别';
COMMENT ON COLUMN TAUSER.LOGINID IS
'登陆账号';
COMMENT ON COLUMN TAUSER.PASSWORD IS
'密码';
COMMENT ON COLUMN TAUSER.PASSWORDFAULTNUM IS
'口令错误次数';
COMMENT ON COLUMN TAUSER.PWDLASTMODIFYDATE IS
'口令最后修改时间';
COMMENT ON COLUMN TAUSER.ISLOCK IS
'锁定标志';
COMMENT ON COLUMN TAUSER.SORT IS
'排序号';
COMMENT ON COLUMN TAUSER.EFFECTIVE IS
'有效标志';
COMMENT ON COLUMN TAUSER.TEL IS
'联系电话';
COMMENT ON COLUMN TAUSER.AUTHMODE IS
'权限认证方式';
COMMENT ON COLUMN TAUSER.CREATEUSER IS
'创建人';
COMMENT ON COLUMN TAUSER.CREATETIME IS
'创建时间';
COMMENT ON COLUMN TAUSER.DIRECTORGID IS
'直属组织';
COMMENT ON COLUMN TAUSER.DESTORY IS
'是否销毁';
COMMENT ON COLUMN TAUSER.TYPEFLAG IS
'类标识';

/*==============================================================*/
/* Table: TAUSERPOSITION                                        */
/*==============================================================*/
CREATE TABLE TAUSERPOSITION
(
   USERID               NUMBER(10,0) NOT NULL ,
   POSITIONID           NUMBER(10,0) NOT NULL ,
   MAINPOSITION         CHAR(1) NOT NULL ,
   CREATEUSER           NUMBER(10,0) NOT NULL ,
   CREATETIME           DATE NOT NULL ,
   constraint PK_TAUSERPOSITION primary key (USERID, POSITIONID)
);
COMMENT ON TABLE TAUSERPOSITION IS
'人员岗位关系表';
COMMENT ON COLUMN TAUSERPOSITION.USERID IS
'人员id';
COMMENT ON COLUMN TAUSERPOSITION.POSITIONID IS
'岗位id';
COMMENT ON COLUMN TAUSERPOSITION.MAINPOSITION IS
'默认岗位';
COMMENT ON COLUMN TAUSERPOSITION.CREATEUSER IS
'创建人';
COMMENT ON COLUMN TAUSERPOSITION.CREATETIME IS
'创建时间';

/*==============================================================*/
/* Table: TAORGMG                                               */
/*==============================================================*/
CREATE TABLE TAORGMG
(
   POSITIONID           NUMBER(10,0) ,
   ORGID                NUMBER(10,0) 
);
COMMENT ON TABLE TAORGMG IS
'组织管理表';
COMMENT ON COLUMN TAORGMG.POSITIONID IS
'岗位id';
COMMENT ON COLUMN TAORGMG.ORGID IS
'组织id';

/*==============================================================*/
/* Table: TAMANAGERMG                                               */
/*==============================================================*/
CREATE TABLE TAMANAGERMG
(
   POSITIONID           NUMBER(10,0) ,
   ORGID                NUMBER(10,0) 
);
COMMENT ON TABLE TAMANAGERMG IS
'组织负责人（副职）管理表';
COMMENT ON COLUMN TAMANAGERMG.POSITIONID IS
'岗位id';
COMMENT ON COLUMN TAMANAGERMG.ORGID IS
'组织id';
/*==============================================================*/
/* Table: TASHAREPOSITION                                               */
/*==============================================================*/
CREATE TABLE TASHAREPOSITION
(
   SPOSITIONID           NUMBER(10),
   DPOSITIONID           NUMBER(10)
);
COMMENT ON TABLE TASHAREPOSITION IS
'共享岗位表';
COMMENT ON COLUMN TASHAREPOSITION.SPOSITIONID IS
'源岗位id';
COMMENT ON COLUMN TASHAREPOSITION.DPOSITIONID IS
'复制岗位id';

/*==============================================================*/
/* Table: TAACCESSLOG                                              */
/*==============================================================*/
CREATE TABLE TAACCESSLOG (
  LOGID NUMBER(15) NOT NULL,
  USERID NUMBER(10) NOT NULL,
  POSITIONID NUMBER(10) NOT NULL ,
  PERMISSIONID NUMBER(10) NOT NULL ,
  ISPERMISSION CHAR(1) NOT NULL,
  ACCESSTIME DATE NOT NULL,
  URL VARCHAR2(1024),
  SYSFLAG VARCHAR2(50),
  constraint PK_TAACCESSLOG primary key (LOGID)
);
COMMENT ON TABLE TAACCESSLOG IS
'功能日志表';
COMMENT ON COLUMN TAACCESSLOG.LOGID IS
'日志id';
COMMENT ON COLUMN TAACCESSLOG.USERID IS
'用户id';
COMMENT ON COLUMN TAACCESSLOG.POSITIONID IS
'岗位id';
COMMENT ON COLUMN TAACCESSLOG.PERMISSIONID IS
'功能id';
COMMENT ON COLUMN TAACCESSLOG.ISPERMISSION IS
'是否有权限';
COMMENT ON COLUMN TAACCESSLOG.ACCESSTIME IS
'访问时间';
COMMENT ON COLUMN TAACCESSLOG.URL IS '访问路径';
COMMENT ON COLUMN TAACCESSLOG.SYSFLAG IS '系统标识';

/*==============================================================*/
/* Table: TAMENUPOSITIONYAB003                                            */
/*==============================================================*/
CREATE TABLE TAMENUPOSITIONYAB003 (
  MENUID NUMBER(10) NOT NULL,
  POSITIONID NUMBER(10) NOT NULL ,
  YAB003   VARCHAR2(6) NOT NULL,
  constraint PK_TAMENUPOSITIONYAB003 primary key (MENUID,POSITIONID,YAB003)
);
COMMENT ON TABLE TAMENUPOSITIONYAB003 IS
'功能数据权限';
COMMENT ON COLUMN TAMENUPOSITIONYAB003.MENUID IS
'菜单id';
COMMENT ON COLUMN TAMENUPOSITIONYAB003.POSITIONID IS
'岗位id';
COMMENT ON COLUMN TAMENUPOSITIONYAB003.YAB003 IS
'分中心';


/*==============================================================*/
/* Table:TAYAB003SCOPE                                    */
/*==============================================================*/
CREATE TABLE TAYAB003SCOPE (
  YAB003   VARCHAR2(6) NOT NULL,
  YAB139    VARCHAR2(6) NOT NULL,
  constraint PK_TAYAB003SCOPE primary key (YAB003,YAB139)
);
COMMENT ON TABLE TAYAB003SCOPE IS
'分中心数据权限范围表';
COMMENT ON COLUMN TAYAB003SCOPE.YAB003 IS
'分中心';
COMMENT ON COLUMN TAYAB003SCOPE.YAB139 IS
'分中心数据权限';

/*==============================================================*/
/* Table: TACOMMONMENU                                               */
/*==============================================================*/
CREATE TABLE TACOMMONMENU (
  USERID NUMBER(10) NOT NULL,
  MENUID NUMBER(10) NOT NULL,
 constraint PK_TACOMMONMENU  PRIMARY KEY (USERID,MENUID)
) ;
COMMENT ON TABLE TACOMMONMENU
  IS '常用菜单';
COMMENT ON COLUMN TACOMMONMENU.USERID IS
'用户id';
COMMENT ON COLUMN TACOMMONMENU.MENUID IS
'菜单id';

/*==============================================================*/
/* Table: TACONSOLEMODULE                                               */
/*==============================================================*/
CREATE TABLE TACONSOLEMODULE
(
  MODULE_ID      NUMBER(10) not NULL,
  MODULE_NAME    VARCHAR2(100) not NULL,
  MODULE_URL     VARCHAR2(200) not NULL,
  MODULE_STA     VARCHAR2(1) default 1 not NULL,
  MODULE_DEFAULT VARCHAR2(1) default 1,
  MODULE_HEIGHT  VARCHAR2(10) default 200
);

COMMENT ON TABLE TACONSOLEMODULE
  IS 'ECADMIN工作台自定义组件';
COMMENT ON COLUMN TACONSOLEMODULE.MODULE_ID
  IS '模块编号';
COMMENT ON COLUMN TACONSOLEMODULE.MODULE_NAME
  IS '模块名称';
COMMENT ON COLUMN TACONSOLEMODULE.MODULE_URL
  IS '模块链接';
COMMENT ON COLUMN TACONSOLEMODULE.MODULE_STA
  IS '模块有效标识';
COMMENT ON COLUMN TACONSOLEMODULE.MODULE_DEFAULT
  IS '是否默认显示';
COMMENT ON COLUMN TACONSOLEMODULE.MODULE_HEIGHT
  is '模块默认高度';
ALTER TABLE TACONSOLEMODULE
  ADD CONSTRAINT PK_ECADMIN_CONSLE_MODULE PRIMARY KEY (MODULE_ID);
  
/*==============================================================*/
/* Table: TACONSOLEMODULELOCATION                                               */
/*==============================================================*/
CREATE TABLE TACONSOLEMODULELOCATION
(
  MARK       VARCHAR2(20) NOT NULL,
  POSITIONID NUMBER(10) NOT NULL,
  LOCATION   VARCHAR2(1000) NOT NULL
);

COMMENT ON TABLE TACONSOLEMODULELOCATION
  IS 'ECADMIN工作台自定义组件位置信息';
COMMENT ON COLUMN TACONSOLEMODULELOCATION.MARK
  IS '页面标识';
COMMENT ON COLUMN TACONSOLEMODULELOCATION.POSITIONID
  IS '岗位ID';
COMMENT ON COLUMN TACONSOLEMODULELOCATION.LOCATION
  IS '位置信息数据';
  
/*==============================================================*/
/* Table: TACONSOLEMODULEPRIVILEGE                                               */
/*==============================================================*/
CREATE TABLE TACONSOLEMODULEPRIVILEGE
(
  POSITIONID NUMBER(10) NOT NULL,
  MODULEID   NUMBER(10) NOT NULL
);
 
COMMENT ON TABLE TACONSOLEMODULEPRIVILEGE
  IS 'CRM工作台自定义组件权限信息';
COMMENT ON COLUMN TACONSOLEMODULEPRIVILEGE.POSITIONID
  IS '角色编号';
COMMENT ON COLUMN TACONSOLEMODULEPRIVILEGE.MODULEID
  IS '模块编号';
ALTER TABLE TACONSOLEMODULEPRIVILEGE
  ADD CONSTRAINT PK_CRM_CONSLE_MODULE_PRIVILEGE PRIMARY KEY (POSITIONID, MODULEID);

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

create table TALOGINHISTORYLOG
(
  logid      NUMBER(15) not NULL,
  userid     NUMBER(10) not NULL,
  logintime  DATE not NULL,
  logouttime DATE not NULL,
  clientip   VARCHAR2(200) not NULL,
  sessionid  VARCHAR2(200) not NULL,
  serverip   VARCHAR2(200),
  syspath    VARCHAR2(50)
);
ALTER TABLE TALOGINHISTORYLOG
  ADD CONSTRAINT PK_TALOGINHISTORYLOG PRIMARY KEY (LOGID);
create table TAONLINELOG
(
  logid       NUMBER(15) not NULL,
  userid      NUMBER(10) not NULL,
  logintime   DATE not NULL,
  curresource VARCHAR2(1000),
  clientip    VARCHAR2(200) not NULL,
  sessionid   VARCHAR2(200) not NULL,
  syspath     VARCHAR2(50),
  serverip    VARCHAR2(200)
);
ALTER TABLE TAONLINELOG
  ADD CONSTRAINT PK_TAONLINELOG PRIMARY KEY (LOGID);

DROP TABLE TALOCALCACHEVERSION CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: TALOCALCACHEVERSION                                   */
/*==============================================================*/
CREATE TABLE TALOCALCACHEVERSION
(
   VERSION		NUMBER(11) NOT NULL,
   CODETYPE		VARCHAR2(20)
);
COMMENT ON TABLE TALOCALCACHEVERSION
  IS '本地缓存码表版本号';
COMMENT ON COLUMN TALOCALCACHEVERSION.VERSION
  IS '版本号';
COMMENT ON COLUMN TALOCALCACHEVERSION.CODETYPE
  IS '改动的type';

DROP TABLE  TADATAACCESSDIMENSION CASCADE CONSTRAINTS;
/*==============================================================*/
/* Table: TADATAACCESSDIMENSION                                                  */
/*==============================================================*/
CREATE TABLE TADATAACCESSDIMENSION (
  DIMENSIONID NUMBER(10,0) NOT NULL,
  POSITIONID NUMBER(10,0) NOT NULL,
  MENUID NUMBER(10,0) NOT NULL,
  DIMENSIONTYPE VARCHAR2(20) NOT NULL,
  DIMENSIONPERMISSIONID VARCHAR2(20),
  ALLACCESS VARCHAR2(1),
  SYSPATH VARCHAR2(50),
  CONSTRAINT PK_TADATAACCESSDIMENSION PRIMARY KEY (DIMENSIONID)
);
COMMENT ON TABLE TADATAACCESSDIMENSION IS '维度数据权限表';
COMMENT ON COLUMN TADATAACCESSDIMENSION.DIMENSIONID IS '维度ID';
COMMENT ON COLUMN TADATAACCESSDIMENSION.POSITIONID IS '岗位ID';
COMMENT ON COLUMN TADATAACCESSDIMENSION.MENUID IS '菜单ID';
COMMENT ON COLUMN TADATAACCESSDIMENSION.DIMENSIONTYPE IS '维度类型';
COMMENT ON COLUMN TADATAACCESSDIMENSION.DIMENSIONPERMISSIONID IS '维度权限ID';
COMMENT ON COLUMN TADATAACCESSDIMENSION.ALLACCESS IS '是否具有该维度所有权限';
COMMENT ON COLUMN TADATAACCESSDIMENSION.SYSPATH IS '系统标识';


DROP TABLE TAADMINYAB139SCOPE CASCADE CONSTRAINTS;
CREATE TABLE TAADMINYAB139SCOPE (
  POSITIONID NUMBER(10) NOT NULL ,
  YAB139   VARCHAR2(6) NOT NULL,
  constraint PK_TAADMINYAB139SCOPE primary key (POSITIONID,YAB139)
);
COMMENT ON TABLE TAADMINYAB139SCOPE IS
'管理员数据区管理范围';
COMMENT ON COLUMN TAADMINYAB139SCOPE.POSITIONID IS
'岗位id';
COMMENT ON COLUMN TAADMINYAB139SCOPE.YAB139 IS
'数据区';

DROP TABLE TAYAB139MG CASCADE CONSTRAINTS;
CREATE TABLE TAYAB139MG
(
  YAB003 VARCHAR2(6),
  YAB139 VARCHAR2(20)
);

comment on table TAYAB139MG
  is '经办机构管理数据区范围';
comment on column TAYAB139MG.YAB003
  is '经办机构';
comment on column TAYAB139MG.YAB139
  is '数据区';
  
DROP TABLE TAYAB003LEVELMG CASCADE CONSTRAINTS;
CREATE TABLE TAYAB003LEVELMG
(
  PYAB003 VARCHAR2(6),
  YAB003  VARCHAR2(6)
);

comment on table TAYAB003LEVELMG
  is '经办机构层级关系管理';
comment on column TAYAB003LEVELMG.PYAB003
  is '父经办机构';
comment on column TAYAB003LEVELMG.YAB003
  is '经办机构';


DROP TABLE QRTZ_JOB_MSGS CASCADE CONSTRAINTS;
DROP TABLE YHCIP_ORACLE_JOBS CASCADE CONSTRAINTS;
CREATE TABLE YHCIP_ORACLE_JOBS  (
   JOBID                VARCHAR2(20)                    not NULL,
   ORACLEJOBID          VARCHAR2(20)                    not NULL,
   JOBNAME              VARCHAR2(100)                   not NULL,
   USERID               VARCHAR2(20)                    not NULL,
   WHAT                 VARCHAR2(200)                   not NULL,
   STARTTIME            VARCHAR2(200)                   not NULL,
   ENDTIME              DATE,
   INTERVAL             VARCHAR2(200),
   SUBMITTIME           DATE,
   constraint PK_YHCIP_ORACLE_JOBS primary key (JOBID)
);

COMMENT ON TABLE YHCIP_ORACLE_JOBS IS
'oracle定时';

COMMENT ON COLUMN YHCIP_ORACLE_JOBS.JOBID IS
'任务id';

COMMENT ON COLUMN YHCIP_ORACLE_JOBS.ORACLEJOBID IS
'oracle的jobid';

COMMENT ON COLUMN YHCIP_ORACLE_JOBS.JOBNAME IS
'任务名称';

COMMENT ON COLUMN YHCIP_ORACLE_JOBS.USERID IS
'用户id';

COMMENT ON COLUMN YHCIP_ORACLE_JOBS.WHAT IS
'执行的过程';

COMMENT ON COLUMN YHCIP_ORACLE_JOBS.STARTTIME IS
'开始时间';

COMMENT ON COLUMN YHCIP_ORACLE_JOBS.ENDTIME IS
'结束时间';

COMMENT ON COLUMN YHCIP_ORACLE_JOBS.INTERVAL IS
'时间间隔';

COMMENT ON COLUMN YHCIP_ORACLE_JOBS.SUBMITTIME IS
'提交时间';
CREATE TABLE QRTZ_JOB_MSGS  (
   ID                   VARCHAR2(50)                    NOT NULL,
   JOB_NAME             VARCHAR2(80)                    NOT NULL,
   JOB_GROUP            VARCHAR2(80)                    NOT NULL,
   USERID               VARCHAR2(20),
   EXECSTARTTIME        VARCHAR2(30),
   EXECENDTIME          VARCHAR2(30),
   ISSUCCESS            VARCHAR2(2),
   SUCCESSMSG           VARCHAR2(1024),
   ERRORMSG             VARCHAR2(1024),
   CONSTRAINT SYS_C0052264 PRIMARY KEY (ID)
);
CREATE OR REPLACE PACKAGE pkg_YHCIP AS
   /*---------------------------------------------------------------------------
   ||  程序包名 ：pkg_YHCIP                                                   
   ||  业务环节 ：YHCIP                                                       
   ||  对象列表 ：私有过程函数                                                
   ||             --------------------------------------------------------------
   ||             公用过程函数                                                
   ||             --------------------------------------------------------------
   ||                                                                         
   ||  其它说明 ：                                                            
   ||  完成日期 ：                                                            
   ||  版本编号 ：Ver 1.0                                                     
   ||  审 查 人 ：×××                      审查日期 ：YYYY-MM-DD           
   ||-------------------------------------------------------------------------*/

   /*-------------------------------------------------------------------------*/
   /* 公用全局数据类型声明                                                    */
   /*-------------------------------------------------------------------------*/

   /*-------------------------------------------------------------------------*/
   /* 公用全局常量声明                                                        */
   /*-------------------------------------------------------------------------*/
   gn_def_OK  CONSTANT VARCHAR2(12) := 'NOERROR'; -- 成功
   gn_def_ERR CONSTANT VARCHAR2(12) := '9999'; -- 系统错误
   
   GN_DEF_YES CONSTANT VARCHAR2(12) := '是'; -- 是
   GN_DEF_NO  CONSTANT VARCHAR2(12) := '否'; -- 否

   gs_def_DatetimeFormat  CONSTANT VARCHAR2(21) := 'YYYY-MM-DD HH24:MI:SS';
   gs_def_DateFormat      CONSTANT VARCHAR2(10) := 'YYYY-MM-DD';
   gs_def_YearMonthFormat CONSTANT VARCHAR2(6) := 'YYYYMM';
   gs_def_YearFormat      CONSTANT VARCHAR2(4) := 'YYYY';
   gs_def_TimeFormat      CONSTANT VARCHAR2(10) := 'HH24:MI:SS';
   gs_def_NumberFormat    CONSTANT VARCHAR2(15) := '999999999999.99';
   gs_def_NOFormat        CONSTANT VARCHAR2(15) := '999999999999999';
   gs_def_NullDate        CONSTANT DATE := TO_DATE('1900-01-01',
                                                   gs_def_DateFormat);

   /*-------------------------------------------------------------------------*/
   /* 公用过程函数声明                                                        */
   /*-------------------------------------------------------------------------*/

   /*模版*/
   PROCEDURE prc_Template (
      prm_AppCode                         OUT      VARCHAR2          ,
      prm_ErrorMsg                        OUT      VARCHAR2          );
   

   PROCEDURE prc_oracleJob (
			      prm_jobid                          IN OUT VARCHAR2      ,--jobid外部传进来的是用于YHCIP_ORACLE_JOBS表的主键，出去的是oracle生成的jobid
			      prm_jobname			                 IN     VARCHAR2      ,--任务名称
			      prm_what                           IN     VARCHAR2      ,--执行过程，需要“;”分号结尾
			      prm_next_date                      IN     VARCHAR2      ,--执行时间
			      prm_interval                       IN     VARCHAR2      ,--间隔循环时间
			      prm_userid                         IN     VARCHAR2      ,--用户id
			      prm_AppCode                        OUT    VARCHAR2      ,
			      prm_ErrorMsg                       OUT    VARCHAR2      );     
   PROCEDURE prc_oraclejobbroken(
			      prm_jobid                          IN     NUMBER        ,--jobid外部传进来的是用于YHCIP_ORACLE_JOBS表的主键，出去的是oracle生成的jobid
			      prm_broken                         IN     VARCHAR2      ,--是否暂停
			      prm_next_date                      IN     VARCHAR2      ,--下次执行时间
			      prm_AppCode                        OUT    VARCHAR2      ,
			      prm_ErrorMsg                       OUT    VARCHAR2      );
   PROCEDURE prc_oraclejobchange (
			      prm_jobid                          IN OUT VARCHAR2      ,--jobid外部传进来的是用于YHCIP_ORACLE_JOBS表的主键，出去的是oracle生成的jobid 
			      prm_jobname                        IN     VARCHAR2      ,--任务名称                                                                 
			      prm_what                           IN     VARCHAR2      ,--执行过程，需要“;”分号结尾                                              
			      prm_next_date                      IN     VARCHAR2      ,--执行时间                                                                 
			      prm_interval                       IN     VARCHAR2      ,--间隔循环时间                                                             
			      prm_userid                         IN     VARCHAR2      ,--用户id                                                                   
			      prm_AppCode                        OUT    VARCHAR2      ,                                                                           
			      prm_ErrorMsg                       OUT    VARCHAR2      )	;		      			                          

END pkg_YHCIP;
/

show error;

CREATE OR REPLACE PACKAGE BODY pkg_YHCIP AS
   /*---------------------------------------------------------------------------
   ||  程序包名 ：pkg_YHCIP                                                   
   ||  业务环节 ：YHCIP                                                       
   ||  对象列表 ：私有过程函数                                                
   ||             --------------------------------------------------------------
   ||             公用过程函数                                                
   ||             --------------------------------------------------------------
   ||                                                                         
   ||  其它说明 ：                                                            
   ||  完成日期 ：                                                            
   ||  版本编号 ：Ver 1.0                                                     
   ||  审 查 人 ：×××                      审查日期 ：YYYY-MM-DD           
   ||-------------------------------------------------------------------------*/
   /*------------------------------------------------------------------------*/
   /* 私有全局数据类型声明                                                   */
   /*------------------------------------------------------------------------*/

   /*------------------------------------------------------------------------*/
   /* 私有全局常量声明                                                       */
   /*------------------------------------------------------------------------*/
   PRE_ERRCODE  CONSTANT VARCHAR2(12) := 'YHCIP'; -- 本包的错误编号前缀
   NULL_PREFIX  CONSTANT VARCHAR2(1) := ' ';

   /*------------------------------------------------------------------------*/
   /* 私有全局变量声明                                                       */
   /*------------------------------------------------------------------------*/

   /*------------------------------------------------------------------------*/
   /* 私有过程函数声明                                                       */
   /*------------------------------------------------------------------------*/

   /*------------------------------------------------------------------------*/
   /* 公用过程函数描述                                                       */
   /*------------------------------------------------------------------------*/

   /*---------------------------------------------------------------------------
   || 业务环节 ：prc_Template
   || 过程名称 ：
   || 功能描述 ：
   || 使用场合 ：
   || 参数描述 ：标识                  名称             输入输出   数据类型
   ||            ---------------------------------------------------------------
   ||            prm_AppCode        执行代码             输出     VARCHAR2(12)
   ||            prm_ErrorMsg       出错信息             输出     VARCHAR2(128)
   ||
   || 参数说明 ：标识               详细说明
   ||            ---------------------------------------------------------------
   ||
   || 其它说明 ：
   || 作    者 ：
   || 完成日期 ：
   ||---------------------------------------------------------------------------
   ||                                 修改记录
   ||---------------------------------------------------------------------------
   || 修 改 人 ：×××                        修改日期 ：YYYY-MM-DD
   || 修改描述 ：
   ||-------------------------------------------------------------------------*/
   PROCEDURE prc_Template (
      prm_AppCode                        OUT      VARCHAR2          ,
      prm_ErrorMsg                       OUT      VARCHAR2          )
   IS
      /*变量声明*/
      /*游标声明*/
   BEGIN
      /*初始化变量*/
      prm_AppCode  := PRE_ERRCODE || gn_def_ERR;
      prm_ErrorMsg := ''                                ;

      /*成功处理*/
      <<label_OK>>
      /*关闭打开的游标*/
      /*给返回参数赋值*/
      prm_AppCode  := gn_def_OK ;
      prm_ErrorMsg := ''                 ;
      RETURN ;

      /*处理失败*/
      <<label_ERROR>>
      /*关闭打开的游标*/
      /*给返回参数赋值*/
      IF prm_AppCode = gn_def_OK THEN
         prm_AppCode  := PRE_ERRCODE || gn_def_ERR;
      END IF ;
      RETURN ;

   EXCEPTION
      -- WHEN NO_DATA_FOUND THEN
      -- WHEN TOO_MANY_ROWS THEN
      -- WHEN DUP_VAL_ON_INDEX THEN
      WHEN OTHERS THEN
         /*关闭打开的游标*/
         prm_AppCode  := PRE_ERRCODE || gn_def_ERR;
         prm_ErrorMsg := '数据库错误:'|| SQLERRM ;
         RETURN;
   END prc_Template ;



  /*---------------------------------------------------------------------------
  || 业务环节 ：创建一个定时服务
  || 过程名称 ：prc_oracleJob
  || 功能描述 ：创建一个定时服务
  ||
  || 使用场合 ：对需要定时执行的过程
  || 参数描述 ：标识                  名称             输入输出   数据类型
  ||            ---------------------------------------------------------------
  ||		prm_jobid          任务编号             输入/输出     VARCHAR2
  ||		prm_jobname	   任务名称		 输入         VARCHAR2 
  ||		prm_what           需要执行的过程        输入         VARCHAR2
  ||		prm_next_date      执行时间              输入         VARCHAR2 
  ||		prm_interval       间隔循环时间          输入         VARCHAR2 
  ||		prm_userid         用户id                输入         VARCHAR2
  ||            prm_AppCode        执行代码              输出         VARCHAR2(12) 
  ||            prm_ErrorMsg       出错信息              输出         VARCHAR2(128)   
  ||
  || 参数说明 ：标识               详细说明
  ||            ---------------------------------------------------------------
  ||
  || 其它说明 ：
  || 作    者 ：林隆永
  || 完成日期 ：
  ||---------------------------------------------------------------------------
  ||                                 修改记录
  ||---------------------------------------------------------------------------
  || 修 改 人 ：×××                        修改日期 ：YYYY-MM-DD
  || 修改描述 ：
  ||-------------------------------------------------------------------------*/
  PROCEDURE prc_oracleJob (
      prm_jobid                          IN OUT VARCHAR2      ,--jobid外部传进来的是用于YHCIP_ORACLE_JOBS表的主键，出去的是oracle生成的jobid
      prm_jobname			 IN     VARCHAR2      ,--任务名称
      prm_what                           IN     VARCHAR2      ,--执行过程，需要“;”分号结尾
      prm_next_date                      IN     VARCHAR2      ,--执行时间
      prm_interval                       IN     VARCHAR2      ,--间隔循环时间
      prm_userid                         IN     VARCHAR2      ,--用户id
      prm_AppCode                        OUT    VARCHAR2      ,
      prm_ErrorMsg                       OUT    VARCHAR2      )
   IS
      /*变量声明*/
      v_next_date date;
      jobid BINARY_INTEGER;
      sqlStr varchar2(200);
      /*游标声明*/
   BEGIN
      /*给返回参数赋值*/
      prm_AppCode  := gn_def_OK;
      prm_ErrorMsg := '';
      sqlStr := 'select to_date(to_char('||prm_next_date||',''yyyy-MM-dd HH24:mi:ss''),''yyyy-MM-dd HH24:mi:ss'') from dual';
      execute immediate sqlStr into v_next_date;

     IF prm_interval IS NULL THEN
       dbms_job.submit(jobid,prm_what,v_next_date);
     ELSE
        dbms_job.submit(jobid,prm_what,v_next_date,prm_interval);
     END IF;

      INSERT INTO YHCIP_ORACLE_JOBS(
                       JOBID,--代表一个定时任务
      		       JOBNAME,--定时任务的名称
      		       STARTTIME,--开始执行时间
      		       USERID,--执行的用户
                       oraclejobid,
                       what,
                       interval)--oracle的jobid
      		VALUES (
      		       prm_jobid,
      		       prm_jobname,
      		       prm_next_date,
      	               prm_userid,
                       jobid,
                       prm_what,
                       prm_interval);

      prm_jobid := jobid;
      
      INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOB_NAME,
                  JOB_GROUP,
                  USERID,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  SUCCESSMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobname,
                  'DEFAULT',
                  prm_userid,
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '0',
                  'LOG:创建成功');
      commit;
   EXCEPTION
      -- WHEN NO_DATA_FOUND THEN
      -- WHEN TOO_MANY_ROWS THEN
      -- WHEN DUP_VAL_ON_INDEX THEN
      WHEN OTHERS THEN
         /*关闭打开的游标*/
         INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOB_NAME,
                  JOB_GROUP,
                  USERID,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  ERRORMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobname,
                  'DEFAULT',
                  prm_userid,
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '1',
                  'LOG:创建失败');
         commit;          
         prm_AppCode  := PRE_ERRCODE||'0006';
         prm_ErrorMsg := '数据库错误:'|| SQLERRM ;
         RETURN;
   END prc_oracleJob ;
  /*---------------------------------------------------------------------------
  || 业务环节 ：暂停、继续 任务
  || 过程名称 ：prc_oraclejobbroken
  || 功能描述 ：暂停、继续 任务
  ||
  || 使用场合 ：对任务进行暂停、继续任务
  || 参数描述 ：标识                  名称             输入输出   数据类型
  ||            ---------------------------------------------------------------
  ||		prm_jobid          任务编号             输入/输出     VARCHAR2
  ||		prm_broken	   暂停、继续(true,false)输入         VARCHAR2 
  ||		prm_next_date      执行时间              输入         VARCHAR2 
  ||            prm_AppCode        执行代码              输出         VARCHAR2(12) 
  ||            prm_ErrorMsg       出错信息              输出         VARCHAR2(128)   
  ||
  || 参数说明 ：标识               详细说明
  ||            ---------------------------------------------------------------
  ||
  || 其它说明 ：
  || 作    者 ：林隆永
  || 完成日期 ：
  ||---------------------------------------------------------------------------
  ||                                 修改记录
  ||---------------------------------------------------------------------------
  || 修 改 人 ：×××                        修改日期 ：YYYY-MM-DD
  || 修改描述 ：
  ||-------------------------------------------------------------------------*/  
	PROCEDURE prc_oraclejobbroken(
	      prm_jobid                          IN     NUMBER        ,
	      prm_broken                         IN     VARCHAR2      ,
	      prm_next_date                      IN     VARCHAR2      ,
	      prm_AppCode                        OUT    VARCHAR2      ,
	      prm_ErrorMsg                       OUT    VARCHAR2      )
	IS
	      v_next_date date;
	      sqlStr varchar2(200);
	BEGIN
	      /*给返回参数赋值*/
	  prm_AppCode  := gn_def_OK ;--pkg_COMM.gn_def_OK
	  prm_ErrorMsg := '';


	  IF prm_broken = 'false' OR prm_broken = 'FALSE' THEN
	    sqlStr := 'select to_date(to_char('||prm_next_date||',''yyyy-MM-dd HH24:mi:ss''),''yyyy-MM-dd HH24:mi:ss'') from dual';
	    execute immediate sqlStr into v_next_date;
	    DBMS_JOB.broken(prm_jobid,false,v_next_date);
      INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOB_NAME,
                  JOB_GROUP,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  SUCCESSMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobid,
                  'DEFAULT',
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '0',
                  'LOG:继续执行成功');
	  ELSIF prm_broken = 'true' OR prm_broken = 'TRUE' THEN
	    DBMS_JOB.broken(prm_jobid,true);
       INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOB_NAME,
                  JOB_GROUP,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  SUCCESSMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobid,
                  'DEFAULT',
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '0',
                  'LOG:暂停成功');
	  END IF;
	  commit;
	  EXCEPTION
	      -- WHEN NO_DATA_FOUND THEN
	      -- WHEN TOO_MANY_ROWS THEN
	      -- WHEN DUP_VAL_ON_INDEX THEN
	      WHEN OTHERS THEN
	         /*关闭打开的游标*/
           INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOB_NAME,
                  JOB_GROUP,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  ERRORMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobid,
                  'DEFAULT',
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '1',
                  'LOG:暂停或继续执行失败');
           commit;
	         prm_AppCode  := PRE_ERRCODE||'0007';
	         prm_ErrorMsg := '数据库错误:'|| SQLERRM ;
	         RETURN;
	END prc_oraclejobbroken;
  /*---------------------------------------------------------------------------
  || 业务环节 ：更改一个定时服务
  || 过程名称 ：prc_oraclejobchange
  || 功能描述 ：更改一个定时服务
  ||
  || 使用场合 ：更改一个定时服务
  || 参数描述 ：标识                  名称             输入输出   数据类型
  ||            ---------------------------------------------------------------
  ||		prm_jobid          任务编号             输入/输出     VARCHAR2
  ||		prm_jobname	   任务名称		 输入         VARCHAR2 
  ||		prm_what           需要执行的过程        输入         VARCHAR2
  ||		prm_next_date      执行时间              输入         VARCHAR2 
  ||		prm_interval       间隔循环时间          输入         VARCHAR2 
  ||		prm_userid         用户id                输入         VARCHAR2
  ||            prm_AppCode        执行代码              输出         VARCHAR2(12) 
  ||            prm_ErrorMsg       出错信息              输出         VARCHAR2(128)   
  ||
  || 参数说明 ：标识               详细说明
  ||            ---------------------------------------------------------------
  ||
  || 其它说明 ：
  || 作    者 ：林隆永
  || 完成日期 ：
  ||---------------------------------------------------------------------------
  ||                                 修改记录
  ||---------------------------------------------------------------------------
  || 修 改 人 ：×××                        修改日期 ：YYYY-MM-DD
  || 修改描述 ：
  ||-------------------------------------------------------------------------*/	
  PROCEDURE prc_oraclejobchange (
      prm_jobid                          IN OUT VARCHAR2      ,--jobid外部传进来的是用于YHCIP_ORACLE_JOBS表的主键，出去的是oracle生成的jobid
      prm_jobname                        IN     VARCHAR2      ,--任务名称
      prm_what                           IN     VARCHAR2      ,--执行过程，需要“;”分号结尾
      prm_next_date                      IN     VARCHAR2      ,--执行时间
      prm_interval                       IN     VARCHAR2      ,--间隔循环时间
      prm_userid                         IN     VARCHAR2      ,--用户id
      prm_AppCode                        OUT    VARCHAR2      ,
      prm_ErrorMsg                       OUT    VARCHAR2      )
   IS
      /*变量声明*/
      v_next_date date;
      sqlStr varchar2(200);
      /*游标声明*/
   BEGIN
      /*给返回参数赋值*/
      prm_AppCode  := gn_def_OK;--pkg_COMM.gn_def_OK
      prm_ErrorMsg := '';

      sqlStr := 'select to_date(to_char('||prm_next_date||',''yyyy-MM-dd HH24:mi:ss''),''yyyy-MM-dd HH24:mi:ss'') from dual';
      execute immediate sqlStr into v_next_date;

      dbms_job.change(prm_jobid,prm_what,v_next_date,prm_interval);
      UPDATE YHCIP_ORACLE_JOBS
          SET userid     = prm_userid   ,
              starttime  = prm_next_date,
              jobname    = prm_jobname  ,
              what       = prm_what     ,
              interval   = prm_interval
          WHERE
              oraclejobid=prm_jobid;
      
      INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOB_NAME,
                  JOB_GROUP,
                  USERID,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  SUCCESSMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobid,
                  'DEFAULT',
                  prm_userid,
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '0',
                  'LOG:更改成功');              
      commit;
   EXCEPTION
      -- WHEN NO_DATA_FOUND THEN
      -- WHEN TOO_MANY_ROWS THEN
      -- WHEN DUP_VAL_ON_INDEX THEN
      WHEN OTHERS THEN
         /*关闭打开的游标*/
  INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOB_NAME,
                  JOB_GROUP,
                  USERID,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  ERRORMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobid,
                  'DEFAULT',
                  prm_userid,
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '1',
                  'LOG:更改失败'); 
   commit; 
	 prm_AppCode  := PRE_ERRCODE||'0008';
	 prm_ErrorMsg := '数据库错误:'|| SQLERRM ;
         RETURN;
   END prc_oraclejobchange ;

END pkg_YHCIP;
/
show error;

commit;


ALTER TABLE TARUNQIANRESOURCE
   DROP CONSTRAINT FK_YHCIP_RU_REFERENCE_YHCIP_RU;
ALTER TABLE TARUNQIANAD52REFERENCE
   DROP CONSTRAINT FK_REPORT_INFO;

DROP TABLE  TARUNQIANRESOURCE CASCADE CONSTRAINTS;

DROP TABLE TARUNQIANPRINTSETUP CASCADE CONSTRAINTS;

DROP TABLE TARUNQIANAD52REFERENCE CASCADE CONSTRAINTS;
-- Create table
create table TARUNQIANRESOURCE
(
  raqfilename       VARCHAR2(200) not NULL,
  parentraqfilename VARCHAR2(200),
  raqname           VARCHAR2(200),
  raqtype           VARCHAR2(6),
  raqfile           BLOB,
  uploador          VARCHAR2(19),
  uploadtime        DATE,
  subrow            NUMBER,
  subcell           NUMBER,
  raqdatasource     VARCHAR2(19),
  raqparam          VARCHAR2(500),
  orgid             VARCHAR2(15),
  constraint PK_YHCIP_RUNQIAN_RESOURCE primary key (RAQFILENAME)
);
-- Add comments to the table 
comment on table TARUNQIANRESOURCE
  is '润乾报表模板';
-- Add comments to the columns 
comment on column TARUNQIANRESOURCE.raqfilename
  is '文件名/报表标识（RaqfileName）';
comment on column TARUNQIANRESOURCE.parentraqfilename
  is '父报表标识（ParentRaqfileName）';
comment on column TARUNQIANRESOURCE.raqname
  is '报表名称（RaqName）';
comment on column TARUNQIANRESOURCE.raqtype
  is '报表类型（RaqType）';
comment on column TARUNQIANRESOURCE.raqfile
  is '资源文件（RaqFile）';
comment on column TARUNQIANRESOURCE.uploador
  is '上传人（Uploador）';
comment on column TARUNQIANRESOURCE.uploadtime
  is '上传时间（UploadTime）';
comment on column TARUNQIANRESOURCE.subrow
  is '父报表位置行（SubRow）';
comment on column TARUNQIANRESOURCE.subcell
  is '父报表位置列（SubCell）';
comment on column TARUNQIANRESOURCE.raqdatasource
  is '数据源（RaqDataSource）';
comment on column TARUNQIANRESOURCE.raqparam
  is '报表参数JSON格式Str（RaqParam）';
comment on column TARUNQIANRESOURCE.orgid
  is '部门编号(OrgId)';

alter table TARUNQIANRESOURCE
  add constraint FK_YHCIP_RU_REFERENCE_YHCIP_RU foreign key (PARENTRAQFILENAME)
  references TARUNQIANRESOURCE (RAQFILENAME);

-- Create table
create table TARUNQIANPRINTSETUP
(
  setupid    VARCHAR2(200) not NULL,
  setupvalue VARCHAR2(400) not NULL,
  constraint PK_PRINTSETUP primary key (SETUPID)
);
-- Add comments to the table 
comment on table TARUNQIANPRINTSETUP
  is '打印设置信息表';
-- Add comments to the columns 
comment on column TARUNQIANPRINTSETUP.setupid
  is '打印设置编号（SetupId）';
comment on column TARUNQIANPRINTSETUP.setupvalue
  is '打印设置信息（SetupValue）';

-- Create table
create table TARUNQIANAD52REFERENCE
(
  menuid              NUMBER(10),
  raqfilename         VARCHAR2(200),
  limited             NUMBER,
  scaleexp            NUMBER,
  isgroup             VARCHAR2(6),
  needsaveasexcel     VARCHAR2(6),
  needsaveasexcel2007 VARCHAR2(6),
  needsaveaspdf       VARCHAR2(6),
  needsaveasword      VARCHAR2(6),
  needsaveastext      VARCHAR2(6),
  needprint           VARCHAR2(6),
  id                  NUMBER(10) not NULL
);
-- Add comments to the table 
comment on table TARUNQIANAD52REFERENCE
  is 'YHCIP_RUNQIAN_AD52_REFERENCE润乾报表菜单信息';
-- Add comments to the columns 
comment on column TARUNQIANAD52REFERENCE.menuid
  is '功能编号';
comment on column TARUNQIANAD52REFERENCE.raqfilename
  is '文件名/报表标识（RaqfileName）';
comment on column TARUNQIANAD52REFERENCE.limited
  is '每页显示数（Limited）';
comment on column TARUNQIANAD52REFERENCE.scaleexp
  is 'JSP中缩放比率（ScaleExp）';
comment on column TARUNQIANAD52REFERENCE.isgroup
  is '是否按行分页（IsGroup）';
comment on column TARUNQIANAD52REFERENCE.needsaveasexcel
  is '是否保存为Excel（NeedSaveAsExcel）';
comment on column TARUNQIANAD52REFERENCE.needsaveasexcel2007
  is '是否保存为Excel2007（NeedSaveAsExcel2007）';
comment on column TARUNQIANAD52REFERENCE.needsaveaspdf
  is '是否保存为Pdf（NeedSaveAsPdf）';
comment on column TARUNQIANAD52REFERENCE.needsaveasword
  is '是否保存为Word（NeedSaveAsWord）';
comment on column TARUNQIANAD52REFERENCE.needsaveastext
  is '是否保存为Text（NeedSaveAsText）';
comment on column TARUNQIANAD52REFERENCE.needprint
  is '是否保存为Print（NeedPrint）';
comment on column TARUNQIANAD52REFERENCE.id
  is '主键ID';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TARUNQIANAD52REFERENCE
  add constraint PK_REPORT_INFO primary key (ID)
  using index ;
alter table TARUNQIANAD52REFERENCE
  add constraint FK_REPORT_INFO foreign key (RAQFILENAME)
  references TARUNQIANRESOURCE (RAQFILENAME);


INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('MENUTYPE', '菜单类型', '0', '通用菜单', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('MENUTYPE', '菜单类型', '1', '系统管理菜单', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('MENUTYPE', '菜单类型', '2', '业务功能菜单', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('POSITIONTYPE', '岗位类型', '1', '公有岗位', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('POSITIONTYPE', '岗位类型', '2', '个人专属岗位', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('POSITIONTYPE', '岗位类型', '3', '委派岗位', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('SEX', '性别', '1', '男', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('SEX', '性别', '2', '女', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('SEX', '性别', '0', '无', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('ORGTYPE', '组织类型', '01', '机构', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('ORGTYPE', '组织类型', '02', '部门', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('ORGTYPE', '组织类型', '04', '组', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('EFFECTIVE', '有效标志', '1', '有效', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('EFFECTIVE', '有效标志', '0', '无效', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('YESORNO', '是否', '1', '是', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('YESORNO', '是否', '0', '否', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('POLICY', '安全策略', '1', '要认证要显示', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('POLICY', '安全策略', '2', '要认证不显示', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('POLICY', '安全策略', '3', '不认证不显示', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('POLICY', '安全策略', '4', '不认证要显示', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '01', '新增组织', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '02', '编辑组织', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '03', '禁用组织', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '04', '新增人员', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '05', '编辑人员', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '06', '禁用人员', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '07', '密码重置', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '08', '赋予岗位给人员', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '09', '设置主岗位给人员', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '10', '新增岗位', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '11', '编辑岗位', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '12', '禁用岗位', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '13', '赋予岗位使用权限', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '14', '回收岗位使用权限', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '15', '启用岗位', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '16', '启用人员', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '17', '启用组织', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '18', '删除组织', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '19', '赋予岗位授权别人使用权限', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '20', '回收岗位授权别人使用权限', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '21', '赋予岗位授权别人授权权限', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '22', '回收岗位授权别人授权权限', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '23', '删除管理员', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '24', '新增管理员', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '25', '删除岗位', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPTYPE', '操作类型', '26', '删除人员', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPOBJTYPE', '操作对象类型', '01', '组织', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPOBJTYPE', '操作对象类型', '02', '人员', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPOBJTYPE', '操作对象类型', '03', '岗位', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('OPOBJTYPE', '操作对象类型', '04', '权限', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('YAB003', '分中心', '9999', '9999', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('RAQTYPE', '报表类型', '0', '参数报表', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('RAQTYPE', '报表类型', '1', '主报表', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('RAQTYPE', '报表类型', '2', '子报表', '9999', '0', 0);
INSERT INTO aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('AUTHMODE', '认证方式类型', '04', 'Key盘', '9999', '0', 0);
INSERT INTO aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('AUTHMODE', '权限认证方式', '03', '人脸识别', '9999', '0', 0);
INSERT INTO aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('AUTHMODE', '权限认证方式', '02', '指纹识别', '9999', '0', 0);
INSERT INTO aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('AUTHMODE', '权限认证方式', '01', '用户名密码', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('LOGLEVEL', '日志等级', '0', 'OFF', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('LOGLEVEL', '日志等级', '1', 'FATAL', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('LOGLEVEL', '日志等级', '2', 'ERROR', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('LOGLEVEL', '日志等级', '3', 'WARN', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('LOGLEVEL', '日志等级', '4', 'INFO', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('LOGLEVEL', '日志等级', '5', 'DEBUG', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('LOGLEVEL', '日志等级', '6', 'TRACE', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)
VALUES ('LOGLEVEL', '日志等级', '7', 'ALL', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER) VALUES ('POSITIONCATEGORY', '岗位类别', '01', '业务岗', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER) VALUES ('POSITIONCATEGORY', '岗位类别', '02', '稽核岗', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER) VALUES ('AUDITSTATE', '审核状态', '0', '无需审核', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER) VALUES ('AUDITSTATE', '审核状态', '1', '待审核', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER) VALUES ('AUDITSTATE', '审核状态', '2', '审核通过', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER) VALUES ('AUDITSTATE', '审核状态', '3', '审核未通过', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER) VALUES ('TEMPLATETYPE', '模板类型', '1', 'word模板(.xml)', '9999', '0', 0);
INSERT INTO AA10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER) VALUES ('TEMPLATETYPE', '模板类型', '2', 'excel模板(.xls/.xlsx)', '9999', '0', 0);


INSERT INTO TACONFIGSYSPATH (SERIALID, ID, NAME, URL, PY, CURSYSTEM) VALUES('1','sysmg','系统管理','http://127.0.0.1:8080/ta3/','xtgl','0');

INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (1, 0, '银海软件', NULL, '1', '银海软件', 'tree-organisation', NULL, NULL, NULL, '1', '1', '0', NULL, 0, '01', 1, '0', '0', NULL, 'sysmg', '1', 0, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (2, 1, '权限管理', NULL, '1/2', '银海软件/权限管理', 'icon-004?#87bfe8', NULL, NULL, NULL, '1', '1', '1', NULL, 0, '01', 2, '0', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (3, 1, '开发管理', NULL, '1/3', '银海软件/开发管理', 'icon-001?#6A6A6A', NULL, NULL, NULL, '1', '1', '1', NULL, 1, '01', 2, '0', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (4, 1, '系统管理', NULL, '1/4', '银海软件/系统管理', 'icon-005?#E1785A', NULL, NULL, NULL, '1', '1', '0', NULL, 2, '01', 2, '0', '1', NULL, 'sysmg', NULL, 0, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (5, 1, '工作台示例', NULL, '1/5', '银海软件/工作台示例', 'icon-010?#E7B346', NULL, NULL, NULL, '1', '2', '1', NULL, 3, '01', 2, '0', '2', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (6, 2, '组织人员管理', 'org/orgUserMgAction.do', '1/2/6', '银海软件/权限管理/组织人员管理', 'icon-029?#88BBF1', NULL, NULL, NULL, '1', '1', '1', NULL, 0, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (7, 2, '管理员权限管理', 'org/admin/adminUserMgAction.do', '1/2/7', '银海软件/权限管理/管理员权限管理', 'icon-011?#33475F', NULL, NULL, NULL, '1', '1', '1', NULL, 1, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (8, 2, '经办用户权限管理', 'org/position/positionUserMgAction.do', '1/2/8', '银海软件/权限管理/经办用户权限管理', 'icon-023?#B59BC9', NULL, NULL, NULL, '1', '1', '1', NULL, 2, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (9, 2, '相似权限授权', 'org/position/similarAuthorityAction.do', '1/2/9', '银海软件/权限管理/相似权限授权', 'icon-014?#936BB2', NULL, NULL, NULL, '1', '1', '1', NULL, 3, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (10, 2, '功能权限代理', 'org/position/delegatePositionAction.do', '1/2/10', '银海软件/权限管理/功能权限代理', 'icon-057?#56ABE4', NULL, NULL, NULL, '1', '1', '1', NULL, 4, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (11, 3, '菜单管理', 'sysapp/menuMgAction.do', '1/3/11', '银海软件/开发管理/菜单管理', 'icon-065?#E5937C', NULL, NULL, NULL, '1', '1', '1', NULL, 0, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (12, 3, '码表管理', 'appCodeTable/appCodeMainController.do', '1/3/12', '银海软件/开发管理/码表管理', 'icon-026?#B4D984', NULL, NULL, NULL, '1', '1', '1', NULL, 1, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (14, 3, '批量导入组织人员', 'org/upload/uploadOrgUserAction.do', '1/3/14', '银海软件/开发管理/批量导入组织人员', 'icon-035?#97CC52', NULL, NULL, NULL, '1', '1', '1', NULL, 3, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (15, 4, 'oracle定时服务', 'scheduler/oracleJobAction.do', '1/4/15', '银海软件/系统管理/oracle定时服务', 'icon-060?#00BB9C', NULL, NULL, NULL, '1', '1', '1', NULL, 0, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (16, 4, '润乾报表管理', NULL, '1/4/16', '银海软件/系统管理/润乾报表管理', 'icon-053?#deaf49', NULL, NULL, NULL, '1', '1', '1', NULL, 1, '01', 3, '0', '1', NULL, 'sysmg', NULL, NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (17, 4, '系统路径管理', 'sysapp/configSysPathAction.do', '1/4/17', '银海软件/系统管理/系统路径管理', 'icon-028?#B196C7', NULL, NULL, NULL, '1', '1', '1', NULL, 2, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (18, 4, '系统异常日志', 'sysapp/syslogmg/serverExceptionLog/serverExceptionLogController.do', '1/4/18', '银海软件/系统管理/系统异常日志', 'icon-067?#E1785A', NULL, NULL, NULL, '1', '1', '1', NULL, 3, '01', 3, '1', '1', NULL, 'sysmg', NULL, 0, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (19, 16, '润乾报表菜单管理', 'runqian/queryReportMgAction.do', '1/2/16/19', '银海软件/系统管理/润乾报表管理/润乾报表菜单管理', 'icon-065?#deaf49', NULL, NULL, NULL, '1', '1', '1', NULL, 1, '01', 4, '1', '1', NULL, 'sysmg', NULL, NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (20, 16, '润乾报表模板管理', 'runqian/runQianReportFileMgAction.do', '1/2/16/20', '银海软件/系统管理/润乾报表管理/润乾报表模板管理', 'icon-037?#deaf49', NULL, NULL, NULL, '1', '1', '1', NULL, 0, '01', 4, '1', '1', NULL, 'sysmg', NULL, NULL, '0', '0', NULL, '1');
/**********Updata By MinusZero In 2018/08/16 Start *****/
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (21, 5, '待办任务', 'indexSRC/worktable/console/daiban.html', '1/5/21', '银海软件/工作台示例/待办任务', 'icon-006?#E1785A', NULL, NULL, NULL, '1', '2', '1', NULL, 0, '01', 3, '1', '2', NULL, 'sysmg', '1', NULL, '0', '1', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (22, 5, '系统', 'indexSRC/worktable/console/system.html', '1/5/22', '银海软件/工作台示例/系统', 'icon-005?#E1785A', NULL, NULL, NULL, '1', '2', '1', NULL, 1, '01', 3, '1', '2', NULL, 'sysmg', '1', NULL, '0', '1', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (23, 5, '通知', 'indexSRC/worktable/console/tongzhi.html', '1/5/23', '银海软件/工作台示例/通知', 'icon-015?#97CC52', NULL, NULL, NULL, '1', '2', '1', NULL, 2, '01', 3, '1', '2', NULL, 'sysmg', '1', NULL, '0', '1', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (24, 5, '协同管理', 'indexSRC/worktable/console/xietong.html', '1/5/24', '银海软件/工作台示例/协同管理', 'icon-045?#33475F', NULL, NULL, NULL, '1', '2', '1', NULL, 3, '01', 3, '1', '2', NULL, 'sysmg', '1', NULL, '0', '1', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (25, 5, '工作量统计', 'indexSRC/worktable/console/yewuliang.html', '1/5/25', '银海软件/工作台示例/工作量统计', 'icon-048?#42dbd4', NULL, NULL, NULL, '1', '2', '1', NULL, 4, '01', 3, '1', '2', NULL, 'sysmg', '1', NULL, '0', '1', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (26, 5, '仪表盘', 'indexSRC/worktable/console/yibiaopan.html', '1/5/26', '银海软件/工作台示例/仪表盘', 'icon-008?#a397e8', NULL, NULL, NULL, '1', '2', '1', NULL, 5, '01', 3, '1', '2', NULL, 'sysmg', '1', NULL, '0', '1', NULL, '1');
/****End In 2018/08/16**********/
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (27, 2, '经办机构配置', 'org/agencies/agenciesMgAction.do', '1/2/27', '银海软件/权限管理/经办机构配置', 'icon-001?#6A6A6A', NULL, NULL, NULL, '1', '1', '1', NULL, 5, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (28, 2, '字段授权', 'org/field/fieldAuthorMgAction.do', '1/2/28', '银海软件/权限管理/字段授权', 'icon-027?#00BB9C', NULL, NULL, NULL, '1', '1', '1', NULL, 6, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (29, 1, '通知管理', NULL, '1/29', '银海软件/通知管理', 'icon-017?#cf829d', NULL, NULL, NULL, '1', '1', '1', NULL, 4, '01', 2, '0', '2', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (30, 29, '通知发送', 'message/messageSend.do', '1/29/30', '银海软件/通知管理/通知发送', 'icon-071?#e37de3', NULL, NULL, NULL, '1', '1', '1', NULL, 0, '01', 3, '1', '2', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (31, 3, '限流管理', 'limiter/limitRateAction.do', '1/3/31', '银海软件/开发管理/限流管理', 'icon-024?#6f8a0c', NULL, NULL, NULL, '1', '1', '1', NULL, 12, '01', 3, '0', '0', NULL, 'sysmg', '0', NULL, '0', '0', '02', '0');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (32, 29, '通知删除', 'message/messageDelete.do', '1/29/32', '银海软件/通知管理/通知删除', 'icon-039?#db4b89', NULL, NULL, NULL, '1', '1', '1', NULL, 2, '01', 3, '1', '2', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (33, 3, '模板管理', 'templateMg/templateMgController.do', '1/3/33', '银海软件/开发管理/模板管理', 'icon-037?#8ABCF1', NULL, NULL, NULL, '1', '1', '1', NULL, 4, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (34, 4, '登录日志分析', 'security/loginLogAnalysisAction.do', '1/4/110054', '银海软件/系统管理/登录日志分析', 'icon-048?#97CC52', NULL, NULL, NULL, '1', '1', '1', NULL, 4, '01', 3, '0', '1', NULL, 'sysmg', '0', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (35, 4, 'Java定时服务', 'scheduler/schedulerMgAction.do', '1/2/213169', '银海软件/系统管理/Java定时服务', 'icon-051?#4D69B5', NULL, NULL, NULL, '1', '1', '1', NULL, 5, '01', 3, '0', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (36, 4, '配置管理', 'config/configMgController.do', '1/173013/273014', '银海软件/开发管理/配置管理', 'icon-009?#AFD67C', NULL, NULL, NULL, '1', '1', '1', NULL, 0, '01', 3, '0', '1', NULL, 'sysmg', '1', NULL, '0', '1', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (37, 4, '集群server配置管理', 'clusterConfig/clusterConfigController.do', '1/4/37', '银海软件/开发管理/集群server配置管理', 'icon-049?#56ABE4', NULL, NULL, NULL, '1', '1', '1', NULL, 10, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (38, 3, '缓存管理', 'cache/cacheMgAction.do', '1/3/38', '银海软件/开发管理/缓存管理', 'icon-032?#E1785A', NULL, NULL, NULL, '1', '1', '1', NULL, 2, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (100, 29, '我的通知', 'message/messageSearch.do', '1/29/100', '银海软件/通知管理/我的通知', 'icon-052?#86a9eb', NULL, NULL, NULL, '1', '1', '1', NULL, 2, '01', 3, '1', '2', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (101, 4, '接入系统管理', 'sysapp/accessSystemAction.do', '1/4/101', '银海软件/系统管理/接入系统管理', 'icon-024?#94C1F1', NULL, NULL, NULL, '0', '1', '1', NULL, 4, '01', 3, '0', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (280, 2, '超级管理员配置', 'org/developer/developerMg.do', '1/2/280', '银海软件/权限管理/超级管理员配置', 'icon-022?#e82774', NULL, NULL, NULL, '1', '1', '1', NULL, 6, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', NULL, '1');
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE)
VALUES (290,4,'日志输出级别','logLevel/logLevelController.do','1/4/232732','银海软件/系统管理/日志输出级别','icon-036?#00fafa','','','','1','1','1','',9,'01',3,'1','1','','sysmg','0',NULL,'0','0');
/****Add By MinusZero In 2018/08/16****/
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (291, 2, '组织正副职管理', 'org/positionMgAction.do', '1/2/291', '银海软件/权限管理/组织正副职管理', 'icon-011?#09dceb', NULL, NULL, '', '1', '1', '1', NULL, 8, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', '', '1');
/*End By MinusZero In 2018/08/16*/
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


INSERT INTO TAORG (ORGID,PORGID,ORGNAME,COSTOMNO,ORGIDPATH,ORGNAMEPATH,COSTOMNOPATH,ORGTYPE,SORT,YAB003,DIMENSION,CREATEUSER,CREATETIME,EFFECTIVE,ORGLEVEL,ISLEAF,TYPEFLAG,YAB139) VALUES (1,1,'银海软件','1','1','银海软件','0','01',0,'9999',NULL,0,sysdate,'1',0,'1',0,'9999');

INSERT INTO TAUSER (USERID,NAME,SEX,LOGINID,PASSWORD,PASSWORDFAULTNUM,PWDLASTMODIFYDATE,ISLOCK,SORT,EFFECTIVE,TEL,CREATEUSER,CREATETIME,DIRECTORGID,TYPEFLAG,AUTHMODE) VALUES (1,'超级管理员','1','developer','29PYtt0CYAXxrlJgzd/HUg==',0,sysdate,'0',0,'1','0',1,sysdate,1,0,'01');

INSERT INTO TAPOSITION (POSITIONID,ORGID,POSITIONNAME,POSITIONTYPE,CREATEPOSITIONID,ORGIDPATH,ORGNAMEPATH,VALIDTIME,CREATEUSER,CREATETIME,EFFECTIVE,ISADMIN,TYPEFLAG,ISDEVELOPER) VALUES (1,1,'超级管理员','2',1,'1','银海软件',NULL,1,sysdate,'1','1',0,1);

INSERT INTO TAUSERPOSITION (USERID,POSITIONID,MAINPOSITION,CREATEUSER,CREATETIME) VALUES (1,1,'1',1,sysdate);

INSERT INTO TALOCALCACHEVERSION(VERSION, CODETYPE) VALUES (1,'');

INSERT INTO TAYAB003LEVELMG (PYAB003, YAB003) VALUES ('0', '9999');

ALTER TABLE TAORGMG ADD CONSTRAINT FK_REFERENCE_8 FOREIGN KEY (ORGID)
      REFERENCES TAORG (ORGID);

ALTER TABLE TAORGMG ADD CONSTRAINT FK_REFERENCE_9 FOREIGN KEY (POSITIONID)
      REFERENCES TAPOSITION (POSITIONID) ;

ALTER TABLE TAORG ADD CONSTRAINT FK_REFERENCE_6 FOREIGN KEY (PORGID)
      REFERENCES TAORG (ORGID) ;

ALTER TABLE TAPOSITION ADD CONSTRAINT FK_RELATIONSHIP_5 FOREIGN KEY (ORGID)
      REFERENCES TAORG (ORGID) ;

ALTER TABLE TAPOSITIONAUTHRITY ADD CONSTRAINT FK_REFERENCE_7 FOREIGN KEY (MENUID)
      REFERENCES TAMENU (MENUID) ;

ALTER TABLE TAPOSITIONAUTHRITY ADD CONSTRAINT FK_RELATIONSHIP_11 FOREIGN KEY (POSITIONID)
      REFERENCES TAPOSITION (POSITIONID) ;

ALTER TABLE TAUSERPOSITION ADD CONSTRAINT FK_RELATIONSHIP_10 FOREIGN KEY (POSITIONID)
      REFERENCES TAPOSITION (POSITIONID) ;

ALTER TABLE TAUSERPOSITION ADD CONSTRAINT FK_RELATIONSHIP_9 FOREIGN KEY (USERID)
      REFERENCES TAUSER (USERID);

CREATE OR REPLACE view AA10A1(
      AAA100 ,
      AAA101 ,
      AAA102 ,
      AAA103 ,
      YAB003 ,
      AAE120 ,
      ver) AS
select
   AAA100,
   AAA101,
   AAA102,
   AAA103,
   YAB003,
   AAE120,
   VER
from
   AA10;

CREATE OR REPLACE VIEW V_YAB139 AS
SELECT
       U.USERID AS USERID,
       M.MENUID AS MENUID,
       DAD.DIMENSIONPERMISSIONID AS YAB139,
       DAD.SYSPATH AS SYSPATH
FROM
       TAMENU M ,
       TADATAACCESSDIMENSION DAD,
       TAUSER U,
       TAPOSITION P,
       TAUSERPOSITION UP
WHERE 1=1
      AND U.USERID=UP.USERID
      AND (U.DESTORY IS NULL OR U.DESTORY=0)
      AND U.EFFECTIVE=1
      AND UP.POSITIONID=P.POSITIONID
      AND P.EFFECTIVE=1
      AND (P.VALIDTIME IS NULL OR P.VALIDTIME>=sysdate)
      AND P.POSITIONID=DAD.POSITIONID
      AND DAD.MENUID=M.MENUID
      AND DAD.DIMENSIONTYPE='YAB139'
      AND DAD.ALLACCESS=0
UNION
SELECT
       U.USERID AS USERID,
       M.MENUID AS MENUID,
       A.AAA102 AS YAB139,
       DAD.SYSPATH AS SYSPATH
FROM
       TAMENU M ,
       TADATAACCESSDIMENSION DAD,
       TAUSER U,
       TAPOSITION P,
       TAUSERPOSITION UP,
       AA10A1 A
WHERE 1=1
      AND U.USERID=UP.USERID
      AND (U.DESTORY IS NULL OR U.DESTORY=0)
      AND U.EFFECTIVE=1
      AND UP.POSITIONID=P.POSITIONID
      AND P.EFFECTIVE=1
      AND (P.VALIDTIME IS NULL OR P.VALIDTIME>=sysdate)
      AND P.POSITIONID=DAD.POSITIONID
      AND DAD.MENUID=M.MENUID
      AND DAD.DIMENSIONTYPE='YAB139'
      AND DAD.ALLACCESS<>0
      AND A.AAA100='YAB139';

delete from qrtz_fired_triggers;
delete from qrtz_simple_triggers;
delete from qrtz_simprop_triggers;
delete from qrtz_cron_triggers;
delete from qrtz_blob_triggers;
delete from qrtz_triggers;
delete from qrtz_job_details;
delete from qrtz_calendars;
delete from qrtz_paused_trigger_grps;
delete from qrtz_locks;
delete from qrtz_scheduler_state;
delete from QRTZ_JOB_LOG;

drop table qrtz_calendars;
drop table qrtz_fired_triggers;
drop table qrtz_blob_triggers;
drop table qrtz_cron_triggers;
drop table qrtz_simple_triggers;
drop table qrtz_simprop_triggers;
drop table qrtz_triggers;
drop table qrtz_job_details;
drop table qrtz_paused_trigger_grps;
drop table qrtz_locks;
drop table qrtz_scheduler_state;
drop table QRTZ_JOB_LOG;

CREATE TABLE qrtz_job_details
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    JOB_NAME  VARCHAR2(200) NOT NULL,
    JOB_GROUP VARCHAR2(200) NOT NULL,
    DESCRIPTION VARCHAR2(250) NULL,
    JOB_CLASS_NAME   VARCHAR2(250) NOT NULL,
    IS_DURABLE VARCHAR2(1) NOT NULL,
    IS_NONCONCURRENT VARCHAR2(1) NOT NULL,
    IS_UPDATE_DATA VARCHAR2(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR2(1) NOT NULL,
    JOB_DATA BLOB NULL,
    CONSTRAINT QRTZ_JOB_DETAILS_PK PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
);
CREATE TABLE qrtz_triggers
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    JOB_NAME  VARCHAR2(200) NOT NULL,
    JOB_GROUP VARCHAR2(200) NOT NULL,
    DESCRIPTION VARCHAR2(250) NULL,
    NEXT_FIRE_TIME NUMBER(13) NULL,
    PREV_FIRE_TIME NUMBER(13) NULL,
    PRIORITY NUMBER(13) NULL,
    TRIGGER_STATE VARCHAR2(16) NOT NULL,
    TRIGGER_TYPE VARCHAR2(8) NOT NULL,
    START_TIME NUMBER(13) NOT NULL,
    END_TIME NUMBER(13) NULL,
    CALENDAR_NAME VARCHAR2(200) NULL,
    MISFIRE_INSTR NUMBER(2) NULL,
    JOB_DATA BLOB NULL,
    CONSTRAINT QRTZ_TRIGGERS_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_TRIGGER_TO_JOBS_FK FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
      REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
);
CREATE TABLE qrtz_simple_triggers
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    REPEAT_COUNT NUMBER(7) NOT NULL,
    REPEAT_INTERVAL NUMBER(12) NOT NULL,
    TIMES_TRIGGERED NUMBER(10) NOT NULL,
    CONSTRAINT QRTZ_SIMPLE_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_SIMPLE_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
	REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_cron_triggers
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    CRON_EXPRESSION VARCHAR2(120) NOT NULL,
    TIME_ZONE_ID VARCHAR2(80),
    CONSTRAINT QRTZ_CRON_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_CRON_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
      REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_simprop_triggers
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    STR_PROP_1 VARCHAR2(512) NULL,
    STR_PROP_2 VARCHAR2(512) NULL,
    STR_PROP_3 VARCHAR2(512) NULL,
    INT_PROP_1 NUMBER(10) NULL,
    INT_PROP_2 NUMBER(10) NULL,
    LONG_PROP_1 NUMBER(13) NULL,
    LONG_PROP_2 NUMBER(13) NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR2(1) NULL,
    BOOL_PROP_2 VARCHAR2(1) NULL,
    CONSTRAINT QRTZ_SIMPROP_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_SIMPROP_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
      REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_blob_triggers
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    BLOB_DATA BLOB NULL,
    CONSTRAINT QRTZ_BLOB_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_BLOB_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_calendars
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    CALENDAR_NAME  VARCHAR2(200) NOT NULL,
    CALENDAR BLOB NOT NULL,
    CONSTRAINT QRTZ_CALENDARS_PK PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
);
CREATE TABLE qrtz_paused_trigger_grps
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_GROUP  VARCHAR2(200) NOT NULL,
    CONSTRAINT QRTZ_PAUSED_TRIG_GRPS_PK PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
);
CREATE TABLE qrtz_fired_triggers
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    ENTRY_ID VARCHAR2(95) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    INSTANCE_NAME VARCHAR2(200) NOT NULL,
    FIRED_TIME NUMBER(13) NOT NULL,
    SCHED_TIME NUMBER(13) NOT NULL,
    PRIORITY NUMBER(13) NOT NULL,
    STATE VARCHAR2(16) NOT NULL,
    JOB_NAME VARCHAR2(200) NULL,
    JOB_GROUP VARCHAR2(200) NULL,
    IS_NONCONCURRENT VARCHAR2(1) NULL,
    REQUESTS_RECOVERY VARCHAR2(1) NULL,
    CONSTRAINT QRTZ_FIRED_TRIGGER_PK PRIMARY KEY (SCHED_NAME,ENTRY_ID)
);
CREATE TABLE qrtz_scheduler_state
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    INSTANCE_NAME VARCHAR2(200) NOT NULL,
    LAST_CHECKIN_TIME NUMBER(13) NOT NULL,
    CHECKIN_INTERVAL NUMBER(13) NOT NULL,
    CONSTRAINT QRTZ_SCHEDULER_STATE_PK PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);
CREATE TABLE qrtz_locks
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    LOCK_NAME  VARCHAR2(40) NOT NULL,
    CONSTRAINT QRTZ_LOCKS_PK PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);

create table QRTZ_JOB_LOG
(
  LOG_ID     VARCHAR2(17) not NULL,
  JOB_NAME   VARCHAR2(200) not NULL,
  ADDRESS    VARCHAR2(200) not NULL,
  SERVICE_ID VARCHAR2(200) not NULL,
  FIRED_TIME DATE not NULL,
  SUCCESS    VARCHAR2(6) not NULL,
  LOG_MSG    VARCHAR2(255),
  constraint QRTZ_JOB_LOG_PK primary key (LOG_ID)
);
comment on table QRTZ_JOB_LOG
  is 'Quartz定时任务执行日志';
comment on column QRTZ_JOB_LOG.LOG_ID
  is '日志序列号';
comment on column QRTZ_JOB_LOG.JOB_NAME
  is '任务名称';
comment on column QRTZ_JOB_LOG.ADDRESS
  is '服务器地址';
comment on column QRTZ_JOB_LOG.SERVICE_ID
  is '服务ID';
comment on column QRTZ_JOB_LOG.FIRED_TIME
  is '发生时间';
comment on column QRTZ_JOB_LOG.SUCCESS
  is '成功标志';
comment on column QRTZ_JOB_LOG.LOG_MSG
  is '日志消息';

create index idx_qrtz_j_req_recovery on qrtz_job_details(SCHED_NAME,REQUESTS_RECOVERY);
create index idx_qrtz_j_grp on qrtz_job_details(SCHED_NAME,JOB_GROUP);

create index idx_qrtz_t_j on qrtz_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
create index idx_qrtz_t_jg on qrtz_triggers(SCHED_NAME,JOB_GROUP);
create index idx_qrtz_t_c on qrtz_triggers(SCHED_NAME,CALENDAR_NAME);
create index idx_qrtz_t_g on qrtz_triggers(SCHED_NAME,TRIGGER_GROUP);
create index idx_qrtz_t_state on qrtz_triggers(SCHED_NAME,TRIGGER_STATE);
create index idx_qrtz_t_n_state on qrtz_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
create index idx_qrtz_t_n_g_state on qrtz_triggers(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
create index idx_qrtz_t_next_fire_time on qrtz_triggers(SCHED_NAME,NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_st on qrtz_triggers(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_misfire on qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_st_misfire on qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
create index idx_qrtz_t_nft_st_misfire_grp on qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

create index idx_qrtz_ft_trig_inst_name on qrtz_fired_triggers(SCHED_NAME,INSTANCE_NAME);
create index idx_qrtz_ft_inst_job_req_rcvry on qrtz_fired_triggers(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
create index idx_qrtz_ft_j_g on qrtz_fired_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
create index idx_qrtz_ft_jg on qrtz_fired_triggers(SCHED_NAME,JOB_GROUP);
create index idx_qrtz_ft_t_g on qrtz_fired_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
create index idx_qrtz_ft_tg on qrtz_fired_triggers(SCHED_NAME,TRIGGER_GROUP);
--create ziduan shouquan
create table TAFIELD
(
  id         NUMBER(10) not NULL,
  menuid     NUMBER(10),
  fieldid    VARCHAR2(100),
  fieldname  VARCHAR2(100),
  tableid    VARCHAR2(10),
  pid        NUMBER(10),
  fieldlevel NUMBER
);
comment on column TAFIELD.id
  is '主键';
comment on column TAFIELD.menuid
  is '菜单ID';
comment on column TAFIELD.fieldid
  is '字段id';
comment on column TAFIELD.fieldname
  is '字段名称';
comment on column TAFIELD.tableid
  is '表id(备用)';
comment on column TAFIELD.pid
  is '父节点Id';
comment on column TAFIELD.fieldlevel
  is '字段层级';
create table TAFIELDAUTHRITY
(
  positionid NUMBER(10) not NULL,
  menuid     NUMBER(10) not NULL,
  fieldid    VARCHAR2(100) not NULL,
  look       CHAR(1),
  edit       CHAR(1),
  createtime DATE,
  createuser NUMBER(10)
);
comment on column TAFIELDAUTHRITY.positionid
  is '岗位id';
comment on column TAFIELDAUTHRITY.menuid
  is '菜单id';
comment on column TAFIELDAUTHRITY.fieldid
  is '字段id';
comment on column TAFIELDAUTHRITY.look
  is '是否可查看（1：可查看，0不可查看）';
comment on column TAFIELDAUTHRITY.edit
  is '是否可编辑（1：可编辑，0，不可查看）';
comment on column TAFIELDAUTHRITY.createtime
  is '创建时间';
comment on column TAFIELDAUTHRITY.createuser
  is '创建人';
-- create message table
create table TAMESSAGE
(
  userid  NUMBER(10),
  orgid   NUMBER(10),
  sign    VARCHAR2(6) default 0,
  mgid    VARCHAR2(20) not NULL,
  mgdate  TIMESTAMP(6),
  title   VARCHAR2(100),
  content CLOB,
  mgcheck VARCHAR2(1) default 0
);
comment on column TAMESSAGE.userid
  is '发送人员id';
comment on column TAMESSAGE.orgid
  is '接收组织';
comment on column TAMESSAGE.sign
  is '指定岗位标志';
comment on column TAMESSAGE.mgid
  is '消息id';
comment on column TAMESSAGE.mgdate
  is '发送日期';
comment on column TAMESSAGE.title
  is '通知标题';
comment on column TAMESSAGE.content
  is '通知内容';
comment on column TAMESSAGE.mgcheck
  is '0未审核/a通过/b不通过';
alter table TAMESSAGE
  add constraint ID primary key (MGID, ORGID)
  using index;


create table TAMESSAGEATTACHMENT
(
  mgid       VARCHAR2(20) not NULL,
  attachment BLOB,
  name       VARCHAR2(50)
);
comment on column TAMESSAGEATTACHMENT.mgid
  is '消息id';
comment on column TAMESSAGEATTACHMENT.attachment
  is '通知附件';
comment on column TAMESSAGEATTACHMENT.name
  is '附件名称';
create table TAZKADDRESSMG
(
  zkid            VARCHAR2(20) not NULL,
  zkaddress       VARCHAR2(200),
  appname         VARCHAR2(60),
  appnamespace    VARCHAR2(30),
  CONSTRAINT PK_TAZKADDRESSMG PRIMARY KEY (zkid)
);
comment on column TAZKADDRESSMG.zkid
  is 'zk注册中心id';
comment on column TAZKADDRESSMG.zkaddress
  is 'zk地址';
comment on column TAZKADDRESSMG.appname
  is '应用名称';
comment on column TAZKADDRESSMG.appnamespace
  is 'zk命名空间';

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

create table TAMESSAGESTATE
(
  mgid   VARCHAR2(20) not NULL,
  userid NUMBER(10) not NULL,
  state  VARCHAR2(6) default 0
);
comment on column TAMESSAGESTATE.mgid
  is '消息id';
comment on column TAMESSAGESTATE.userid
  is '接收人员';
comment on column TAMESSAGESTATE.state
  is '已读标志';
-- create template table
create table TEMPLATEMANAGER
(
  templateid      VARCHAR2(50) not NULL,
  templatename    VARCHAR2(50) not NULL,
  templatecontent BLOB,
  templatetype    VARCHAR2(10),
  createpeople    NUMBER(10),
  createtime      DATE,
  effective       CHAR(1),
  content         VARCHAR2(20)
);

create sequence SEQ_MESSAGE
minvalue 1
maxvalue 999999999999999999999999999
start with 201
increment by 1
cache 10;

create sequence TEMPLATE_SEQUENCE
minvalue 1
maxvalue 9999999999999999999999999999
start with 61
increment by 1
cache 20;

create table SIGNRECORD
(
  userid    NUMBER(10),
  signtime  DATE,
  signstate VARCHAR2(2),
  ip        VARCHAR2(20),
  mac       VARCHAR2(20),
  signid    NUMBER(20) not NULL
);
-- Add comments to the table
comment on table SIGNRECORD
  is '签到签退记录表';
-- Add comments to the columns
comment on column SIGNRECORD.userid
  is '人员ID';
comment on column SIGNRECORD.signtime
  is '签到时间';
comment on column SIGNRECORD.signstate
  is '签到状态(1 签到 2 签退)';
comment on column SIGNRECORD.ip
  is 'IP 地址';
comment on column SIGNRECORD.mac
  is 'MAC地址';
comment on column SIGNRECORD.signid
  is '流水号';

drop table taaccesssystem;

CREATE TABLE taaccesssystem (
  SYSID number(11) primary key,
  SYSTEMKEY varchar2(40) NOT NULL,
  SYSTEMNAME varchar2(100) NOT NULL ,
  PRIVATEKEY blob ,
  PUBLICKEY blob
);
comment on table taaccesssystem  is '接入系统管理';
comment on column taaccesssystem.sysid is '接入系统ID';
comment on column taaccesssystem.systemkey is '接入标识';
comment on column taaccesssystem.systemname is '系统名称';
comment on column taaccesssystem.privatekey is '私钥';
comment on column taaccesssystem.publickey is '公钥';
alter table taaccesssystem add constraint UK_TAACCESSSYSTEM unique (SYSTEMKEY);

drop table TAPAGEREVIEW;
create table TAPAGEREVIEW
(
  page_id     NUMBER(20) not NULL,
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

-- ta3cloud修改

-- Create table

drop table TAONLINESTATLOG;
create table TAONLINESTATLOG
(
  statdate    VARCHAR2(20) not NULL,
  pointintime VARCHAR2(20) not NULL,
  loginnum    NUMBER(15) not NULL
);
-- Add comments to the table
comment on table TAONLINESTATLOG
  is '时点登录情况统计表';
-- Add comments to the columns
comment on column TAONLINESTATLOG.statdate
  is '统计日期';
comment on column TAONLINESTATLOG.pointintime
  is '统计时间点';
comment on column TAONLINESTATLOG.loginnum
  is '在线人数';
-- Create/Recreate primary, unique and foreign key constraints
alter table TAONLINESTATLOG
  add constraint TAONLINESTATLOG primary key (STATDATE, POINTINTIME)
  using index;

drop table TALIMITRATE;
-- Create table
create table TALIMITRATE
(
  menuid    NUMBER(10) not NULL,
  limitopen CHAR(1) not NULL,
  rate      NUMBER(16,2) not NULL,
  timeout   NUMBER(38) not NULL,
  maxcount  NUMBER(10) not NULL,
  url       VARCHAR2(100) not NULL
);
-- Add comments to the table 
comment on table TALIMITRATE
  is '访问限流';
-- Add comments to the columns 
comment on column TALIMITRATE.menuid
  is '菜单id';
comment on column TALIMITRATE.limitopen
  is '是否开启限流';
comment on column TALIMITRATE.rate
  is '允许的访问频率';
comment on column TALIMITRATE.timeout
  is '获取访问许可的超时时间';
comment on column TALIMITRATE.maxcount
  is '允许访问的最大并发量';
comment on column TALIMITRATE.url
  is '访问路径';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TALIMITRATE
  add constraint PK_TALIMITRATE primary key (url)
  using index ;


-- Add/modify columns 
alter table TAONLINELOG add clientsystem VARCHAR2(50);
alter table TAONLINELOG add clientbrowser VARCHAR2(50);
alter table TAONLINELOG add clientscreensize VARCHAR2(50);

-- Add/modify columns 
alter table TALOGINHISTORYLOG add clientsystem VARCHAR2(50);
alter table TALOGINHISTORYLOG add clientbrowser VARCHAR2(50);
alter table TALOGINHISTORYLOG add clientscreensize VARCHAR2(50);

alter table YHCIP_ORACLE_JOBS modify what VARCHAR2(4000);
alter table QRTZ_JOB_MSGS rename column job_name to JOBID;

-- Add VIEW
create or replace view v_oraclejobs as
select
        a.jobid as jobid,
        to_char(a.submittime,'yyyy-MM-dd HH24:mi:ss') as submittime,
        a.jobname as jobname,
        a.oraclejobid as oraclejobid,
        a.userid as userid,
        to_char(b.last_date,'yyyy-MM-dd HH24:mi:ss') as last_date,
        to_char(b.next_date,'yyyy-MM-dd HH24:mi:ss') as next_date,
        a.starttime as starttime,
        b.total_time as total_time,
        decode(b.broken,'Y','暂停','N','运行中','已删除')  as broken,
        decode(b.broken,'Y',b.interval,'N',b.interval,a.interval) as interval,
        b.failures as failures,
        decode(b.broken,'Y',b.what,'N',b.what,a.what) as what
      from yhcip_oracle_jobs a,user_jobs b
      where a.oraclejobid = b.job(+)
      order by b.broken,a.oraclejobid ,b.next_date;
comment on table V_ORACLEJOBS is 'ORACLE定时任务';

CREATE OR REPLACE VIEW V_TALOGINLOG AS
SELECT
  a.logid,
  a.userid,
  (SELECT name FROM tauser where userid = a.userid) as username,
  a.logintime,
  a.clientip,
  a.sessionid,
  a.syspath,
  a.serverip,
  NVL(a.clientsystem,'UNKNOW') as clientsystem,
  NVL(a.clientbrowser,'UNKNOW') as clientbrowser,
  NVL(a.clientscreensize,'UNKNOW') as clientscreensize
FROM TAONLINELOG a
Union all
SELECT
  a.logid,
  a.userid,
  (SELECT name FROM tauser where userid = a.userid) as username,
  a.logintime,
  a.clientip,
  a.sessionid,
  a.syspath,
  a.serverip,
  NVL(a.clientsystem,'UNKNOW') as clientsystem,
  NVL(a.clientbrowser,'UNKNOW') as clientbrowser,
  NVL(a.clientscreensize,'UNKNOW') as clientscreensize
FROM TALOGINHISTORYLOG a;
comment on table V_TALOGINLOG is '用户登录日志';

-- UPDATE 存储过程
CREATE OR REPLACE PACKAGE pkg_YHCIP AS
   /*---------------------------------------------------------------------------
   ||  程序包名 ：pkg_YHCIP                                                   
   ||  业务环节 ：YHCIP                                                       
   ||  对象列表 ：私有过程函数                                                
   ||             --------------------------------------------------------------
   ||             公用过程函数                                                
   ||             --------------------------------------------------------------
   ||                                                                         
   ||  其它说明 ：                                                            
   ||  完成日期 ：                                                            
   ||  版本编号 ：Ver 1.0                                                     
   ||  审 查 人 ：×××                      审查日期 ：YYYY-MM-DD           
   ||-------------------------------------------------------------------------*/

   /*-------------------------------------------------------------------------*/
   /* 公用全局数据类型声明                                                    */
   /*-------------------------------------------------------------------------*/

   /*-------------------------------------------------------------------------*/
   /* 公用全局常量声明                                                        */
   /*-------------------------------------------------------------------------*/
   gn_def_OK  CONSTANT VARCHAR2(12) := 'NOERROR'; -- 成功
   gn_def_ERR CONSTANT VARCHAR2(12) := '9999'; -- 系统错误
   
   GN_DEF_YES CONSTANT VARCHAR2(12) := '是'; -- 是
   GN_DEF_NO  CONSTANT VARCHAR2(12) := '否'; -- 否

   gs_def_DatetimeFormat  CONSTANT VARCHAR2(21) := 'YYYY-MM-DD HH24:MI:SS';
   gs_def_DateFormat      CONSTANT VARCHAR2(10) := 'YYYY-MM-DD';
   gs_def_YearMonthFormat CONSTANT VARCHAR2(6) := 'YYYYMM';
   gs_def_YearFormat      CONSTANT VARCHAR2(4) := 'YYYY';
   gs_def_TimeFormat      CONSTANT VARCHAR2(10) := 'HH24:MI:SS';
   gs_def_NumberFormat    CONSTANT VARCHAR2(15) := '999999999999.99';
   gs_def_NOFormat        CONSTANT VARCHAR2(15) := '999999999999999';
   gs_def_NullDate        CONSTANT DATE := TO_DATE('1900-01-01',
                                                   gs_def_DateFormat);

   /*-------------------------------------------------------------------------*/
   /* 公用过程函数声明                                                        */
   /*-------------------------------------------------------------------------*/

   /*模版*/
   PROCEDURE prc_Template (
      prm_AppCode                         OUT      VARCHAR2          ,
      prm_ErrorMsg                        OUT      VARCHAR2          );
   

   PROCEDURE prc_oracleJob (
			      prm_jobid                          IN OUT VARCHAR2      ,--jobid外部传进来的是用于YHCIP_ORACLE_JOBS表的主键，出去的是oracle生成的jobid
			      prm_jobname			                   IN     VARCHAR2      ,--任务名称
			      prm_what                           IN     VARCHAR2      ,--执行过程，需要“;”分号结尾
			      prm_next_date                      IN     VARCHAR2      ,--执行时间
			      prm_interval                       IN     VARCHAR2      ,--间隔循环时间
			      prm_userid                         IN     VARCHAR2      ,--用户id
			      prm_AppCode                        OUT    VARCHAR2      ,
			      prm_ErrorMsg                       OUT    VARCHAR2      );     
   PROCEDURE prc_oraclejobbroken(
			      prm_jobid                          IN     VARCHAR2      ,--jobid外部传进来的是用于YHCIP_ORACLE_JOBS表的主键，出去的是oracle生成的jobid
			      prm_broken                         IN     VARCHAR2      ,--是否暂停
			      prm_next_date                      IN     VARCHAR2      ,--下次执行时间
			      prm_AppCode                        OUT    VARCHAR2      ,
			      prm_ErrorMsg                       OUT    VARCHAR2      );
   PROCEDURE prc_oraclejobchange (
			      prm_jobid                          IN OUT VARCHAR2      ,--jobid外部传进来的是用于YHCIP_ORACLE_JOBS表的主键，出去的是oracle生成的jobid 
            prm_jobname                        IN     VARCHAR2      ,--任务名称                                                                 
            prm_what                           IN     VARCHAR2      ,--执行过程，需要“;”分号结尾                                              
            prm_next_date                      IN     VARCHAR2      ,--执行时间                                                                 
            prm_interval                       IN     VARCHAR2      ,--间隔循环时间                                                             
            prm_userid                         IN     VARCHAR2      ,--用户id                                                                   
            prm_AppCode                        OUT    VARCHAR2      ,                                                                           
            prm_ErrorMsg                       OUT    VARCHAR2      )  ;       
   PROCEDURE prc_removeJob (
      prm_jobid                          IN       VARCHAR2          ,
      prm_AppCode                        OUT      VARCHAR2          ,
      prm_ErrorMsg                       OUT      VARCHAR2          ) ;                                  

END pkg_YHCIP;
/

show error;

CREATE OR REPLACE PACKAGE BODY pkg_YHCIP AS
   /*---------------------------------------------------------------------------
   ||  程序包名 ：pkg_YHCIP                                                   
   ||  业务环节 ：YHCIP                                                       
   ||  对象列表 ：私有过程函数                                                
   ||             --------------------------------------------------------------
   ||             公用过程函数                                                
   ||             --------------------------------------------------------------
   ||                                                                         
   ||  其它说明 ：                                                            
   ||  完成日期 ：                                                            
   ||  版本编号 ：Ver 1.0                                                     
   ||  审 查 人 ：×××                      审查日期 ：YYYY-MM-DD           
   ||-------------------------------------------------------------------------*/
   /*------------------------------------------------------------------------*/
   /* 私有全局数据类型声明                                                   */
   /*------------------------------------------------------------------------*/

   /*------------------------------------------------------------------------*/
   /* 私有全局常量声明                                                       */
   /*------------------------------------------------------------------------*/
   PRE_ERRCODE  CONSTANT VARCHAR2(12) := 'YHCIP'; -- 本包的错误编号前缀
   NULL_PREFIX  CONSTANT VARCHAR2(1) := ' ';

   /*------------------------------------------------------------------------*/
   /* 私有全局变量声明                                                       */
   /*------------------------------------------------------------------------*/

   /*------------------------------------------------------------------------*/
   /* 私有过程函数声明                                                       */
   /*------------------------------------------------------------------------*/

   /*------------------------------------------------------------------------*/
   /* 公用过程函数描述                                                       */
   /*------------------------------------------------------------------------*/

   /*---------------------------------------------------------------------------
   || 业务环节 ：prc_Template
   || 过程名称 ：
   || 功能描述 ：
   || 使用场合 ：
   || 参数描述 ：标识                  名称             输入输出   数据类型
   ||            ---------------------------------------------------------------
   ||            prm_AppCode        执行代码             输出     VARCHAR2(12)
   ||            prm_ErrorMsg       出错信息             输出     VARCHAR2(128)
   ||
   || 参数说明 ：标识               详细说明
   ||            ---------------------------------------------------------------
   ||
   || 其它说明 ：
   || 作    者 ：
   || 完成日期 ：
   ||---------------------------------------------------------------------------
   ||                                 修改记录
   ||---------------------------------------------------------------------------
   || 修 改 人 ：×××                        修改日期 ：YYYY-MM-DD
   || 修改描述 ：
   ||-------------------------------------------------------------------------*/
   PROCEDURE prc_Template (
      prm_AppCode                        OUT      VARCHAR2          ,
      prm_ErrorMsg                       OUT      VARCHAR2          )
   IS
      /*变量声明*/
      /*游标声明*/
   BEGIN
      /*初始化变量*/
      prm_AppCode  := PRE_ERRCODE || gn_def_ERR;
      prm_ErrorMsg := ''                                ;

      /*成功处理*/
      <<label_OK>>
      /*关闭打开的游标*/
      /*给返回参数赋值*/
      prm_AppCode  := gn_def_OK ;
      prm_ErrorMsg := ''                 ;
      RETURN ;

      /*处理失败*/
      <<label_ERROR>>
      /*关闭打开的游标*/
      /*给返回参数赋值*/
      IF prm_AppCode = gn_def_OK THEN
         prm_AppCode  := PRE_ERRCODE || gn_def_ERR;
      END IF ;
      RETURN ;

   EXCEPTION
      -- WHEN NO_DATA_FOUND THEN
      -- WHEN TOO_MANY_ROWS THEN
      -- WHEN DUP_VAL_ON_INDEX THEN
      WHEN OTHERS THEN
         /*关闭打开的游标*/
         prm_AppCode  := PRE_ERRCODE || gn_def_ERR;
         prm_ErrorMsg := '数据库错误:'|| SQLERRM ;
         RETURN;
   END prc_Template ;



  /*---------------------------------------------------------------------------
  || 业务环节 ：创建一个定时服务
  || 过程名称 ：prc_oracleJob
  || 功能描述 ：创建一个定时服务
  ||
  || 使用场合 ：对需要定时执行的过程
  || 参数描述 ：标识                  名称             输入输出   数据类型
  ||            ---------------------------------------------------------------
  ||		prm_jobid          任务编号             输入/输出     VARCHAR2
  ||		prm_jobname	       任务名称		 输入         VARCHAR2 
  ||		prm_what           需要执行的过程        输入         VARCHAR2
  ||		prm_next_date      执行时间              输入         VARCHAR2 
  ||		prm_interval       间隔循环时间          输入         VARCHAR2 
  ||		prm_userid         用户id                输入         VARCHAR2
  ||    prm_AppCode        执行代码              输出         VARCHAR2(12) 
  ||    prm_ErrorMsg       出错信息              输出         VARCHAR2(128)   
  ||
  || 参数说明 ：标识               详细说明
  ||            ---------------------------------------------------------------
  ||
  || 其它说明 ：
  || 作    者 ：林隆永
  || 完成日期 ：
  ||---------------------------------------------------------------------------
  ||                                 修改记录
  ||---------------------------------------------------------------------------
  || 修 改 人 ：×××                        修改日期 ：YYYY-MM-DD
  || 修改描述 ：
  ||-------------------------------------------------------------------------*/
  PROCEDURE prc_oracleJob (
      prm_jobid                          IN OUT VARCHAR2      ,--jobid外部传进来的是用于YHCIP_ORACLE_JOBS表的主键，出去的是oracle生成的jobid
      prm_jobname			                   IN     VARCHAR2      ,--任务名称
      prm_what                           IN     VARCHAR2      ,--执行过程，需要“;”分号结尾
      prm_next_date                      IN     VARCHAR2      ,--执行时间
      prm_interval                       IN     VARCHAR2      ,--间隔循环时间
      prm_userid                         IN     VARCHAR2      ,--用户id
      prm_AppCode                        OUT    VARCHAR2      ,
      prm_ErrorMsg                       OUT    VARCHAR2      )
   IS
      /*变量声明*/
      v_next_date date;
      oraclejobid BINARY_INTEGER;
      sqlStr varchar2(250);
      /*游标声明*/
   BEGIN
      /*给返回参数赋值*/
      prm_AppCode  := gn_def_OK;
      prm_ErrorMsg := '';
      
      BEGIN
          sqlStr := 'select to_date('''||prm_next_date||''',''yyyy-MM-dd HH24:mi:ss'') from dual';
          execute immediate sqlStr into v_next_date;
      EXCEPTION  
         WHEN OTHERS THEN
             sqlStr := 'select '||prm_next_date||' from dual'; 
             execute immediate sqlStr into v_next_date;
      END;
     
     IF prm_interval IS NULL THEN
       dbms_job.submit(oraclejobid,prm_what,v_next_date);
     ELSE
        dbms_job.submit(oraclejobid,prm_what,v_next_date,prm_interval);
     END IF;

      INSERT INTO YHCIP_ORACLE_JOBS(
                       JOBID,--代表一个定时任务
      		             JOBNAME,--定时任务的名称
      		             STARTTIME,--开始执行时间
      		             USERID,--执行的用户
                       ORACLEJOBID,
                       WHAT,
                       INTERVAL)--oracle的jobid
      		     VALUES (
      		             prm_jobid,
      		             prm_jobname,
      		             prm_next_date,
      	               prm_userid,
                       oraclejobid,
                       prm_what,
                       prm_interval);

      INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOBID,
                  JOB_GROUP,
                  USERID,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  SUCCESSMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobid,
                  'DEFAULT',
                  prm_userid,
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '0',
                  'LOG:创建成功');
      commit;
   EXCEPTION
      -- WHEN NO_DATA_FOUND THEN
      -- WHEN TOO_MANY_ROWS THEN
      -- WHEN DUP_VAL_ON_INDEX THEN
      WHEN OTHERS THEN
         /*关闭打开的游标*/
         /* 创建失败记录没有意义
         INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOBID,
                  JOB_GROUP,
                  USERID,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  ERRORMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobid,
                  'DEFAULT',
                  prm_userid,
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '1',
                  'LOG:创建失败');
         commit;       */   
         prm_AppCode  := PRE_ERRCODE||'0006';
         prm_ErrorMsg := '数据库错误:'|| SQLERRM;
         RETURN;
   END prc_oracleJob ;
  /*---------------------------------------------------------------------------
  || 业务环节 ：暂停、继续 任务
  || 过程名称 ：prc_oraclejobbroken
  || 功能描述 ：暂停、继续 任务
  ||
  || 使用场合 ：对任务进行暂停、继续任务
  || 参数描述 ：标识                  名称             输入输出   数据类型
  ||            ---------------------------------------------------------------
  ||		prm_jobid          任务编号             输入/输出     VARCHAR2
  ||		prm_broken	       暂停、继续(true,false)输入         VARCHAR2 
  ||		prm_next_date      执行时间              输入         VARCHAR2 
  ||    prm_AppCode        执行代码              输出         VARCHAR2(12) 
  ||    prm_ErrorMsg       出错信息              输出         VARCHAR2(128)   
  ||
  || 参数说明 ：标识               详细说明
  ||            ---------------------------------------------------------------
  ||
  || 其它说明 ：
  || 作    者 ：林隆永
  || 完成日期 ：
  ||---------------------------------------------------------------------------
  ||                                 修改记录
  ||---------------------------------------------------------------------------
  || 修 改 人 ：×××                        修改日期 ：YYYY-MM-DD
  || 修改描述 ：
  ||-------------------------------------------------------------------------*/  
	PROCEDURE prc_oraclejobbroken(
	      prm_jobid                          IN     VARCHAR2      ,
	      prm_broken                         IN     VARCHAR2      ,
	      prm_next_date                      IN     VARCHAR2      ,
	      prm_AppCode                        OUT    VARCHAR2      ,
	      prm_ErrorMsg                       OUT    VARCHAR2      )
	IS
	      v_next_date date;
        oraclejobid BINARY_INTEGER;
	      sqlStr varchar2(250);
	BEGIN
	      /*给返回参数赋值*/
	  prm_AppCode  := gn_def_OK ;--pkg_COMM.gn_def_OK
	  prm_ErrorMsg := '';
    
    SELECT a.oraclejobid
      INTO oraclejobid
      FROM YHCIP_ORACLE_JOBS a
     WHERE a.jobid = prm_jobid;
    IF oraclejobid IS NULL THEN
        prm_AppCode  := gn_def_ERR;
	      prm_ErrorMsg := '未获取到相应的定时任务信息，请核查！';
        RETURN;
    END IF;

	  IF prm_broken = 'false' OR prm_broken = 'FALSE' THEN
	      
      BEGIN
          sqlStr := 'select to_date('''||prm_next_date||''',''yyyy-MM-dd HH24:mi:ss'') from dual';
          execute immediate sqlStr into v_next_date;
      EXCEPTION  
         WHEN OTHERS THEN
             sqlStr := 'select '||prm_next_date||' from dual'; 
             execute immediate sqlStr into v_next_date;
      END;
    
	    DBMS_JOB.broken(oraclejobid,false,v_next_date);
      
      INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOBID,
                  JOB_GROUP,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  SUCCESSMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobid,
                  'DEFAULT',
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '0',
                  'LOG:继续执行成功');
	  ELSIF prm_broken = 'true' OR prm_broken = 'TRUE' THEN
	     DBMS_JOB.broken(oraclejobid,true);
       INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOBID,
                  JOB_GROUP,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  SUCCESSMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobid,
                  'DEFAULT',
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '0',
                  'LOG:暂停成功');
	  END IF;
	  commit;
	  EXCEPTION
	      -- WHEN NO_DATA_FOUND THEN
	      -- WHEN TOO_MANY_ROWS THEN
	      -- WHEN DUP_VAL_ON_INDEX THEN
	      WHEN OTHERS THEN
	         /*关闭打开的游标*/
           INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOBID,
                  JOB_GROUP,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  ERRORMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobid,
                  'DEFAULT',
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '1',
                  'LOG:暂停或继续执行失败');
           commit;
	         prm_AppCode  := PRE_ERRCODE||'0007';
	         prm_ErrorMsg := '数据库错误:'|| SQLERRM ;
	         RETURN;
	END prc_oraclejobbroken;
  /*---------------------------------------------------------------------------
  || 业务环节 ：更改一个定时服务
  || 过程名称 ：prc_oraclejobchange
  || 功能描述 ：更改一个定时服务
  ||
  || 使用场合 ：更改一个定时服务
  || 参数描述 ：标识                  名称             输入输出   数据类型
  ||            ---------------------------------------------------------------
  ||		prm_jobid          任务编号             输入/输出     VARCHAR2
  ||		prm_jobname	   任务名称		 输入         VARCHAR2 
  ||		prm_what           需要执行的过程        输入         VARCHAR2
  ||		prm_next_date      执行时间              输入         VARCHAR2 
  ||		prm_interval       间隔循环时间          输入         VARCHAR2 
  ||		prm_userid         用户id                输入         VARCHAR2
  ||            prm_AppCode        执行代码              输出         VARCHAR2(12) 
  ||            prm_ErrorMsg       出错信息              输出         VARCHAR2(128)   
  ||
  || 参数说明 ：标识               详细说明
  ||            ---------------------------------------------------------------
  ||
  || 其它说明 ：
  || 作    者 ：林隆永
  || 完成日期 ：
  ||---------------------------------------------------------------------------
  ||                                 修改记录
  ||---------------------------------------------------------------------------
  || 修 改 人 ：×××                        修改日期 ：YYYY-MM-DD
  || 修改描述 ：
  ||-------------------------------------------------------------------------*/	
  PROCEDURE prc_oraclejobchange (
      prm_jobid                          IN OUT VARCHAR2      ,--jobid外部传进来的是用于YHCIP_ORACLE_JOBS表的主键，出去的是oracle生成的jobid
      prm_jobname                        IN     VARCHAR2      ,--任务名称
      prm_what                           IN     VARCHAR2      ,--执行过程，需要“;”分号结尾
      prm_next_date                      IN     VARCHAR2      ,--执行时间
      prm_interval                       IN     VARCHAR2      ,--间隔循环时间
      prm_userid                         IN     VARCHAR2      ,--用户id
      prm_AppCode                        OUT    VARCHAR2      ,
      prm_ErrorMsg                       OUT    VARCHAR2      )
   IS
      /*变量声明*/
      oraclejobid BINARY_INTEGER;
      v_next_date date;
      sqlStr varchar2(250);
      /*游标声明*/
   BEGIN
      /*给返回参数赋值*/
      prm_AppCode  := gn_def_OK;--pkg_COMM.gn_def_OK
      prm_ErrorMsg := '';
      
      SELECT a.oraclejobid
       INTO oraclejobid
       FROM YHCIP_ORACLE_JOBS a
      WHERE a.jobid = prm_jobid;
      
      IF oraclejobid IS NULL THEN
         prm_AppCode  := gn_def_ERR;
	       prm_ErrorMsg := '未获取到相应的定时任务信息，请核查！';
         RETURN;
      END IF;

      BEGIN
          sqlStr := 'select to_date('''||prm_next_date||''',''yyyy-MM-dd HH24:mi:ss'') from dual';
          execute immediate sqlStr into v_next_date;
      EXCEPTION  
         WHEN OTHERS THEN
             sqlStr := 'select '||prm_next_date||' from dual'; 
             execute immediate sqlStr into v_next_date;
      END;

      dbms_job.change(oraclejobid,prm_what,v_next_date,prm_interval);
      
      UPDATE YHCIP_ORACLE_JOBS
          SET userid     = prm_userid   ,
              starttime  = prm_next_date,
              jobname    = prm_jobname  ,
              what       = prm_what     ,
              interval   = prm_interval
          WHERE
              jobid=prm_jobid;
      
      INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOBID,
                  JOB_GROUP,
                  USERID,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  SUCCESSMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobid,
                  'DEFAULT',
                  prm_userid,
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '0',
                  'LOG:更改成功');              
      commit;
   EXCEPTION
      -- WHEN NO_DATA_FOUND THEN
      -- WHEN TOO_MANY_ROWS THEN
      -- WHEN DUP_VAL_ON_INDEX THEN
      WHEN OTHERS THEN
         /*关闭打开的游标*/
  INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOBID,
                  JOB_GROUP,
                  USERID,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  ERRORMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobid,
                  'DEFAULT',
                  prm_userid,
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '1',
                  'LOG:更改失败'); 
   commit; 
	 prm_AppCode  := PRE_ERRCODE||'0008';
	 prm_ErrorMsg := '数据库错误:'|| SQLERRM ;
         RETURN;
   END prc_oraclejobchange ;

/*---------------------------------------------------------------------------
   || 业务环节 ：prc_removeJob
   || 过程名称 ：
   || 功能描述 ：
   || 使用场合 ：
   || 参数描述 ：标识                  名称             输入输出   数据类型
   ||            ---------------------------------------------------------------
   ||		         prm_jobid          任务编号             输入/输出     VARCHAR2
   ||            prm_AppCode        执行代码             输出     VARCHAR2(12)
   ||            prm_ErrorMsg       出错信息             输出     VARCHAR2(128)
   ||
   || 参数说明 ：标识               详细说明
   ||            ---------------------------------------------------------------
   ||
   || 其它说明 ：
   || 作    者 ：
   || 完成日期 ：
   ||---------------------------------------------------------------------------
   ||                                 修改记录
   ||---------------------------------------------------------------------------
   || 修 改 人 ：×××                        修改日期 ：YYYY-MM-DD
   || 修改描述 ：
   ||-------------------------------------------------------------------------*/
   PROCEDURE prc_removeJob (
      prm_jobid                          IN       VARCHAR2          ,
      prm_AppCode                        OUT      VARCHAR2          ,
      prm_ErrorMsg                       OUT      VARCHAR2          )
   IS
      oraclejobid BINARY_INTEGER;
      /*变量声明*/
      /*游标声明*/
   BEGIN
      /*初始化变量*/
      prm_AppCode  := gn_def_OK;
      prm_ErrorMsg := '';  
      
      SELECT a.oraclejobid
        INTO oraclejobid
        FROM YHCIP_ORACLE_JOBS a
       WHERE a.jobid = prm_jobid;
      
      IF oraclejobid IS NULL THEN
         prm_AppCode  := gn_def_ERR;
	       prm_ErrorMsg := '未获取到相应的定时任务信息，请核查！';
         RETURN;
      END IF;

      dbms_job.remove(oraclejobid);

   EXCEPTION
      -- WHEN NO_DATA_FOUND THEN
      -- WHEN TOO_MANY_ROWS THEN
      -- WHEN DUP_VAL_ON_INDEX THEN
      WHEN OTHERS THEN
       INSERT INTO QRTZ_JOB_MSGS(
                  ID,
                  JOBID,
                  JOB_GROUP,
                  EXECSTARTTIME,
                  EXECENDTIME,
                  ISSUCCESS,
                  SUCCESSMSG)VALUES(
                  SEQ_DEFAULT.NEXTVAL,
                  prm_jobid,
                  'DEFAULT',
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),
                  '1',
                  'LOG:删除失败');
         commit;   
         /*关闭打开的游标*/
         prm_AppCode  := gn_def_ERR;
         prm_ErrorMsg := '数据库错误:'|| SQLERRM ;
         RETURN;
   END prc_removeJob ;
END pkg_YHCIP;
/

show error;

create table TADATASOURCE
(
  datasourceid    NUMBER(20) not null,
  datasourcename  VARCHAR2(60),
  datasourcetype  VARCHAR2(10),
  driverclassname VARCHAR2(120),
  url             VARCHAR2(120),
  username        VARCHAR2(60),
  password        VARCHAR2(60),
  effective       CHAR(1),
  connecttype   VARCHAR2(10),
  jndiname VARCHAR2(50)
)
;
comment on table TADATASOURCE
  is '数据源表';
comment on column TADATASOURCE.datasourceid
  is '数据源id';
comment on column TADATASOURCE.datasourcename
  is '数据源名称';
comment on column TADATASOURCE.datasourcetype
  is '数据源类型';
comment on column TADATASOURCE.driverclassname
  is '数据库驱动名字';
comment on column TADATASOURCE.url
  is '数据库链接';
comment on column TADATASOURCE.username
  is '用户名';
comment on column TADATASOURCE.password
  is '密码';
comment on column TADATASOURCE.effective
  is '有效标志';
 comment on column TADATASOURCE.connecttype
  is '连接类型';
 comment on column TADATASOURCE.jndiname
  is 'jndi名称';
alter table TADATASOURCE
  add primary key (DATASOURCEID);


-- Add comments to the table
comment on table TAONLINESTATLOG
  is '时点在线情况统计表';

-- Create table
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
-- Create/Recreate primary, unique and foreign key constraints
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


