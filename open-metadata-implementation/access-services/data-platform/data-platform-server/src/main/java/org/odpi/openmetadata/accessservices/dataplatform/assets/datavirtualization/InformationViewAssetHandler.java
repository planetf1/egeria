package org.odpi.openmetadata.accessservices.dataplatform.assets.datavirtualization;

public class InformationViewAssetHandler
{
/*    private DataPlatformEvent event;
    private OMEntityDao omEntityDao;

    public InformationViewAssetHandler(DataPlatformEvent event, OMEntityDao omEntityDao) {
        this.event = event;
        this.omEntityDao = omEntityDao;
    }


    public InformationViewAsset call() throws TypeErrorException, InvalidParameterException,
            StatusNotSupportedException, PropertyErrorException,
            EntityNotKnownException, FunctionNotSupportedException,
            PagingErrorException, ClassificationErrorException,
            UserNotAuthorizedException, RepositoryErrorException,
            TypeDefNotKnownException {

        String qualifiedNameForRelationalDbSchemaType =
                QualifiedNameUtils.buildQualifiedNameForRelationalDbSchemaType(event.getTableSource().getNetworkAddress().split(":")[0], event.getTableSource().getDatabaseName(), event.getTableSource().getSchemaName());
        EntityDetail relationalDbSchemaType = omEntityDao.getEntity(Constants.RELATIONAL_DB_SCHEMA_TYPE,
                qualifiedNameForRelationalDbSchemaType);

        if (relationalDbSchemaType == null) {
            return createInformationView();
        }
        InformationViewAsset informationViewAsset = new InformationViewAsset();
        informationViewAsset.setRelationalDbSchemaType(relationalDbSchemaType);
        return informationViewAsset;

    }

    private InformationViewAsset createInformationView() throws TypeErrorException,
            InvalidParameterException,
            StatusNotSupportedException,
            PropertyErrorException,
            EntityNotKnownException,
            FunctionNotSupportedException,
            PagingErrorException,
            ClassificationErrorException,
            UserNotAuthorizedException,
            RepositoryErrorException,
            TypeDefNotKnownException {
        InformationViewAsset informationViewAsset = new InformationViewAsset();
        String qualifiedNameForSoftwareServer = QualifiedNameUtils.buildQualifiedName("", Constants.SOFTWARE_SERVER, event.getTableSource().getNetworkAddress().split(":")[0]);
        InstanceProperties softwareServerProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForSoftwareServer)
                .withStringProperty(Constants.NAME, qualifiedNameForSoftwareServer)
                .build();
        List<Classification> classificationList = new ArrayList();
        classificationList.add(omEntityDao.buildClassification(Constants.DATABASE_SERVER, Constants.SOFTWARE_SERVER, new InstanceProperties()));
        EntityDetail softwareServerEntity = omEntityDao.addEntity(Constants.SOFTWARE_SERVER,
                qualifiedNameForSoftwareServer, softwareServerProperties, classificationList);
        informationViewAsset.setSoftwareServerEntity(softwareServerEntity);

        String qualifiedNameForEndpoint = QualifiedNameUtils.buildQualifiedName("", Constants.ENDPOINT, event.getTableSource().getProtocol() + event.getTableSource().getNetworkAddress()) ;
        InstanceProperties endpointProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForEndpoint)
                .withStringProperty(Constants.NAME, qualifiedNameForEndpoint)
                .withStringProperty(Constants.NETWORK_ADDRESS, event.getTableSource().getNetworkAddress() )
                .withStringProperty(Constants.PROTOCOL, event.getTableSource().getProtocol())
                .build();
        EntityDetail endpointEntity = omEntityDao.addEntity(Constants.ENDPOINT,
                qualifiedNameForEndpoint,
                endpointProperties);
        informationViewAsset.setEndpointProperties(endpointEntity);

        omEntityDao.addRelationship(Constants.SERVER_ENDPOINT,
                softwareServerEntity.getGUID(),
                endpointEntity.getGUID(),
                Constants.DATA_PLATFORM_OMAS_NAME,
                new InstanceProperties());

        String qualifiedNameForConnection = QualifiedNameUtils.buildQualifiedName(qualifiedNameForEndpoint, Constants.CONNECTION, event.getTableSource().getUser());
        InstanceProperties connectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForConnection)
                .withStringProperty(Constants.DESCRIPTION, "Connection to " + qualifiedNameForConnection)
                .build();
        EntityDetail connectionEntity = omEntityDao.addEntity(Constants.CONNECTION,
                qualifiedNameForConnection, connectionProperties);
        informationViewAsset.setConnectionEntity(connectionEntity);

        omEntityDao.addRelationship(Constants.CONNECTION_TO_ENDPOINT,
                endpointEntity.getGUID(),
                connectionEntity.getGUID(),
                Constants.DATA_PLATFORM_OMAS_NAME,
                new InstanceProperties());


        String qualifiedNameForConnectorType = QualifiedNameUtils.buildQualifiedName("", Constants.CONNECTION_CONNECTOR_TYPE, event.getTableSource().getConnectorProviderName());
        InstanceProperties connectorTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForConnectorType)
                .withStringProperty(Constants.CONNECTOR_PROVIDER_CLASSNAME, event.getTableSource().getConnectorProviderName())
                .build();
        EntityDetail connectorTypeEntity = omEntityDao.addEntity(Constants.CONNECTOR_TYPE,
                qualifiedNameForConnectorType, connectorTypeProperties);
        informationViewAsset.setConnectorTypeEntity(connectorTypeEntity);

        omEntityDao.addRelationship(Constants.CONNECTION_CONNECTOR_TYPE,
                connectionEntity.getGUID(),
                connectorTypeEntity.getGUID(),
                Constants.DATA_PLATFORM_OMAS_NAME,
                new InstanceProperties());

        String qualifiedNameForDataStore =  QualifiedNameUtils.buildQualifiedName(qualifiedNameForSoftwareServer, Constants.DATA_STORE, event.getTableSource().getDatabaseName());
        InstanceProperties dataStoreProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDataStore)
                .withStringProperty(Constants.NAME, event.getTableSource().getDatabaseName())
                .build();
        EntityDetail dataStore = omEntityDao.addEntity(Constants.DATA_STORE, qualifiedNameForDataStore, dataStoreProperties);
        informationViewAsset.setDataStore(dataStore);

        omEntityDao.addRelationship(Constants.CONNECTION_TO_ASSET,
                connectionEntity.getGUID(),
                dataStore.getGUID(),
                Constants.DATA_PLATFORM_OMAS_NAME,
                new InstanceProperties());

        String qualifiedNameForInformationView = QualifiedNameUtils.buildQualifiedName(qualifiedNameForDataStore, Constants.DATA_PLATFORM, event.getTableSource().getSchemaName());
        InstanceProperties ivProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForInformationView)
                .withStringProperty(Constants.NAME, event.getTableSource().getSchemaName())
                .withStringProperty(Constants.OWNER, "")
                .withStringProperty(Constants.DESCRIPTION, "This asset is an " + "information " + "view")
                .build();
        EntityDetail informationViewEntity = omEntityDao.addEntity(Constants.DATA_PLATFORM,
                qualifiedNameForInformationView, ivProperties);
        informationViewAsset.setInformationViewEntity(informationViewEntity);

        omEntityDao.addRelationship(Constants.DATA_CONTENT_FOR_DATASET,
                dataStore.getGUID(),
                informationViewEntity.getGUID(),
                Constants.DATA_PLATFORM_OMAS_NAME,
                new InstanceProperties());

        String qualifiedNameForDbSchemaType = QualifiedNameUtils.buildQualifiedName(qualifiedNameForDataStore, Constants.RELATIONAL_DB_SCHEMA_TYPE, event.getTableSource().getSchemaName() + Constants.TYPE_SUFFIX);
        InstanceProperties dbSchemaTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDbSchemaType)
                .withStringProperty(Constants.DISPLAY_NAME, event.getTableSource().getSchemaName() + Constants.TYPE_SUFFIX)
                .withStringProperty(Constants.AUTHOR, "")
                .withStringProperty(Constants.USAGE, "")
                .withStringProperty(Constants.ENCODING_STANDARD, "").build();
        EntityDetail relationalDbSchemaType = omEntityDao.addEntity(Constants.RELATIONAL_DB_SCHEMA_TYPE,
                qualifiedNameForDbSchemaType,
                dbSchemaTypeProperties);
        informationViewAsset.setRelationalDbSchemaType(relationalDbSchemaType);


        omEntityDao.addRelationship(Constants.ASSET_SCHEMA_TYPE,
                informationViewEntity.getGUID(),
                relationalDbSchemaType.getGUID(),
                Constants.DATA_PLATFORM_OMAS_NAME,
                new InstanceProperties());
        return informationViewAsset;
    }*/
}
