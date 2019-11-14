--drop table
DROP VIEW IF EXISTS V_YAB139;

DROP VIEW IF EXISTS V_TALOGINLOG;

DROP VIEW IF EXISTS AA10A1;

DROP TABLE IF EXISTS AA10 cascade;

DROP TABLE IF EXISTS CLUSTERCONFIG cascade;

DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS cascade;

DROP TABLE IF EXISTS QRTZ_CALENDARS cascade;

DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS cascade;

DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS cascade;

DROP TABLE IF EXISTS QRTZ_JOB_DETAILS cascade;

DROP TABLE IF EXISTS QRTZ_JOB_LOG cascade;

DROP TABLE IF EXISTS QRTZ_JOB_MSGS cascade;

DROP TABLE IF EXISTS QRTZ_LOCKS cascade;

DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS cascade;

DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE cascade;

DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS cascade;

DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS cascade;

DROP TABLE IF EXISTS QRTZ_TRIGGERS cascade;

DROP TABLE IF EXISTS SIGNRECORD cascade;

DROP TABLE IF EXISTS TAACCESSLOG cascade;

DROP TABLE IF EXISTS TAACCESSSYSTEM cascade;

DROP TABLE IF EXISTS TAADMINYAB139SCOPE cascade;

DROP TABLE IF EXISTS TACOMMONMENU cascade;

DROP TABLE IF EXISTS TACONFIG cascade;

DROP TABLE IF EXISTS TACONFIGSYSPATH cascade;

DROP TABLE IF EXISTS TACONSOLEMODULE cascade;

DROP TABLE IF EXISTS TACONSOLEMODULELOCATION cascade;

DROP TABLE IF EXISTS TACONSOLEMODULEPRIVILEGE cascade;

DROP TABLE IF EXISTS TADATAACCESSDIMENSION cascade;

DROP TABLE IF EXISTS TADATASOURCE cascade;

DROP TABLE IF EXISTS TAFIELD cascade;

DROP TABLE IF EXISTS TAFIELDAUTHRITY cascade;

DROP TABLE IF EXISTS TALIMITRATE cascade;

DROP TABLE IF EXISTS TALOCALCACHEVERSION cascade;

DROP TABLE IF EXISTS TALOGINHISTORYLOG cascade;

DROP TABLE IF EXISTS TALOGINSTATLOG cascade;

DROP TABLE IF EXISTS TAMANAGERMG cascade;

DROP TABLE IF EXISTS TAMENU cascade;

DROP TABLE IF EXISTS TAMENUPOSITIONYAB003 cascade;

DROP TABLE IF EXISTS TAMESSAGE cascade;

DROP TABLE IF EXISTS TAMESSAGEATTACHMENT cascade;

DROP TABLE IF EXISTS TAMESSAGESTATE cascade;

DROP TABLE IF EXISTS TAONLINELOG cascade;

DROP TABLE IF EXISTS TAONLINESTATLOG cascade;

DROP TABLE IF EXISTS TAORG cascade;

DROP TABLE IF EXISTS TAORGMG cascade;

DROP TABLE IF EXISTS TAORGOPLOG cascade;

DROP TABLE IF EXISTS TAPAGEREVIEW cascade;

DROP TABLE IF EXISTS TAPOSITION cascade;

DROP TABLE IF EXISTS TAPOSITIONAUTHRITY cascade;

DROP TABLE IF EXISTS TARUNQIANAD52REFERENCE cascade;

DROP TABLE IF EXISTS TARUNQIANPRINTSETUP cascade;

DROP TABLE IF EXISTS TARUNQIANRESOURCE cascade;

DROP TABLE IF EXISTS TASERVEREXCEPTIONLOG cascade;

DROP TABLE IF EXISTS TASHAREPOSITION cascade;

DROP TABLE IF EXISTS TAUSER cascade;

DROP TABLE IF EXISTS TAUSERPOSITION cascade;

DROP TABLE IF EXISTS TAYAB003LEVELMG cascade;

DROP TABLE IF EXISTS TAYAB003SCOPE cascade;

DROP TABLE IF EXISTS TAYAB139MG cascade;

DROP TABLE IF EXISTS TEMPLATEMANAGER cascade;

DROP TABLE IF EXISTS YHCIP_ORACLE_JOBS cascade;

DROP TABLE IF EXISTS TAZKADDRESSMG cascade;

DROP TABLE IF EXISTS JOB_BUSY_FREE_MG cascade;

DROP SEQUENCE IF EXISTS HIBERNATE_SEQUENCE;

DROP SEQUENCE IF EXISTS SEQ_DEFAULT;

DROP SEQUENCE IF EXISTS SEQ_MESSAGE;

DROP SEQUENCE IF EXISTS TEMPLATE_SEQUENCE;


-- Create table

CREATE TABLE ta410_test:aa10 (
	aaa100 VARCHAR(20) NOT NULL,
	aaa101 VARCHAR(50) NOT NULL,
	aaa102 VARCHAR(20) NOT NULL,
	aaa103 VARCHAR(50) NOT NULL,
	yab003 VARCHAR(6) NOT NULL,
	aae120 VARCHAR(6) NOT NULL,
	ver DECIMAL(10,0),
	PRIMARY KEY (aaa100,aaa102) CONSTRAINT PK_AA10
)
 in data ;

CREATE TABLE ta410_test:clusterconfig (
	clusterid DECIMAL(10,0) NOT NULL,
	clustername VARCHAR(200),
	clusterapp VARCHAR(200),
	clusterurl VARCHAR(200) NOT NULL,
	clusterdesc VARCHAR(200),
	PRIMARY KEY (clusterid) CONSTRAINT PK_CLUSTERCONFIG
)
 in data ;

CREATE TABLE ta410_test:qrtz_blob_triggers (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME varchar(80) NOT NULL,
TRIGGER_GROUP varchar(80) NOT NULL,
BLOB_DATA byte in table
);

