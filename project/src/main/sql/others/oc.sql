
create table TA_OC_ACCEPTANCEINFO
(
  acceptanceinfoid  NUMBER(10) not null,
  acceptanceid      NUMBER(10) not null,
  datajson          BLOB,
  creator           VARCHAR2(100),
  createtime        DATE,
  processinstanceid VARCHAR2(50)
)
;
comment on column TA_OC_ACCEPTANCEINFO.acceptanceinfoid
  is '受理信息id';
comment on column TA_OC_ACCEPTANCEINFO.acceptanceid
  is '受理业务id';
comment on column TA_OC_ACCEPTANCEINFO.datajson
  is '数据json';
comment on column TA_OC_ACCEPTANCEINFO.creator
  is '创建人';
comment on column TA_OC_ACCEPTANCEINFO.createtime
  is '创建时间';
comment on column TA_OC_ACCEPTANCEINFO.processinstanceid
  is '流程实例ID';
alter table TA_OC_ACCEPTANCEINFO
  add constraint PK_TA_OC_ACCEPTANCEINFO primary key (ACCEPTANCEINFOID);

create table TA_OC_ACCEPTANCEMG
(
  acceptanceid         NUMBER(10) not null,
  acceptancename       VARCHAR2(100) not null,
  businessid           VARCHAR2(30),
  businesstype         VARCHAR2(6),
  formid               NUMBER(10),
  acceptanceobjecttype VARCHAR2(6),
  flowid               VARCHAR2(50),
  creator              NUMBER(10),
  createtime           DATE,
  menuid               NUMBER(10),
  deployid             VARCHAR2(50)
)
;
comment on table TA_OC_ACCEPTANCEMG
  is '受理配置信息';
comment on column TA_OC_ACCEPTANCEMG.acceptanceid
  is '受理配置ID';
comment on column TA_OC_ACCEPTANCEMG.acceptancename
  is '业务名称';
comment on column TA_OC_ACCEPTANCEMG.businessid
  is '业务标识';
comment on column TA_OC_ACCEPTANCEMG.businesstype
  is '业务分类';
comment on column TA_OC_ACCEPTANCEMG.formid
  is '表单ID';
comment on column TA_OC_ACCEPTANCEMG.acceptanceobjecttype
  is '受理对象类型';
comment on column TA_OC_ACCEPTANCEMG.flowid
  is '流程ID';
comment on column TA_OC_ACCEPTANCEMG.creator
  is '创建人';
comment on column TA_OC_ACCEPTANCEMG.createtime
  is '创建时间';
comment on column TA_OC_ACCEPTANCEMG.menuid
  is '受理菜单ID';
comment on column TA_OC_ACCEPTANCEMG.deployid
  is '流程部署ID';

create table TA_OC_ACCEPTANCEOBJECTINFO
(
  acceptanceid           NUMBER(10) not null,
  accpeptanceobjecttype  VARCHAR2(6) not null,
  propertyname           VARCHAR2(30),
  acceptanceobjectinfoid NUMBER(10) not null
);
comment on table TA_OC_ACCEPTANCEOBJECTINFO
  is '受理对象信息';
comment on column TA_OC_ACCEPTANCEOBJECTINFO.acceptanceid
  is '受理配置ID';
comment on column TA_OC_ACCEPTANCEOBJECTINFO.accpeptanceobjecttype
  is '受理对象类型';
comment on column TA_OC_ACCEPTANCEOBJECTINFO.propertyname
  is '属性名';
comment on column TA_OC_ACCEPTANCEOBJECTINFO.acceptanceobjectinfoid
  is '受理对象信息ID';
alter table TA_OC_ACCEPTANCEOBJECTINFO
  add constraint PK_TA_OC_ACCEPTANCEOBJECTINFO primary key (ACCEPTANCEOBJECTINFOID);


create table TA_OC_ATTACHMENT
(
  acceptanceid NUMBER(10) not null,
  attachid     NUMBER(10) not null,
  attachname   VARCHAR2(100),
  acctchtype   VARCHAR2(6)
)
;
comment on table TA_OC_ATTACHMENT
  is '受理配置附件信息';
comment on column TA_OC_ATTACHMENT.acceptanceid
  is '受理配置ID';
comment on column TA_OC_ATTACHMENT.attachid
  is '附件id';
