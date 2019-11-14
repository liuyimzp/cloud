
create or replace force view act_id_group as
select t.positionid as id_,
1 as rev_, t.positionname as name_, 'assignment' as type_
 from taposition t;

create or replace force view act_id_membership as
select up.userid as user_id_, p.positionid as group_id_
from taposition p, tauserposition up
where up.positionid=p.positionid;

create or replace force view act_id_user as
Select t.userid as id_, 1 as rev_ , t.name as first_ , t.loginid as last_, null as email_,
null as pwd_, null as picture_id_ from tauser t;


create table ACT_ID_INFO (
    ID_ NVARCHAR2(64),
    REV_ INTEGER,
    USER_ID_ NVARCHAR2(64),
    TYPE_ NVARCHAR2(64),
    KEY_ NVARCHAR2(255),
    VALUE_ NVARCHAR2(255),
    PASSWORD_ BLOB,
    PARENT_ID_ NVARCHAR2(255),
    primary key (ID_)
);

