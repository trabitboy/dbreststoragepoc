--1. Create LEOS user
create user leos identified by leos;
grant dba to leos;
grant resource to leos;
grant connect to leos;

--2. Create datafiles to accommodate 200GB of data
alter database datafile 'C:\app09\oracle\base\oradata\ORACLE\users01.dbf' autoextend on maxsize unlimited;
alter tablespace USERS add datafile 'C:\app09\oracle\base\oradata\ORACLE\USERS02.DBF' size 10000M autoextend on next 10000M maxsize unlimited;
alter tablespace USERS add datafile 'C:\app09\oracle\base\oradata\ORACLE\USERS03.DBF' size 10000M autoextend on next 10000M maxsize unlimited;
alter tablespace USERS add datafile 'C:\app09\oracle\base\oradata\ORACLE\USERS04.DBF' size 10000M autoextend on next 10000M maxsize unlimited;
alter tablespace USERS add datafile 'C:\app09\oracle\base\oradata\ORACLE\USERS05.DBF' size 10000M autoextend on next 10000M maxsize unlimited;
alter tablespace USERS add datafile 'C:\app09\oracle\base\oradata\ORACLE\USERS06.DBF' size 10000M autoextend on next 10000M maxsize unlimited;
alter tablespace USERS add datafile 'C:\app09\oracle\base\oradata\ORACLE\USERS07.DBF' size 10000M autoextend on next 10000M maxsize unlimited;
alter tablespace USERS add datafile 'C:\app09\oracle\base\oradata\ORACLE\USERS08.DBF' size 10000M autoextend on next 10000M maxsize unlimited;
alter tablespace USERS add datafile 'C:\app09\oracle\base\oradata\ORACLE\USERS09.DBF' size 10000M autoextend on next 10000M maxsize unlimited;

--3. Create just 1 package then
ALTER TABLE xml_content ADD insert_timestamp timestamp DEFAULT systimestamp;