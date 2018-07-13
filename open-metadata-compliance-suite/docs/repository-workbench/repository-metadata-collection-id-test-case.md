<!-- SPDX-License-Identifier: Apache-2.0 -->

# repository metadata collection id test case

Validate the retrieval of the metadata collection id from the open metadata repository.

## Operation

This test uses the ../open-metadata/repository-services/metadata-collection-id
operation to test that the repository knows its metadata collection id.

## Assertions

#### repository-metadata-collection-id-test-case-01: Metadata collection id retrieved from repository.

The metadata collection id has successfully been retrieved from the server.
If this assertion fails, check that the server is started and the
open metadata services started.

#### repository-metadata-collection-id-test-case-02: Consistent metadata collection id retrieved from repository.

The same metadata collection id was retrieved when the server was called
for a second time.