alter table AA10_QUERY
   drop primary key cascade;

drop table AA10_QUERY cascade constraints;

create table AA10_QUERY
(
   AAA100               VARCHAR2(20)         not null,
   AAA101               VARCHAR2(100)        not null,
   AAA102               VARCHAR2(10)         not null,
   AAA103               VARCHAR2(200)        not null,
   YAB003               VARCHAR2(6)          not null,
   AAE120               VARCHAR2(6)          not null,
   VER                  NUMBER(10),
   PXH                  VARCHAR2(10),
   AAE013               VARCHAR2(100)
);

comment on table AA10_QUERY is
'报表的代码表';

comment on column AA10_QUERY.AAA100 is
'代码类别';

comment on column AA10_QUERY.AAA101 is
'类别名称';

comment on column AA10_QUERY.AAA102 is
'代码值';

comment on column AA10_QUERY.AAA103 is
'代码名称';

comment on column AA10_QUERY.YAB003 is
'经办机构';

comment on column AA10_QUERY.AAE120 is
'注销标志';

comment on column AA10_QUERY.PXH is
'排序号';

comment on column AA10_QUERY.AAE013 is
'说明';

alter table AA10_QUERY
   add constraint PK_AA10_QUERY primary key (AAA100, AAA102);

drop index IDX_ZB61_1;

alter table ZB61
   drop primary key cascade;

drop table ZB61 cascade constraints;

create table ZB61
(
   YZB610               NUMBER(20)           not null,
   YZB617               VARCHAR2(2)          not null,
   YZB611               VARCHAR2(20),
   YZB612               VARCHAR2(200),
   YZB613               VARCHAR2(40),
   YZB615               VARCHAR2(1000),
   YZB616               VARCHAR2(1000),
   AAE011               VARCHAR2(100)        not null,
   YAE116               NUMBER(15)           not null,
   AAE017               NUMBER(15)           not null,
   AAE036               DATE                 not null
);

comment on table ZB61 is
'查询统计主题';

comment on column ZB61.YZB610 is
'查询统计主题流水号';

comment on column ZB61.YZB617 is
'查询统计类型（1查询2统计）';

comment on column ZB61.YZB611 is
'查询统计主题代码';

comment on column ZB61.YZB612 is
'查询统计主题名称';

comment on column ZB61.YZB613 is
'查询统计主题库表';

comment on column ZB61.YZB615 is
'基础WHERE条件';

comment on column ZB61.YZB616 is
'基础WHERE条件描述';

comment on column ZB61.AAE011 is
'经办人';

comment on column ZB61.YAE116 is
'经办人编号';

comment on column ZB61.AAE017 is
'经办机构';

comment on column ZB61.AAE036 is
'经办日期';

alter table ZB61
   add constraint PK_ZB61 primary key (YZB610);

create unique index IDX_ZB61_1 on ZB61 (
   YZB611 ASC
);
drop index IDX_ZB62_1;

alter table ZB62
   drop primary key cascade;

drop table ZB62 cascade constraints;

create table ZB62
(
   YZB620               NUMBER(20)           not null,
   YZB610               NUMBER(20)           not null,
   YZB621               NUMBER(4),
   YZB623               VARCHAR2(200),
   YZB624               VARCHAR2(30),
   YZB625               VARCHAR2(200),
   YZB626               VARCHAR2(10),
   YZB628               VARCHAR2(60),
   YZB62D               VARCHAR2(2),
   YZB62A               VARCHAR2(2),
   YZB62B               VARCHAR2(2),
   YZB62C               VARCHAR2(2),
   YZB641               VARCHAR2(2),
   YZB62F               VARCHAR2(2),
   AAE100               VARCHAR2(1)
);

comment on table ZB62 is
'查询统计项目';

comment on column ZB62.YZB620 is
'查询统计项目流水号';

comment on column ZB62.YZB610 is
'查询统计主题流水号';

comment on column ZB62.YZB621 is
'排序号';

comment on column ZB62.YZB623 is
'数据库字段或表达式';

comment on column ZB62.YZB624 is
'数据库字段AS别名';

comment on column ZB62.YZB625 is
'数据库字段中文';

comment on column ZB62.YZB626 is
'数据类型（1字符型2数字型3日期型）';

comment on column ZB62.YZB628 is
'代码类别';

comment on column ZB62.YZB62D is
'是否作为查询条件（1是0否）';

comment on column ZB62.YZB62A is
'查询条件的展现形式（11文本12年月13日期14数字21代码值平铺22树23DATAGRID中选择）';