ALTER TABLE ta410_test:qrtz_blob_triggers
ADD CONSTRAINT PRIMARY KEY (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP);


CREATE TABLE ta410_test:qrtz_calendars (
SCHED_NAME VARCHAR(120) NOT NULL,
CALENDAR_NAME varchar(80) NOT NULL,
CALENDAR byte in table NOT NULL
);

ALTER TABLE ta410_test:qrtz_calendars
ADD CONSTRAINT PRIMARY KEY (SCHED_NAME,CALENDAR_NAME);


CREATE TABLE ta410_test:qrtz_cron_triggers (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME varchar(80) NOT NULL,
TRIGGER_GROUP varchar(80) NOT NULL,
CRON_EXPRESSION varchar(120) NOT NULL,
TIME_ZONE_ID varchar(80)
);

ALTER TABLE ta410_test:qrtz_cron_triggers
ADD CONSTRAINT PRIMARY KEY (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP);


CREATE TABLE ta410_test:qrtz_fired_triggers (
SCHED_NAME VARCHAR(120) NOT NULL,
ENTRY_ID varchar(95) NOT NULL,
TRIGGER_NAME varchar(80) NOT NULL,
TRIGGER_GROUP varchar(80) NOT NULL,
INSTANCE_NAME varchar(80) NOT NULL,
FIRED_TIME numeric(13) NOT NULL,
SCHED_TIME numeric(13) NOT NULL,
PRIORITY integer NOT NULL,
STATE varchar(16) NOT NULL,
JOB_NAME varchar(80),
JOB_GROUP varchar(80),
IS_NONCONCURRENT varchar(5),
REQUESTS_RECOVERY varchar(5)
);

ALTER TABLE ta410_test:qrtz_fired_triggers
ADD CONSTRAINT PRIMARY KEY (SCHED_NAME,ENTRY_ID);


CREATE TABLE ta410_test:qrtz_paused_trigger_grps (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_GROUP  varchar(80) NOT NULL
);

ALTER TABLE ta410_test:qrtz_paused_trigger_grps
ADD CONSTRAINT PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP);


CREATE TABLE ta410_test:qrtz_scheduler_state (
SCHED_NAME VARCHAR(120) NOT NULL,
INSTANCE_NAME varchar(80) NOT NULL,
LAST_CHECKIN_TIME numeric(13) NOT NULL,
CHECKIN_INTERVAL numeric(13) NOT NULL
);

ALTER TABLE ta410_test:qrtz_scheduler_state
ADD CONSTRAINT PRIMARY KEY (SCHED_NAME,INSTANCE_NAME);


CREATE TABLE ta410_test:qrtz_locks (
SCHED_NAME VARCHAR(120) NOT NULL,
LOCK_NAME  varchar(40) NOT NULL
);

ALTER TABLE ta410_test:qrtz_locks
ADD CONSTRAINT PRIMARY KEY (SCHED_NAME,LOCK_NAME);
alter table qrtz_locks lock mode(row);


CREATE TABLE ta410_test:qrtz_job_details (
SCHED_NAME VARCHAR(120) NOT NULL,
JOB_NAME varchar(80) NOT NULL,
JOB_GROUP varchar(80) NOT NULL,
DESCRIPTION varchar(120),
JOB_CLASS_NAME varchar(128) NOT NULL,
IS_DURABLE varchar(5) NOT NULL,
IS_NONCONCURRENT varchar(5) NOT NULL,
IS_UPDATE_DATA varchar(5) NOT NULL,
REQUESTS_RECOVERY varchar(5) NOT NULL,
JOB_DATA byte in table
);

ALTER TABLE ta410_test:qrtz_job_details
ADD CONSTRAINT PRIMARY KEY (SCHED_NAME,JOB_NAME, JOB_GROUP);

create table ta410_test:QRTZ_JOB_LOG
(
  LOG_ID     VARCHAR(17) not NULL,
  JOB_NAME   VARCHAR(200) not NULL,
  ADDRESS    VARCHAR(200) not NULL,
  SERVICE_ID VARCHAR(200) not NULL,
  FIRED_TIME DATE not NULL,
  SUCCESS    VARCHAR(6) not NULL,
  LOG_MSG    VARCHAR(255)
);
ALTER TABLE ta410_test:QRTZ_JOB_LOG
ADD CONSTRAINT PRIMARY KEY (LOG_ID);

CREATE TABLE ta410_test:QRTZ_JOB_MSGS  (
   ID                   VARCHAR(50)                    NOT NULL,
   JOB_NAME             VARCHAR(80)                    NOT NULL,
   JOB_GROUP            VARCHAR(80)                    NOT NULL,
   USERID               VARCHAR(20),
   EXECSTARTTIME        VARCHAR(30),
   EXECENDTIME          VARCHAR(30),
   ISSUCCESS            VARCHAR(2),
   SUCCESSMSG           LVARCHAR(1024),
   ERRORMSG             LVARCHAR(1024)
);
ALTER TABLE ta410_test:QRTZ_JOB_MSGS
ADD CONSTRAINT PRIMARY KEY (ID);

CREATE TABLE ta410_test:qrtz_simprop_triggers
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 LVARCHAR(512) NULL,
    STR_PROP_2 LVARCHAR(512) NULL,
    STR_PROP_3 LVARCHAR(512) NULL,
    INT_PROP_1 DECIMAL(10) NULL,
    INT_PROP_2 DECIMAL(10) NULL,
    LONG_PROP_1 DECIMAL(13) NULL,
    LONG_PROP_2 DECIMAL(13) NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(5) NULL,
    BOOL_PROP_2 VARCHAR(5) NULL
);

CREATE TABLE ta410_test:qrtz_simple_triggers (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME varchar(80) NOT NULL,
TRIGGER_GROUP varchar(80) NOT NULL,
REPEAT_COUNT numeric(7) NOT NULL,
REPEAT_INTERVAL numeric(12) NOT NULL,
TIMES_TRIGGERED numeric(10) NOT NULL
);

