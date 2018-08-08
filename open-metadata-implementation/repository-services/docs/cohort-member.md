<!-- SPDX-License-Identifier: Apache-2.0 -->

# Cohort member

A cohort member is an **[open metadata repository](open-metadata-repository.md)**
that has registered with an **[open metadata repository cohort](open-metadata-repository-cohort.md)**.

The registration process is triggered through 
the **[administrative services](../../governance-servers/admin-services/Using-the-Admin-Services.md)**.
It is necessary to configure an **event bus connector** and then enable
access to a named cohort.   These two commands update the configuration document for
a server and so the next time the open metadata services are activated the configuration is retained.
