DROP TABLE IF EXISTS Field;
CREATE TABLE Field (
f_id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL UNIQUE,
title VARCHAR NOT NULL ,
xcoord INTEGER NOT NULL ,
width INTEGER NOT NULL ,
helphtml VARCHAR NOT NULL ,
column_no INTEGER NOT NULL ,
project_id INTEGER NOT NULL ,
knowndata VARCHAR);

DROP TABLE IF EXISTS Batch;
CREATE TABLE Batch (
b_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,
file VARCHAR NOT NULL , 
project_id INTEGER NOT NULL , 
status INTEGER NOT NULL);

DROP TABLE IF EXISTS Project;
CREATE TABLE Project (
p_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 
title VARCHAR NOT NULL , 
recordsperimage INTEGER NOT NULL , 
firstycoord INTEGER NOT NULL , 
recordheight INTEGER NOT NULL );

DROP TABLE IF EXISTS Record;
CREATE TABLE Record (
r_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 
value VARCHAR, 
record_no INTEGER NOT NULL , 
field_id INTEGER NOT NULL , 
batch_id INTEGER NOT NULL );

DROP TABLE IF EXISTS User;
CREATE TABLE User (
u_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE ,
username VARCHAR NOT NULL ,
password VARCHAR NOT NULL ,
firstname VARCHAR NOT NULL ,
lastname VARCHAR NOT NULL ,
email VARCHAR NOT NULL ,
indexedrecords INTEGER, 
currentbatch INTEGER);