ALTER TABLE ta410_test:qrtz_simple_triggers
ADD CONSTRAINT PRIMARY KEY (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP);


CREATE TABLE ta410_test:qrtz_triggers (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME varchar(80) NOT NULL,
TRIGGER_GROUP varchar(80) NOT NULL,
JOB_NAME varchar(80) NOT NULL,
JOB_GROUP varchar(80) NOT NULL,
DESCRIPTION varchar(120),
NEXT_FIRE_TIME numeric(13),
PREV_FIRE_TIME numeric(13),
PRIORITY integer,
TRIGGER_STATE varchar(16) NOT NULL,
TRIGGER_TYPE varchar(8) NOT NULL,
START_TIME numeric(13) NOT NULL,
END_TIME numeric(13),
CALENDAR_NAME varchar(80),
MISFIRE_INSTR numeric(2),
JOB_DATA byte in table
);

ALTER TABLE ta410_test:qrtz_triggers
ADD CONSTRAINT PRIMARY KEY (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP);


ALTER TABLE ta410_test:qrtz_blob_triggers
ADD CONSTRAINT FOREIGN KEY (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP)
REFERENCES ta410_test:qrtz_triggers;


ALTER TABLE ta410_test:qrtz_cron_triggers
ADD CONSTRAINT FOREIGN KEY (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP)
REFERENCES ta410_test:qrtz_triggers;


ALTER TABLE ta410_test:qrtz_simple_triggers
ADD CONSTRAINT FOREIGN KEY (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP)
REFERENCES ta410_test:qrtz_triggers;

ALTER TABLE ta410_test:qrtz_triggers
ADD CONSTRAINT FOREIGN KEY (SCHED_NAME,JOB_NAME, JOB_GROUP)
REFERENCES ta410_test:qrtz_job_details;

CREATE TABLE ta410_test:signrecord (
	userid DECIMAL(10,0),
	signtime DATETIME YEAR TO FRACTION(5),
	signstate VARCHAR(2),
	ip VARCHAR(20),
	mac VARCHAR(20),
	signid SERIAL8 NOT NULL
)
 in data ;
CREATE TABLE ta410_test:taaccesslog (
	logid SERIAL8 NOT NULL,
	userid DECIMAL(10,0) NOT NULL,
	positionid DECIMAL(10,0) NOT NULL,
	permissionid DECIMAL(10,0) NOT NULL,
	ispermission CHAR(1) NOT NULL,
	accesstime DATETIME YEAR TO FRACTION(5) NOT NULL,
	url LVARCHAR(1024),
	sysflag VARCHAR(50),
	PRIMARY KEY (logid) CONSTRAINT PK_TAACCESSLOG
)
 in data ;

CREATE TABLE ta410_test:taaccesssystem (
	sysid DECIMAL(11,0) NOT NULL,
	systemkey VARCHAR(40) NOT NULL,
	systemname VARCHAR(100) NOT NULL,
	privatekey BYTE,
	publickey BYTE,
	PRIMARY KEY (sysid) CONSTRAINT TAACCESSSYSTEM
)
 in data ;

CREATE UNIQUE INDEX UK_TAACCESSSYSTEM ON ta410_test:taaccesssystem (systemkey) ;

CREATE TABLE ta410_test:taadminyab139scope (
	positionid DECIMAL(10,0) NOT NULL,
	yab139 VARCHAR(6) NOT NULL,
	PRIMARY KEY (positionid,yab139) CONSTRAINT PK_TAADMINYAB139SCOPE
)
 in data ;

CREATE TABLE ta410_test:tacommonmenu (
	userid DECIMAL(10,0) NOT NULL,
	menuid DECIMAL(10,0) NOT NULL,
	PRIMARY KEY (userid,menuid) CONSTRAINT PK_TACOMMONMENU
)
 in data ;

CREATE TABLE ta410_test:taconfig (
	configid DECIMAL(10,0) NOT NULL,
	configname VARCHAR(100) NOT NULL,
	configvalue LVARCHAR(1024),
	configtype LVARCHAR(1024),
	configflag VARCHAR(20) NOT NULL,
	configdesc VARCHAR(200),
	PRIMARY KEY (configid) CONSTRAINT PK_TACONFIG
)
 in data ;

CREATE TABLE ta410_test:taconfigsyspath (
	serialid SERIAL8 NOT NULL,
	id VARCHAR(20) NOT NULL,
	name VARCHAR(50) NOT NULL,
	url VARCHAR(100) NOT NULL,
	py VARCHAR(20),
	cursystem VARCHAR(1) NOT NULL,
	PRIMARY KEY (serialid) CONSTRAINT PK_TACONFIGSYSPATH
)
 in data ;

CREATE TABLE ta410_test:taconsolemodule (
	module_id DECIMAL(10,0) NOT NULL,
	module_name VARCHAR(100) NOT NULL,
	module_url VARCHAR(200) NOT NULL,
	module_sta VARCHAR(1) NOT NULL DEFAULT '1',
	module_default VARCHAR(1) DEFAULT '1',
	module_height VARCHAR(10) DEFAULT '200',
	PRIMARY KEY (module_id) CONSTRAINT PK_TACONSOLEMODULE
)
 in data ;

CREATE TABLE ta410_test:taconsolemodulelocation (
	mark VARCHAR(20) NOT NULL,
	positionid DECIMAL(10,0) NOT NULL,
	location LVARCHAR(1000) NOT NULL
)
 in data ;
CREATE TABLE ta410_test:taconsolemoduleprivilege (
	positionid DECIMAL(10,0) NOT NULL,
	moduleid DECIMAL(10,0) NOT NULL,
	PRIMARY KEY (positionid,moduleid) CONSTRAINT PK_TACONSOLEMODULEPRIVILEGE
)
 in data ;

