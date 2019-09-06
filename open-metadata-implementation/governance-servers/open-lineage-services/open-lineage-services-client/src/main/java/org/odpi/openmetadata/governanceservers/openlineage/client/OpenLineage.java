/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.client;

import org.odpi.openmetadata.governanceservers.openlineage.model.GraphName;
import org.odpi.openmetadata.governanceservers.openlineage.model.Query;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.responses.VoidResponse;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidParameterException;

public class OpenLineage  {

    private String serverName;
    private String omasServerURL;
    private RestTemplate restTemplate;

    /**
     * Create a new OpenLineage client.
     *
     * @param serverName   name of the server to connect to.
     * @param newServerURL the network address of the server running the OMAS REST servers.
     */
    public OpenLineage(String serverName,
                       String newServerURL) {
        this.serverName = serverName;
        this.omasServerURL = newServerURL;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Returns the graph that the user will initially see when querying lineage. In the future, this method will be
     * extended to condense large paths to prevent cluttering of the users screen. The user will be able to extended
     * the condensed path by querying a different method.
     *
     * @param userId calling user.
     * @param scope TABLEVIEW, COLUMNVIEW.
     * @param lineageQuery ULTIMATESOURCE, ULTIMATEDESTINATION, GLOSSARY.
     * @param graphName MAIN, BUFFER, MOCK, HISTORY.
     * @param guid The guid of the node of which the lineage is queried of.
     * @return A subgraph containing all relevant paths, in graphSON format.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public String queryLineage(String userId, Scope scope, Query lineageQuery, GraphName graphName, String guid) throws InvalidParameterException {
        String methodName = "queryLineage";
        String url = "/open-metadata/open-lineage/users/{0}/servers/{1}/query-lineage/{2}/{3}/{4}/{5}";
        return getRestCall(url, String.class, userId, serverName, scope, lineageQuery, graphName, guid);

    }

    /**
     * Write an entire graph to disc in the Egeria root folder, in the .GraphMl format.
     *
     * @param userId calling user.
     * @param graphName MAIN, BUFFER, MOCK, HISTORY.
     * @return Voidresponse.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public VoidResponse dumpGraph(String userId, GraphName graphName) throws InvalidParameterException {
        String methodName = "dumpGraph";
        String url = "/open-metadata/open-lineage/users/{0}/servers/{1}/dump/{2}";
        return getRestCall(url, VoidResponse.class, userId, serverName, graphName);

    }

    /**
     * Return an entire graph, in GraphSon format.
     *
     * @param userId calling user.
     * @param graphName MAIN, BUFFER, MOCK, HISTORY.
     * @return The queried graph, in graphSON format.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public String exportGraph(String userId, GraphName graphName) throws InvalidParameterException {
        String methodName = "exportGraph";
        String url = "/open-metadata/open-lineage/users/{0}/servers/{1}/export/{2}";
        return getRestCall(url, String.class, userId, serverName, graphName);

    }

    /**
     * Generate the MOCK graph, which can be used for performance testing, or demoing lineage with large amounts of
     * data.
     *
     * @param userId calling user.
     * @return Voidresponse.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public String generateMockGraph(String userId) throws InvalidParameterException {
        String methodName = "generateMockGraph";
        String url = "/open-metadata/open-lineage/users/{0}/servers/{1}/generate-mock-graph";
        return getRestCall(url, String.class, userId, serverName);
    }

    private <T> T getRestCall(String url, Class<T> clazz, Object... params){
        return restTemplate.getForObject(omasServerURL + url, clazz, params);
    }

}