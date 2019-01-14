-- SPDX-License-Identifier: Apache-2.0
-- Copyright Contributors to the ODPi Egeria project.
disconnect all$
connect to Patient$

DROP TABLE Patient$

CREATE TABLE Patient (
  PatientId INT NOT NULL,
  Patient VARCHAR(40) NOT NULL,
  LH_Addr_1 VARCHAR(40),
  LH_Addr_2 VARCHAR(40),
  LH_Addr_3 VARCHAR(40),
  LH_Addr_4 VARCHAR(40),
  LH_Addr_5 VARCHAR(40),
  Trial VARCHAR(10) NOT NULL
)$
import from '{{ egeria_samples_cocopharma_targets.files }}/Patient-Patient.csv' of DEL modified by coldel; skipcount 1 insert into Patient$

terminate$
