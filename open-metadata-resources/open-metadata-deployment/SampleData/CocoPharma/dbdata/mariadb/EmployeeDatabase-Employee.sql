-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
create database IF NOT EXISTS EmployeeDatabase  character set = UTF8;

use EmployeeDatabase;

drop table if exists Employee;
CREATE TABLE Employee (
  PNUM INT NOT NULL,
  FNAME VARCHAR(40) NOT NULL,
  LNAME VARCHAR(120) NOT NULL,
  EMPSTATUS INT NOT NULL,
  LVL INT NOT NULL,
  DEPT INT NOT NULL,
  ROLE VARCHAR(40) NOT NULL,
  LOCCODE INT NOT NULL
) ;

load data infile '/tmp/_mariadbimport-ocopharma_tmp/EmployeeDatabase-Employee.csv' into table Employee columns terminated by ';' ignore 1 lines;