comment on column TA_OC_ATTACHMENT.attachname
  is '附件名';
comment on column TA_OC_ATTACHMENT.acctchtype
  is '附件类型';
alter table TA_OC_ATTACHMENT
  add constraint PK_TA_OC_ATTACHMENT primary key (ATTACHID);


create table TA_OC_ATTACHMENTADDR
(
  attachmentaddrid NUMBER(10) not null,
  acceptanceinfoid NUMBER(10) not null,
  attachmentname   VARCHAR2(100),
  address          VARCHAR2(200) not null,
  creator          VARCHAR2(100),
  createtime       DATE,
  originalname     VARCHAR2(100),
  attachmentid     NUMBER(10)
)
;
comment on column TA_OC_ATTACHMENTADDR.attachmentaddrid
  is '上传附件id';
comment on column TA_OC_ATTACHMENTADDR.acceptanceinfoid
  is '受理业务信息id';
comment on column TA_OC_ATTACHMENTADDR.attachmentname
  is '附件上传后名称';
comment on column TA_OC_ATTACHMENTADDR.address
  is '附件地址';
comment on column TA_OC_ATTACHMENTADDR.creator
  is '上传人';
comment on column TA_OC_ATTACHMENTADDR.createtime
  is '上传时间';
comment on column TA_OC_ATTACHMENTADDR.originalname
  is '附件原名称';
comment on column TA_OC_ATTACHMENTADDR.attachmentid
  is '关联附件表的id';
alter table TA_OC_ATTACHMENTADDR
  add constraint PK_TA_OC_ATTACHMENTADDR primary key (ATTACHMENTADDRID);


create table TA_OC_DISTINGUISHSERVICE
(
  distinguishid   NUMBER(10),
  distinguishname VARCHAR2(50),
  businesstype    VARCHAR2(2),
  distinguishtype VARCHAR2(2),
  url             VARCHAR2(500),
  sqldefid        NUMBER(10),
  opentype        VARCHAR2(2),
  creator         VARCHAR2(100),
  createtime      DATE,
  microserviceurl VARCHAR2(500),
  raqname         VARCHAR2(100)
)
;
comment on column TA_OC_DISTINGUISHSERVICE.distinguishid
  is '甄别服务主键';
comment on column TA_OC_DISTINGUISHSERVICE.distinguishname
  is '甄别名称';
comment on column TA_OC_DISTINGUISHSERVICE.businesstype
  is '业务类型';
comment on column TA_OC_DISTINGUISHSERVICE.distinguishtype
  is '甄别类型(url,模板,sql)';
comment on column TA_OC_DISTINGUISHSERVICE.url
  is '功能url';
comment on column TA_OC_DISTINGUISHSERVICE.sqldefid
  is 'sql定义时,sql的id';
comment on column TA_OC_DISTINGUISHSERVICE.opentype
  is '打开方式';
comment on column TA_OC_DISTINGUISHSERVICE.creator
  is '创建人';
comment on column TA_OC_DISTINGUISHSERVICE.createtime
  is '创建时间';
comment on column TA_OC_DISTINGUISHSERVICE.microserviceurl
  is '微服务地址';
comment on column TA_OC_DISTINGUISHSERVICE.raqname
  is '润乾模板名';


create table TA_OC_EXAMINEDISTINGUISH
(
  examinedistinguishid   NUMBER(10) not null,
  examinedistinguishname VARCHAR2(100),
  distinguishid          NUMBER(10),
  opentype               VARCHAR2(6),
  inparam                VARCHAR2(100),
  examineid              NUMBER(10) not null
)
;
comment on table TA_OC_EXAMINEDISTINGUISH
  is '审核业务甄别服务配置';
comment on column TA_OC_EXAMINEDISTINGUISH.examinedistinguishid
  is '审核业务甄别服务配置ID';
comment on column TA_OC_EXAMINEDISTINGUISH.examinedistinguishname
  is '审核业务甄别服务业务名称';
comment on column TA_OC_EXAMINEDISTINGUISH.distinguishid
  is '甄别服务ID';
comment on column TA_OC_EXAMINEDISTINGUISH.opentype
  is '打开方式';
comment on column TA_OC_EXAMINEDISTINGUISH.inparam
  is '甄别服务入参';
comment on column TA_OC_EXAMINEDISTINGUISH.examineid
  is '审核业务配置ID';
