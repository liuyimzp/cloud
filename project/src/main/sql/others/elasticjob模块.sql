-- Create table
create table TAZKADDRESSMG
(
  zkid         VARCHAR2(20) not null,
  zkaddress    VARCHAR2(200),
  appname      VARCHAR2(60),
  appnamespace VARCHAR2(30)
);
alter table TAZKADDRESSMG
  add constraint PK_ZKID primary key (ZKID)
  using index;


CREATE TABLE     JOB_EXECUTION_LOG     ( 
                       id VARCHAR(40) NOT NULL,  
                       job_name VARCHAR(100) NOT NULL,  
                       task_id VARCHAR(255) NOT NULL,  
                       hostname VARCHAR(255) NOT NULL,  
                       ip VARCHAR(50) NOT NULL,  
                       sharding_item NUMBER(10) NOT NULL,  
                       execution_source VARCHAR(20) NOT NULL,  
                       failure_cause VARCHAR(4000) NULL,  
                       is_success NUMBER(1) NOT NULL,  
                       start_time TIMESTAMP NULL,  
                       complete_time TIMESTAMP NULL,  
                       PRIMARY KEY (id));
					   
CREATE TABLE     JOB_STATUS_TRACE_LOG     ( 
                       id VARCHAR(40) NOT NULL,  
                       job_name VARCHAR(100) NOT NULL,  
                       original_task_id VARCHAR(255),  
                       task_id VARCHAR(255) NOT NULL,  
                       slave_id VARCHAR(50) NOT NULL,  
                       source VARCHAR(50) NOT NULL,  
                       execution_type VARCHAR(20) NOT NULL,  
                       sharding_item VARCHAR(100) NOT NULL,  
                       state VARCHAR(20) NOT NULL,  
                       message VARCHAR(4000) NULL,  
                       creation_time TIMESTAMP NULL,  
                       PRIMARY KEY (id));
CREATE INDEX     TASK_ID_STATE_INDEX     ON     JOB_STATUS_TRACE_LOG     (task_id, state);