CREATE TABLE ta410_test:tadataaccessdimension (
	dimensionid SERIAL8 NOT NULL,
	positionid DECIMAL(10,0) NOT NULL,
	menuid DECIMAL(10,0) NOT NULL,
	dimensiontype VARCHAR(20) NOT NULL,
	dimensionpermissionid VARCHAR(20),
	allaccess VARCHAR(1),
	syspath VARCHAR(50),
	PRIMARY KEY (dimensionid) CONSTRAINT PK_TADATAACCESSDIMENSION
)
 in data ;

CREATE TABLE ta410_test:tadatasource (
	datasourceid SERIAL8 NOT NULL,
	datasourcename VARCHAR(60),
	datasourcetype VARCHAR(10),
	driverclassname VARCHAR(120),
	url VARCHAR(120),
	username VARCHAR(60),
	password VARCHAR(60),
	effective CHAR(1),
	connecttype VARCHAR(10),
	jndiname VARCHAR(50),
	PRIMARY KEY (datasourceid) CONSTRAINT PK_TADATASOURCE
)
 in data ;

CREATE TABLE ta410_test:tafield (
	id SERIAL8 NOT NULL,
	menuid DECIMAL(10,0),
	fieldid VARCHAR(100),
	fieldname VARCHAR(100),
	tableid VARCHAR(10),
	pid DECIMAL(10,0),
	fieldlevel INT
)
 in data ;
CREATE TABLE ta410_test:tafieldauthrity (
	positionid DECIMAL(10,0) NOT NULL,
	menuid DECIMAL(10,0) NOT NULL,
	fieldid VARCHAR(100) NOT NULL,
	look CHAR(1),
	edit CHAR(1),
	createtime DATETIME YEAR TO FRACTION(5),
	createuser DECIMAL(10,0)
)
 in data ;
CREATE TABLE ta410_test:talimitrate (
	menuid DECIMAL(10,0) NOT NULL,
	limitopen CHAR(1) NOT NULL,
	rate DECIMAL(16,2) NOT NULL,
	timeout DECIMAL(32,0) NOT NULL,
	maxcount DECIMAL(10,0) NOT NULL,
	url VARCHAR(100) NOT NULL,
	PRIMARY KEY (url) CONSTRAINT pk_talimitrate
)
 in data ;

CREATE TABLE ta410_test:talocalcacheversion (
	version DECIMAL(11,0) NOT NULL,
	codetype VARCHAR(20)
)
 in data ;
CREATE TABLE ta410_test:taloginhistorylog (
	logid SERIAL8 NOT NULL,
	userid DECIMAL(10,0) NOT NULL,
	logintime DATETIME YEAR TO FRACTION(5) NOT NULL,
	logouttime DATETIME YEAR TO FRACTION(5) NOT NULL,
	clientip VARCHAR(200) NOT NULL,
	sessionid VARCHAR(200) NOT NULL,
	serverip VARCHAR(200),
	syspath VARCHAR(50),
	clientsystem VARCHAR(50),
	clientbrowser VARCHAR(50),
	clientscreensize VARCHAR(50),
	PRIMARY KEY (logid) CONSTRAINT pk_taloginhistorylog
)
 in data ;

CREATE TABLE ta410_test:taloginstatlog (
	statdate VARCHAR(20) NOT NULL,
	pointintime VARCHAR(20) NOT NULL,
	loginnum DECIMAL(15,0) NOT NULL,
	PRIMARY KEY (statdate,pointintime) CONSTRAINT pk_taloginstatlog
)
 in data ;

CREATE TABLE ta410_test:tamanagermg (
	positionid DECIMAL(10,0),
	orgid DECIMAL(10,0)
)
 in data ;
CREATE TABLE ta410_test:tamenu (
	menuid SERIAL8 NOT NULL,
	pmenuid DECIMAL(10,0) NOT NULL,
	menuname VARCHAR(60),
	url VARCHAR(100),
	menuidpath LVARCHAR(1024),
	menunamepath LVARCHAR(1024),
	iconskin VARCHAR(200),
	selectimage VARCHAR(200),
	reportid VARCHAR(50),
	accesstimeel VARCHAR(200),
	effective CHAR(1) NOT NULL,
	securitypolicy CHAR(1) NOT NULL,
	isdismultipos CHAR(1) NOT NULL,
	quickcode VARCHAR(20),
	sortno INT,
	resourcetype CHAR(2) NOT NULL,
	menulevel INT,
	isleaf CHAR(1),
	menutype CHAR(1),
	iscache CHAR(1),
	syspath VARCHAR(20),
	useyab003 CHAR(1),
	typeflag DECIMAL(10,0),
	isaudite VARCHAR(1),
	consolemodule CHAR(1) DEFAULT '1',
	customencoding VARCHAR(20),
	isfiledscontrol CHAR(1) DEFAULT '1',
	PRIMARY KEY (menuid) CONSTRAINT pk_tamenu
)
 in data ;

CREATE TABLE ta410_test:tamenupositionyab003 (
	menuid DECIMAL(10,0) NOT NULL,
	positionid DECIMAL(10,0) NOT NULL,
	yab003 VARCHAR(6) NOT NULL,
	PRIMARY KEY (menuid,positionid,yab003) CONSTRAINT pk_tamenupositionyab003
)
 in data ;

CREATE TABLE ta410_test:tamessage (
	userid DECIMAL(10,0),
	orgid DECIMAL(10,0) NOT NULL,
	sign VARCHAR(6) DEFAULT '0',
	mgid VARCHAR(20) NOT NULL,
	mgdate DATETIME YEAR TO FRACTION(5),
	title VARCHAR(100),
	content TEXT,
	mgcheck VARCHAR(1) DEFAULT '0',
	PRIMARY KEY (mgid,orgid) CONSTRAINT pk_tamessage
)
 in data ;

CREATE TABLE ta410_test:tamessageattachment (
	mgid VARCHAR(20) NOT NULL,
	attachment BYTE,
	name VARCHAR(50)
)
 in data ;

CREATE TABLE ta410_test:tamessagestate (
	mgid VARCHAR(20) NOT NULL,
	userid DECIMAL(10,0) NOT NULL,
	state VARCHAR(6) DEFAULT '0'
)
 in data ;