alter table TA_OC_EXAMINEDISTINGUISH
  add constraint PK_TA_OC_EXAMINEDISTINGUISH primary key (EXAMINEDISTINGUISHID);


create table TA_OC_EXAMINEMG
(
  examineid    NUMBER(10) not null,
  examinename  VARCHAR2(100) not null,
  acceptanceid NUMBER(10),
  showopinion  VARCHAR2(6),
  creator      NUMBER(10),
  createtime   DATE,
  menuid       NUMBER(10)
)
;
comment on table TA_OC_EXAMINEMG
  is '审核业务管理配置';
comment on column TA_OC_EXAMINEMG.examineid
  is '审核业务管理配置ID';
comment on column TA_OC_EXAMINEMG.examinename
  is '审核业务名称';
comment on column TA_OC_EXAMINEMG.acceptanceid
  is '受理业务ID';
comment on column TA_OC_EXAMINEMG.showopinion
  is '显示审核意见';
comment on column TA_OC_EXAMINEMG.creator
  is '创建人';
comment on column TA_OC_EXAMINEMG.createtime
  is '创建时间';
comment on column TA_OC_EXAMINEMG.menuid
  is '审核菜单ID';
alter table TA_OC_EXAMINEMG
  add constraint PK_TA_OC_EXAMINEMG primary key (EXAMINEID);
alter table TA_OC_EXAMINEMG
  add constraint IDX1_TA_OC_EXAMINEMG unique (ACCEPTANCEID);


create table TA_OC_EXAMINERESULT
(
  examineresultid  NUMBER(10) not null,
  ispass           VARCHAR2(6) not null,
  opinion          VARCHAR2(500),
  examineid        NUMBER(10),
  acceptanceinfoid NUMBER(10),
  creator          VARCHAR2(100),
  createtime       DATE
)
;
comment on column TA_OC_EXAMINERESULT.examineresultid
  is '审核结果id';
comment on column TA_OC_EXAMINERESULT.ispass
  is '审核是否通过';
comment on column TA_OC_EXAMINERESULT.opinion
  is '审核意见';
comment on column TA_OC_EXAMINERESULT.examineid
  is '审核业务配置id';
comment on column TA_OC_EXAMINERESULT.acceptanceinfoid
  is '审核对应受理信息id';
comment on column TA_OC_EXAMINERESULT.creator
  is '审核人';
comment on column TA_OC_EXAMINERESULT.createtime
  is '审核时间';


create table TA_OC_FORM
(
  formid       NUMBER(10) not null,
  formname     VARCHAR2(100),
  formurl      VARCHAR2(100),
  businesstype VARCHAR2(10),
  createtime   DATE,
  creator      VARCHAR2(100),
  formcode     BLOB,
  formType     VARCHAR2(6)
)
;
comment on column TA_OC_FORM.formid
  is '主键id';
comment on column TA_OC_FORM.formname
  is '表单名字';
comment on column TA_OC_FORM.formurl
  is '模板url';
comment on column TA_OC_FORM.businesstype
  is '业务类型';
comment on column TA_OC_FORM.createtime
  is '创建时间';
comment on column TA_OC_FORM.creator
  is '创建人';
comment on column TA_OC_FORM.formcode
  is '模板代码';
comment on column TA_OC_FORM.formType
  is '表单类型';
alter table TA_OC_FORM
  add constraint PK_TA_OC_FORM primary key (FORMID);


create table TA_OC_MICROCOLMAP
(
  microid   NUMBER(10) not null,
  sqlkey    VARCHAR2(50),
  methodkey VARCHAR2(50)
)
;
comment on column TA_OC_MICROCOLMAP.microid
  is '微服务对应的甄别id';
comment on column TA_OC_MICROCOLMAP.sqlkey
  is 'sql中返回的字段';
comment on column TA_OC_MICROCOLMAP.methodkey
  is '对应sql的中字段的key';


create table TA_OC_SQLCOLMAP
(
  sqldefid  NUMBER(10) not null,
  methodkey VARCHAR2(50),
  sqlkey    VARCHAR2(50)
)
;
comment on column TA_OC_SQLCOLMAP.sqldefid
  is 'sql定义id';
comment on column TA_OC_SQLCOLMAP.methodkey
  is '对应sql的中字段的key';
comment on column TA_OC_SQLCOLMAP.sqlkey
  is 'sql中返回的字段';


