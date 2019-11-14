/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2019/2/22 17:35:57                           */
/*==============================================================*/


drop view V_YAB139;

drop view V_TALOGINLOG;

drop view AA10A1;

drop table AA10;

drop table CLUSTERCONFIG CASCADE ;

drop table QRTZ_BLOB_TRIGGERS CASCADE ;

drop table QRTZ_CALENDARS CASCADE;

drop table QRTZ_CRON_TRIGGERS CASCADE;

drop index IDX_QRTZ_FT_TG;

drop index IDX_QRTZ_FT_T_G;

drop index IDX_QRTZ_FT_JG;

drop index IDX_QRTZ_FT_J_G;

drop index IDX_QRTZ_FT_INST_JOB_REQ_RCVRY;

drop index IDX_QRTZ_FT_TRIG_INST_NAME;

drop table QRTZ_FIRED_TRIGGERS CASCADE;

drop index IDX_QRTZ_J_GRP;

drop index IDX_QRTZ_J_REQ_RECOVERY;

drop table QRTZ_JOB_DETAILS CASCADE;

drop table QRTZ_JOB_LOG CASCADE;

drop table QRTZ_JOB_MSGS CASCADE;

drop table QRTZ_LOCKS CASCADE;

drop table QRTZ_PAUSED_TRIGGER_GRPS CASCADE;

drop table QRTZ_SCHEDULER_STATE CASCADE;

drop table QRTZ_SIMPLE_TRIGGERS CASCADE;

drop table QRTZ_SIMPROP_TRIGGERS CASCADE;

drop index IDX_QRTZ_T_NFT_ST_MISFIRE_GRP;

drop index IDX_QRTZ_T_NFT_ST_MISFIRE;

drop index IDX_QRTZ_T_NFT_MISFIRE;

drop index IDX_QRTZ_T_NFT_ST;

drop index IDX_QRTZ_T_NEXT_FIRE_TIME;

drop index IDX_QRTZ_T_N_G_STATE;

drop index IDX_QRTZ_T_N_STATE;

drop index IDX_QRTZ_T_STATE;

drop index IDX_QRTZ_T_G;

drop index IDX_QRTZ_T_C;

drop index IDX_QRTZ_T_JG;

drop index IDX_QRTZ_T_J;

drop table QRTZ_TRIGGERS CASCADE;

drop table SIGNRECORD CASCADE;

drop table TAACCESSLOG CASCADE;

drop table TAACCESSSYSTEM CASCADE;

drop table TAADMINYAB139SCOPE CASCADE;

drop table TACOMMONMENU CASCADE;

drop table TACONFIG CASCADE;

drop table TACONFIGSYSPATH CASCADE;

drop table TACONSOLEMODULE CASCADE;

drop table TACONSOLEMODULELOCATION CASCADE;

drop table TACONSOLEMODULEPRIVILEGE CASCADE;

drop table TADATAACCESSDIMENSION CASCADE;

drop table TADATASOURCE CASCADE;

drop table TAFIELD CASCADE;

drop table TAFIELDAUTHRITY CASCADE;

drop table TALIMITRATE CASCADE;

drop table TALOCALCACHEVERSION CASCADE;

drop table TALOGINHISTORYLOG CASCADE;

drop table TALOGINSTATLOG CASCADE;

drop table TAMANAGERMG CASCADE;

drop table TAMENU CASCADE;

drop table TAMENUPOSITIONYAB003 CASCADE;

drop table TAMESSAGE CASCADE;

drop table TAMESSAGEATTACHMENT CASCADE;

drop table TAMESSAGESTATE CASCADE;

drop table TAONLINELOG CASCADE;

drop table TAONLINESTATLOG CASCADE;

drop table TAORG CASCADE;

drop table TAORGMG CASCADE;

drop table TAORGOPLOG CASCADE;

drop table TAPAGEREVIEW CASCADE;

drop table TAPOSITION CASCADE;

drop table TAPOSITIONAUTHRITY CASCADE;

drop table TARUNQIANAD52REFERENCE CASCADE;

drop table TARUNQIANPRINTSETUP CASCADE;

drop table TARUNQIANRESOURCE CASCADE;

drop table TASERVEREXCEPTIONLOG CASCADE;

drop table TASHAREPOSITION CASCADE;

drop table TAUSER CASCADE;

drop table TAUSERPOSITION CASCADE;

drop table TAYAB003LEVELMG CASCADE;

drop table TAYAB003SCOPE CASCADE;

drop table TAYAB139MG CASCADE;

drop table TEMPLATEMANAGER CASCADE;

drop table YHCIP_ORACLE_JOBS CASCADE;

drop table TAZKADDRESSMG CASCADE;

drop table JOB_BUSY_FREE_MG CASCADE;

drop sequence HIBERNATE_SEQUENCE CASCADE;

drop sequence SEQ_DEFAULT CASCADE;

drop sequence SEQ_MESSAGE CASCADE;

drop sequence TEMPLATE_SEQUENCE CASCADE;

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

create sequence SEQ_MESSAGE
minvalue 1
no maxvalue
start with 201
increment by 1
cache 10;

create sequence TEMPLATE_SEQUENCE
minvalue 1
no maxvalue
start with 61
increment by 1
cache 20;

/*==============================================================*/
/* Table: AA10                                                  */
/*==============================================================*/
create table AA10 (
  AAA100               VARCHAR(20)          not null,
  AAA101               VARCHAR(50)          not null,
  AAA102               VARCHAR(6)           not null,
  AAA103               VARCHAR(50)          not null,
  YAB003               VARCHAR(6)           not null,
  AAE120               VARCHAR(6)           not null,
  VER                  NUMERIC(10,0)        null,
  constraint PK_AA10 primary key (AAA100, AAA102)
);

comment on table AA10 is
  'AA10代码表';

comment on column AA10.AAA100 is
  'AAA100代码类别';

comment on column AA10.AAA101 is
  'AAA102代码值';

comment on column AA10.AAA102 is
  'AAA102代码值';

comment on column AA10.AAA103 is
  'AAA103代码名称';

comment on column AA10.YAB003 is
  'YAB003经办机构';

comment on column AA10.AAE120 is
  'AAE120注销标志';

/*==============================================================*/
/* Table: CLUSTERCONFIG                                         */
/*==============================================================*/
create table CLUSTERCONFIG (
  CLUSTERID            NUMERIC(10,0)        not null,
  CLUSTERNAME          VARCHAR(200)         null,
  CLUSTERAPP           VARCHAR(200)         null,
  CLUSTERURL           VARCHAR(200)         not null,
  CLUSTERDESC          VARCHAR(200)         null,
  constraint PK_CLUSTERCONFIG primary key (CLUSTERID)
);

comment on table CLUSTERCONFIG is
  '集群配置表';

comment on column CLUSTERCONFIG.CLUSTERID is
  '集群配置id';

comment on column CLUSTERCONFIG.CLUSTERNAME is
  '集群名称';

comment on column CLUSTERCONFIG.CLUSTERAPP is
  '应用名称';

comment on column CLUSTERCONFIG.CLUSTERURL is
  '服务URL';

comment on column CLUSTERCONFIG.CLUSTERDESC is
  '描述';