CREATE TABLE ta410_test:taonlinelog (
	logid SERIAL8 NOT NULL,
	userid DECIMAL(10,0) NOT NULL,
	logintime DATETIME YEAR TO FRACTION(5) NOT NULL,
	curresource LVARCHAR(1000),
	clientip VARCHAR(200) NOT NULL,
	sessionid VARCHAR(200) NOT NULL,
	syspath VARCHAR(50),
	serverip VARCHAR(200),
	clientsystem VARCHAR(50),
	clientbrowser VARCHAR(50),
	clientscreensize VARCHAR(50),
	PRIMARY KEY (logid) CONSTRAINT pk_taonlinelog
)
 in data ;

CREATE TABLE ta410_test:taonlinestatlog (
	statdate VARCHAR(20) NOT NULL,
	pointintime VARCHAR(20) NOT NULL,
	loginnum DECIMAL(15,0) NOT NULL,
	PRIMARY KEY (statdate,pointintime) CONSTRAINT pk_taonlinestatlog
)
 in data ;

CREATE TABLE ta410_test:taorg (
	orgid SERIAL8 NOT NULL,
	porgid DECIMAL(10,0),
	orgname VARCHAR(60),
	costomno VARCHAR(10),
	orgidpath LVARCHAR(1024),
	orgnamepath LVARCHAR(1024),
	costomnopath LVARCHAR(1024),
	orgtype CHAR(2),
	sort INTEGER,
	yab003 VARCHAR(6),
	dimension CHAR(2),
	createuser DECIMAL(10,0) NOT NULL,
	createtime DATETIME YEAR TO FRACTION(5) NOT NULL,
	effective CHAR(1) NOT NULL,
	orglevel DECIMAL(10,0),
	isleaf CHAR(1),
	orgmanager DECIMAL(10,0),
	destory CHAR(1),
	typeflag DECIMAL(10,0),
	yab139 VARCHAR(6),
	PRIMARY KEY (orgid) CONSTRAINT pk_taorg
)
 in data ;

CREATE TABLE ta410_test:taposition (
	positionid SERIAL NOT NULL,
	orgid SERIAL8 NOT NULL,
	positionname VARCHAR(60) NOT NULL,
	positiontype CHAR(1) NOT NULL,
	createpositionid DECIMAL(10,0) NOT NULL,
	orgidpath LVARCHAR(1024),
	orgnamepath LVARCHAR(1024),
	validtime DATETIME YEAR TO FRACTION(5),
	createuser DECIMAL(10,0) NOT NULL,
	createtime DATETIME YEAR TO FRACTION(5) NOT NULL,
	effective CHAR(1) NOT NULL,
	isadmin CHAR(1),
	isshare CHAR(1),
	iscopy CHAR(1),
	typeflag DECIMAL(10,0),
	positioncategory VARCHAR(2),
	isdeveloper VARCHAR(1),
	PRIMARY KEY (positionid) CONSTRAINT pk_taposition,
	FOREIGN KEY (orgid) REFERENCES ta410_test:informix.taorg(orgid) CONSTRAINT FK_RELATIONSHIP_5
)
 in data ;

CREATE TABLE ta410_test:taorgmg (
	positionid SERIAL,
	orgid SERIAL8,
	FOREIGN KEY (orgid) REFERENCES ta410_test:informix.taorg(orgid) CONSTRAINT FK_REFERENCE_8,
	FOREIGN KEY (positionid) REFERENCES ta410_test:informix.taposition(positionid) CONSTRAINT FK_REFERENCE_9
)
 in data ;

CREATE TABLE ta410_test:taorgoplog (
	logid SERIAL8 NOT NULL,
	batchno VARCHAR(10),
	optype CHAR(2),
	influencebodytype CHAR(2),
	influencebody VARCHAR(10),
	opbody CHAR(2),
	opsubjekt VARCHAR(10),
	changcontent LVARCHAR(2048),
	optime DATETIME YEAR TO FRACTION(5) NOT NULL,
	opuser VARCHAR(10) NOT NULL,
	opposition VARCHAR(10) NOT NULL,
	ispermission VARCHAR(1),
	PRIMARY KEY (logid) CONSTRAINT pk_taorgoplog
)
 in data ;

CREATE TABLE ta410_test:tapagereview (
	page_id DECIMAL(20,0) NOT NULL,
	function_id DECIMAL(20,0),
	create_time DATETIME YEAR TO FRACTION(5),
	context BYTE,
	data BYTE,
	olddata BYTE,
	otherdata TEXT,
	userinfo BYTE,
	storetype VARCHAR(20),
	doc_id VARCHAR(20),
	PRIMARY KEY (page_id) CONSTRAINT pk_tapagereview
)
 in data ;

CREATE TABLE ta410_test:tapositionauthrity (
	positionid SERIAL NOT NULL,
	menuid SERIAL8 NOT NULL,
	usepermission CHAR(1),
	repermission CHAR(1),
	reauthrity CHAR(1),
	createuser DECIMAL(10,0) NOT NULL,
	createtime DATETIME YEAR TO FRACTION(5) NOT NULL,
	effecttime DATETIME YEAR TO FRACTION(5),
	auditeaccessdate DATETIME YEAR TO FRACTION(5),
	auditeuser DECIMAL(10,0),
	auditstate VARCHAR(1) DEFAULT '0',
	PRIMARY KEY (positionid,menuid) CONSTRAINT pk_tapositionauthrity,
	FOREIGN KEY (menuid) REFERENCES ta410_test:informix.tamenu(menuid) CONSTRAINT FK_REFERENCE_7,
	FOREIGN KEY (positionid) REFERENCES ta410_test:informix.taposition(positionid) CONSTRAINT FK_RELATIONSHIP_11
)
 in data ;