create table TA_OC_SQLDEFINITION
(
  sqldefid     NUMBER(10),
  sqlcontent   VARCHAR2(4000),
  creator      VARCHAR2(100),
  createtime   DATE,
  datasourceid VARCHAR2(50)
)
;
comment on column TA_OC_SQLDEFINITION.sqldefid
  is 'sql定义id';
comment on column TA_OC_SQLDEFINITION.sqlcontent
  is 'sql具体内容';
comment on column TA_OC_SQLDEFINITION.creator
  is '创建人';
comment on column TA_OC_SQLDEFINITION.createtime
  is '创建时间';
comment on column TA_OC_SQLDEFINITION.datasourceid
  is '数据源的beanid';


insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('BUSINESSTYPE', '业务分类', '1', '分类1', '9999', '0', 0);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('BUSINESSTYPE', '业务分类', '2', '分类2', '9999', '0', 0);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('BUSINESSTYPE', '业务分类', '3', '分类3', '9999', '0', 0);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('BUSINESSTYPE', '业务分类', '4', '分类4', '9999', '0', 0);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('ATTACHMENTTYPE', '附件类型', '1', '附件类型一', '9999', '0', 0);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('ATTACHMENTTYPE', '附件类型', '2', '附件类型二', '9999', '0', 0);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('ATTACHMENTTYPE', '附件类型', '3', '附件类型三', '9999', '0', 0);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('ACCEPTANCEOBJECTTYPE', '业务对象类型', '1', '个人', '9999', '0', 0);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('ACCEPTANCEOBJECTTYPE', '业务对象类型', '2', '组织', '9999', '0', 0);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('ACCEPTANCEOBJECTTYPE', '业务对象类型', '3', '家庭', '9999', '0', 0);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('IDENTIFYOPENTYPE', '甄别服务打开方式', '1', '弹窗', '9999', '0', 0);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('IDENTIFYOPENTYPE', '甄别服务打开方式', '2', '菜单页', '9999', '0', 0);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('OCFORMTYPE', '表单类型', '1', '在线开发', '9999', '0', 0);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('OCFORMTYPE', '表单类型', '2', '模板代码', '9999', '0', 0);
insert into AA10 (aaa100, aaa101, aaa102, aaa103, yab003, aae120, ver)
values ('OCFORMTYPE', '表单类型', '3', 'url', '9999', '0', 0);
commit;

