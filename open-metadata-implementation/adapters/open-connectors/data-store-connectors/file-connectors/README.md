<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# File Connectors

The File Connectors module contains connectors to different types of data stores.  These connectors implement the
[Open Connector Framework (OCF)](../../../../frameworks/open-connector-framework) **Connector** interface.

* **[basic-file-connector](basic-file-connector)** provides connector to read a file.
It has no special knowledge of the format.

* **[avro-file-connector](avro-file-connector)** provides connector to read files
that have an [Apache Avro](https://avro.apache.org/https://avro.apache.org/) format.

* **[csv-file-connector](csv-file-connector)** provides connector to read files
that have a CSV tabular format.

* **[data-folder-connector](data-folder-connector)** provides connector to read a data set that is made up of many files
stored within a data folder.