comment on column ZB62.YZB62B is
'分组控制（0不用于分组1默认选中2默认未选中）';

comment on column ZB62.YZB62C is
'分组计算控制（0不能用于计算1默认用于计算2默认不用于计算）';

comment on column ZB62.YZB641 is
'默认统计方式（1计数2去重后计数3求和4求平均5最大值6最小值）';

comment on column ZB62.YZB62F is
'查询列控制（0不能作为查询列1默认作为查询列2默认不作为查询列）';

comment on column ZB62.AAE100 is
'有效标志（1有效0无效）';

alter table ZB62
   add constraint PK_ZB62 primary key (YZB620);

create index IDX_ZB62_1 on ZB62 (
   YZB610 ASC,
   AAE100 ASC,
   YZB621 ASC
);
drop index IDX_ZB63_1;

alter table ZB63
   drop primary key cascade;

drop table ZB63 cascade constraints;

create table ZB63
(
   YZB630               NUMBER(20)           not null,
   YZB620               NUMBER(20)           not null,
   YZB631               VARCHAR2(2)          not null
);

comment on table ZB63 is
'查询条件支持的关系符';

comment on column ZB63.YZB630 is
'关系符流水号';

comment on column ZB63.YZB620 is
'查询统计项目流水号';

comment on column ZB63.YZB631 is
'支持的关系（11等于12不等于21大于22大于等于31小于32小于等于41包含42不包含）';

alter table ZB63
   add constraint PK_ZB63 primary key (YZB630);

create index IDX_ZB63_1 on ZB63 (
   YZB620 ASC
);
drop index IDX_ZB64_1;

alter table ZB64
   drop primary key cascade;

drop table ZB64 cascade constraints;

create table ZB64
(
   YZB640               NUMBER(20)           not null,
   YZB620               NUMBER(20)           not null,
   YZB641               VARCHAR2(2)
);

comment on table ZB64 is
'查询结果支持的计算方式';

comment on column ZB64.YZB640 is
'计算方式流水号';

comment on column ZB64.YZB620 is
'查询统计项目流水号';

comment on column ZB64.YZB641 is
'查询结果的计算方式（1计数2去重后计数3求和4求平均5最大值6最小值）';

alter table ZB64
   add constraint PK_ZB64 primary key (YZB640);

create index IDX_ZB64_1 on ZB64 (
   YZB620 ASC
);
drop index IDX_ZB65_1;

alter table ZB65
   drop primary key cascade;

drop table ZB65 cascade constraints;

create table ZB65
(
   YZB650               NUMBER(20)           not null,
   YZB651               NUMBER(4),
   YZB620               NUMBER(20)           not null,
   YZB652               VARCHAR2(2)
);

comment on table ZB65 is
'查询结果缺省排序项目';

comment on column ZB65.YZB650 is
'查询统计主题排序项目流水号';

comment on column ZB65.YZB651 is
'排序号';

comment on column ZB65.YZB620 is
'查询统计项目流水号';

comment on column ZB65.YZB652 is
'排序方式（1升序2降序）';

alter table ZB65
   add constraint PK_ZB65 primary key (YZB650);

create index IDX_ZB65_1 on ZB65 (
   YZB620 ASC
);
drop index IDX_ZB71_1;

alter table ZB71
   drop primary key cascade;

drop table ZB71 cascade constraints;

create table ZB71
(
   YZB710               NUMBER(20)           not null,
   YZB610               NUMBER(20)           not null,
   YZB711               VARCHAR2(200),
   YZB617               VARCHAR2(2)          not null,
   YZB713               VARCHAR2(1),
   AAE011               VARCHAR2(100)        not null,
   YAE116               NUMBER(15)           not null,
   AAE017               VARCHAR(20)          not null,
   AAE036               DATE                 not null
);

comment on table ZB71 is
'查询统计方案';

comment on column ZB71.YZB710 is
'查询统计方案流水号';

comment on column ZB71.YZB610 is
'查询统计主题流水号';

comment on column ZB71.YZB711 is
'查询统计方案名称';

comment on column ZB71.YZB617 is
'查询统计类型（1查询2统计）';

comment on column ZB71.YZB713 is
'方案保存期限（1永久2临时）';

comment on column ZB71.AAE011 is
'经办人';

comment on column ZB71.YAE116 is
'经办人编号';

comment on column ZB71.AAE017 is
'经办机构';

comment on column ZB71.AAE036 is
'经办日期';

alter table ZB71
   add constraint PK_ZB71 primary key (YZB710);

create index IDX_ZB71_1 on ZB71 (
   YZB610 ASC
);
drop index IDX_ZB72_1;