insert into TAMENU (menuid, pmenuid, menuname, url, menuidpath, menunamepath, iconskin, selectimage, reportid, accesstimeel, effective, securitypolicy, isdismultipos, quickcode, sortno, resourcetype, menulevel, isleaf, menutype, iscache, syspath, useyab003, typeflag, isaudite, consolemodule, customencoding, isfiledscontrol)
values (110085, 110004, '甄别服务功能管理', 'distinguish/DistinguishServiceMgController.do', '1/110004/110085', '银海软件/受理甄别审批组件/甄别服务功能管理', 'icon-027?#687da3', null, null, null, '1', '1', '1', null, 2, '01', 3, '1', '0', null, 'sysmg', '0', null, '0', '0', null, '0');
insert into TAMENU (menuid, pmenuid, menuname, url, menuidpath, menunamepath, iconskin, selectimage, reportid, accesstimeel, effective, securitypolicy, isdismultipos, quickcode, sortno, resourcetype, menulevel, isleaf, menutype, iscache, syspath, useyab003, typeflag, isaudite, consolemodule, customencoding, isfiledscontrol)
values (110288, 1, '受理业务', null, '1/110288', '银海软件/受理业务', 'icon-001?#de16de', null, null, null, '1', '1', '1', null, 6, '01', 2, '0', '2', null, 'sysmg', '0', null, '0', '0', null, '0');
insert into TAMENU (menuid, pmenuid, menuname, url, menuidpath, menunamepath, iconskin, selectimage, reportid, accesstimeel, effective, securitypolicy, isdismultipos, quickcode, sortno, resourcetype, menulevel, isleaf, menutype, iscache, syspath, useyab003, typeflag, isaudite, consolemodule, customencoding, isfiledscontrol)
values (110319, 110004, '流程管理', 'bpm/editor/bpmController.do', '1/110004/110319', '银海软件/受理甄别审批组件/流程管理', 'icon-001?#1e87e3', null, null, null, '1', '1', '1', null, 5, '01', 3, '1', '0', null, 'sysmg', '0', null, '0', '0', null, '0');
insert into TAMENU (menuid, pmenuid, menuname, url, menuidpath, menunamepath, iconskin, selectimage, reportid, accesstimeel, effective, securitypolicy, isdismultipos, quickcode, sortno, resourcetype, menulevel, isleaf, menutype, iscache, syspath, useyab003, typeflag, isaudite, consolemodule, customencoding, isfiledscontrol)
values (110056, 110004, '受理组件', 'acceptanceMg/acceptanceMgController.do', '1/110004/110056', '银海软件/受理甄别审批组件/受理组件', 'icon-014?#89a5d9', null, null, null, '1', '1', '1', null, 1, '01', 3, '1', '0', null, 'sysmg', '0', null, '0', '0', null, '0');
insert into TAMENU (menuid, pmenuid, menuname, url, menuidpath, menunamepath, iconskin, selectimage, reportid, accesstimeel, effective, securitypolicy, isdismultipos, quickcode, sortno, resourcetype, menulevel, isleaf, menutype, iscache, syspath, useyab003, typeflag, isaudite, consolemodule, customencoding, isfiledscontrol)
values (110229, 110004, '甄别审批业务管理', 'examineMg/examineMgController.do', '1/110004/110229', '银海软件/受理甄别审批组件/甄别审批业务管理', '?', null, null, null, '1', '1', '1', null, 3, '01', 3, '1', '0', null, 'sysmg', '0', null, '0', '0', null, '0');
insert into TAMENU (menuid, pmenuid, menuname, url, menuidpath, menunamepath, iconskin, selectimage, reportid, accesstimeel, effective, securitypolicy, isdismultipos, quickcode, sortno, resourcetype, menulevel, isleaf, menutype, iscache, syspath, useyab003, typeflag, isaudite, consolemodule, customencoding, isfiledscontrol)
values (110004, 1, '受理甄别审批组件', null, '1/110004', '银海软件/受理甄别审批组件', 'icon-017?#e31ee3', null, null, null, '1', '1', '1', null, 5, '01', 2, '0', '0', null, 'sysmg', '0', null, '0', '0', null, '0');
insert into TAMENU (menuid, pmenuid, menuname, url, menuidpath, menunamepath, iconskin, selectimage, reportid, accesstimeel, effective, securitypolicy, isdismultipos, quickcode, sortno, resourcetype, menulevel, isleaf, menutype, iscache, syspath, useyab003, typeflag, isaudite, consolemodule, customencoding, isfiledscontrol)
values (112949, 1, '甄别审批任务', 'processTask/processTaskController.do', '1/112949', '银海软件/甄别审批任务', 'icon-002?#e082e0', null, null, null, '1', '1', '1', null, 9, '01', 2, '1', '2', null, 'sysmg', '0', null, '0', '0', null, '0');
insert into TAMENU (menuid, pmenuid, menuname, url, menuidpath, menunamepath, iconskin, selectimage, reportid, accesstimeel, effective, securitypolicy, isdismultipos, quickcode, sortno, resourcetype, menulevel, isleaf, menutype, iscache, syspath, useyab003, typeflag, isaudite, consolemodule, customencoding, isfiledscontrol)
values (110009, 110004, '表单管理', 'form/formMgController.do', '1/110004/110009', '银海软件/受理甄别审批组件/表单管理', 'icon-026?#7d4f7d', null, null, null, '1', '4', '1', null, 0, '01', 3, '1', '0', null, 'sysmg', '0', null, '0', '0', null, '0');
insert into TAMENU (menuid, pmenuid, menuname, url, menuidpath, menunamepath, iconskin, selectimage, reportid, accesstimeel, effective, securitypolicy, isdismultipos, quickcode, sortno, resourcetype, menulevel, isleaf, menutype, iscache, syspath, useyab003, typeflag, isaudite, consolemodule, customencoding, isfiledscontrol)
values (110029, 110004, '在线表单绘制', 'demo/formDragController.do', '1/110004/110029', '银海软件/受理甄别审批组件/在线表单绘制', 'icon-044?#635fd4', null, null, null, '1', '1', '1', null, 5, '01', 3, '1', '0', null, 'sysmg', '0', null, '0', '0', null, '0');

commit;


