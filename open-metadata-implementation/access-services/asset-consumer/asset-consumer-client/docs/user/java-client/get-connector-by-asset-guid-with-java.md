<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# getConnectorForAsset

```
    /**
     * Returns the connector corresponding to the supplied asset GUID.
     *
     * @param userId       userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository.
     *
     * @return Connector   connector instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    Connector getConnectorForAsset(String userId,
                                   String assetGUID) throws InvalidParameterException,
                                                            ConnectionCheckedException,
                                                            ConnectorCheckedException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException;

```



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.