alter table ZB72
   drop primary key cascade;

drop table ZB72 cascade constraints;

create table ZB72
(
   YZB720               NUMBER(20)           not null,
   YZB710               NUMBER(20)           not null,
   AAE036               DATE                 not null
);

comment on table ZB72 is
'查询统计方案的WHERE条件分组（各分组用OR关联，分组内用AND关联）';

comment on column ZB72.YZB720 is
'查询统计方案WHERE条件分组流水号';

comment on column ZB72.YZB710 is
'查询统计方案流水号';

comment on column ZB72.AAE036 is
'经办日期';

alter table ZB72
   add constraint PK_ZB72 primary key (YZB720);

create index IDX_ZB72_1 on ZB72 (
   YZB710 ASC
);
drop index IDX_ZB73_2;

drop index IDX_ZB73_1;

alter table ZB73
   drop primary key cascade;

drop table ZB73 cascade constraints;

create table ZB73
(
   YZB730               NUMBER(20)           not null,
   YZB710               NUMBER(20)           not null,
   YZB720               NUMBER(20)           not null,
   YZB731               NUMBER(4),
   YZB620               NUMBER(20)           not null,
   YZB631               VARCHAR2(2),
   YZB733               VARCHAR2(400),
   YZB734               VARCHAR2(2),
   AAE036               DATE                 not null
);

comment on table ZB73 is
'查询统计方案WHERE条件';

comment on column ZB73.YZB730 is
'查询统计方案WHERE条件流水号';

comment on column ZB73.YZB710 is
'查询统计方案流水号';

comment on column ZB73.YZB720 is
'查询统计方案WHERE条件分组流水号';

comment on column ZB73.YZB731 is
'分组内排序号';

comment on column ZB73.YZB620 is
'查询统计项目流水号';

comment on column ZB73.YZB631 is
'选择的关系（11等于12不等于21大于22大于等于31小于32小于等于41包含42不包含）';

comment on column ZB73.YZB733 is
'输入框中录入的值';

comment on column ZB73.YZB734 is
'值对象（1固定值2项目）';

comment on column ZB73.AAE036 is
'经办日期';

alter table ZB73
   add constraint PK_ZB73 primary key (YZB730);

create index IDX_ZB73_1 on ZB73 (
   YZB710 ASC,
   YZB620 ASC
);

create index IDX_ZB73_2 on ZB73 (
   YZB620 ASC
);
drop index IDX_ZB74_1;

alter table ZB74
   drop primary key cascade;

drop table ZB74 cascade constraints;

create table ZB74
(
   YZB740               NUMBER(20)           not null,
   YZB730               NUMBER(20)           not null,
   YZB741               VARCHAR2(50),
   AAE036               DATE                 not null
);

comment on table ZB74 is
'查询统计方案WHERE条件的代码值';

comment on column ZB74.YZB740 is
'查询统计方案WHERE条件代码值流水号';

comment on column ZB74.YZB730 is
'查询统计方案的WHERE条件流水号';

comment on column ZB74.YZB741 is
'代码值';

comment on column ZB74.AAE036 is
'经办日期';

alter table ZB74
   add constraint PK_ZB74 primary key (YZB740);

create index IDX_ZB74_1 on ZB74 (
   YZB730 ASC
);
drop index IDX_ZB76_2;

drop index IDX_ZB76_1;

alter table ZB76
   drop primary key cascade;

drop table ZB76 cascade constraints;

create table ZB76
(
   YZB760               NUMBER(20)           not null,
   YZB710               NUMBER(20)           not null,
   YZB761               NUMBER(4),
   YZB620               NUMBER(20)           not null,
   YZB641               VARCHAR2(2),
   AAE036               DATE                 not null
);

comment on table ZB76 is
'查询统计方案查询项目';

comment on column ZB76.YZB760 is
'查询统计方案查询项目流水号';

comment on column ZB76.YZB710 is
'查询统计方案流水号';

comment on column ZB76.YZB761 is
'排序号';

comment on column ZB76.YZB620 is
'查询统计项目流水号';

comment on column ZB76.YZB641 is
'查询结果的计算方式（1计数2去重后计数3求和4求平均5最大值6最小值）';

comment on column ZB76.AAE036 is
'经办日期';

alter table ZB76
   add constraint PK_ZB76 primary key (YZB760);

create index IDX_ZB76_1 on ZB76 (
   YZB710 ASC
);

create index IDX_ZB76_2 on ZB76 (
   YZB620 ASC
);
drop index IDX_ZB77_2;

