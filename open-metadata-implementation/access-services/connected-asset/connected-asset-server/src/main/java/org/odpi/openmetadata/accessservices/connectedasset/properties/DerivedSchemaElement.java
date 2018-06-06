/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Derived schema elements are used in views to define elements that are calculated using data from other sources.
 * It contains a list of queries and a formula to combine the resulting values.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DerivedSchemaElement extends PrimitiveSchemaElement
{
    private String                          formula = null;
    private List<SchemaImplementationQuery> queries = null;

    /**
     * Default constructor
     */
    public DerivedSchemaElement()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param templateSchemaElement - template object to copy.
     */
    public DerivedSchemaElement(DerivedSchemaElement templateSchemaElement)
    {
        super(templateSchemaElement);

        if (templateSchemaElement != null)
        {
            List<SchemaImplementationQuery>   templateQueries = templateSchemaElement.getQueries();

            formula = templateSchemaElement.getFormula();
            queries = new ArrayList<>(templateQueries);
        }
    }


    /**
     * Return the formula used to combine the values of the queries.  Each query is numbers 0, 1, ... and the
     * formula has placeholders in it to show how the query results are combined.
     *
     * @return String formula
     */
    public String getFormula() { return formula; }


    /**
     * Set up the formula used to combine the values of the queries.  Each query is numbers 0, 1, ... and the
     * formula has placeholders in it to show how the query results are combined.
     *
     * @param formula String formula
     */
    public void setFormula(String formula) { this.formula = formula; }


    /**
     * Return the list of queries that are used to create the derived schema element.
     *
     * @return SchemaImplementationQueries - list of queries
     */
    public List<SchemaImplementationQuery> getQueries()
    {
        if (queries == null)
        {
            return queries;
        }
        else
        {
            return new ArrayList<>(queries);
        }
    }


    /**
     * Set up the list of queries that are used to create the derived schema element.
     *
     * @param queries - SchemaImplementationQueries - list of queries
     */
    public void setQueries(List<SchemaImplementationQuery> queries)
    {
        this.queries = queries;
    }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return PrimitiveSchemaElement object
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new DerivedSchemaElement(this);
    }
}