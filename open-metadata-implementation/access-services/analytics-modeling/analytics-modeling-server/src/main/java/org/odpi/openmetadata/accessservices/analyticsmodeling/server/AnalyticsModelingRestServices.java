/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.server;

import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabase;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabaseSchema;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerModule;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerSchemaTables;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.AnalyticsModelingOMASAPIResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.DatabasesResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.ErrorResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.ModuleResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.SchemaTablesResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.SchemasResponse;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.slf4j.LoggerFactory;

/**
 * Server-side implementation of the Analytics Modeling OMAS interface for modeling.
 */
public class AnalyticsModelingRestServices {

	private AnalyticsModelingInstanceHandler instanceHandler = new AnalyticsModelingInstanceHandler();

	private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(AnalyticsModelingRestServices.class),
			AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceFullName());
	private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
	
	public RESTExceptionHandler getExceptionHandler() {
		return restExceptionHandler;
	}

	public AnalyticsModelingInstanceHandler getHandler() {
		return instanceHandler;
	}

	/**
	 * Get databases available on the server for the user.
	 * 
	 * @param serverName	of the server.
	 * @param userId		of the user.
     * @param startFrom		starting element (used in paging through large result sets)
     * @param pageSize		maximum number of results to return
	 * @return list of databases for the requested server/user.
	 */
	public AnalyticsModelingOMASAPIResponse getDatabases(String serverName, String userId, Integer startFrom, Integer pageSize) {

		String methodName = "getDatabases";
		AnalyticsModelingOMASAPIResponse ret;
		RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

		try {
			DatabasesResponse response = new DatabasesResponse();
			List<ResponseContainerDatabase> databases = getHandler()
					.getDatabaseContextHandler(serverName, userId, methodName).getDatabases(startFrom, pageSize);
			response.setDatabasesList(databases);
			ret = response;
		} catch (AnalyticsModelingCheckedException e) {
			ret = handleErrorResponse(e, methodName);
		}

		restCallLogger.logRESTCallReturn(token, ret.toString());
		return ret;
	}

	/**
	 * Get schema defined by data source GUID.
	 * 
	 * @param serverName     of the request.
	 * @param userId         of the request.
	 * @param dataSourceGuid of the requested database.
     * @param startFrom		 starting element (used in paging through large result sets)
     * @param pageSize		 maximum number of results to return
	 * @return list of schemas for the requested database.
	 */
	public AnalyticsModelingOMASAPIResponse getSchemas(String serverName, String userId, String dataSourceGuid,
			Integer startFrom, Integer pageSize) {

		String methodName = "getSchemas";
		AnalyticsModelingOMASAPIResponse ret;
		RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

		try {
			SchemasResponse response = new SchemasResponse();
			List<ResponseContainerDatabaseSchema> databasesSchemas = getHandler()
					.getDatabaseContextHandler(serverName, userId, methodName)
					.getDatabaseSchemas(dataSourceGuid, startFrom, pageSize);
			
			response.setSchemaList(databasesSchemas);
			ret = response;
		} catch (AnalyticsModelingCheckedException e) {
			ret = handleErrorResponse(e, methodName);
		} catch (InvalidParameterException e) {
			ret = handleInvalidParameterResponse(e, methodName);
		}

		restCallLogger.logRESTCallReturn(token, ret.toString());
		return ret;
	}

	/**
	 * Get tables for the schema.
	 * 
	 * @param serverName   of the request.
	 * @param userId       of the request.
	 * @param databaseGuid of the requested database.
	 * @param schema       schema name on the database.
	 * @return list of tables for the requested schema.
	 */
	public AnalyticsModelingOMASAPIResponse getTables(String serverName, String userId, String databaseGuid, String schema) {

		String methodName = "getTables";
		AnalyticsModelingOMASAPIResponse ret;
		RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

		try {
			SchemaTablesResponse response = new SchemaTablesResponse();
			ResponseContainerSchemaTables tables = getHandler()
					.getDatabaseContextHandler(serverName, userId, methodName).getSchemaTables(databaseGuid, schema);
			response.setTableList(tables);
			ret = response;
		} catch (AnalyticsModelingCheckedException e) {
			ret = handleErrorResponse(e, methodName);
		} catch (InvalidParameterException e) {
			ret = handleInvalidParameterResponse(e, methodName);
		}
		restCallLogger.logRESTCallReturn(token, ret.toString());
		return ret;
	}

	/**
	 * Build module for the schema.
	 * 
	 * @param serverName   of the request.
	 * @param userId       of the request.
	 * @param databaseGuid of the requested database.
	 * @param catalog      catalog name of the database.
	 * @param schema       schema name of the database.
	 * @return module for the requested schema.
	 */
	public AnalyticsModelingOMASAPIResponse getModule(String serverName, String userId, String databaseGuid, String catalog,
			String schema) {

		String methodName = "getModule";
		AnalyticsModelingOMASAPIResponse ret;
		RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

		try {

			ModuleResponse response = new ModuleResponse();
			ResponseContainerModule module = getHandler().getDatabaseContextHandler(serverName, userId, "getModule")
					.getModule(databaseGuid, catalog, schema);
			response.setModule(module);
			ret = response;
		} catch (AnalyticsModelingCheckedException e) {
			ret = handleErrorResponse(e, "getModule");
		} catch (InvalidParameterException e) {
			ret = handleInvalidParameterResponse(e, methodName);
		}
		
		restCallLogger.logRESTCallReturn(token, ret.toString());
		return ret;
	}

	/**
	 * Handle error in processing: build error response, and log with standard handler.
	 * @param error being thrown
	 * @param methodName context
	 * @return response with error definition.
	 */
	private AnalyticsModelingOMASAPIResponse handleErrorResponse(AnalyticsModelingCheckedException error, String methodName) {
		AnalyticsModelingOMASAPIResponse ret = new ErrorResponse(error);
		getExceptionHandler().captureThrowable(ret, error, methodName);
		return ret;
	}
	
	private AnalyticsModelingOMASAPIResponse handleInvalidParameterResponse(InvalidParameterException e, String methodName)	{
		AnalyticsModelingCheckedException error = new AnalyticsModelingCheckedException(
				AnalyticsModelingErrorCode.INVALID_REQUEST_PARAMER.getMessageDefinition(e.getParameterName()),
				this.getClass().getSimpleName(),
				methodName,
				e);
		AnalyticsModelingOMASAPIResponse ret = new ErrorResponse(error);
		getExceptionHandler().captureThrowable(ret, error, methodName);
		return ret;
	}
}