drop index IDX_ZB77_1;

alter table ZB77
   drop primary key cascade;

drop table ZB77 cascade constraints;

create table ZB77
(
   YZB770               NUMBER(20)           not null,
   YZB710               NUMBER(20)           not null,
   YZB771               NUMBER(4),
   YZB620               NUMBER(20)           not null,
   YZB652               VARCHAR2(2),
   AAE036               DATE                 not null
);

comment on table ZB77 is
'查询统计方案排序项目';

comment on column ZB77.YZB770 is
'查询统计方案排序项目流水号';

comment on column ZB77.YZB710 is
'查询统计方案流水号';

comment on column ZB77.YZB771 is
'排序号';

comment on column ZB77.YZB620 is
'查询统计项目流水号';

comment on column ZB77.YZB652 is
'排序方式（1升序2降序）';

comment on column ZB77.AAE036 is
'经办日期';

alter table ZB77
   add constraint PK_ZB77 primary key (YZB770);

create index IDX_ZB77_1 on ZB77 (
   YZB710 ASC
);

create index IDX_ZB77_2 on ZB77 (
   YZB620 ASC
);

drop index IDX_ZB78_2;

drop index IDX_ZB78_1;

alter table ZB78
   drop primary key cascade;

drop table ZB78 cascade constraints;

create table ZB78
(
   YZB780               NUMBER(20)           not null,
   YZB710               NUMBER(20)           not null,
   YZB781               NUMBER(4),
   YZB620               NUMBER(20)           not null,
   AAE036               DATE                 not null
);

comment on table ZB78 is
'查询统计方案分组项目';

comment on column ZB78.YZB780 is
'查询统计方案分组项目流水号';

comment on column ZB78.YZB710 is
'查询统计方案流水号';

comment on column ZB78.YZB781 is
'排序号';

comment on column ZB78.YZB620 is
'查询统计项目流水号';

comment on column ZB78.AAE036 is
'经办日期';

alter table ZB78
   add constraint PK_ZB78 primary key (YZB780);

create index IDX_ZB78_1 on ZB78 (
   YZB710 ASC
);

create index IDX_ZB78_2 on ZB78 (
   YZB620 ASC
);
drop index IDX_ZB79_2;

drop index IDX_ZB79_1;

alter table ZB79
   drop primary key cascade;

drop table ZB79 cascade constraints;

create table ZB79
(
   YZB790               NUMBER(20)           not null,
   YZB710               NUMBER(20)           not null,
   YZB791               NUMBER(4),
   YZB620               NUMBER(20)           not null,
   YZB641               VARCHAR2(2),
   AAE036               DATE                 not null
);

comment on table ZB79 is
'查询统计方案统计指标';

comment on column ZB79.YZB790 is
'查询统计方案统计指标流水号';

comment on column ZB79.YZB710 is
'查询统计方案流水号';

comment on column ZB79.YZB791 is
'排序号';

comment on column ZB79.YZB620 is
'查询统计项目流水号';

comment on column ZB79.YZB641 is
'统计方式（1计数2去重后计数3求和4求平均5最大值6最小值）';

comment on column ZB79.AAE036 is
'经办日期';

alter table ZB79
   add constraint PK_ZB79 primary key (YZB790);

create index IDX_ZB79_1 on ZB79 (
   YZB710 ASC
);

create index IDX_ZB79_2 on ZB79 (
   YZB620 ASC
);

