ALTER TABLE "TAXQTHEME"DROP CONSTRAINT "PK_TAXQTHEME" CASCADE;ALTER TABLE "TAXQFIELD"DROP CONSTRAINT "PK_TAXQFIELD" CASCADE;ALTER TABLE "TAXQFIELDRELATION"DROP CONSTRAINT "PK_TAXQFIELDRELATION" CASCADE;ALTER TABLE "TAXQFIELDCOUNTWAY"DROP CONSTRAINT "PK_TAXQFIELDCOUNTWAY" CASCADE;ALTER TABLE "TAXQPLAN"DROP CONSTRAINT "PK_TAXQPLAN" CASCADE;ALTER TABLE "TAXQPLANFIELDWHERE"DROP CONSTRAINT "PK_TAXQPLANFIELDWHERE" CASCADE;ALTER TABLE "TAXQPLANFIELDGROUP"DROP CONSTRAINT "PK_TAXQPLANFIELDGROUP" CASCADE;ALTER TABLE "TAXQPLANFIELDCOUNT"DROP CONSTRAINT "PK_TAXQPLANFIELDCOUNT" CASCADE;DROP TABLE "TAXQTHEME" CASCADE CONSTRAINTS;DROP TABLE "TAXQFIELD" CASCADE CONSTRAINTS;DROP TABLE "TAXQFIELDRELATION" CASCADE CONSTRAINTS;DROP TABLE "TAXQFIELDCOUNTWAY" CASCADE CONSTRAINTS;DROP TABLE "TAXQPLAN" CASCADE CONSTRAINTS;DROP TABLE "TAXQPLANFIELDWHERE" CASCADE CONSTRAINTS;DROP TABLE "TAXQPLANFIELDGROUP" CASCADE CONSTRAINTS;DROP TABLE "TAXQPLANFIELDCOUNT" CASCADE CONSTRAINTS;DROP TABLE "TADATASOURCEPRO" CASCADE CONSTRAINTS;CREATE TABLE "TAXQTHEME" ("THEMEID" NUMBER(20,0) NOT NULL,"DATASOURCEID" NUMBER(20,0) NOT NULL,"MENUID" NUMBER(20,0) NOT NULL,"THEMENAME" VARCHAR2(200 BYTE) NULL,"THEMETABLE" VARCHAR2(40 BYTE) NULL,"THEMEWHERE" VARCHAR2(200 BYTE) NULL,"THEMEWHEREDESC" VARCHAR2(500 BYTE) NULL,"THEMECREATERID" NUMBER(15,0) NOT NULL,"THEMEDATE" DATE NOT NULL,CONSTRAINT "PK_TAXQTHEME" PRIMARY KEY ("THEMEID"));COMMENT ON TABLE "TAXQTHEME" IS '查询统计主题';COMMENT ON COLUMN "TAXQTHEME"."THEMEID" IS '查询统计主题id';COMMENT ON COLUMN "TAXQTHEME"."DATASOURCEID" IS '数据源ID';COMMENT ON COLUMN "TAXQTHEME"."MENUID" IS '菜单ID';COMMENT ON COLUMN "TAXQTHEME"."THEMENAME" IS '查询统计主题名称';COMMENT ON COLUMN "TAXQTHEME"."THEMETABLE" IS '查询统计主题库表(视图)';COMMENT ON COLUMN "TAXQTHEME"."THEMEWHERE" IS '基础WHERE条件';COMMENT ON COLUMN "TAXQTHEME"."THEMEWHEREDESC" IS '基础WHERE条件描述';COMMENT ON COLUMN "TAXQTHEME"."THEMECREATERID" IS '创建人ID';COMMENT ON COLUMN "TAXQTHEME"."THEMEDATE" IS '经办日期';CREATE TABLE "TAXQFIELD" ("FIELDID" NUMBER(20,0) NOT NULL,"THEMEID" NUMBER(20,0) NOT NULL,"FIELDNAME" VARCHAR2(200 BYTE) NULL,"FIELDALIAS" VARCHAR2(30 BYTE) NULL,"FIELDCOMMENT" VARCHAR2(200 BYTE) NULL,"FIELDTYPE" VARCHAR2(10 BYTE) NULL,"FIELDTREECODE" VARCHAR2(60 BYTE) NULL,"FIELDISCONDITION" VARCHAR2(2 BYTE) NULL,"FIELDDISPLAYTYPE" VARCHAR2(2 BYTE) NULL,"FIELDISGROUP" VARCHAR2(2 BYTE) NULL,"FIELDISCOUNT" VARCHAR2(2 BYTE) NULL,"FIELDSORT" VARCHAR2(255) NULL,CONSTRAINT "PK_TAXQFIELD" PRIMARY KEY ("FIELDID"));COMMENT ON TABLE "TAXQFIELD" IS '查询统计项目';COMMENT ON COLUMN "TAXQFIELD"."FIELDID" IS '查询统计字段id';COMMENT ON COLUMN "TAXQFIELD"."THEMEID" IS '查询统计主题id';COMMENT ON COLUMN "TAXQFIELD"."FIELDNAME" IS '数据库字段或表达式';COMMENT ON COLUMN "TAXQFIELD"."FIELDALIAS" IS '数据库字段AS别名';COMMENT ON COLUMN "TAXQFIELD"."FIELDCOMMENT" IS '数据库字段注释';COMMENT ON COLUMN "TAXQFIELD"."FIELDTYPE" IS '数据类型（1字符型2数字型3日期型）';COMMENT ON COLUMN "TAXQFIELD"."FIELDTREECODE" IS '树型代码类别';COMMENT ON COLUMN "TAXQFIELD"."FIELDISCONDITION" IS '是否作为查询条件（1是0否）';COMMENT ON COLUMN "TAXQFIELD"."FIELDDISPLAYTYPE" IS '查询条件的展现形式（11文本12年月13日期14数字21代码值平铺22树23DATAGRID中选择）';COMMENT ON COLUMN "TAXQFIELD"."FIELDISGROUP" IS '是否分组(作为分组,就默认作为展示项）';COMMENT ON COLUMN "TAXQFIELD"."FIELDISCOUNT" IS '是否统计';COMMENT ON COLUMN "TAXQFIELD"."FIELDSORT" IS '排序号';CREATE TABLE "TAXQFIELDRELATION" ("FIELDRELAID" NUMBER(20,0) NOT NULL,"FIELDID" NUMBER(20,0) NOT NULL,"FIELDRELATION" VARCHAR2(2 BYTE) NOT NULL,CONSTRAINT "PK_TAXQFIELDRELATION" PRIMARY KEY ("FIELDRELAID"));COMMENT ON TABLE "TAXQFIELDRELATION" IS '查询条件支持的关系符';COMMENT ON COLUMN "TAXQFIELDRELATION"."FIELDRELAID" IS '关系符id';COMMENT ON COLUMN "TAXQFIELDRELATION"."FIELDID" IS '查询统计字段id';COMMENT ON COLUMN "TAXQFIELDRELATION"."FIELDRELATION" IS '支持的关系（11等于12不等于21大于22大于等于31小于32小于等于41包含42不包含）';CREATE TABLE "TAXQFIELDCOUNTWAY" ("COUNTWAYID" NUMBER(20,0) NOT NULL,"FIELDID" NUMBER(20,0) NOT NULL,"COUNTWAY" VARCHAR2(2 BYTE) NULL,CONSTRAINT "PK_TAXQFIELDCOUNTWAY" PRIMARY KEY ("COUNTWAYID"));COMMENT ON TABLE "TAXQFIELDCOUNTWAY" IS '查询结果支持的计算方式';COMMENT ON COLUMN "TAXQFIELDCOUNTWAY"."COUNTWAYID" IS '计算方式流id';COMMENT ON COLUMN "TAXQFIELDCOUNTWAY"."FIELDID" IS '查询统计字段ID';COMMENT ON COLUMN "TAXQFIELDCOUNTWAY"."COUNTWAY" IS '查询结果的计算方式（1计数2去重后计数3求和4求平均5最大值6最小值）';CREATE TABLE "TAXQPLAN" ("PLANID" NUMBER(20,0) NOT NULL,"THEMEID" NUMBER(20,0) NOT NULL,"PLANNAME" VARCHAR2(200 BYTE) NULL,"PLANCREATERID" VARCHAR2(100 BYTE) NOT NULL,"PLANDATE" DATE NOT NULL,CONSTRAINT "PK_TAXQPLAN" PRIMARY KEY ("PLANID"));COMMENT ON TABLE "TAXQPLAN" IS '查询统计方案';COMMENT ON COLUMN "TAXQPLAN"."PLANID" IS '查询统计方案ID';COMMENT ON COLUMN "TAXQPLAN"."THEMEID" IS '查询统计主题ID';COMMENT ON COLUMN "TAXQPLAN"."PLANNAME" IS '查询统计方案名称';COMMENT ON COLUMN "TAXQPLAN"."PLANCREATERID" IS '创建人id';COMMENT ON COLUMN "TAXQPLAN"."PLANDATE" IS '经办日期';CREATE TABLE "TAXQPLANFIELDWHERE" ("WHEREID" NUMBER(20,0) NOT NULL,"PLANID" NUMBER(20,0) NOT NULL,"WHEREGROUP" NUMBER(20,0) NOT NULL,"FIELDID" NUMBER(20,0) NOT NULL,"FIELDRELATION" VARCHAR2(2 BYTE) NULL,"INPUTVALUE" VARCHAR2(400 BYTE) NULL,"SORT" NUMBER(3,0) NULL,CONSTRAINT "PK_TAXQPLANFIELDWHERE" PRIMARY KEY ("WHEREID"));COMMENT ON TABLE "TAXQPLANFIELDWHERE" IS '查询统计方案WHERE条件';COMMENT ON COLUMN "TAXQPLANFIELDWHERE"."WHEREID" IS '查询统计方案WHERE条件id';COMMENT ON COLUMN "TAXQPLANFIELDWHERE"."PLANID" IS '查询统计方案ID';COMMENT ON COLUMN "TAXQPLANFIELDWHERE"."WHEREGROUP" IS '查询统计方案WHERE条件分组ID';COMMENT ON COLUMN "TAXQPLANFIELDWHERE"."FIELDID" IS '查询统计项目ID';COMMENT ON COLUMN "TAXQPLANFIELDWHERE"."FIELDRELATION" IS '选择的关系（11等于12不等于21大于22大于等于31小于32小于等于41包含42不包含）';COMMENT ON COLUMN "TAXQPLANFIELDWHERE"."INPUTVALUE" IS '输入框中录入的值';COMMENT ON COLUMN "TAXQPLANFIELDWHERE"."SORT" IS '排序号';CREATE TABLE "TAXQPLANFIELDGROUP" ("GROUPID" NUMBER(20,0) NOT NULL,"PLANID" NUMBER(20,0) NOT NULL,"FIELDID" NUMBER(20,0) NOT NULL,"SORT" NUMBER(4,0) NULL,"SORTTYPE" VARCHAR2(2) NULL,CONSTRAINT "PK_TAXQPLANFIELDGROUP" PRIMARY KEY ("GROUPID"));COMMENT ON TABLE "TAXQPLANFIELDGROUP" IS '查询统计方案分组项目';COMMENT ON COLUMN "TAXQPLANFIELDGROUP"."GROUPID" IS '查询统计方案分组字段id';COMMENT ON COLUMN "TAXQPLANFIELDGROUP"."PLANID" IS '查询统计方案流水号';COMMENT ON COLUMN "TAXQPLANFIELDGROUP"."FIELDID" IS '查询统计字段ID';COMMENT ON COLUMN "TAXQPLANFIELDGROUP"."SORT" IS '排序号';COMMENT ON COLUMN "TAXQPLANFIELDGROUP"."SORTTYPE" IS '排序方式(1升序,2降序)';CREATE TABLE "TAXQPLANFIELDCOUNT" ("COUNTID" NUMBER(20,0) NOT NULL,"PLANID" NUMBER(20,0) NOT NULL,"SORT" NUMBER(4,0) NULL,"FIELDID" NUMBER(20,0) NOT NULL,"COUNTWAY" VARCHAR2(2 BYTE) NULL,CONSTRAINT "PK_TAXQPLANFIELDCOUNT" PRIMARY KEY ("COUNTID"));COMMENT ON TABLE "TAXQPLANFIELDCOUNT" IS '查询统计方案统计指标';COMMENT ON COLUMN "TAXQPLANFIELDCOUNT"."COUNTID" IS '查询统计方案统计指标id';COMMENT ON COLUMN "TAXQPLANFIELDCOUNT"."PLANID" IS '查询统计方案id';COMMENT ON COLUMN "TAXQPLANFIELDCOUNT"."SORT" IS '排序号';COMMENT ON COLUMN "TAXQPLANFIELDCOUNT"."FIELDID" IS '查询统计项目ID';COMMENT ON COLUMN "TAXQPLANFIELDCOUNT"."COUNTWAY" IS '统计方式（1计数2去重后计数3求和4求平均5最大值6最小值）';CREATE TABLE "TADATASOURCEPRO" ("PROPERTYID" NUMBER(20,0) NOT NULL,"PROPERTYNAME" VARCHAR2(60) NULL,"VALUE" VARCHAR2(10) NULL,PRIMARY KEY ("PROPERTYID"));COMMENT ON TABLE "TADATASOURCEPRO" IS '数据源属性';COMMENT ON COLUMN "TADATASOURCEPRO"."PROPERTYID" IS '数据源属性id';COMMENT ON COLUMN "TADATASOURCEPRO"."PROPERTYNAME" IS '属性名称';COMMENT ON COLUMN "TADATASOURCEPRO"."VALUE" IS '属性值';insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQDATATYPE', '数据类型', '93', '日期型', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQDATATYPE', '数据类型', '3', '数字型', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQDATATYPE', '数据类型', '1', 'CHAR型', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQDATATYPE', '数据类型', '12', 'VARCHAR2型', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQDISPLAY', '查询统计项目值的展现形式', '11', '文本', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQDISPLAY', '查询统计项目值的展现形式', '12', '年月', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQDISPLAY', '查询统计项目值的展现形式', '13', '日期', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQDISPLAY', '查询统计项目值的展现形式', '14', '数字', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQORDERBY', '排序方式', '2', '降序', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQORDERBY', '排序方式', '1', '升序', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQDISPLAY', '查询统计项目值的展现形式', '22', '树', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQDISPLAY', '查询统计项目值的展现形式', '21', '代码值平铺', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQRELA', '支持的关系', '42', '不包含', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQCOUNT', '查询统计结果的计算方式', '6', '最小值', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQCOUNT', '查询统计结果的计算方式', '5', '最大值', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQCOUNT', '查询统计结果的计算方式', '4', '求平均', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQCOUNT', '查询统计结果的计算方式', '3', '求和', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQCOUNT', '查询统计结果的计算方式', '2', '去重后计数', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQCOUNT', '查询统计结果的计算方式', '1', '计数', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQRELA', '支持的关系', '41', '包含', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQRELA', '支持的关系', '32', '小于等于', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQRELA', '支持的关系', '31', '小于', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQRELA', '支持的关系', '22', '大于等于', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQRELA', '支持的关系', '21', '大于', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQRELA', '支持的关系', '12', '不等于', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('XQRELA', '支持的关系', '11', '等于', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('DBTYPE', '数据源类型', '1', 'Oracle', '9999', '0', '0');insert into aa10 (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER)values ('DBTYPE', '数据源类型', '2', 'MySQL', '9999', '0', '0');INSERT INTO tamenu (menuid, pmenuid, menuname, url, menuidpath, menunamepath, iconskin, selectimage, reportid, accesstimeel, effective, securitypolicy, isdismultipos, quickcode, sortno, resourcetype, menulevel, isleaf, menutype, iscache, syspath, useyab003, typeflag, isaudite, consolemodule, customencoding, isfiledscontrol)VALUES (400, 3, '通用查询', 'theme/themeController.do', '1/3/400', '银海软件/开发管理/通用查询', '', null, null, '', '1', '1', '1', null, 6, '01', 3, '0', '1', null, 'sysmg', '0', null, '0', '0', '', '0');CREATE OR REPLACE VIEW APPTREECODE ASSELECT  THEMEID AS ID,  DATASOURCEID AS PID,  THEMENAME AS NAME,  '' AS isParent,  'THEMETREE' AS TYPE,  '' AS icon,  'icon-028?#259c7a' AS iconskin,  NULL AS py  FROM TAXQTHEMEUNION ALLSELECT  DATASOURCEID AS ID,  0 AS PID,  DATASOURCENAME AS NAME,  '' AS isParent,  'THEMETREE' AS TYPE,  '' AS icon,  'icon-035?#259c8e' AS iconskin,  NULL AS py  FROM TADATASOURCEUNION ALLSELECT  0 AS ID,  NULL AS PID,  '数据源' AS NAME,  '' AS isParent,  'THEMETREE' AS TYPE,  '' AS icon,  'icon-040?#35899c' AS iconskin,  NULL AS py  FROM DUAL;