-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
create database IF NOT EXISTS CompanyDirectoryDatabase  character set = UTF8;

use CompanyDirectoryDatabase;

drop table if exists ContactEmail;
CREATE TABLE ContactEmail (
  RedIf INT NOT NULL,
  EType CHAR NOT NULL,
  Email VARCHAR(120) NOT NULL
) ;

load data infile '/tmp/_mariadbimport-ocopharma_tmp/CompanyDirectoryDatabase-ContactEmail.csv' into table ContactEmail columns terminated by ';' ignore 1 lines;