CREATE TABLE ta410_test:tarunqianresource (
	raqfilename VARCHAR(200) NOT NULL,
	parentraqfilename VARCHAR(200),
	raqname VARCHAR(200),
	raqtype VARCHAR(6),
	raqfile BYTE,
	uploador VARCHAR(19),
	uploadtime DATETIME YEAR TO FRACTION(5),
	subrow INT,
	subcell INT,
	raqdatasource VARCHAR(19),
	raqparam LVARCHAR(500),
	orgid VARCHAR(15),
	PRIMARY KEY (raqfilename) CONSTRAINT pk_tarunqianresource,
	FOREIGN KEY (parentraqfilename) REFERENCES ta410_test:informix.tarunqianresource(raqfilename) CONSTRAINT FK_YHCIP_RU_REFERENCE_YHCIP_RU
)
 in data ;

CREATE TABLE ta410_test:tarunqianad52reference (
	menuid DECIMAL(10,0),
	raqfilename VARCHAR(200),
	limited INT,
	scaleexp INT,
	isgroup VARCHAR(6),
	needsaveasexcel VARCHAR(6),
	needsaveasexcel2007 VARCHAR(6),
	needsaveaspdf VARCHAR(6),
	needsaveasword VARCHAR(6),
	needsaveastext VARCHAR(6),
	needprint VARCHAR(6),
	id DECIMAL(10,0) NOT NULL,
	PRIMARY KEY (id) CONSTRAINT pk_tarunqianad52reference,
	FOREIGN KEY (raqfilename) REFERENCES ta410_test:informix.tarunqianresource(raqfilename) CONSTRAINT FK_REPORT_INFO
)
 in data ;


CREATE TABLE ta410_test:tarunqianprintsetup (
	setupid VARCHAR(200) NOT NULL,
	setupvalue LVARCHAR(400) NOT NULL,
	PRIMARY KEY (setupid) CONSTRAINT u162_331
)
 in data ;

CREATE TABLE ta410_test:taserverexceptionlog (
	id VARCHAR(50) NOT NULL,
	ipaddress VARCHAR(255),
	port VARCHAR(10),
	type VARCHAR(255),
	content byte,
	time DATETIME YEAR TO FRACTION(5),
	syspath VARCHAR(50),
	clientip VARCHAR(50),
	url VARCHAR(100),
	menuid VARCHAR(8),
	menuname VARCHAR(30),
	useragent VARCHAR(200),
	exceptiontype VARCHAR(2),
	parameter BYTE
)
 in data ;

CREATE TABLE ta410_test:tashareposition (
	spositionid DECIMAL(10,0),
	dpositionid DECIMAL(10,0)
)
 in data ;

CREATE TABLE ta410_test:tauser (
	userid SERIAL8 NOT NULL,
	name VARCHAR(60) NOT NULL,
	sex CHAR(1),
	loginid VARCHAR(20) NOT NULL,
	password VARCHAR(50) NOT NULL,
	passwordfaultnum INT,
	pwdlastmodifydate DATETIME YEAR TO FRACTION(5),
	islock CHAR(1),
	sort INT,
	effective CHAR(1) NOT NULL,
	tel VARCHAR(15),
	authmode VARCHAR(2),
	createuser DECIMAL(10,0),
	createtime DATETIME YEAR TO FRACTION(5) NOT NULL,
	directorgid DECIMAL(10,0) NOT NULL,
	destory CHAR(1),
	typeflag DECIMAL(10,0),
	PRIMARY KEY (userid) CONSTRAINT pk_tauser
)
 in data ;

CREATE TABLE ta410_test:tauserposition (
	userid SERIAL8 NOT NULL,
	positionid SERIAL NOT NULL,
	mainposition CHAR(1) NOT NULL,
	createuser DECIMAL(10,0) NOT NULL,
	createtime DATETIME YEAR TO FRACTION(5) NOT NULL,
	PRIMARY KEY (userid,positionid) CONSTRAINT u181_446,
	FOREIGN KEY (positionid) REFERENCES ta410_test:informix.taposition(positionid) CONSTRAINT r181_584,
	FOREIGN KEY (userid) REFERENCES ta410_test:informix.tauser(userid) CONSTRAINT pk_tauserposition
)
 in data ;

CREATE TABLE ta410_test:tayab003levelmg (
	pyab003 VARCHAR(6),
	yab003 VARCHAR(6)
)
 in data ;
CREATE TABLE ta410_test:tayab003scope (
	yab003 VARCHAR(6) NOT NULL,
	yab139 VARCHAR(6) NOT NULL,
	PRIMARY KEY (yab003,yab139) CONSTRAINT pk_tayab003scope
)
 in data ;

CREATE TABLE ta410_test:tayab139mg (
	yab003 VARCHAR(6),
	yab139 VARCHAR(20)
)
 in data ;
CREATE TABLE ta410_test:templatemanager (
	templateid VARCHAR(50) NOT NULL,
	templatename VARCHAR(50) NOT NULL,
	templatecontent BYTE,
	templatetype VARCHAR(10),
	createpeople DECIMAL(10,0),
	createtime DATETIME YEAR TO FRACTION(5),
	effective CHAR(1),
	content VARCHAR(20)
)
 in data ;
CREATE TABLE ta410_test:yhcip_oracle_jobs (
	jobid VARCHAR(20) NOT NULL,
	oraclejobid VARCHAR(20) NOT NULL,
	jobname VARCHAR(100) NOT NULL,
	userid VARCHAR(20) NOT NULL,
	what LVARCHAR(4000) NOT NULL,
	starttime VARCHAR(200) NOT NULL,
	endtime DATETIME YEAR TO FRACTION(5),
	interval VARCHAR(200),
	submittime DATETIME YEAR TO FRACTION(5),
	PRIMARY KEY (jobid) CONSTRAINT pk_yhcip_oracle_jobs
)
 in data ;
 CREATE TABLE ta410_test:tazkaddressmg (
	zkid          VARCHAR(20) NOT NULL,
	zkaddress     VARCHAR(200),
	appname       VARCHAR(60),
	appnamespace  VARCHAR(30),
	PRIMARY KEY (zkid) CONSTRAINT pk_tazkaddressmg
)
 in data ;
 CREATE TABLE ta410_test:job_busy_free_mg (
	jobstatus       VARCHAR(2),
	zkid            VARCHAR(20) NOT NULL,
	cron            VARCHAR(20),
	jobtype         VARCHAR(2),
	jobname         VARCHAR(20),
	ips             VARCHAR(20),
	ebfid           VARCHAR(20) NOT NULL,
	PRIMARY KEY (ebfid) CONSTRAINT pk_job_busy_free_mg
)
 in data ;

 -- Create sequence