/*==============================================================*/
/* Table: QRTZ_BLOB_TRIGGERS                                    */
/*==============================================================*/
create table QRTZ_BLOB_TRIGGERS (
  SCHED_NAME           VARCHAR(120)         not null,
  TRIGGER_NAME         VARCHAR(200)         not null,
  TRIGGER_GROUP        VARCHAR(200)         not null,
  BLOB_DATA            BYTEA                 null,
  constraint QRTZ_BLOB_TRIG_PK primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

/*==============================================================*/
/* Table: QRTZ_CALENDARS                                        */
/*==============================================================*/
create table QRTZ_CALENDARS (
  SCHED_NAME           VARCHAR(120)         not null,
  CALENDAR_NAME        VARCHAR(200)         not null,
  CALENDAR             BYTEA                 not null,
  constraint QRTZ_CALENDARS_PK primary key (SCHED_NAME, CALENDAR_NAME)
);

/*==============================================================*/
/* Table: QRTZ_CRON_TRIGGERS                                    */
/*==============================================================*/
create table QRTZ_CRON_TRIGGERS (
  SCHED_NAME           VARCHAR(120)         not null,
  TRIGGER_NAME         VARCHAR(200)         not null,
  TRIGGER_GROUP        VARCHAR(200)         not null,
  CRON_EXPRESSION      VARCHAR(120)         not null,
  TIME_ZONE_ID         VARCHAR(80)          null,
  constraint QRTZ_CRON_TRIG_PK primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

/*==============================================================*/
/* Table: QRTZ_FIRED_TRIGGERS                                   */
/*==============================================================*/
create table QRTZ_FIRED_TRIGGERS (
  SCHED_NAME           VARCHAR(120)         not null,
  ENTRY_ID             VARCHAR(95)          not null,
  TRIGGER_NAME         VARCHAR(200)         not null,
  TRIGGER_GROUP        VARCHAR(200)         not null,
  INSTANCE_NAME        VARCHAR(200)         not null,
  FIRED_TIME           NUMERIC(13)          not null,
  SCHED_TIME           NUMERIC(13)          not null,
  PRIORITY             NUMERIC(13)          not null,
  STATE                VARCHAR(16)          not null,
  JOB_NAME             VARCHAR(200)         null,
  JOB_GROUP            VARCHAR(200)         null,
  IS_NONCONCURRENT     BOOL           null,
  REQUESTS_RECOVERY    BOOL           null,
  constraint QRTZ_FIRED_TRIGGER_PK primary key (SCHED_NAME, ENTRY_ID)
);

/*==============================================================*/
/* Index: IDX_QRTZ_FT_TRIG_INST_NAME                            */
/*==============================================================*/
create  index IDX_QRTZ_FT_TRIG_INST_NAME on QRTZ_FIRED_TRIGGERS (
  SCHED_NAME,
  INSTANCE_NAME
);

/*==============================================================*/
/* Index: IDX_QRTZ_FT_INST_JOB_REQ_RCVRY                        */
/*==============================================================*/
create  index IDX_QRTZ_FT_INST_JOB_REQ_RCVRY on QRTZ_FIRED_TRIGGERS (
  SCHED_NAME,
  INSTANCE_NAME,
  REQUESTS_RECOVERY
);

/*==============================================================*/
/* Index: IDX_QRTZ_FT_J_G                                       */
/*==============================================================*/
create  index IDX_QRTZ_FT_J_G on QRTZ_FIRED_TRIGGERS (
  SCHED_NAME,
  JOB_NAME,
  JOB_GROUP
);

/*==============================================================*/
/* Index: IDX_QRTZ_FT_JG                                        */
/*==============================================================*/
create  index IDX_QRTZ_FT_JG on QRTZ_FIRED_TRIGGERS (
  SCHED_NAME,
  JOB_GROUP
);

/*==============================================================*/
/* Index: IDX_QRTZ_FT_T_G                                       */
/*==============================================================*/
create  index IDX_QRTZ_FT_T_G on QRTZ_FIRED_TRIGGERS (
  SCHED_NAME,
  TRIGGER_NAME,
  TRIGGER_GROUP
);

/*==============================================================*/
/* Index: IDX_QRTZ_FT_TG                                        */
/*==============================================================*/
create  index IDX_QRTZ_FT_TG on QRTZ_FIRED_TRIGGERS (
  SCHED_NAME,
  TRIGGER_GROUP
);

/*==============================================================*/
/* Table: QRTZ_JOB_DETAILS                                      */
/*==============================================================*/
create table QRTZ_JOB_DETAILS (
  SCHED_NAME           VARCHAR(120)         not null,
  JOB_NAME             VARCHAR(200)         not null,
  JOB_GROUP            VARCHAR(200)         not null,
  DESCRIPTION          VARCHAR(250)         null,
  JOB_CLASS_NAME       VARCHAR(250)         not null,
  IS_DURABLE           BOOL           not null,
  IS_NONCONCURRENT     BOOL           not null,
  IS_UPDATE_DATA       BOOL           not null,
  REQUESTS_RECOVERY    BOOL           not null,
  JOB_DATA             BYTEA                 null,
  constraint QRTZ_JOB_DETAILS_PK primary key (SCHED_NAME, JOB_NAME, JOB_GROUP)
);

/*==============================================================*/
/* Index: IDX_QRTZ_J_REQ_RECOVERY                               */
/*==============================================================*/
create  index IDX_QRTZ_J_REQ_RECOVERY on QRTZ_JOB_DETAILS (
  SCHED_NAME,
  REQUESTS_RECOVERY
);

/*==============================================================*/
/* Index: IDX_QRTZ_J_GRP                                        */
/*==============================================================*/
create  index IDX_QRTZ_J_GRP on QRTZ_JOB_DETAILS (
  SCHED_NAME,
  JOB_GROUP
);

/*==============================================================*/
/* Table: QRTZ_JOB_LOG                                          */
/*==============================================================*/
create table QRTZ_JOB_LOG (
  LOG_ID               VARCHAR(17)          not null,
  JOB_NAME             VARCHAR(200)         not null,
  ADDRESS              VARCHAR(200)         not null,
  SERVICE_ID           VARCHAR(200)         not null,
  FIRED_TIME           DATE                 not null,
  SUCCESS              VARCHAR(6)           not null,
  LOG_MSG              VARCHAR(255)         null,
  constraint QRTZ_JOB_LOG_PK primary key (LOG_ID)
);

comment on table QRTZ_JOB_LOG is
  'Quartz定时任务执行日志';

comment on column QRTZ_JOB_LOG.LOG_ID is
  '日志序列号';

comment on column QRTZ_JOB_LOG.JOB_NAME is
  '任务名称';

comment on column QRTZ_JOB_LOG.ADDRESS is
  '服务器地址';

comment on column QRTZ_JOB_LOG.SERVICE_ID is
  '服务ID';

comment on column QRTZ_JOB_LOG.FIRED_TIME is
  '发生时间';

comment on column QRTZ_JOB_LOG.SUCCESS is
  '成功标志';

comment on column QRTZ_JOB_LOG.LOG_MSG is
  '日志消息';

/*==============================================================*/
/* Table: QRTZ_JOB_MSGS                                         */
/*==============================================================*/
create table QRTZ_JOB_MSGS (
  ID                   VARCHAR(50)          not null,
  JOB_NAME             VARCHAR(80)          not null,
  JOB_GROUP            VARCHAR(80)          not null,
  USERID               VARCHAR(20)          null,
  EXECSTARTTIME        VARCHAR(30)          null,
  EXECENDTIME          VARCHAR(30)          null,
  ISSUCCESS            VARCHAR(2)           null,
  SUCCESSMSG           VARCHAR(1024)        null,
  ERRORMSG             VARCHAR(1024)        null,
  constraint SYS_C0052264 primary key (ID)
);

/*==============================================================*/
/* Table: QRTZ_LOCKS                                            */
/*==============================================================*/
create table QRTZ_LOCKS (
  SCHED_NAME           VARCHAR(120)         not null,
  LOCK_NAME            VARCHAR(40)          not null,
  constraint QRTZ_LOCKS_PK primary key (SCHED_NAME, LOCK_NAME)
);

/*==============================================================*/
/* Table: QRTZ_PAUSED_TRIGGER_GRPS                              */
/*==============================================================*/
create table QRTZ_PAUSED_TRIGGER_GRPS (
  SCHED_NAME           VARCHAR(120)         not null,
  TRIGGER_GROUP        VARCHAR(200)         not null,
  constraint QRTZ_PAUSED_TRIG_GRPS_PK primary key (SCHED_NAME, TRIGGER_GROUP)
);

/*==============================================================*/
/* Table: QRTZ_SCHEDULER_STATE                                  */
/*==============================================================*/
create table QRTZ_SCHEDULER_STATE (
  SCHED_NAME           VARCHAR(120)         not null,
  INSTANCE_NAME        VARCHAR(200)         not null,
  LAST_CHECKIN_TIME    NUMERIC(13)          not null,
  CHECKIN_INTERVAL     NUMERIC(13)          not null,
  constraint QRTZ_SCHEDULER_STATE_PK primary key (SCHED_NAME, INSTANCE_NAME)
);

/*==============================================================*/
/* Table: QRTZ_SIMPLE_TRIGGERS                                  */
/*==============================================================*/
create table QRTZ_SIMPLE_TRIGGERS (
  SCHED_NAME           VARCHAR(120)         not null,
  TRIGGER_NAME         VARCHAR(200)         not null,
  TRIGGER_GROUP        VARCHAR(200)         not null,
  REPEAT_COUNT         NUMERIC(7)           not null,
  REPEAT_INTERVAL      NUMERIC(12)          not null,
  TIMES_TRIGGERED      NUMERIC(10)          not null,
  constraint QRTZ_SIMPLE_TRIG_PK primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

/*==============================================================*/
/* Table: QRTZ_SIMPROP_TRIGGERS                                 */
/*==============================================================*/
create table QRTZ_SIMPROP_TRIGGERS (
  SCHED_NAME           VARCHAR(120)         not null,
  TRIGGER_NAME         VARCHAR(200)         not null,
  TRIGGER_GROUP        VARCHAR(200)         not null,
  STR_PROP_1           VARCHAR(512)         null,
  STR_PROP_2           VARCHAR(512)         null,
  STR_PROP_3           VARCHAR(512)         null,
  INT_PROP_1           NUMERIC(10)          null,
  INT_PROP_2           NUMERIC(10)          null,
  LONG_PROP_1          NUMERIC(13)          null,
  LONG_PROP_2          NUMERIC(13)          null,
  DEC_PROP_1           NUMERIC(13,4)        null,
  DEC_PROP_2           NUMERIC(13,4)        null,
  BOOL_PROP_1          BOOL           null,
  BOOL_PROP_2          BOOL           null,
  constraint QRTZ_SIMPROP_TRIG_PK primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

/*==============================================================*/
/* Table: QRTZ_TRIGGERS                                         */
/*==============================================================*/
create table QRTZ_TRIGGERS (
  SCHED_NAME           VARCHAR(120)         not null,
  TRIGGER_NAME         VARCHAR(200)         not null,
  TRIGGER_GROUP        VARCHAR(200)         not null,
  JOB_NAME             VARCHAR(200)         not null,
  JOB_GROUP            VARCHAR(200)         not null,
  DESCRIPTION          VARCHAR(250)         null,
  NEXT_FIRE_TIME       NUMERIC(13)          null,
  PREV_FIRE_TIME       NUMERIC(13)          null,
  PRIORITY             NUMERIC(13)          null,
  TRIGGER_STATE        VARCHAR(16)          not null,
  TRIGGER_TYPE         VARCHAR(8)           not null,
  START_TIME           NUMERIC(13)          not null,
  END_TIME             NUMERIC(13)          null,
  CALENDAR_NAME        VARCHAR(200)         null,
  MISFIRE_INSTR        NUMERIC(2)           null,
  JOB_DATA             BYTEA                 null,
  constraint QRTZ_TRIGGERS_PK primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

/*==============================================================*/
/* Index: IDX_QRTZ_T_J                                          */
/*==============================================================*/
create  index IDX_QRTZ_T_J on QRTZ_TRIGGERS (
  SCHED_NAME,
  JOB_NAME,
  JOB_GROUP
);

/*==============================================================*/
/* Index: IDX_QRTZ_T_JG                                         */
/*==============================================================*/
create  index IDX_QRTZ_T_JG on QRTZ_TRIGGERS (
  SCHED_NAME,
  JOB_GROUP
);

/*==============================================================*/
/* Index: IDX_QRTZ_T_C                                          */
/*==============================================================*/
create  index IDX_QRTZ_T_C on QRTZ_TRIGGERS (
  SCHED_NAME,
  CALENDAR_NAME
);

/*==============================================================*/
/* Index: IDX_QRTZ_T_G                                          */
/*==============================================================*/
create  index IDX_QRTZ_T_G on QRTZ_TRIGGERS (
  SCHED_NAME,
  TRIGGER_GROUP
);

/*==============================================================*/
/* Index: IDX_QRTZ_T_STATE                                      */
/*==============================================================*/
create  index IDX_QRTZ_T_STATE on QRTZ_TRIGGERS (
  SCHED_NAME,
  TRIGGER_STATE
);

/*==============================================================*/
/* Index: IDX_QRTZ_T_N_STATE                                    */
/*==============================================================*/
create  index IDX_QRTZ_T_N_STATE on QRTZ_TRIGGERS (
  SCHED_NAME,
  TRIGGER_NAME,
  TRIGGER_GROUP,
  TRIGGER_STATE
);

/*==============================================================*/
/* Index: IDX_QRTZ_T_N_G_STATE                                  */
/*==============================================================*/
create  index IDX_QRTZ_T_N_G_STATE on QRTZ_TRIGGERS (
  SCHED_NAME,
  TRIGGER_GROUP,
  TRIGGER_STATE
);

/*==============================================================*/
/* Index: IDX_QRTZ_T_NEXT_FIRE_TIME                             */
/*==============================================================*/
create  index IDX_QRTZ_T_NEXT_FIRE_TIME on QRTZ_TRIGGERS (
  SCHED_NAME,
  NEXT_FIRE_TIME
);

/*==============================================================*/
/* Index: IDX_QRTZ_T_NFT_ST                                     */
/*==============================================================*/
create  index IDX_QRTZ_T_NFT_ST on QRTZ_TRIGGERS (
  SCHED_NAME,
  TRIGGER_STATE,
  NEXT_FIRE_TIME
);

/*==============================================================*/
/* Index: IDX_QRTZ_T_NFT_MISFIRE                                */
/*==============================================================*/
create  index IDX_QRTZ_T_NFT_MISFIRE on QRTZ_TRIGGERS (
  SCHED_NAME,
  MISFIRE_INSTR,
  NEXT_FIRE_TIME
);

/*==============================================================*/
/* Index: IDX_QRTZ_T_NFT_ST_MISFIRE                             */
/*==============================================================*/
create  index IDX_QRTZ_T_NFT_ST_MISFIRE on QRTZ_TRIGGERS (
  SCHED_NAME,
  MISFIRE_INSTR,
  NEXT_FIRE_TIME,
  TRIGGER_STATE
);

/*==============================================================*/
/* Index: IDX_QRTZ_T_NFT_ST_MISFIRE_GRP                         */
/*==============================================================*/
create  index IDX_QRTZ_T_NFT_ST_MISFIRE_GRP on QRTZ_TRIGGERS (
  SCHED_NAME,
  MISFIRE_INSTR,
  NEXT_FIRE_TIME,
  TRIGGER_GROUP,
  TRIGGER_STATE
);

/*==============================================================*/
/* Table: SIGNRECORD                                            */
/*==============================================================*/
create table SIGNRECORD (
  USERID               NUMERIC(10)          null,
  SIGNTIME             DATE                 null,
  SIGNSTATE            VARCHAR(2)           null,
  IP                   VARCHAR(20)          null,
  MAC                  VARCHAR(20)          null,
  SIGNID               NUMERIC(20)          not null
);

comment on table SIGNRECORD is
  '签到签退记录表';

comment on column SIGNRECORD.USERID is
  '人员ID';

comment on column SIGNRECORD.SIGNTIME is
  '签到时间';

comment on column SIGNRECORD.SIGNSTATE is
  '签到状态(1 签到 2 签退)';

comment on column SIGNRECORD.IP is
  'IP 地址';

comment on column SIGNRECORD.MAC is
  'MAC地址';

comment on column SIGNRECORD.SIGNID is
  '流水号';

/*==============================================================*/
/* Table: TAACCESSLOG                                           */
/*==============================================================*/
create table TAACCESSLOG (
  LOGID                NUMERIC(15)          not null,
  USERID               NUMERIC(10)          not null,
  POSITIONID           NUMERIC(10)          not null,
  PERMISSIONID         NUMERIC(10)          not null,
  ISPERMISSION         CHAR(1)              not null,
  ACCESSTIME           DATE                 not null,
  URL                  VARCHAR(1024)        null,
  SYSFLAG              VARCHAR(50)          null,
  constraint PK_TAACCESSLOG primary key (LOGID)
);

comment on table TAACCESSLOG is
  '功能日志表';

comment on column TAACCESSLOG.LOGID is
  '日志id';

comment on column TAACCESSLOG.USERID is
  '用户id';

comment on column TAACCESSLOG.POSITIONID is
  '岗位id';

comment on column TAACCESSLOG.PERMISSIONID is
  '功能id';

comment on column TAACCESSLOG.ISPERMISSION is
  '是否有权限';

comment on column TAACCESSLOG.ACCESSTIME is
  '访问时间';

comment on column TAACCESSLOG.URL is
  '访问路径';

comment on column TAACCESSLOG.SYSFLAG is
  '系统标识';

/*==============================================================*/
/* Table: TAACCESSSYSTEM                                        */
/*==============================================================*/
create table TAACCESSSYSTEM (
  SYSID                NUMERIC(11)          not null,
  SYSTEMKEY            VARCHAR(40)          not null,
  SYSTEMNAME           VARCHAR(100)         not null,
  PRIVATEKEY           BYTEA                 null,
  PUBLICKEY            BYTEA                 null,
  constraint PK_TAACCESSSYSTEM primary key (SYSID),
  constraint UK_TAACCESSSYSTEM unique (SYSTEMKEY)
);

comment on table TAACCESSSYSTEM is
  '接入系统管理';

comment on column TAACCESSSYSTEM.SYSID is
  '接入系统ID';

comment on column TAACCESSSYSTEM.SYSTEMKEY is
  '接入标识';

comment on column TAACCESSSYSTEM.SYSTEMNAME is
  '系统名称';

comment on column TAACCESSSYSTEM.PRIVATEKEY is
  '私钥';

comment on column TAACCESSSYSTEM.PUBLICKEY is
  '公钥';

/*==============================================================*/
/* Table: TAADMINYAB139SCOPE                                    */
/*==============================================================*/
create table TAADMINYAB139SCOPE (
  POSITIONID           NUMERIC(10)          not null,
  YAB139               VARCHAR(6)           not null,
  constraint PK_TAADMINYAB139SCOPE primary key (POSITIONID, YAB139)
);

comment on table TAADMINYAB139SCOPE is
  '管理员数据区管理范围';

comment on column TAADMINYAB139SCOPE.POSITIONID is
  '岗位id';

comment on column TAADMINYAB139SCOPE.YAB139 is
  '数据区';

/*==============================================================*/
/* Table: TACOMMONMENU                                          */
/*==============================================================*/
create table TACOMMONMENU (
  USERID               NUMERIC(10)          not null,
  MENUID               NUMERIC(10)          not null,
  constraint PK_TACOMMONMENU primary key (USERID, MENUID)
);

comment on table TACOMMONMENU is
  '常用菜单';

comment on column TACOMMONMENU.USERID is
  '用户id';

comment on column TACOMMONMENU.MENUID is
  '菜单id';

/*==============================================================*/
/* Table: TACONFIG                                              */
/*==============================================================*/
create table TACONFIG (
  CONFIGID             NUMERIC(10,0)        not null,
  CONFIGNAME           VARCHAR(100)         not null,
  CONFIGVALUE          VARCHAR(1024)        null,
  CONFIGTYPE           VARCHAR(1024)        null,
  CONFIGFLAG           VARCHAR(20)          not null,
  CONFIGDESC           VARCHAR(200)         null,
  constraint PK_TACONFIG primary key (CONFIGID)
);

comment on table TACONFIG is
  '系统配置表';

comment on column TACONFIG.CONFIGID is
  '配置项ID';

comment on column TACONFIG.CONFIGNAME is
  '配置项名称';

comment on column TACONFIG.CONFIGVALUE is
  '配置项内容';

comment on column TACONFIG.CONFIGTYPE is
  '是否系统参数';

comment on column TACONFIG.CONFIGFLAG is
  '系统标识';

comment on column TACONFIG.CONFIGDESC is
  '参数说明';

/*==============================================================*/
/* Table: TACONFIGSYSPATH                                       */
/*==============================================================*/
create table TACONFIGSYSPATH (
  SERIALID             NUMERIC(10,0)        not null,
  ID                   VARCHAR(20)          not null,
  NAME                 VARCHAR(50)          not null,
  URL                  VARCHAR(100)         not null,
  PY                   VARCHAR(20)          null,
  CURSYSTEM            VARCHAR(1)           not null,
  constraint PK_TACONFIGSYSPATH primary key (SERIALID)
);

comment on table TACONFIGSYSPATH is
  '系统路径配置表';

comment on column TACONFIGSYSPATH.SERIALID is
  '流水号';

comment on column TACONFIGSYSPATH.ID is
  '系统ID';

comment on column TACONFIGSYSPATH.NAME is
  '系统名称';

comment on column TACONFIGSYSPATH.URL is
  '系统路径前缀';

comment on column TACONFIGSYSPATH.PY is
  '拼音';

comment on column TACONFIGSYSPATH.CURSYSTEM is
  '是否为当前系统';

/*==============================================================*/
/* Table: TACONSOLEMODULE                                       */
/*==============================================================*/
create table TACONSOLEMODULE (
  MODULE_ID            NUMERIC(10)          not null,
  MODULE_NAME          VARCHAR(100)         not null,
  MODULE_URL           VARCHAR(200)         not null,
  MODULE_STA           VARCHAR(1)           not null default '1',
  MODULE_DEFAULT       VARCHAR(1)           null default '1',
  MODULE_HEIGHT        VARCHAR(10)          null default '200',
  constraint PK_ECADMIN_CONSLE_MODULE primary key (MODULE_ID)
);

comment on table TACONSOLEMODULE is
  'ECADMIN工作台自定义组件';

comment on column TACONSOLEMODULE.MODULE_ID is
  '模块编号';

comment on column TACONSOLEMODULE.MODULE_NAME is
  '模块名称';

comment on column TACONSOLEMODULE.MODULE_URL is
  '模块链接';

comment on column TACONSOLEMODULE.MODULE_STA is
  '模块有效标识';

comment on column TACONSOLEMODULE.MODULE_DEFAULT is
  '是否默认显示';

comment on column TACONSOLEMODULE.MODULE_HEIGHT is
  '模块默认高度';

/*==============================================================*/
/* Table: TACONSOLEMODULELOCATION                               */
/*==============================================================*/
create table TACONSOLEMODULELOCATION (
  MARK                 VARCHAR(20)          not null,
  POSITIONID           NUMERIC(10)          not null,
  LOCATION             VARCHAR(1000)        not null
);

comment on table TACONSOLEMODULELOCATION is
  'ECADMIN工作台自定义组件位置信息';

comment on column TACONSOLEMODULELOCATION.MARK is
  '页面标识';

comment on column TACONSOLEMODULELOCATION.POSITIONID is
  '岗位ID';

comment on column TACONSOLEMODULELOCATION.LOCATION is
  '位置信息数据';

/*==============================================================*/
/* Table: TACONSOLEMODULEPRIVILEGE                              */
/*==============================================================*/
create table TACONSOLEMODULEPRIVILEGE (
  POSITIONID           NUMERIC(10)          not null,
  MODULEID             NUMERIC(10)          not null,
  constraint PK_CRM_CONSLE_MODULE_PRIVILEGE primary key (POSITIONID, MODULEID)
);

comment on table TACONSOLEMODULEPRIVILEGE is
  'CRM工作台自定义组件权限信息';

comment on column TACONSOLEMODULEPRIVILEGE.POSITIONID is
  '角色编号';

comment on column TACONSOLEMODULEPRIVILEGE.MODULEID is
  '模块编号';

/*==============================================================*/
/* Table: TADATAACCESSDIMENSION                                 */
/*==============================================================*/
create table TADATAACCESSDIMENSION (
  DIMENSIONID          NUMERIC(10,0)        not null,
  POSITIONID           NUMERIC(10,0)        not null,
  MENUID               NUMERIC(10,0)        not null,
  DIMENSIONTYPE        VARCHAR(20)          not null,
  DIMENSIONPERMISSIONID VARCHAR(20)          null,
  ALLACCESS            VARCHAR(1)           null,
  SYSPATH              VARCHAR(50)          null,
  constraint PK_TADATAACCESSDIMENSION primary key (DIMENSIONID)
);

comment on table TADATAACCESSDIMENSION is
  '维度数据权限表';

comment on column TADATAACCESSDIMENSION.DIMENSIONID is
  '维度ID';

comment on column TADATAACCESSDIMENSION.POSITIONID is
  '岗位ID';

comment on column TADATAACCESSDIMENSION.MENUID is
  '菜单ID';

comment on column TADATAACCESSDIMENSION.DIMENSIONTYPE is
  '维度类型';

comment on column TADATAACCESSDIMENSION.DIMENSIONPERMISSIONID is
  '维度权限ID';

comment on column TADATAACCESSDIMENSION.ALLACCESS is
  '是否具有该维度所有权限';

comment on column TADATAACCESSDIMENSION.SYSPATH is
  '系统标识';

/*==============================================================*/
/* Table: TADATASOURCE                                          */
/*==============================================================*/
create table TADATASOURCE (
  DATASOURCEID         NUMERIC(20)          not null,
  DATASOURCENAME       VARCHAR(60)          null,
  DATASOURCETYPE       VARCHAR(10)          null,
  DRIVERCLASSNAME      VARCHAR(120)         null,
  URL                  VARCHAR(120)         null,
  USERNAME             VARCHAR(60)          null,
  PASSWORD             VARCHAR(60)          null,
  EFFECTIVE            CHAR(1)              null,
  CONNECTTYPE          VARCHAR(10)          null,
  JNDINAME             VARCHAR(50)          null,
  constraint PK_TADATASOURCE primary key (DATASOURCEID)
);

comment on table TADATASOURCE is
  '数据源表';

comment on column TADATASOURCE.DATASOURCEID is
  '数据源id';

comment on column TADATASOURCE.DATASOURCENAME is
  '数据源名称';

comment on column TADATASOURCE.DATASOURCETYPE is
  '数据源类型';

comment on column TADATASOURCE.DRIVERCLASSNAME is
  '数据库驱动名字';

comment on column TADATASOURCE.URL is
  '数据库链接';

comment on column TADATASOURCE.USERNAME is
  '用户名';

comment on column TADATASOURCE.PASSWORD is
  '密码';

comment on column TADATASOURCE.EFFECTIVE is
  '有效标志';

comment on column TADATASOURCE.CONNECTTYPE is
  '连接类型';

comment on column TADATASOURCE.JNDINAME is
  'jndi名称';

/*==============================================================*/
/* Table: TAFIELD                                               */
/*==============================================================*/
create table TAFIELD (
  ID                   NUMERIC(10)          not null,
  MENUID               NUMERIC(10)          null,
  FIELDID              VARCHAR(100)         null,
  FIELDNAME            VARCHAR(100)         null,
  TABLEID              VARCHAR(10)          null,
  PID                  NUMERIC(10)          null,
  FIELDLEVEL           NUMERIC              null
);

comment on column TAFIELD.ID is
  '主键';

comment on column TAFIELD.MENUID is
  '菜单ID';

comment on column TAFIELD.FIELDID is
  '字段id';

comment on column TAFIELD.FIELDNAME is
  '字段名称';

comment on column TAFIELD.TABLEID is
  '表id(备用)';

comment on column TAFIELD.PID is
  '父节点Id';

comment on column TAFIELD.FIELDLEVEL is
  '字段层级';

/*==============================================================*/
/* Table: TAFIELDAUTHRITY                                       */
/*==============================================================*/
create table TAFIELDAUTHRITY (
  POSITIONID           NUMERIC(10)          not null,
  MENUID               NUMERIC(10)          not null,
  FIELDID              VARCHAR(100)         not null,
  LOOK                 CHAR(1)              null,
  EDIT                 CHAR(1)              null,
  CREATETIME           DATE                 null,
  CREATEUSER           NUMERIC(10)          null
);

comment on column TAFIELDAUTHRITY.POSITIONID is
  '岗位id';

comment on column TAFIELDAUTHRITY.MENUID is
  '菜单id';

comment on column TAFIELDAUTHRITY.FIELDID is
  '字段id';

comment on column TAFIELDAUTHRITY.LOOK is
  '是否可查看（1：可查看，0不可查看）';

comment on column TAFIELDAUTHRITY.EDIT is
  '是否可编辑（1：可编辑，0，不可查看）';

comment on column TAFIELDAUTHRITY.CREATETIME is
  '创建时间';

comment on column TAFIELDAUTHRITY.CREATEUSER is
  '创建人';

/*==============================================================*/
/* Table: TALIMITRATE                                           */
/*==============================================================*/
create table TALIMITRATE (
  MENUID               NUMERIC(10)          not null,
  LIMITOPEN            CHAR(1)              not null,
  RATE                 NUMERIC(16,2)        not null,
  TIMEOUT              NUMERIC(38)          not null,
  MAXCOUNT             NUMERIC(10)          not null,
  URL                  VARCHAR(100)         not null,
  constraint PK_TALIMITRATE primary key (URL)
);

comment on table TALIMITRATE is
  '访问限流';

comment on column TALIMITRATE.MENUID is
  '菜单id';

comment on column TALIMITRATE.LIMITOPEN is
  '是否开启限流';

comment on column TALIMITRATE.RATE is
  '允许的访问频率';

comment on column TALIMITRATE.TIMEOUT is
  '获取访问许可的超时时间';

comment on column TALIMITRATE.MAXCOUNT is
  '允许访问的最大并发量';

comment on column TALIMITRATE.URL is
  '访问路径';

/*==============================================================*/
/* Table: TALOCALCACHEVERSION                                   */
/*==============================================================*/
create table TALOCALCACHEVERSION (
  VERSION              NUMERIC(11)          not null,
  CODETYPE             VARCHAR(20)          null
);

comment on table TALOCALCACHEVERSION is
  '本地缓存码表版本号';

comment on column TALOCALCACHEVERSION.VERSION is
  '版本号';

comment on column TALOCALCACHEVERSION.CODETYPE is
  '改动的type';

/*==============================================================*/
/* Table: TALOGINHISTORYLOG                                     */
/*==============================================================*/
create table TALOGINHISTORYLOG (
  LOGID                NUMERIC(15)          not null,
  USERID               NUMERIC(10)          not null,
  LOGINTIME            DATE                 not null,
  LOGOUTTIME           DATE                 not null,
  CLIENTIP             VARCHAR(200)         not null,
  SESSIONID            VARCHAR(200)         not null,
  SERVERIP             VARCHAR(200)         null,
  SYSPATH              VARCHAR(50)          null,
  CLIENTSYSTEM         VARCHAR(50)          null,
  CLIENTBROWSER        VARCHAR(50)          null,
  CLIENTSCREENSIZE     VARCHAR(50)          null,
  constraint PK_TALOGINHISTORYLOG primary key (LOGID)
);

/*==============================================================*/
/* Table: TALOGINSTATLOG                                        */
/*==============================================================*/
create table TALOGINSTATLOG (
  STATDATE             VARCHAR(20)          not null,
  POINTINTIME          VARCHAR(20)          not null,
  LOGINNUM             NUMERIC(15)          not null,
  constraint PK_TALOGINSTATLOG primary key (STATDATE, POINTINTIME)
);

comment on table TALOGINSTATLOG is
  '时点登录人数分析';

comment on column TALOGINSTATLOG.STATDATE is
  '统计日期';

comment on column TALOGINSTATLOG.POINTINTIME is
  '统计时间点';

comment on column TALOGINSTATLOG.LOGINNUM is
  '登录人数';

/*==============================================================*/
/* Table: TAMANAGERMG                                           */
/*==============================================================*/
create table TAMANAGERMG (
  POSITIONID           NUMERIC(10,0)        null,
  ORGID                NUMERIC(10,0)        null
);

comment on table TAMANAGERMG is
  '组织负责人（副职）管理表';

comment on column TAMANAGERMG.POSITIONID is
  '岗位id';

comment on column TAMANAGERMG.ORGID is
  '组织id';

/*==============================================================*/
/* Table: TAMENU                                                */
/*==============================================================*/
create table TAMENU (
  MENUID               NUMERIC(10)          not null,
  PMENUID              NUMERIC(10)          not null,
  MENUNAME             VARCHAR(60)          null,
  URL                  VARCHAR(100)         null,
  MENUIDPATH           VARCHAR(1024)        null,
  MENUNAMEPATH         VARCHAR(1024)        null,
  ICONSKIN             VARCHAR(200)         null,
  SELECTIMAGE          VARCHAR(200)         null,
  REPORTID             VARCHAR(50)          null,
  ACCESSTIMEEL         VARCHAR(200)         null,
  EFFECTIVE            CHAR(1)              not null,
  SECURITYPOLICY       CHAR(1)              not null,
  ISDISMULTIPOS        CHAR(1)              not null,
  QUICKCODE            VARCHAR(20)          null,
  SORTNO               NUMERIC              null,
  RESOURCETYPE         CHAR(2)              not null,
  MENULEVEL            NUMERIC              null,
  ISLEAF               CHAR(1)              null,
  MENUTYPE             CHAR(1)              null,
  ISCACHE              CHAR(1)              null,
  SYSPATH              VARCHAR(20)          null,
  USEYAB003            CHAR(1)              null,
  TYPEFLAG             NUMERIC(10)          null,
  ISAUDITE             VARCHAR(1)           null,
  CONSOLEMODULE        CHAR(1)              null default '1',
  CUSTOMENCODING       VARCHAR(20)          null,
  ISFILEDSCONTROL      CHAR(1)              null default '1',
  constraint PK_TAMENU primary key (MENUID)
);

comment on table TAMENU is
  '功能菜单';

comment on column TAMENU.MENUID is
  '菜单Id';

comment on column TAMENU.PMENUID is
  '父菜单id';

comment on column TAMENU.MENUNAME is
  '功能地址';

comment on column TAMENU.URL is
  '功能名称';

comment on column TAMENU.MENUIDPATH is
  '菜单id路径';

comment on column TAMENU.MENUNAMEPATH is
  '菜单名称路径';

comment on column TAMENU.ICONSKIN is
  '选择前图片';

comment on column TAMENU.SELECTIMAGE is
  '选择后图片';

comment on column TAMENU.REPORTID is
  '查询报表的报表id';

comment on column TAMENU.ACCESSTIMEEL is
  '访问限制时间表达式';

comment on column TAMENU.EFFECTIVE is
  '有效标志';

comment on column TAMENU.SECURITYPOLICY is
  '安全策略';

comment on column TAMENU.ISDISMULTIPOS is
  '是否显示多岗';

comment on column TAMENU.QUICKCODE is
  '快捷访问码';

comment on column TAMENU.SORTNO is
  '排序号';

comment on column TAMENU.RESOURCETYPE is
  '权限类型（功能权限\按钮权限\表单只读\表单可编辑\表格列只读\表格列可编辑）';

comment on column TAMENU.MENULEVEL is
  '菜单层级';

comment on column TAMENU.ISLEAF is
  '是否叶子节点';

comment on column TAMENU.MENUTYPE is
  '菜单类型';

comment on column TAMENU.ISCACHE is
  '是否缓存';

comment on column TAMENU.SYSPATH is
  '系统路径';

comment on column TAMENU.USEYAB003 is
  '是否启用分中心数据权限';

comment on column TAMENU.TYPEFLAG is
  '类标识';

comment on column TAMENU.ISAUDITE is
  '是否需要审核';

comment on column TAMENU.CONSOLEMODULE is
  '是否为工作台模块';

comment on column TAMENU.CUSTOMENCODING is
  '自定义编码';

comment on column TAMENU.ISFILEDSCONTROL is
  '是否需要字段权限控制';

/*==============================================================*/
/* Table: TAMENUPOSITIONYAB003                                  */
/*==============================================================*/
create table TAMENUPOSITIONYAB003 (
  MENUID               NUMERIC(10)          not null,
  POSITIONID           NUMERIC(10)          not null,
  YAB003               VARCHAR(6)           not null,
  constraint PK_TAMENUPOSITIONYAB003 primary key (MENUID, POSITIONID, YAB003)
);

comment on table TAMENUPOSITIONYAB003 is
  '功能数据权限';

comment on column TAMENUPOSITIONYAB003.MENUID is
  '菜单id';

comment on column TAMENUPOSITIONYAB003.POSITIONID is
  '岗位id';

comment on column TAMENUPOSITIONYAB003.YAB003 is
  '分中心';

/*==============================================================*/
/* Table: TAMESSAGE                                             */
/*==============================================================*/
create table TAMESSAGE (
  USERID               NUMERIC(10)          null,
  ORGID                NUMERIC(10)          not null,
  SIGN                 VARCHAR(6)           null default '0',
  MGID                 VARCHAR(20)          not null,
  MGDATE               DATE                 null,
  TITLE                VARCHAR(100)         null,
  CONTENT              TEXT                 null,
  MGCHECK              VARCHAR(1)           null default '0',
  constraint ID primary key (MGID, ORGID)
);

comment on column TAMESSAGE.USERID is
  '发送人员id';

comment on column TAMESSAGE.ORGID is
  '接收组织';

comment on column TAMESSAGE.SIGN is
  '指定岗位标志';

comment on column TAMESSAGE.MGID is
  '消息id';

comment on column TAMESSAGE.MGDATE is
  '发送日期';

comment on column TAMESSAGE.TITLE is
  '通知标题';

comment on column TAMESSAGE.CONTENT is
  '通知内容';

comment on column TAMESSAGE.MGCHECK is
  '0未审核/a通过/b不通过';

/*==============================================================*/
/* Table: TAMESSAGEATTACHMENT                                   */
/*==============================================================*/
create table TAMESSAGEATTACHMENT (
  MGID                 VARCHAR(20)          not null,
  ATTACHMENT           BYTEA                 null,
  NAME                 VARCHAR(50)          null
);

comment on column TAMESSAGEATTACHMENT.MGID is
  '消息id';

comment on column TAMESSAGEATTACHMENT.ATTACHMENT is
  '通知附件';

comment on column TAMESSAGEATTACHMENT.NAME is
  '附件名称';

/*==============================================================*/
/* Table: TAMESSAGESTATE                                        */
/*==============================================================*/
create table TAMESSAGESTATE (
  MGID                 VARCHAR(20)          not null,
  USERID               NUMERIC(10)          not null,
  STATE                VARCHAR(6)           null default '0'
);

comment on column TAMESSAGESTATE.MGID is
  '消息id';

comment on column TAMESSAGESTATE.USERID is
  '接收人员';

comment on column TAMESSAGESTATE.STATE is
  '已读标志';

/*==============================================================*/
/* Table: TAONLINELOG                                           */
/*==============================================================*/
create table TAONLINELOG (
  LOGID                NUMERIC(15)          not null,
  USERID               NUMERIC(10)          not null,
  LOGINTIME            DATE                 not null,
  CURRESOURCE          VARCHAR(1000)        null,
  CLIENTIP             VARCHAR(200)         not null,
  SESSIONID            VARCHAR(200)         not null,
  SYSPATH              VARCHAR(50)          null,
  SERVERIP             VARCHAR(200)         null,
  CLIENTSYSTEM         VARCHAR(50)          null,
  CLIENTBROWSER        VARCHAR(50)          null,
  CLIENTSCREENSIZE     VARCHAR(50)          null,
  constraint PK_TAONLINELOG primary key (LOGID)
);

/*==============================================================*/
/* Table: TAONLINESTATLOG                                       */
/*==============================================================*/
create table TAONLINESTATLOG (
  STATDATE             VARCHAR(20)          not null,
  POINTINTIME          VARCHAR(20)          not null,
  LOGINNUM             NUMERIC(15)          not null,
  constraint PK_TAONLINESTATLOG primary key (STATDATE, POINTINTIME)
);

comment on table TAONLINESTATLOG is
  '时点在线情况统计表';

comment on column TAONLINESTATLOG.STATDATE is
  '统计日期';

comment on column TAONLINESTATLOG.POINTINTIME is
  '统计时间点';

comment on column TAONLINESTATLOG.LOGINNUM is
  '在线人数';

/*==============================================================*/
/* Table: TAORG                                                 */
/*==============================================================*/
create table TAORG (
  ORGID                NUMERIC(10,0)        not null,
  PORGID               NUMERIC(10,0)        null,
  ORGNAME              VARCHAR(60)          null,
  COSTOMNO             VARCHAR(10)          null,
  ORGIDPATH            VARCHAR(1024)        null,
  ORGNAMEPATH          VARCHAR(1024)        null,
  COSTOMNOPATH         VARCHAR(1024)        null,
  ORGTYPE              CHAR(2)              null,
  SORT                 NUMERIC              null,
  YAB003               VARCHAR(6)           null,
  DIMENSION            CHAR(2)              null,
  CREATEUSER           NUMERIC(10)          not null,
  CREATETIME           DATE                 not null,
  EFFECTIVE            CHAR(1)              not null,
  ORGLEVEL             NUMERIC(10)          null,
  ISLEAF               CHAR(1)              null,
  ORGMANAGER           NUMERIC(10)          null,
  DESTORY              CHAR(1)              null,
  TYPEFLAG             NUMERIC(10)          null,
  YAB139               VARCHAR(6)           null,
  constraint PK_TAORG primary key (ORGID)
);

comment on table TAORG is
  '组织视图';

comment on column TAORG.ORGID is
  '组织id';

comment on column TAORG.PORGID is
  '组织父id';

comment on column TAORG.ORGNAME is
  '组织名称';

comment on column TAORG.COSTOMNO is
  '自定义编码';

comment on column TAORG.ORGIDPATH is
  '组织id路径';

comment on column TAORG.ORGNAMEPATH is
  '组织名称路径';

comment on column TAORG.COSTOMNOPATH is
  '自定义编码路径';

comment on column TAORG.ORGTYPE is
  '组织类型（机构（集团、子公司）、部门、组）';

comment on column TAORG.SORT is
  '排序号';

comment on column TAORG.YAB003 is
  '经办机构';

comment on column TAORG.DIMENSION is
  '视图维度';

comment on column TAORG.CREATEUSER is
  '创建人';

comment on column TAORG.CREATETIME is
  '创建时间';

comment on column TAORG.EFFECTIVE is
  '有效性';

comment on column TAORG.ORGLEVEL is
  '组织层级';

comment on column TAORG.ISLEAF is
  '是否叶子节点';

comment on column TAORG.ORGMANAGER is
  '组织负责人（正职）';

comment on column TAORG.DESTORY is
  '是否销毁';

comment on column TAORG.TYPEFLAG is
  '类标识';

comment on column TAORG.YAB139 is
  '数据区';

/*==============================================================*/
/* Table: TAORGMG                                               */
/*==============================================================*/
create table TAORGMG (
  POSITIONID           NUMERIC(10,0)        null,
  ORGID                NUMERIC(10,0)        null
);

comment on table TAORGMG is
  '组织管理表';

comment on column TAORGMG.POSITIONID is
  '岗位id';

comment on column TAORGMG.ORGID is
  '组织id';

/*==============================================================*/
/* Table: TAORGOPLOG                                            */
/*==============================================================*/
create table TAORGOPLOG (
  LOGID                NUMERIC(10,0)        not null,
  BATCHNO              VARCHAR(10)          null,
  OPTYPE               CHAR(2)              null,
  INFLUENCEBODYTYPE    CHAR(2)              null,
  INFLUENCEBODY        VARCHAR(10)          null,
  OPBODY               CHAR(2)              null,
  OPSUBJEKT            VARCHAR(10)          null,
  CHANGCONTENT         VARCHAR(2048)        null,
  OPTIME               DATE                 not null,
  OPUSER               VARCHAR(10)          not null,
  OPPOSITION           VARCHAR(10)          not null,
  ISPERMISSION         VARCHAR(1)           null,
  constraint PK_TAORGOPLOG primary key (LOGID)
);

comment on table TAORGOPLOG is
  '组织及权限操作日志';

comment on column TAORGOPLOG.LOGID is
  '日志id';

comment on column TAORGOPLOG.BATCHNO is
  '操作批次号';

comment on column TAORGOPLOG.OPTYPE is
  '操作类型（新增、编辑、删除、授权、回收权限）';

comment on column TAORGOPLOG.INFLUENCEBODYTYPE is
  '影响主体类型（组织、人员、岗位、角色、权限资源）';

comment on column TAORGOPLOG.INFLUENCEBODY is
  '影响主体';

comment on column TAORGOPLOG.OPBODY is
  '操作主体类型（人员、组织、岗位、角色、权限资源）';

comment on column TAORGOPLOG.OPSUBJEKT is
  '操作主体';

comment on column TAORGOPLOG.CHANGCONTENT is
  '主体变更内容';

comment on column TAORGOPLOG.OPTIME is
  '经办时间';

comment on column TAORGOPLOG.OPUSER is
  '经办人';

comment on column TAORGOPLOG.OPPOSITION is
  '经办岗位';

comment on column TAORGOPLOG.ISPERMISSION is
  '是否有权限';

/*==============================================================*/
/* Table: TAPAGEREVIEW                                          */
/*==============================================================*/
create table TAPAGEREVIEW (
  PAGE_ID              NUMERIC(20)          not null,
  FUNCTION_ID          NUMERIC(20)          null,
  CREATE_TIME          DATE                 null,
  CONTEXT              BYTEA                 null,
  DATA                 BYTEA                 null,
  OLDDATA              BYTEA                 null,
  OTHERDATA            TEXT                 null,
  USERINFO             BYTEA                 null,
  STORETYPE            VARCHAR(20)          null,
  DOC_ID               VARCHAR(20)          null,
  constraint 主键 primary key (PAGE_ID)
);

comment on column TAPAGEREVIEW.PAGE_ID is
  'id';

comment on column TAPAGEREVIEW.FUNCTION_ID is
  '页面功能id';

comment on column TAPAGEREVIEW.CREATE_TIME is
  '创建时间';

comment on column TAPAGEREVIEW.CONTEXT is
  '页面内容';

comment on column TAPAGEREVIEW.DATA is
  '页面新数据';

comment on column TAPAGEREVIEW.OLDDATA is
  '页面老数据';

comment on column TAPAGEREVIEW.OTHERDATA is
  '其它数据';

comment on column TAPAGEREVIEW.USERINFO is
  '人员信息';

comment on column TAPAGEREVIEW.STORETYPE is
  '存储方式';

comment on column TAPAGEREVIEW.DOC_ID is
  'ucm存储标志';

/*==============================================================*/
/* Table: TAPOSITION                                            */
/*==============================================================*/
create table TAPOSITION (
  POSITIONID           NUMERIC(10,0)        not null,
  ORGID                NUMERIC(10,0)        not null,
  POSITIONNAME         VARCHAR(60)          not null,
  POSITIONTYPE         CHAR(1)              not null,
  CREATEPOSITIONID     NUMERIC(10,0)        not null,
  ORGIDPATH            VARCHAR(1024)        null,
  ORGNAMEPATH          VARCHAR(1024)        null,
  VALIDTIME            DATE                 null,
  CREATEUSER           NUMERIC(10,0)        not null,
  CREATETIME           DATE                 not null,
  EFFECTIVE            CHAR(1)              not null,
  ISADMIN              CHAR(1)              null,
  ISSHARE              CHAR(1)              null,
  ISCOPY               CHAR(1)              null,
  TYPEFLAG             NUMERIC(10)          null,
  POSITIONCATEGORY     VARCHAR(2)           null,
  ISDEVELOPER          VARCHAR(1)           null,
  constraint PK_TAPOSITION primary key (POSITIONID)
);

comment on table TAPOSITION is
  '岗位';

comment on column TAPOSITION.POSITIONID is
  '岗位id';

comment on column TAPOSITION.ORGID is
  '组织id';

comment on column TAPOSITION.POSITIONNAME is
  '岗位名称';

comment on column TAPOSITION.POSITIONTYPE is
  '岗位类型（个人专属岗位/公有岗位）';

comment on column TAPOSITION.CREATEPOSITIONID is
  '创建人使用的岗位';

comment on column TAPOSITION.ORGIDPATH is
  '所在组织id路径';

comment on column TAPOSITION.ORGNAMEPATH is
  '所在组织名称路径';

comment on column TAPOSITION.VALIDTIME is
  '只针对委派生成的岗位';

comment on column TAPOSITION.CREATEUSER is
  '创建人';

comment on column TAPOSITION.CREATETIME is
  '创建时间';

comment on column TAPOSITION.EFFECTIVE is
  '有效标志';

comment on column TAPOSITION.ISADMIN is
  '是否管理员';

comment on column TAPOSITION.ISSHARE is
  '是否为共享岗位';

comment on column TAPOSITION.ISCOPY is
  '是否为复制岗位';

comment on column TAPOSITION.TYPEFLAG is
  '类标识';

comment on column TAPOSITION.POSITIONCATEGORY is
  '岗位类别';

comment on column TAPOSITION.ISDEVELOPER is
  '是否是超级管理员';

/*==============================================================*/
/* Table: TAPOSITIONAUTHRITY                                    */
/*==============================================================*/
create table TAPOSITIONAUTHRITY (
  POSITIONID           NUMERIC(10,0)        not null,
  MENUID               NUMERIC(10,0)        not null,
  USEPERMISSION        CHAR(1)              null,
  REPERMISSION         CHAR(1)              null,
  REAUTHRITY           CHAR(1)              null,
  CREATEUSER           NUMERIC(10,0)        not null,
  CREATETIME           DATE                 not null,
  EFFECTTIME           DATE                 null,
  AUDITEACCESSDATE     DATE                 null,
  AUDITEUSER           NUMERIC(10)          null,
  AUDITSTATE           VARCHAR(1)           null default '0',
  constraint PK_TAPOSITIONAUTHRITY primary key (POSITIONID, MENUID)
);

comment on table TAPOSITIONAUTHRITY is
  '岗位权限表';

comment on column TAPOSITIONAUTHRITY.POSITIONID is
  '岗位id';

comment on column TAPOSITIONAUTHRITY.MENUID is
  '菜单Id';

comment on column TAPOSITIONAUTHRITY.USEPERMISSION is
  '使用权限';

comment on column TAPOSITIONAUTHRITY.REPERMISSION is
  '授权别人使用权限';

comment on column TAPOSITIONAUTHRITY.REAUTHRITY is
  '授权别人授权权限';

comment on column TAPOSITIONAUTHRITY.CREATEUSER is
  '创建人';

comment on column TAPOSITIONAUTHRITY.CREATETIME is
  '创建时间';

comment on column TAPOSITIONAUTHRITY.EFFECTTIME is
  '有效时间';

comment on column TAPOSITIONAUTHRITY.AUDITEACCESSDATE is
  '审核通过时间';

comment on column TAPOSITIONAUTHRITY.AUDITEUSER is
  '审核人';

comment on column TAPOSITIONAUTHRITY.AUDITSTATE is
  '审核状态';

/*==============================================================*/
/* Table: TARUNQIANAD52REFERENCE                                */
/*==============================================================*/
create table TARUNQIANAD52REFERENCE (
  MENUID               NUMERIC(10)          null,
  RAQFILENAME          VARCHAR(200)         null,
  LIMITED              NUMERIC              null,
  SCALEEXP             NUMERIC              null,
  ISGROUP              VARCHAR(6)           null,
  NEEDSAVEASEXCEL      VARCHAR(6)           null,
  NEEDSAVEASEXCEL2007  VARCHAR(6)           null,
  NEEDSAVEASPDF        VARCHAR(6)           null,
  NEEDSAVEASWORD       VARCHAR(6)           null,
  NEEDSAVEASTEXT       VARCHAR(6)           null,
  NEEDPRINT            VARCHAR(6)           null,
  ID                   NUMERIC(10)          not null,
  constraint PK_REPORT_INFO primary key (ID)
);

comment on table TARUNQIANAD52REFERENCE is
  'YHCIP_RUNQIAN_AD52_REFERENCE润乾报表菜单信息';

comment on column TARUNQIANAD52REFERENCE.MENUID is
  '功能编号';

comment on column TARUNQIANAD52REFERENCE.RAQFILENAME is
  '文件名/报表标识（RaqfileName）';

comment on column TARUNQIANAD52REFERENCE.LIMITED is
  '每页显示数（Limited）';

comment on column TARUNQIANAD52REFERENCE.SCALEEXP is
  'JSP中缩放比率（ScaleExp）';

comment on column TARUNQIANAD52REFERENCE.ISGROUP is
  '是否按行分页（IsGroup）';

comment on column TARUNQIANAD52REFERENCE.NEEDSAVEASEXCEL is
  '是否保存为Excel（NeedSaveAsExcel）';

comment on column TARUNQIANAD52REFERENCE.NEEDSAVEASEXCEL2007 is
  '是否保存为Excel2007（NeedSaveAsExcel2007）';

comment on column TARUNQIANAD52REFERENCE.NEEDSAVEASPDF is
  '是否保存为Pdf（NeedSaveAsPdf）';

comment on column TARUNQIANAD52REFERENCE.NEEDSAVEASWORD is
  '是否保存为Word（NeedSaveAsWord）';

comment on column TARUNQIANAD52REFERENCE.NEEDSAVEASTEXT is
  '是否保存为Text（NeedSaveAsText）';

comment on column TARUNQIANAD52REFERENCE.NEEDPRINT is
  '是否保存为Print（NeedPrint）';

comment on column TARUNQIANAD52REFERENCE.ID is
  '主键ID';

/*==============================================================*/
/* Table: TARUNQIANPRINTSETUP                                   */
/*==============================================================*/
create table TARUNQIANPRINTSETUP (
  SETUPID              VARCHAR(200)         not null,
  SETUPVALUE           VARCHAR(400)         not null,
  constraint PK_PRINTSETUP primary key (SETUPID)
);

comment on table TARUNQIANPRINTSETUP is
  '打印设置信息表';

comment on column TARUNQIANPRINTSETUP.SETUPID is
  '打印设置编号（SetupId）';

comment on column TARUNQIANPRINTSETUP.SETUPVALUE is
  '打印设置信息（SetupValue）';

/*==============================================================*/
/* Table: TARUNQIANRESOURCE                                     */
/*==============================================================*/
create table TARUNQIANRESOURCE (
  RAQFILENAME          VARCHAR(200)         not null,
  PARENTRAQFILENAME    VARCHAR(200)         null,
  RAQNAME              VARCHAR(200)         null,
  RAQTYPE              VARCHAR(6)           null,
  RAQFILE              BYTEA                 null,
  UPLOADOR             VARCHAR(19)          null,
  UPLOADTIME           DATE                 null,
  SUBROW               NUMERIC              null,
  SUBCELL              NUMERIC              null,
  RAQDATASOURCE        VARCHAR(19)          null,
  RAQPARAM             VARCHAR(500)         null,
  ORGID                VARCHAR(15)          null,
  constraint PK_YHCIP_RUNQIAN_RESOURCE primary key (RAQFILENAME)
);

comment on table TARUNQIANRESOURCE is
  '润乾报表模板';

comment on column TARUNQIANRESOURCE.RAQFILENAME is
  '文件名/报表标识（RaqfileName）';

comment on column TARUNQIANRESOURCE.PARENTRAQFILENAME is
  '父报表标识（ParentRaqfileName）';

comment on column TARUNQIANRESOURCE.RAQNAME is
  '报表名称（RaqName）';

comment on column TARUNQIANRESOURCE.RAQTYPE is
  '报表类型（RaqType）';

comment on column TARUNQIANRESOURCE.RAQFILE is
  '资源文件（RaqFile）';

comment on column TARUNQIANRESOURCE.UPLOADOR is
  '上传人（Uploador）';

comment on column TARUNQIANRESOURCE.UPLOADTIME is
  '上传时间（UploadTime）';

comment on column TARUNQIANRESOURCE.SUBROW is
  '父报表位置行（SubRow）';

comment on column TARUNQIANRESOURCE.SUBCELL is
  '父报表位置列（SubCell）';

comment on column TARUNQIANRESOURCE.RAQDATASOURCE is
  '数据源（RaqDataSource）';

comment on column TARUNQIANRESOURCE.RAQPARAM is
  '报表参数JSON格式Str（RaqParam）';

comment on column TARUNQIANRESOURCE.ORGID is
  '部门编号(OrgId)';

/*==============================================================*/
/* Table: TASERVEREXCEPTIONLOG                                  */
/*==============================================================*/
create table TASERVEREXCEPTIONLOG (
  ID                   VARCHAR(50)          not null,
  IPADDRESS            VARCHAR(255)         null,
  PORT                 VARCHAR(10)          null,
  TYPE                 VARCHAR(255)         null,
  CONTENT              BYTEA                 null,
  TIME               DATE                 null,
  SYSPATH              VARCHAR(50)          null,
  CLIENTIP             VARCHAR(50)          null,
  URL                  VARCHAR(100)         null,
  MENUID               VARCHAR(8)           null,
  MENUNAME             VARCHAR(30)          null,
  USERAGENT            VARCHAR(200)         null,
  EXCEPTIONTYPE        VARCHAR(2)           null,
  PARAMETER            BYTEA                 null
);

comment on table TASERVEREXCEPTIONLOG is
  '系统异常日志表';

comment on column TASERVEREXCEPTIONLOG.ID is
  'id';

comment on column TASERVEREXCEPTIONLOG.IPADDRESS is
  '服务器ip地址';

comment on column TASERVEREXCEPTIONLOG.PORT is
  '服务器端口';

comment on column TASERVEREXCEPTIONLOG.TYPE is
  '异常类型';

comment on column TASERVEREXCEPTIONLOG.CONTENT is
  '异常内容';

comment on column TASERVEREXCEPTIONLOG.TIME is
  '报错时间';

comment on column TASERVEREXCEPTIONLOG.SYSPATH is
  '系统标识';

comment on column TASERVEREXCEPTIONLOG.CLIENTIP is
  '客户端ip';

comment on column TASERVEREXCEPTIONLOG.URL is
  '访问功能url';

comment on column TASERVEREXCEPTIONLOG.MENUID is
  '菜单id';

comment on column TASERVEREXCEPTIONLOG.MENUNAME is
  '菜单名称';

comment on column TASERVEREXCEPTIONLOG.USERAGENT is
  '客户端环境';

comment on column TASERVEREXCEPTIONLOG.EXCEPTIONTYPE is
  '异常分类(1 系统异常  2业务异常)';

comment on column TASERVEREXCEPTIONLOG.PARAMETER is
  '传入参数';

/*==============================================================*/
/* Table: TASHAREPOSITION                                       */
/*==============================================================*/
create table TASHAREPOSITION (
  SPOSITIONID          NUMERIC(10)          null,
  DPOSITIONID          NUMERIC(10)          null
);

comment on table TASHAREPOSITION is
  '共享岗位表';

comment on column TASHAREPOSITION.SPOSITIONID is
  '源岗位id';

comment on column TASHAREPOSITION.DPOSITIONID is
  '复制岗位id';

/*==============================================================*/
/* Table: TAUSER                                                */
/*==============================================================*/
create table TAUSER (
  USERID               NUMERIC(10,0)        not null,
  NAME                 VARCHAR(60)          not null,
  SEX                  CHAR(1)              null,
  LOGINID              VARCHAR(20)          not null,
  PASSWORD             VARCHAR(50)          not null,
  PASSWORDFAULTNUM     NUMERIC              null,
  PWDLASTMODIFYDATE    DATE                 null,
  ISLOCK               CHAR(1)              null,
  SORT                 NUMERIC              null,
  EFFECTIVE            CHAR(1)              not null,
  TEL                  VARCHAR(15)          null,
  AUTHMODE             VARCHAR(2)           null,
  CREATEUSER           NUMERIC(10,0)        null,
  CREATETIME           DATE                 not null,
  DIRECTORGID          NUMERIC(10,0)        not null,
  DESTORY              CHAR(1)              null,
  TYPEFLAG             NUMERIC(10,0)        null,
  constraint PK_TAUSER primary key (USERID)
);

comment on table TAUSER is
  '人员';

comment on column TAUSER.USERID is
  '人员id';

comment on column TAUSER.NAME is
  '姓名';

comment on column TAUSER.SEX is
  '性别';

comment on column TAUSER.LOGINID is
  '登陆账号';

comment on column TAUSER.PASSWORD is
  '密码';

comment on column TAUSER.PASSWORDFAULTNUM is
  '口令错误次数';

comment on column TAUSER.PWDLASTMODIFYDATE is
  '口令最后修改时间';

comment on column TAUSER.ISLOCK is
  '锁定标志';

comment on column TAUSER.SORT is
  '排序号';

comment on column TAUSER.EFFECTIVE is
  '有效标志';

comment on column TAUSER.TEL is
  '联系电话';

comment on column TAUSER.AUTHMODE is
  '权限认证方式';

comment on column TAUSER.CREATEUSER is
  '创建人';

comment on column TAUSER.CREATETIME is
  '创建时间';

comment on column TAUSER.DIRECTORGID is
  '直属组织';

comment on column TAUSER.DESTORY is
  '是否销毁';

comment on column TAUSER.TYPEFLAG is
  '类标识';

/*==============================================================*/
/* Table: TAUSERPOSITION                                        */
/*==============================================================*/
create table TAUSERPOSITION (
  USERID               NUMERIC(10,0)        not null,
  POSITIONID           NUMERIC(10,0)        not null,
  MAINPOSITION         CHAR(1)              not null,
  CREATEUSER           NUMERIC(10,0)        not null,
  CREATETIME           DATE                 not null,
  constraint PK_TAUSERPOSITION primary key (USERID, POSITIONID)
);

comment on table TAUSERPOSITION is
  '人员岗位关系表';

comment on column TAUSERPOSITION.USERID is
  '人员id';

comment on column TAUSERPOSITION.POSITIONID is
  '岗位id';

comment on column TAUSERPOSITION.MAINPOSITION is
  '默认岗位';

comment on column TAUSERPOSITION.CREATEUSER is
  '创建人';

comment on column TAUSERPOSITION.CREATETIME is
  '创建时间';

/*==============================================================*/
/* Table: TAYAB003LEVELMG                                       */
/*==============================================================*/
create table TAYAB003LEVELMG (
  PYAB003              VARCHAR(6)           null,
  YAB003               VARCHAR(6)           null
);

comment on table TAYAB003LEVELMG is
  '经办机构层级关系管理';

comment on column TAYAB003LEVELMG.PYAB003 is
  '父经办机构';

comment on column TAYAB003LEVELMG.YAB003 is
  '经办机构';

/*==============================================================*/
/* Table: TAYAB003SCOPE                                         */
/*==============================================================*/
create table TAYAB003SCOPE (
  YAB003               VARCHAR(6)           not null,
  YAB139               VARCHAR(6)           not null,
  constraint PK_TAYAB003SCOPE primary key (YAB003, YAB139)
);

comment on table TAYAB003SCOPE is
  '分中心数据权限范围表';

comment on column TAYAB003SCOPE.YAB003 is
  '分中心';

comment on column TAYAB003SCOPE.YAB139 is
  '分中心数据权限';

/*==============================================================*/
/* Table: TAYAB139MG                                            */
/*==============================================================*/
create table TAYAB139MG (
  YAB003               VARCHAR(6)           null,
  YAB139               VARCHAR(20)          null
);

comment on table TAYAB139MG is
  '经办机构管理数据区范围';

comment on column TAYAB139MG.YAB003 is
  '经办机构';

comment on column TAYAB139MG.YAB139 is
  '数据区';

/*==============================================================*/
/* Table: TEMPLATEMANAGER                                       */
/*==============================================================*/
create table TEMPLATEMANAGER (
  TEMPLATEID           VARCHAR(50)          not null,
  TEMPLATENAME         VARCHAR(50)          not null,
  TEMPLATECONTENT      BYTEA                 null,
  TEMPLATETYPE         VARCHAR(10)          null,
  CREATEPEOPLE         NUMERIC(10)          null,
  CREATETIME           DATE                 null,
  EFFECTIVE            CHAR(1)              null,
  CONTENT              VARCHAR(20)          null
);

/*==============================================================*/
/* Table: YHCIP_ORACLE_JOBS                                     */
/*==============================================================*/
create table YHCIP_ORACLE_JOBS (
  JOBID                VARCHAR(20)          not null,
  ORACLEJOBID          VARCHAR(20)          not null,
  JOBNAME              VARCHAR(100)         not null,
  USERID               VARCHAR(20)          not null,
  WHAT                 VARCHAR(200)         not null,
  STARTTIME            VARCHAR(200)         not null,
  ENDTIME              DATE                 null,
  "INTERVAL"           VARCHAR(200)         null,
  SUBMITTIME           DATE                 null,
  constraint PK_YHCIP_ORACLE_JOBS primary key (JOBID)
);

comment on table YHCIP_ORACLE_JOBS is
  'oracle定时';

comment on column YHCIP_ORACLE_JOBS.JOBID is
  '任务id';

comment on column YHCIP_ORACLE_JOBS.ORACLEJOBID is
  'oracle的jobid';

comment on column YHCIP_ORACLE_JOBS.JOBNAME is
  '任务名称';

comment on column YHCIP_ORACLE_JOBS.USERID is
  '用户id';

comment on column YHCIP_ORACLE_JOBS.WHAT is
  '执行的过程';

comment on column YHCIP_ORACLE_JOBS.STARTTIME is
  '开始时间';

comment on column YHCIP_ORACLE_JOBS.ENDTIME is
  '结束时间';

comment on column YHCIP_ORACLE_JOBS."INTERVAL" is
  '时间间隔';

comment on column YHCIP_ORACLE_JOBS.SUBMITTIME is
  '提交时间';

/*==============================================================*/
/* Table: TAZKADDRESSMG                                     */
/*==============================================================*/
create table TAZKADDRESSMG (
  ZKID               VARCHAR(20)          not null,
  ZKADDRESS          VARCHAR(200),
  APPNAME            VARCHAR(60),
  APPNAMESPACE       VARCHAR(30),
  constraint PK_TAZKADDRESSMG primary key (ZKID)
);

comment on table TAZKADDRESSMG is
  'zk注册中心管理';

comment on column TAZKADDRESSMG.ZKID is
  'zk注册中心id';

comment on column TAZKADDRESSMG.ZKADDRESS is
  'zk地址';

comment on column TAZKADDRESSMG.APPNAME is
  '应用名称';

comment on column TAZKADDRESSMG.APPNAMESPACE is
  'zk命名空间';

/*==============================================================*/
/* Table: JOB_BUSY_FREE_MG                                     */
/*==============================================================*/
create table JOB_BUSY_FREE_MG (
  JOBSTATUS     VARCHAR(2),
  ZKID          VARCHAR(20) not null,
  CRON          VARCHAR(20),
  JOBTYPE       VARCHAR(2),
  JOBNAME       VARCHAR(20),
  IPS           VARCHAR(20),
  EBFID         VARCHAR(20) not null,
  constraint PK_JOB_BUSY_FREE_MG primary key (EBFID)
);

comment on table JOB_BUSY_FREE_MG is
  '闲忙任务管理';

comment on column JOB_BUSY_FREE_MG.JOBSTATUS is
  '任务状态';

comment on column JOB_BUSY_FREE_MG.ZKID is
  '注册中心zkid';

comment on column JOB_BUSY_FREE_MG.CRON is
  '闲忙时间点';

comment on column JOB_BUSY_FREE_MG.JOBTYPE is
  '任务类型';

comment on column JOB_BUSY_FREE_MG.JOBNAME is
  '任务名称';

comment on column JOB_BUSY_FREE_MG.IPS is
  'ip地址';

comment on column JOB_BUSY_FREE_MG.EBFID is
  '闲忙任务id';

/*==============================================================*/
/* View: AA10A1                                                 */
/*==============================================================*/
create or replace view AA10A1 as
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

/*==============================================================*/
/* View: V_TALOGINLOG                                           */
/*==============================================================*/
create or replace view V_TALOGINLOG as
  select
    a.LOGID,
    a.USERID,
    (SELECT name FROM tauser where userid = a.userid) as username,
    a.LOGINTIME,
    a.CLIENTIP,
    a.SESSIONID,
    a.SYSPATH,
    a.SERVERIP,
    COALESCE(a.clientsystem,'UNKNOW') as clientsystem,
    COALESCE(a.clientbrowser,'UNKNOW') as clientbrowser,
    COALESCE(a.clientscreensize,'UNKNOW') as clientscreensize
  from
    TAONLINELOG a

  Union
  select
    a.LOGID,
    a.USERID,
    (SELECT name FROM tauser where userid = a.userid) as username,
    a.LOGINTIME,
    a.CLIENTIP,
    a.SESSIONID,
    a.SYSPATH,
    a.SERVERIP,
    COALESCE(a.clientsystem,'UNKNOW') as clientsystem,
    COALESCE(a.clientbrowser,'UNKNOW') as clientbrowser,
    COALESCE(a.clientscreensize,'UNKNOW') as clientscreensize
  from
    TALOGINHISTORYLOG a;

comment on view V_TALOGINLOG is
  '用户登录日志';

/*==============================================================*/
/* View: V_YAB139                                               */
/*==============================================================*/
create or replace view V_YAB139 as
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
        AND (U.DESTORY IS NULL OR U.DESTORY='0')
        AND U.EFFECTIVE='1'
        AND UP.POSITIONID=P.POSITIONID
        AND P.EFFECTIVE='1'
        AND (P.VALIDTIME IS NULL OR P.VALIDTIME>=now())
        AND P.POSITIONID=DAD.POSITIONID
        AND DAD.MENUID=M.MENUID
        AND DAD.DIMENSIONTYPE='YAB139'
        AND DAD.ALLACCESS='0'
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
        AND (U.DESTORY IS NULL OR U.DESTORY='0')
        AND U.EFFECTIVE='1'
        AND UP.POSITIONID=P.POSITIONID
        AND P.EFFECTIVE='1'
        AND (P.VALIDTIME IS NULL OR P.VALIDTIME>=now())
        AND P.POSITIONID=DAD.POSITIONID
        AND DAD.MENUID=M.MENUID
        AND DAD.DIMENSIONTYPE='YAB139'
        AND DAD.ALLACCESS<>'0'
        AND A.AAA100='YAB139';

alter table QRTZ_BLOB_TRIGGERS
  add constraint QRTZ_BLOB_TRIG_TO_TRIG_FK foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
references QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
on delete restrict on update restrict;

alter table QRTZ_CRON_TRIGGERS
  add constraint QRTZ_CRON_TRIG_TO_TRIG_FK foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
references QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
on delete restrict on update restrict;

alter table QRTZ_SIMPLE_TRIGGERS
  add constraint QRTZ_SIMPLE_TRIG_TO_TRIG_FK foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
references QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
on delete restrict on update restrict;

alter table QRTZ_SIMPROP_TRIGGERS
  add constraint QRTZ_SIMPROP_TRIG_TO_TRIG_FK foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
references QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
on delete restrict on update restrict;

alter table QRTZ_TRIGGERS
  add constraint QRTZ_TRIGGER_TO_JOBS_FK foreign key (SCHED_NAME, JOB_NAME, JOB_GROUP)
references QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP)
on delete restrict on update restrict;

alter table TAORG
  add constraint FK_REFERENCE_6 foreign key (PORGID)
references TAORG (ORGID)
on delete restrict on update restrict;

alter table TAORGMG
  add constraint FK_REFERENCE_8 foreign key (ORGID)
references TAORG (ORGID)
on delete restrict on update restrict;

alter table TAORGMG
  add constraint FK_REFERENCE_9 foreign key (POSITIONID)
references TAPOSITION (POSITIONID)
on delete restrict on update restrict;

alter table TAPOSITION
  add constraint FK_RELATIONSHIP_5 foreign key (ORGID)
references TAORG (ORGID)
on delete restrict on update restrict;

alter table TAPOSITIONAUTHRITY
  add constraint FK_REFERENCE_7 foreign key (MENUID)
references TAMENU (MENUID)
on delete restrict on update restrict;

alter table TAPOSITIONAUTHRITY
  add constraint FK_RELATIONSHIP_11 foreign key (POSITIONID)
references TAPOSITION (POSITIONID)
on delete restrict on update restrict;

alter table TARUNQIANAD52REFERENCE
  add constraint FK_REPORT_INFO foreign key (RAQFILENAME)
references TARUNQIANRESOURCE (RAQFILENAME)
on delete restrict on update restrict;

alter table TARUNQIANRESOURCE
  add constraint FK_YHCIP_RU_REFERENCE_YHCIP_RU foreign key (PARENTRAQFILENAME)
references TARUNQIANRESOURCE (RAQFILENAME)
on delete restrict on update restrict;

alter table TAUSERPOSITION
  add constraint FK_RELATIONSHIP_10 foreign key (POSITIONID)
references TAPOSITION (POSITIONID)
on delete restrict on update restrict;

alter table TAUSERPOSITION
  add constraint FK_RELATIONSHIP_9 foreign key (USERID)
references TAUSER (USERID)
on delete restrict on update restrict;


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


INSERT INTO TAORG (ORGID,PORGID,ORGNAME,COSTOMNO,ORGIDPATH,ORGNAMEPATH,COSTOMNOPATH,ORGTYPE,SORT,YAB003,DIMENSION,CREATEUSER,CREATETIME,EFFECTIVE,ORGLEVEL,ISLEAF,TYPEFLAG,YAB139) VALUES (1,1,'银海软件','1','1','银海软件','0','01',0,'9999',NULL,0,now(),'1',0,'1',0,'9999');

INSERT INTO TAUSER (USERID,NAME,SEX,LOGINID,PASSWORD,PASSWORDFAULTNUM,PWDLASTMODIFYDATE,ISLOCK,SORT,EFFECTIVE,TEL,CREATEUSER,CREATETIME,DIRECTORGID,TYPEFLAG,AUTHMODE) VALUES (1,'超级管理员','1','developer','29PYtt0CYAXxrlJgzd/HUg==',0,now(),'0',0,'1','0',1,now(),1,0,'01');

INSERT INTO TAPOSITION (POSITIONID,ORGID,POSITIONNAME,POSITIONTYPE,CREATEPOSITIONID,ORGIDPATH,ORGNAMEPATH,VALIDTIME,CREATEUSER,CREATETIME,EFFECTIVE,ISADMIN,TYPEFLAG,ISDEVELOPER) VALUES (1,1,'超级管理员','2',1,'1','银海软件',NULL,1,now(),'1','1',0,1);

INSERT INTO TAUSERPOSITION (USERID,POSITIONID,MAINPOSITION,CREATEUSER,CREATETIME) VALUES (1,1,'1',1,now());

INSERT INTO TALOCALCACHEVERSION(VERSION, CODETYPE) VALUES (1,'');

INSERT INTO TAYAB003LEVELMG (PYAB003, YAB003) VALUES ('0', '9999');