-- Create sequence
create sequence SEQ_YZB610
minvalue 1
maxvalue 9999999999999999999999999999
start with 141
increment by 1
cache 20;
-- Create sequence
create sequence SEQ_YZB620
minvalue 1
maxvalue 9999999999999999999999999999
start with 601
increment by 1
cache 20;
-- Create sequence
create sequence SEQ_YZB630
minvalue 1
maxvalue 9999999999999999999999999999
start with 2921
increment by 1
cache 20;
-- Create sequence
create sequence SEQ_YZB640
minvalue 1
maxvalue 9999999999999999999999999999
start with 1641
increment by 1
cache 20;
-- Create sequence
create sequence SEQ_YZB650
minvalue 1
maxvalue 9999999999999999999999999999
start with 141
increment by 1
cache 20;
-- Create sequence
create sequence SEQ_YZB710
minvalue 1
maxvalue 9999999999999999999999999999
start with 1000280
increment by 1
cache 20;
-- Create sequence
create sequence SEQ_YZB720
minvalue 1
maxvalue 9999999999999999999999999999
start with 1000400
increment by 1
cache 20;
-- Create sequence
create sequence SEQ_YZB730
minvalue 1
maxvalue 9999999999999999999999999999
start with 1000380
increment by 1
cache 20;
-- Create sequence
create sequence SEQ_YZB740
minvalue 1
maxvalue 9999999999999999999999999999
start with 1000480
increment by 1
cache 20;
-- Create sequence
create sequence SEQ_YZB760
minvalue 1
maxvalue 9999999999999999999999999999
start with 1001080
increment by 1
cache 20;
-- Create sequence
create sequence SEQ_YZB770
minvalue 1
maxvalue 9999999999999999999999999999
start with 1000280
increment by 1
cache 20;
-- Create sequence
create sequence SEQ_YZB780
minvalue 1
maxvalue 9999999999999999999999999999
start with 1000420
increment by 1
cache 20;
-- Create sequence
create sequence SEQ_YZB790
minvalue 1
maxvalue 9999999999999999999999999999
start with 1000380
increment by 1
cache 20;

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB626', '数据类型', '3', '日期型', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB626', '数据类型', '2', '数字型', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB626', '数据类型', '1', '字符型', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB629', '查询统计限制', '3', '不可选', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB629', '查询统计限制', '2', '必选', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB629', '查询统计限制', '1', '缺省选中', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB629', '查询统计限制', '0', '可选', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62A', '查询统计项目值的展现形式', '11', '文本', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62A', '查询统计项目值的展现形式', '12', '年月', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62A', '查询统计项目值的展现形式', '13', '日期', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62A', '查询统计项目值的展现形式', '14', '数字', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62B', '分组控制', '1', '默认选中', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62B', '分组控制', '0', '不用于分组', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62C', '是否用于统计计算', '1', '默认用于计算', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62C', '是否用于统计计算', '0', '不能用于计算', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB652', '排序方式', '2', '降序', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB652', '排序方式', '1', '升序', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62A', '查询统计项目值的展现形式', '23', 'DATAGRID中选择', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62A', '查询统计项目值的展现形式', '22', '树', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62A', '查询统计项目值的展现形式', '21', '代码值平铺', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB631', '支持的关系', '42', '不包含', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB641', '查询统计结果的计算方式', '6', '最小值', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB641', '查询统计结果的计算方式', '5', '最大值', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB641', '查询统计结果的计算方式', '4', '求平均', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB641', '查询统计结果的计算方式', '3', '求和', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB641', '查询统计结果的计算方式', '2', '去重后计数', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB641', '查询统计结果的计算方式', '1', '计数', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB631', '支持的关系', '41', '包含', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB631', '支持的关系', '32', '小于等于', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB631', '支持的关系', '31', '小于', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB631', '支持的关系', '22', '大于等于', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB631', '支持的关系', '21', '大于', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB631', '支持的关系', '12', '不等于', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB631', '支持的关系', '11', '等于', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB617', '查询统计类型', '2', '统计', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB617', '查询统计类型', '1', '查询', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62D', '是否作为查询条件', '0', '否', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62D', '是否作为查询条件', '1', '是', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62B', '分组控制', '2', '默认未选中', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62C', '是否用于统计计算', '2', '默认不用于计算', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62F', '查询列控制', '2', '默认不作为查询列', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62F', '查询列控制', '1', '默认作为查询列', '9999', '0', null, '', '');

insert into aa10_query (AAA100, AAA101, AAA102, AAA103, YAB003, AAE120, VER, PXH, AAE013)
values ('YZB62F', '查询列控制', '0', '不能作为查询列', '9999', '0', null, '', '');


create FUNCTION FUN_getCodeDesc (
     prm_codeType IN   VARCHAR2,
     prm_codeValue IN   VARCHAR2)
   RETURN VARCHAR2
   IS
      s_return VARCHAR2(200);
      CURSOR cur_aa10a1
      IS
         SELECT DISTINCT
               aaa103
          FROM aa10a1
         WHERE aaa100 = prm_codeType
           AND aaa102 = prm_codeValue;
   BEGIN
      OPEN cur_aa10a1;
      FETCH cur_aa10a1 INTO s_return;
      IF cur_aa10a1%NOTFOUND THEN
         s_return := prm_codeValue;
      END IF;
      RETURN s_return;
   END FUN_getCodeDesc ;

create or replace view aa10a1 as
select
   AAA100,
   AAA101,
   AAA102,
   AAA103,
   YAB003,
   AAE120,
   VER
from
   AA10
Union all
select
   AAA100,
   AAA101,
   AAA102,
   AAA103,
   YAB003,
   AAE120,
   VER
from
   Aa10_Query;


commit;