CREATE SEQUENCE seq_default
START WITH 110000
INCREMENT BY 1
MINVALUE 100000
MAXVALUE 1000000000
NOCYCLE
CACHE 20;

CREATE SEQUENCE hibernate_sequence
START WITH 110000
INCREMENT BY 1
MINVALUE 100000
MAXVALUE 1000000000
NOCYCLE
CACHE 20;

CREATE SEQUENCE seq_message
START WITH 201
INCREMENT BY 1
MINVALUE 1
MAXVALUE 999999999999999999
NOCYCLE
cache 10;

CREATE SEQUENCE template_sequence
START WITH 61
INCREMENT BY 1
MINVALUE 1
MAXVALUE 999999999999999999
NOCYCLE
cache 20;

--create view
create view aa10a1 as
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

create view v_taloginlog (logid,userid,username,logintime,clientip,sessionid,syspath,serverip,clientsystem,clientbrowser,clientscreensize) as
select
  x0.logid ,
  x0.userid ,
  (select x2.name from "informix".tauser x2 where (x2.userid = x0.userid ) ),
  x0.logintime ,
  x0.clientip ,
  x0.sessionid ,
  x0.syspath ,
  x0.serverip ,
  NVL (x0.clientsystem ,'UNKNOW' ),
  NVL (x0.clientbrowser ,'UNKNOW' ),
  NVL (x0.clientscreensize ,'UNKNOW' )
from
  taonlinelog x0
  union all select
    x1.logid ,
    x1.userid ,
    (select x3.name from tauser x3 where (x3.userid = x1.userid ) ) ,
    x1.logintime ,
    x1.clientip ,
    x1.sessionid ,
    x1.syspath ,
    x1.serverip ,
    NVL (x1.clientsystem ,'UNKNOW' ),
    NVL (x1.clientbrowser ,'UNKNOW' ),
    NVL (x1.clientscreensize ,'UNKNOW' )
  from
    taloginhistorylog x1 ;


--create view

create view v_yab139 (userid,menuid,yab139,syspath)
as select
	x2.userid ,
	x0.menuid ,
	x1.dimensionpermissionid ,
	x1.syspath
    from tamenu x0 ,tadataaccessdimension x1 ,tauser x2 ,taposition x3 ,tauserposition x4
	 where ((((((((((x2.userid = x4.userid ) AND ((x2.destory IS NULL )
		OR (x2.destory = 0 ) ) )
		AND (x2.effective = '1' ) )
		AND (x4.positionid = x3.positionid ) )
		AND (x3.effective = '1' ) )
		AND ((x3.validtime IS NULL )
		OR (x3.validtime >= SYSDATE year to fraction(5) ) ) )
		AND (x3.positionid = x1.positionid ) )
		AND (x1.menuid = x0.menuid ) )
		AND (x1.dimensiontype = 'YAB139' ) )
		AND (x1.allaccess = 0 ) )
   union select x7.userid ,x5.menuid ,x10.aaa102 ,x6.syspath
	 from tamenu x5 ,tadataaccessdimension x6 ,tauser x7 ,taposition x8 ,tauserposition x9 ,aa10a1 x10
	 where (((((((((((x7.userid = x9.userid )
	 AND ((x7.destory IS NULL )
	 OR (x7.destory = 0 ) ) )
	 AND (x7.effective = '1' ) )
	 AND(x9.positionid = x8.positionid ) )
	 AND (x8.effective = '1' ) )
	 AND ((x8.validtime IS NULL )
	 OR (x8.validtime >= SYSDATE year to fraction(5) ) ) )
	 AND (x8.positionid = x6.positionid ) )
	 AND (x6.menuid = x5.menuid ) )
	 AND (x6.dimensiontype = 'YAB139' ) )
	 AND(x6.allaccess != 0.0000000000000000 ) )
	 AND (x10.aaa100 = 'YAB139' ) ) ;


INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('MENUTYPE', '菜单类型', '0', '通用菜单', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('MENUTYPE', '菜单类型', '1', '系统管理菜单', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('MENUTYPE', '菜单类型', '2', '业务功能菜单', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('POSITIONTYPE', '岗位类型', '1', '公有岗位', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('POSITIONTYPE', '岗位类型', '2', '个人专属岗位', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('POSITIONTYPE', '岗位类型', '3', '委派岗位', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('SEX', '性别', '1', '男', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('SEX', '性别', '2', '女', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('SEX', '性别', '0', '无', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('ORGTYPE', '组织类型', '01', '机构', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('ORGTYPE', '组织类型', '02', '部门', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('ORGTYPE', '组织类型', '04', '组', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('EFFECTIVE', '有效标志', '1', '有效', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('EFFECTIVE', '有效标志', '0', '无效', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('YESORNO', '是否', '1', '是', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('YESORNO', '是否', '0', '否', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('POLICY', '安全策略', '1', '要认证要显示', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('POLICY', '安全策略', '2', '要认证不显示', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('POLICY', '安全策略', '3', '不认证不显示', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('POLICY', '安全策略', '4', '不认证要显示', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '01', '新增组织', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '02', '编辑组织', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '03', '禁用组织', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '04', '新增人员', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '05', '编辑人员', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '06', '禁用人员', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '07', '密码重置', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '08', '赋予岗位给人员', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '09', '设置主岗位给人员', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '10', '新增岗位', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '11', '编辑岗位', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '12', '禁用岗位', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '13', '赋予岗位使用权限', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '14', '回收岗位使用权限', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '15', '启用岗位', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '16', '启用人员', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '17', '启用组织', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '18', '删除组织', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '19', '赋予岗位授权别人使用权限', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '20', '回收岗位授权别人使用权限', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '21', '赋予岗位授权别人授权权限', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '22', '回收岗位授权别人授权权限', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '23', '删除管理员', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '24', '新增管理员', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '25', '删除岗位', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPTYPE', '操作类型', '26', '删除人员', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPOBJTYPE', '操作对象类型', '01', '组织', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPOBJTYPE', '操作对象类型', '02', '人员', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPOBJTYPE', '操作对象类型', '03', '岗位', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('OPOBJTYPE', '操作对象类型', '04', '权限', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('YAB003', '分中心', '9999', '9999', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('RAQTYPE', '报表类型', '0', '参数报表', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('RAQTYPE', '报表类型', '1', '主报表', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('RAQTYPE', '报表类型', '2', '子报表', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('AUTHMODE', '认证方式类型', '04', 'Key盘', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('AUTHMODE', '权限认证方式', '03', '人脸识别', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('AUTHMODE', '权限认证方式', '02', '指纹识别', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('AUTHMODE', '权限认证方式', '01', '用户名密码', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('LOGLEVEL', '日志等级', '0', 'OFF', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('LOGLEVEL', '日志等级', '1', 'FATAL', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('LOGLEVEL', '日志等级', '2', 'ERROR', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('LOGLEVEL', '日志等级', '3', 'WARN', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('LOGLEVEL', '日志等级', '4', 'INFO', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('LOGLEVEL', '日志等级', '5', 'DEBUG', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('LOGLEVEL', '日志等级', '6', 'TRACE', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('LOGLEVEL', '日志等级', '7', 'ALL', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('POSITIONCATEGORY', '岗位类别', '01', '业务岗', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('POSITIONCATEGORY', '岗位类别', '02', '稽核岗', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('AUDITSTATE', '审核状态', '0', '无需审核', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('AUDITSTATE', '审核状态', '1', '待审核', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('AUDITSTATE', '审核状态', '2', '审核通过', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('AUDITSTATE', '审核状态', '3', '审核未通过', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('TEMPLATETYPE', '模板类型', '1', 'word模板(.xml)', '9999', '0', 0);
INSERT INTO ta410_test:aa10
(aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
VALUES('TEMPLATETYPE', '模板类型', '2', 'excel模板(.xls/.xlsx)', '9999', '0', 0);


INSERT INTO ta410_test:taconfigsyspath
(serialid, id, name, url, py, cursystem)
VALUES(1, 'sysmg', '系统管理', 'http://127.0.0.1:8080/ta3/', 'xtgl', '0');


INSERT INTO ta410_test:talocalcacheversion (version,codetype) VALUES (
1,NULL);


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
VALUES (36, 4, '配置管理', 'sysapp/config/config.do', '1/173013/273014', '银海软件/开发管理/配置管理', 'icon-009?#AFD67C', NULL, NULL, NULL, '1', '1', '1', NULL, 0, '01', 3, '0', '1', NULL, 'sysmg', '1', NULL, '0', '1', NULL, '1');
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
INSERT INTO TAMENU (MENUID, PMENUID, MENUNAME, URL, MENUIDPATH, MENUNAMEPATH, ICONSKIN, SELECTIMAGE, REPORTID, ACCESSTIMEEL, EFFECTIVE, SECURITYPOLICY, ISDISMULTIPOS, QUICKCODE, SORTNO, RESOURCETYPE, MENULEVEL, ISLEAF, MENUTYPE, ISCACHE, SYSPATH, USEYAB003, TYPEFLAG, ISAUDITE, CONSOLEMODULE, CUSTOMENCODING, ISFILEDSCONTROL)
VALUES (291, 2, '组织正副职管理', 'org/positionMgAction.do', '1/2/291', '银海软件/权限管理/组织正副职管理', 'icon-011?#09dceb', NULL, NULL, '', '1', '1', '1', NULL, 8, '01', 3, '1', '1', NULL, 'sysmg', '1', NULL, '0', '0', '', '1');
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


INSERT INTO ta410_test:taorg
(orgid, porgid, orgname, costomno, orgidpath, orgnamepath, costomnopath, orgtype, sort, yab003, dimension, createuser, createtime, effective, orglevel, isleaf, orgmanager, destory, typeflag, yab139)
VALUES(1, 1, '银海软件', '1', '1', '银海软件', '0', '01', 0, '9999', NULL, 0, '2018-11-08 11:25:59.0', '1', 0, '1', NULL, NULL, 0, '9999');


INSERT INTO ta410_test:taposition
(positionid, orgid, positionname, positiontype, createpositionid, orgidpath, orgnamepath, validtime, createuser, createtime, effective, isadmin, isshare, iscopy, typeflag, positioncategory, isdeveloper)
VALUES(1, 1, '超级管理员', '2', 1, '1', '银海软件', NULL, 1, '2018-11-08 11:25:59.0', '1', '1', NULL, NULL, 0, NULL, '1');


INSERT INTO ta410_test:tauser
(userid, name, sex, loginid, password, passwordfaultnum, pwdlastmodifydate, islock, sort, effective, tel, authmode, createuser, createtime, directorgid, destory, typeflag)
VALUES(1, '超级管理员', '1', 'developer', '29PYtt0CYAXxrlJgzd/HUg==', 0, '2018-11-08 11:25:59.0', '0', 0, '1', '0', '01', 1, '2018-11-08 11:25:59.0', 1, NULL, 0);


INSERT INTO ta410_test:tauserposition (userid,positionid,mainposition,createuser,createtime) VALUES (
1,1,'1',1,'2018-11-08 11:25:59.0');

INSERT INTO ta410_test:tayab003levelmg (pyab003,yab003) VALUES (
'0','9999');