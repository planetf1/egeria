/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */


import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import '../shared-styles.js';
import '../token-ajax.js';
import './traversal-filters.js';
import './traversal-history.js';
import './entity-search-results.js';
import './relationship-search-results.js';

/**
*
* InstanceRetriever is the implementation of a web component for retrieving metadata from a repository.
*
* It accepts a GUID and category will fetch the entity or relationship from the server specified by the
* connection-manager component.
*
* Or the user can specify search text, set a search category and optional search filters and search instead.
* This uses the find... methods on the repository.
*
* The Search type filters are only populated when the type information has been loaded from the server - until
* then the type filter selectors are empty.
*
*/

class InstanceRetriever extends PolymerElement {

    static get template() {
        return html`

            <style include="rex-styles">

                /* The existing definition for paper-input is OK but for GUIDs (which people are   */
                /* unlikely to read back, we can save a lot of space by using a 10-pt font.        */

                paper-input {
                    --paper-input-container-label: {font-size: 10px;};
                    --paper-input-container-input: {font-size: 10px;};
                    --paper-input-container:       {font-size: 10px;};
                }


            </style>

            <body>


                <token-ajax id="getEntityDetailAjaxId" last-response="{{lastGetEntityDetailResp}}" ></token-ajax>
                <token-ajax id="getRelationshipAjaxId" last-response="{{lastGetRelationshipResp}}" ></token-ajax>
                <token-ajax id="searchEntitiesAjaxId"  last-response="{{lastEntitySearchResp}}"    ></token-ajax>
                <token-ajax id="searchRelationshipsAjaxId"  last-response="{{lastRelationshipSearchResp}}"    ></token-ajax>



                <div style="width:300px; height:180px; position:absolute; top:0px; left:20px; padding:0px 20px;" >

                    Get Metadata by GUID:

                    <div style="width:180px; position:absolute; top:20px; left:0px;" >

                        <paper-input no-label-float
                                style="width:180px"
                                id = 'entityGUIDInput'
                                class='user-input'
                                label = "Instance GUID"
                                value={{instanceGUID}}
                                on-change="instanceGUIDChanged">
                        </paper-input>

                        <div style="width:180px; text-align:left; padding:0 10px;">
                                <paper-radio-group  id="instance-guid-category-group"  selected="{{selectedCategory}}">
                                    <paper-radio-button name="Entity" selected style="padding:3px;">Entity</paper-radio-button>
                                    <paper-radio-button name="Relationship" style="padding:3px;">Relationship</paper-radio-button>
                                </paper-radio-group>
                        </div>

                    </div>    <!-- end of GUID LHS -->

                    <div style="width:100px; height:50px; position:absolute; top:0px; left:200px; text-align:left;" >

                        <paper-button
                                class="inline-element"
                                style="padding:10px; text-align:center; text-transform:none;"
                                id = "getButton"
                                raised
                                on-click="doGet" >
                                Get
                        </paper-button>

                    </div>     <!-- end of GUID RHS -->

                </div> <!-- end of GUID -->


                <!-- ============================================================ -->

                <div style="width:300px; height:180px; position:absolute; top:0px; left:300px; padding:0px 20px;" >

                    Search:

                    <div style="width=100%; height:80px; position:absolute; top:0px; left:0px;">

                        <div style="width:180px; height:50px; position:absolute; top:20px; left:0px;" >

                            <paper-input no-label-float
                                style="width:180px"
                                id = 'searchTextInput'
                                class='user-input'
                                label = "Search Text"
                                value={{searchText}}
                                on-change="searchTextChanged">
                            </paper-input>

                            <div style="width:180px; text-align:left; padding:0 10px;">
                                <paper-radio-group  id="search-category-group"  selected="{{searchCategory}}">
                                    <paper-radio-button name="Entity" selected style="padding:3px;">Entity</paper-radio-button>
                                    <paper-radio-button name="Relationship" style="padding:3px;">Relationship</paper-radio-button>
                                </paper-radio-group>
                            </div>

                        </div>    <!-- end of Search LHS -->

                        <div style="width:100px; height:50px; position:absolute; top:0px; left:200px; text-align:left;" >

                            <paper-button
                                class="inline-element"
                                style="padding:10px; text-align:center; text-transform:none;"
                                id = "fetchButton"
                                raised
                                on-click="doSearch" >
                                Search
                            </paper-button>

                        </div>     <!-- end of Search RHS -->

                    </div>

                </div>  <!-- end of Search -->

                <div style="width:300px; height:180px; position:absolute; top:0px; left:600px; padding:0px 20px;" >
                    Search Filters:

                    <filter-manager id="filterManager" type-manager="[[typeManager]]" style="padding:0px 0px 20px;"></filter-manager>

                </div> <!-- END OF FILTERS -->

                <!-- ============================================================ -->

                <!--Search results dialog - initially hidden and made visible when search results are received. -->
                <div>
                    <paper-dialog id="searchResultsDialog" style=" height:450px; width:800px; ">

                        <!--This section is used to fetch the input from the input-field and display on the dialog using one-way data binding-->

                        <p>
                        Results of {{searchCategory}} Search on server {{serverName}} using expression \"{{searchText}}\"
                        <p>
                        Please select instances to add to the graph.
                        </p>

                        <!--   Display the following per instance in the search results...      -->
                        <!--   1. The type name and label                                       -->
                        <!--   2. A checkbox to include/exclude from the traversal              -->
                        <!--                                                                    -->

                        <div id="containerForSearchResults">
                        </div>

                        <div class="buttons">
                            <paper-button dialog-dismiss>Cancel</paper-button>
                            <paper-button dialog-confirm autofocus on-tap="_searchResultsSubmitHandler">OK</paper-button>
                        </div>

                    </paper-dialog>

                </div>  <!-- END OF SEARCH RESULTS DIALOG -->

            </body>
        `; }



    static get properties() {

        return {

            serverName : {
                type : String,
                value : ""
            },

            searchText : {
                type : String,
                value : ""
            },

            searchCategory : {
                type : String,
                value : ""
            },


            // User-specified entity GUID
            // This is used both as the property for the entry input field for when a user
            // specifies a GUID (to retrieve using the Get button) AND is also used to
            // store the GUID of the instance that has been selected as the focus - e.g.
            // because it has clicked on it in the diagram.

            instanceGUID: {
                type               : String,
                value              : "",
                notify             : true,
                reflectToAttribute : true
            },


            // TODO - rename - this should only relate to GIUD retrieval - but is I think being used for focus cat as well. separate.
            // selectedCategory tells us whether instanceGUID refers to an entity or relationship.
            // It is set by the radio buttons next to the GUID input field.
            selectedCategory: {
                type               : String,
                value              : "Entity",
                observer           : 'selectedCategoryChanged'    // Observer called when this property changes
            },


            // searchCategory indicates whether instance search is for an entity or relationship.
            // It is set by the radio buttons next to the search input field and may be over-ridden by selection of type filters.
            searchCategory: {
                type               : String,
                value              : "Entity",
                observer           : 'searchCategoryChanged'    // Observer called when this property changes
            },

            currentGen: {
                type              : Number,
                value             : 0
            },

            // Records the number of generations - conveyed in return from getLatestGen() for scaling by diagrams
            numberOfGens: {
                type              : Number,
                value             : 0
            },

            // User-specified search string - using bi-directional databind
            // This is any string that the user wishes to use as the searchCriteria parameter in a find
            // request. It will be searched in any string property owned by the instance.

            searchText: {
                type               : String,
                value              : "",
                notify             : true,
                reflectToAttribute : true
            },


            // Depth control to be used for the next query
            intDepth: {
                type   : Number,
                value  : 1            // default to getting immediate neighbours
            },

            lastGetEntityDetailResp: {
               type: Object,
               observer: '_getEntityDetailRespChanged'    // Observer called when this property changes
            },

            lastGetRelationshipResp: {
                type: Object,
                observer: '_getRelationshipRespChanged'    // Observer called when this property changes
             },

            lastEntitySearchResp : {
                type: Object,
                observer: '_entitySearchRespChanged'    // Observer called when this property changes
            },

            lastRelationshipSearchResp : {
                type: Object,
                observer: '_relationshipSearchRespChanged'    // Observer called when this property changes
            },

            lastLoadGraphExplorerResp: {
                type: Object,
                observer: '_loadGraphExplorerRespChanged'    // Observer called  when this property changes
            },


            // gens - this holds the history of generations
            // Each entry is a RexTraversal with the parameters and resulting entities and relationships
            gens: {
                type  : Array,
                value : []
            },

            /*
             * Map of guids to the gen in which that guid was discovered.
             */
            guidToGen: {
                type  : Map,
                value : {}
            },

            connectionManager: Object,

            typeManager: {
                 type : Object,
                 notify : true,
                 value : undefined
            }

        };
    }

    /*
     * Element is ready
     */
    ready() {
        // Ensure you call super.ready() first to initialise node hash...
        super.ready();

        this.instanceGUIDCategory = "Entity";

        // TODO - consider renaming this to focusInstance - but only after all previous focus refs have been removed...
        // rootInstance stores expEntity or expRelationship - TODO see comments above for (disused) property
        this.rootInstance = {};


        this.preTraversal                     = {};
        this.preTraversal.entityTypes         = {};
        this.preTraversal.relationshipTypes   = {};
        this.preTraversal.classificationTypes = {};

        this.gens = [];
        this.guidToGen = {};

        //console.log("rex instance-retriever ready complete");
    }


    // UI handlers


    /*
     * There is no need to retrieve the entity whose GUID is given until the user presses the Get! button
     * But now is a great time to enable the Get! button
     */
    instanceGUIDChanged() {
        //if (this.instanceGUID != undefined && this.instanceGUID != "") {
        //   this.enableGetButton();
        //}
        //else {
        //    this.disableGetButton();
        //}
    }


    /*
     * There is no need to search for the instance yet - until the user presses the Search button
     */
    searchTextChanged() {

    }

    selectedCategoryChanged(newValue,oldValue) {
        //console.log("selectedCategoryChanged invoked : selectedCategory="+ this.selectedCategory);
        if (newValue !== undefined && newValue !== null) {
            //console.log("selectedCategoryChanged newValue="+ newValue);
        }
    }


   searchCategoryChanged(newValue,oldValue) {
        //console.log("searchCategoryChanged invoked : searchCategory="+ this.searchCategory);
        if (newValue !== undefined && newValue !== null) {
            //console.log("searchCategoryChanged newValue="+ newValue);
        }
    }


    // Inter-component event handlers


    /*
     *  Inbound event: types-loaded
     *
     *  This event is delegated to the embedded filter-manager so the type filters can be populated.
     */
    inEvtTypesLoaded() {
        this.$.filterManager.inEvtTypesLoaded();
    }

    /*
     * Some other UI cpt has requested a changed of focus - could have been in a diagram or details panel.
     * Update the displayed instanceGUID. Retrieve the instance from the repository server.
     */
    inEvtChangeFocusEntity(entityGUID) {
        /*
         * If the currently selected entity has been clicked - then deselect it.
         */
        if (entityGUID === this.instanceGUID) {
            this.instanceGUID = undefined;
            this.selectedCategory = undefined;
            this.rootInstance = undefined;
            // Generate focus changed event
            this.outEvtFocusEntityCleared();
        }
        else {
            this.instanceGUID = entityGUID;
            this.selectedCategory = "Entity";
            this.doGet();
        }
    }

    /*
      * Some other UI cpt has requested a changed of focus - could have been in a diagram or details panel.
      * Update the displayed instanceGUID. Retrieve the instance from the repository server.
      */
    inEvtChangeFocusRelationship(relationshipGUID) {
        /*
         * If the currently selected entity has been clicked - then deselect it.
         */
        if (relationshipGUID === this.instanceGUID) {
            this.instanceGUID = undefined;
            this.selectedCategory = undefined;
            this.rootInstance = undefined;
            // Generate focus changed event
            this.outEvtFocusRelationshipCleared();
        }
        else {
            this.instanceGUID = relationshipGUID;
            this.selectedCategory = "Relationship";
            this.doGet();
        }
    }

    /*
     * This may seem odd since the i-r is the source of this event - but it is appropriate that the last thing
     * that happens as a result of an entity focus change is to ensure the explore button is enabled.
     */
    inEvtFocusEntityChanged(entityGUID) {
        //this.enableExploreButton();
    }

    /*
     * This may seem odd since the i-r is the source of this event - but it is appropriate that if anything
     * further actions are needed on completion of event processing by the other components that the i-r gets
     * the last word. For an entity focus change it will enable the explore button. For relationship focus
     * change there is currently nothing that needs doing.
     */
    inEvtFocusRelationshipChanged(relationshipGUID) {
        // NO OP
    }

    inEvtFocusEntityCleared() {

    }

    inEvtFocusRelationshipCleared() {

    }


    /*
     * This function will clear the whole graph, and the selected instance.
     */
    inEvtGraphCleared() {


        // Empty the root/focus fields
        this.instanceGUID = undefined;
        // The selectedCategory property drives the radio buttons for GUID retrieval category
        // Reset it to its default setting of "Entity" rather than clearing it completely.
        this.selectedCategory = "Entity";
        this.rootInstance = undefined;
        // Empty the graph
        this.gens = [];
        this.guidToGen = {};
        this.numberOfGens = 0;
        this.currentGen = 0;
    }


    /*
     * This function will undo the last operation on the graph, by removing the most recent gen
     */
    inEvtUndo() {
        /*
         *  This function needs to parse the current (latest) gen and remove all the guidToGen entries relating
         *  to GUIDs in that gen. It then needs to pop the gen from the list of gens, and reduce numberOfGens
         *  When all done, fire a graph-reduced event.
         */
        var latestIdx = this.currentGen - 1;
        var genToUndo = this.gens[latestIdx];

        var entsToUndo = genToUndo.entities;
        var relsToUndo =  genToUndo.relationships;
        if (entsToUndo !== undefined && entsToUndo.length >0) {
            entsToUndo.forEach(function(ent) {
                this.guidToGen.delete(ent);
            });
        }
        if (relsToUndo !== undefined && relsToUndo.length >0) {
            relsToUndo.forEach(function(rel) {
                this.guidToGen.delete(rel);
            });
        }

        this.gens.pop();
        this.currentGen = this.currentGen - 1;
        this.numberOfGens = this.numberOfGens -1;

        /*
          *  Leave nothing selected...
          *  We could refine this to test whether the selected instance is still valid (still in the graph)
          *  and if so leave it selected.
          */
         if (guidToGen[this.instanceGUID] === undefined) {
             this.instanceGUID = undefined;
             this.selectedCategory = undefined;
             this.rootInstance = undefined;
             // Generate focus changed event
             this.outEvtFocusEntityCleared();
        }
        /*
         * Finally fire the graph-reduced event
         */
        this.outEvtGraphReduced();
    }



    /*
     * This function will undo the last operation on the graph, by removing the most recent gen
     * This is a 2-phase operation - it highlights the gen to be removed, fires an event that can be
     * caught and processed by other components and when it sees the event from the diagram the 2nd phase
     * completes the removal.
     */
    inEvtUndoPhaseOne() {
        this.outEvtGraphBeingReduced();
    }

    inEvtUndoPhaseTwo() {
        /*
         *  This function needs to parse the current (latest) gen and remove all the guidToGen entries relating
         *  to GUIDs in that gen. It then needs to pop the gen from the list of gens, and reduce numberOfGens
         *  When all done, fire a graph-reduced event.
         */
        var latestIdx = this.currentGen - 1;
        var genToUndo = this.gens[latestIdx];

        var entsToUndo = genToUndo.entities;
        var relsToUndo =  genToUndo.relationships;
        if (entsToUndo !== undefined && entsToUndo.length >0) {
            entsToUndo.forEach(function(ent) {
                //console.log("i-r: removing entity "+ent);
                this.guidToGen.delete(ent);
            });
        }
        if (relsToUndo !== undefined && relsToUndo.length >0) {
            relsToUndo.forEach(function(rel) {
                //console.log("i-r: removing relationship "+rel);
                this.guidToGen.delete(rel);
            });
        }

        this.gens.pop();
        this.currentGen = this.currentGen - 1;
        this.numberOfGens = this.numberOfGens -1;

        /*
          *  Leave nothing selected...
          *  We could refine this to test whether the selected instance is still valid (still in the graph)
          *  and if so leave it selected.
          */
         if (this.guidToGen[this.instanceGUID] === undefined) {
             this.instanceGUID = undefined;
             this.selectedCategory = undefined;
             this.rootInstance = undefined;
             // Generate focus changed event
             this.outEvtFocusEntityCleared();
        }
        /*
         * Finally fire the graph-reduced event
         */
        //this.outEvtGraphReduced();
    }



   /*
    * This function is called on submit of the search results dialog. It needs to access the selected instances
    * and formulate a traversal results based on them. The selectedInstances is a list of entity digests.
    */
    _searchResultsSubmitHandler() {

        // Get the user's selected instances from the search-results element..
        var containerForSearchResults = this.$.containerForSearchResults;
        var searchResults = containerForSearchResults.firstChild;
        //console.log("searchResults is "+searchResults);

        // Process results...
        var selectedInstances = undefined;
        var selectedInstances = searchResults.getSelectedInstances();
        if (selectedInstances !== undefined && selectedInstances !== null && selectedInstances.length > 0) {

            // If there is one entity (or relationship) it becomes the selected instance - which means
            // we have to retrieve the entity detail for the details panel.
            // If there are multiple instances in the search result then Rex does not know which to select
            // and does not select any - so rootInstance is not set.

            console.log("i-r: Now absorb the resulting instances - you need digests at this point.....");


            var numInstancesFound = selectedInstances.length;

            var searchUnique         = false;
            var searchUniqueCategory = "";
            var searchUniqueGUID     = "";
            if (numInstancesFound === 1) {
                searchUnique = true;
                if (this.searchCategory === "Entity") {
                    searchUniqueCategory = "Entity";
                    var instance         = selectedInstances[0];
                    var searchUniqueGUID = instance.entityGUID;
                }
                else {  // searchCategory is "Relationship"
                    searchUniqueCategory = "Relationship";
                    var instance         = selectedInstances[0];
                    var searchUniqueGUID = instance.relationshipGUID;
                }
            }



            // Process the list of entity digests...any that are not already known are added to a traversal that will be added as a new gen.....
            var rexTraversal             = {};
            rexTraversal.entities        = {};
            rexTraversal.relationships   = {};
            // Set the traversal operation to show how this result was generated - provides informative summary in history
            if (this.searchCategory === "Entity") {
                rexTraversal.operation = "entitySearch";
            }
            else {
                rexTraversal.operation = "relationshipSearch";
            }
            var serverName = this.serverName;
            rexTraversal.serverName  = serverName;
            var searchText = this.searchText;
            rexTraversal.searchText = searchText;


            var newInstancesDiscovered    = false;

            // Do not select any of the search results.
            // Populate the gen with the digests. No focus change request needed.

            for (var i=0; i< numInstancesFound; i++) {
                // Do not select any of the search results.
                // Check which (if any) are not already known and populate a gen with their digests.
                // No focus change request needed.

                // ... this is what you would do for a single entity detail after a get request...
                var instance = selectedInstances[i];
                if (this.searchCategory === "Entity") {
                    var entityGUID = instance.entityGUID;

                    // Determine whether entity is already known ...
                    var entityKnown = false;
                    var gen;
                    // Search the existing gens looking for guid
                    for (var g=0; g< this.gens.length; g++) {
                        var igen = this.gens[g];
                        var igenEntities = igen.entities;
                        if (igenEntities !== undefined) {
                            if (igenEntities[entityGUID] !== undefined) {
                                entityKnown = true;
                                gen = g+1;
                                break;
                            }
                        }
                    }
                    if (entityKnown === false) {
                        newInstancesDiscovered = true;
                        // If this is an entity we have not already seen - add it to the traversal which will go to the diagram manager
                        // gen is not advanced until we have processed all instances in the search result.
                        gen = this.currentGen + 1;
                        instance.gen = gen;
                        rexTraversal.entities[entityGUID] = instance;
                        this.guidToGen[entityGUID] = gen;
                    }
                }
                else {

                    var relationshipGUID = instance.relationshipGUID;

                    // Determine whether relationship is already known ...
                    var relationshipKnown = false;
                    var gen;
                    // Search the existing gens looking for guid
                    for (var g=0; g< this.gens.length; g++) {
                        var igen = this.gens[g];
                        var igenRelationships = igen.entities;
                        if (igenRelationships !== undefined) {
                            if (igenRelationships[relationshipGUID] !== undefined) {
                                relationshipKnown = true;
                                gen = g+1;
                                break;
                            }
                        }
                    }
                    if (relationshipKnown === false) {
                        newInstancesDiscovered = true;
                        // If this is an relationship we have not already seen - add it to the traversal which will go to the diagram manager
                        // gen is not advanced until we have processed all instances in the search result.
                        gen = this.currentGen + 1;
                        instance.gen = gen;
                        rexTraversal.relationships[relationshipGUID] = instance;
                        this.guidToGen[relationshipGUID] = gen;
                    }
                }
            }

            if (newInstancesDiscovered === true) {
                // Advance the currentGen and extend the graph
                this.advanceCurrentGen();

                // Add the traversal to the sequence of gens in the graph. Then generate the graph-changed event.
                this.gens.push(rexTraversal);

                //console.log("instance-retriever: generate graph-changed event");
                this.outEvtGraphExtended();

            }

            // If the search resulted in a single instance being selected, optimise
            // the flow by proactively requesting that it becomes the focus instance.
            if (searchUnique) {
                  if (searchUniqueCategory === "Entity") {
                      this.outEvtChangeFocusEntity(searchUniqueGUID);
                  }
                  else {
                      this.outEvtChangeFocusRelationship(searchUniqueGUID);
                  }
            }
        }
    }

    outEvtChangeFocusEntity(entityGUID) {
        var customEvent = new CustomEvent('change-focus-entity', { bubbles: true, composed: true,
                                         detail: {entityGUID: entityGUID, source: "instance-retriever"} });
        this.dispatchEvent(customEvent);
    }

    /*
     * This function needs to build the history summary and render the history dialog
     * Launch a dialog box with the history including list of gen, query and results
     */
    getHistoryList() {

        // Clear any existing history from the dialog - we want to build a new one from the template...
       // var containerForHistory = this.$.containerForHistory;
       // while (containerForHistory.firstChild) {
       //     containerForHistory.removeChild(containerForHistory.firstChild);
       // }

        // Now add the latest history to the container....
       // var traversalHistory = document.createElement("traversal-history");

        // Build a history as a list of gens with summaries...
        var historyList = [];

        /* Each gen consists of the following:
         *
         *   private String               entityGUID;               // must be non-null
         *   private List<String>         entityTypeGUIDs;          // a list of type guids or null
         *   private List<String>         relationshipTypeGUIDs;    // a list of type guids or null
         *   private List<String>         classificationNames;      // a list of names or null
         *   private Integer              depth;                    // the depth used to create the subgraph
         *   private Integer              gen;                      // which generation this subgraph pertains to
         *
         *   There are also fields that contain maps of instance summaries.
         *   An instance summary is much smaller than the full instance.
         *   The entities map is keyed by entityGUID and the value part consists of
         *       { entityGUID, label, gen }
         *   The relationships map is keyed by relationshipGUID and the value part consists of
         *       { relationshipGUID, end1GUID, end2GUID, idx, label, gen }
         *   The above value types are described by the RexEntityDigest and RexRelationshipDigest Java classes.
         *   private Map<String,RexEntityDigest>         entities;
         *   private Map<String,RexRelationshipDigest>   relationships;
         *
         *   The traversal is augmented in the client by the addition of an operation field. This is only meaningful in the
         *   client code.
         *   private String                operation  - has values { 'getEntity' | 'getRelationship' | 'traversal' }
         */

        for (var i=0; i<this.gens.length; i++) {
            var gen = i+1;
            var genContent = this.gens[i];

            /*
             *  Build the query description
             */

            /*
             * The querySummary always starts with the Repository Server's name
             */

            var serverName = genContent.serverName;
            var querySummary = "["+serverName+"]";

            switch (genContent.operation) {

                case "getEntity":
                    /*
                     * Format querySummary as "Entity retrieval \n GUID: <guid>"
                     */
                    querySummary = querySummary.concat(" Entity retrieval using GUID");
                    break;

                case "getRelationship":
                    /*
                     * Format querySummary as "Relationship retrieval \n GUID: <guid>"
                     */
                    querySummary = querySummary.concat(" Relationship retrieval using GUID");
                    break;

                case "traversal":
                    /*
                     * Format querySummary as "Traversal"
                     * Would like to present user with label rather than guid. genContent.root contains GUID.
                     * The reason for the lookup is that the root of the traversal will not be in the same gen
                     * as the traversal results. It is not known which gen it is (except it must have existed prior
                     * to traversal).
                     */
                    var entityGUID = genContent.entityGUID;
                    var rootGenNumber = this.guidToGen[entityGUID];
                    var rootGen = this.gens[rootGenNumber-1];
                    var rootDigest = rootGen.entities[entityGUID];
                    var rootLabel = rootDigest.label;
                    querySummary = querySummary.concat(" Traversal from entity "+rootLabel);
                    querySummary = querySummary.concat(" Depth: "+genContent.depth);

                    // Entity Type Filters - show type names rather than type GUIDs
                    querySummary = querySummary.concat(" Entity Type Filters: ");
                    var entityTypeNames = genContent.entityTypeNames;
                    if (entityTypeNames != undefined && entityTypeNames != null) {
                        var first = true;
                        entityTypeNames.forEach(function(etn){
                            if (first) {
                                first = false;
                                querySummary = querySummary.concat(etn);
                            }
                            else {
                                querySummary = querySummary.concat(", "+etn);
                            }
                        });
                    }
                    else
                        querySummary = querySummary.concat("none");

                    // Relationship Type Filters - show type names rather than type GUIDs
                    querySummary = querySummary.concat(" Relationship Type Filters: ");
                    var relationshipTypeNames = genContent.relationshipTypeNames;
                    if (relationshipTypeNames != undefined && relationshipTypeNames != null) {
                        var first = true;
                        relationshipTypeNames.forEach(function(rtn){
                        if (first) {
                            first = false;
                                querySummary = querySummary.concat(rtn);
                            }
                            else {
                                querySummary = querySummary.concat(", "+rtn);
                            }
                        });
                    }
                    else
                        querySummary = querySummary.concat("none");

                    // Classification Filters - shown as names
                    querySummary = querySummary.concat(" Classification Filters: ");
                    var ClassificationNames = genContent.ClassificationNames;
                    if (ClassificationNames != undefined && ClassificationNames != null) {
                        var first = true;
                        ClassificationNames.forEach(function(rtn){
                           if (first) {
                               first = false;
                               querySummary = querySummary.concat(rtn);
                            }
                            else {
                                querySummary = querySummary.concat(", "+rtn);
                            }
                        });
                    }
                    else
                        querySummary = querySummary.concat("none");
                    break;


                case "entitySearch":
                    /*
                     * Format querySummary as "Entity retrieval \n GUID: <guid>"
                     */
                    querySummary = querySummary.concat(" Entity Search");
                    querySummary = querySummary.concat(" expression ["+genContent.searchText+"]");
                    break;

                default:
                    // Put message to console and add error message to gen so this is noticed in history.
                    console.log("instance-retriever: found a gen result with no operation type");
                    querySummary = "Operation not recognised!";
                    break;
            }
            //console.log("traversal-history: querySummary is "+querySummary);

            /*
             *  Build the instances section
             */

             var instanceList = [];
             var entities = genContent.entities;
             for (var guid in entities) {
                 //console.log("showHistory: guid "+guid);
                 var ent = entities[guid];
                 instanceList.push( { "category" : "Entity" , "label" : ent.label , "guid" : ent.entityGUID } );
             }

             var relationships = genContent.relationships;
             for (var guid in relationships) {
                 //console.log("showHistory: guid "+guid);
                 var rel = relationships[guid];
                 instanceList.push( { "category" : "Relationship" , "label" : rel.label , "guid" : rel.relationshipGUID } );
             }
            var historyItem = {  "gen" : gen , "query" : querySummary , "instances" : instanceList};

            historyList.push(historyItem);
        }

        return historyList;


    }




    doGet() {
        if (this.instanceGUID === undefined || this.instanceGUID === null) {
            console.log("doGet: cannot proceed because instanceGUID is not set - please provide a GUID and try again");
        }
        else {
            var serverDetails = this.connectionManager.getServerDetails();
            if (this.validate(serverDetails.serverName) && this.validate(serverDetails.serverURLRoot)) {
                //console.log("doGet - server details are valid");
                var body = {};
                body.serverName       = serverDetails.serverName;
                body.serverURLRoot    = serverDetails.serverURLRoot;
                body.enterpriseOption = serverDetails.enterpriseOption;
                if (this.selectedCategory === 'Entity') {
                    body.entityGUID = this.instanceGUID;
                    this.getEntityFromRepo(body);
                }
                else {
                    body.relationshipGUID = this.instanceGUID;
                    this.getRelationshipFromRepo(body);
                }
            }
        }
    }

     getEntityFromRepo(body) {
         // Get Entity - this is a post operation to the UI server
         this.$.getEntityDetailAjaxId.method ="post";
         this.$.getEntityDetailAjaxId.body   = body;
         this.$.getEntityDetailAjaxId.url    = "/api/instances/entity";
         this.$.getEntityDetailAjaxId._go();
     }

     getRelationshipFromRepo(body) {
         // Get Relationship - this is a post operation to the UI server
         this.$.getRelationshipAjaxId.method ="post";
         this.$.getRelationshipAjaxId.body   = body;
         this.$.getRelationshipAjaxId.url    = "/api/instances/relationship";
         this.$.getRelationshipAjaxId._go();

     }


    /*
     * Search for instances that contain the search text.
     * The searchCategory (set by radio buttons) narrows the search to either entity or relationship category.
     * If a filter has been set it will have forced the category as follows:
     *    If a type filter has been set, the search will only be for the appropriate category and only for instances of the selected type.
     *    If a classification filter has been set the search is across entities.
     * If no filters have been set, the search depends on the radio button setting for category - either entities or relationships.
     */
    doSearch() {
        if (this.searchText === undefined || this.searchText === null) {
            console.log("doSearch: cannot proceed because searchText is not set - please type a string and try again");
        }
        else {
            var serverDetails = this.connectionManager.getServerDetails();
            if (this.validate(serverDetails.serverName) && this.validate(serverDetails.serverURLRoot)) {

                var body = {};
                body.serverName       = serverDetails.serverName;
                body.serverURLRoot    = serverDetails.serverURLRoot;
                body.enterpriseOption = serverDetails.enterpriseOption;

                /*
                 * Check the searchCategory, then check whether a search filter has been set and augment body accordingly.
                 */

                if (this.searchCategory === "Entity") {
                    var searchFilter = this.$.filterManager.getSelection();
                    if (searchFilter.typeName === "none") {
                          body.typeName = null;
                    }
                    else {
                          body.typeName = searchFilter.typeName;
                    }

                    var searchTextStringified = JSON.stringify(this.searchText);
                    body.searchText = this.searchText;
                    // Perform instance search - this is a post operation to the UI server
                    this.$.searchEntitiesAjaxId.method = "post";
                    this.$.searchEntitiesAjaxId.body   = body;
                    this.$.searchEntitiesAjaxId.url    = "/api/instances/entities/by-property-value";
                    this.$.searchEntitiesAjaxId._go();
                }
                else { // search for relationships...
                    console.log("i-r: Implement call to relationship search");
                    var searchFilter = this.$.filterManager.getSelection();
                    if (searchFilter.typeName === "none") {
                        body.typeName = null;
                    }
                    else {
                        body.typeName = searchFilter.typeName;
                    }
                    body.searchText = this.searchText;
                    // Perform instance search - this is a post operation to the UI server
                    this.$.searchRelationshipsAjaxId.method = "post";
                    this.$.searchRelationshipsAjaxId.body   = body;
                    this.$.searchRelationshipsAjaxId.url    = "/api/instances/relationships/by-property-value";
                    this.$.searchRelationshipsAjaxId._go();
                }

            }
        }
    }

    outEvtGraphExtended() {
        var customEvent = new CustomEvent('graph-extended',
            { bubbles: true, composed: true,
              detail: {source: "instance-retriever"} } );
        this.dispatchEvent(customEvent);
    }

    outEvtGraphReduced() {
        var customEvent = new CustomEvent('graph-reduced',
            { bubbles: true, composed: true,
              detail: {source: "instance-retriever"} } );
        this.dispatchEvent(customEvent);
    }

    outEvtGraphBeingReduced() {
        var customEvent = new CustomEvent('graph-being-reduced',
             { bubbles: true, composed: true,
               detail: {source: "instance-retriever"} } );
        this.dispatchEvent(customEvent);
    }


    outEvtFocusEntityChanged(entityGUID) {
        var customEvent = new CustomEvent('focus-entity-changed',
                                  { bubbles: true,
                                    composed: true,
                                    detail: {
                                        guid : entityGUID ,
                                        source: "instance-retriever"}
                                  });
        this.dispatchEvent(customEvent);
    }

    outEvtFocusEntityCleared() {
        var customEvent = new CustomEvent('focus-entity-cleared',
                                  { bubbles: true,
                                    composed: true,
                                    detail: {
                                        source: "instance-retriever"}
                                  });
        this.dispatchEvent(customEvent);
    }


    outEvtEntityNotLoaded() {
        var customEvent = new CustomEvent('entity-not-loaded',
                                                 { bubbles: true,
                                                   composed: true,
                                                   detail: {
                                                       source: "instance-retriever"}
                                                 } );
        this.dispatchEvent(customEvent);
    }


    outEvtFocusRelationshipChanged(relationshipGUID) {
        var customEvent = new CustomEvent('focus-relationship-changed',
                                { bubbles: true,
                                  composed: true,
                                  detail: {
                                      guid : relationshipGUID ,
                                      source: "instance-retriever"}
                                });
        this.dispatchEvent(customEvent);
    }

    outEvtFocusRelationshipCleared() {
        var customEvent = new CustomEvent('focus-relationship-cleared',
                                { bubbles: true,
                                  composed: true,
                                  detail: {
                                      source: "instance-retriever"}
                                });
        this.dispatchEvent(customEvent);
    }

    outEvtRelationshipNotLoaded() {
        var customEvent = new CustomEvent('relationship-not-loaded',
                                { bubbles: true,
                                  composed: true,
                                  detail: {
                                      source: "instance-retriever"}
                                });
        this.dispatchEvent(customEvent);
    }

     /*
      * The pre-traversal asks the view-service to get the types of relationship and neighboring entity adjacent to
      * the focus entity. It returns traversal statistics that are used in the display of the user's choices for filtering
      * of the real traversal, if the user chooses to proceed. The user can adjust the filters and either cancel or
      * submit the request.
      */
     getPreTraversalAjax(body) {
         // Pre-Traversal - this is a post operation to the UI server
         this.$.getPreTraversalAjaxId.method ="post";
         this.$.getPreTraversalAjaxId.body   = body;
         this.$.getPreTraversalAjaxId.url    = "/api/instances/rex-pre-traversal";
         this.$.getPreTraversalAjaxId._go();
     }




    // TODO - need to reconcile selectedCategory, instanceGUID and rootInstance - the first two are input fields
    // TODO - and the last should be the focus instance; which when changed should update the input fields but
    // TODO - not vice versa - input fields chaneg then we do a retrieve then the focus instance catches up.



    validate(parameter) {
        if (parameter === undefined || parameter === null || parameter === "" || parameter.length <= 0)
            return false;
        return true;
    }

    /*
     * Observer to handle receipt of packaged instance data response from UI Application
     */
    _getEntityDetailRespChanged(newValue,oldValue) {

        if (newValue !== undefined && newValue !== null) {

            if (newValue.httpStatusCode == 200) {
                // Success

                // Determine whether this is an entity we already have in the diagram (in the gens)
                // or whether it is new to this exploration. If it is new we need to assign it to the
                // current gen and tell the diagram manager - so we pick the entity digest from the
                // expanded entity and wrap it in a traversal and issue a graph-changed event.
                // If it was not new we need to update the entity digest to retain the original gen.
                // This is so the gen remains as it was so that the entity retains its location in the
                // user's exploration.
                // Whether it was already known or not we save the most recent information into the
                // root instance - the entity content may have changed in the repo.
                // Whether new or known the focus is not this entity so we issue a focus-entity-changed
                // event.

                var serverName = newValue.expandedEntityDetail.serverName;

                var entityGUID = newValue.expandedEntityDetail.entityDigest.entityGUID;

                // Determine whether entity is already known ...
                var entityKnown = false;
                var gen;
                if (this.guidToGen[entityGUID] !== undefined) {
                    entityKnown = true;
                    gen = this.guidToGen[entityGUID];
                }
                // Search the existing gens looking for guid   // TODO - clean up
                //for (var i=0; i< this.gens.length; i++) {
                //    var igen = this.gens[i];
                //    var igenEntities = igen.entities;
                //    if (igenEntities !== undefined) {
                //        if (igenEntities[entityGUID] !== undefined) {
                //            entityKnown = true;
                //            gen = i+1;
                //            break;
                //        }
                //    }
                //}
                if (entityKnown === false) {
                    // Advance the currentGen
                    this.advanceCurrentGen();
                    gen = this.currentGen;
                }

                // Store the expanded entity into rootInstance, maintain the gen as above.
                // rootInstance may have been cleared (to undefined) due to deselection of the focus instance.
                if (this.rootInstance === undefined) {
                    this.rootInstance = {};
                }
                this.rootInstance.category = "entity";
                this.rootInstance.expEntity = newValue.expandedEntityDetail;
                this.rootInstance.expEntity.entityDigest.gen = gen;
                this.rootInstance.expRelationship = undefined;

                // If this is an entity we have not already seen - tell the diagram manager
                if (entityKnown === false) {

                    /*
                     * For the diagram, construct an entity digest in a traversal add it the logically
                     * next gen and issue a graph-changed event.
                     * We can do this by fabricating a traversal for the entity (only)
                     * This should be formatted into the following form.
                     *
                     *   a map of entityGUID       --> { entityGUID, label, gen }
                     */

                    var rexTraversal                  = {};
                    rexTraversal.entities             = {};
                    rexTraversal.relationships        = {};
                    rexTraversal.entities[entityGUID] = this.rootInstance.expEntity.entityDigest;
                    rexTraversal.serverName           = serverName;

                    // Record in the traversal object, how this result was generated so we can provide informative summary in history
                    rexTraversal.operation = "getEntity";

                    // Add the traversal to the sequence of gens in the graph.
                    this.gens.push(rexTraversal);
                    this.guidToGen[entityGUID] = this.currentGen;

                    // Generate the graph-changed event.
                    this.outEvtGraphExtended();

                }

                // Regardless of whether we had seen it before or it was new - this instance is now the focus.
                this.outEvtFocusEntityChanged(entityGUID);

            }
            else {
                 // Failure
                 console.log("_getEntityDetailRespChanged newValue has bad status code");
                 if (newValue.exceptionText) {
                     alert('Error occurred: ' +newValue.exceptionText);
                 }
                 else {
                     alert('Error occurred: no exception message given');
                 }
                 // Generate a failure to load event - this will allow the status to be reported
                 console.log("getEntityResp: issue entity-not-loaded");
                 this.outEvtEntityNotLoaded();
                 console.log("getEntityResp: done entity-not-loaded");
            }
        }
        else {
            console.log("_getEntityDetailRespChanged newValue was null");
        }
    }

    /*
     * Observer to handle receipt of packaged instance data response from UI Application
     */
    _getRelationshipRespChanged(newValue,oldValue) {
        //console.log("_getRelationshipRespChanged invoked");
        if (newValue !== undefined && newValue !== null) {
            //console.log("_getRelationshipRespChanged newValue : "+newValue);
            console.log("_getRelationshipRespChanged httpStatusCode : "+newValue.httpStatusCode);
            console.log("_getRelationshipRespChanged expRelationship : "+newValue.expandedRelationship);


            if (newValue.httpStatusCode == 200) {
                // Success

                // Determine whether this is a relationship we already have in the diagram (in the gens)
                // or whether it is new to this exploration. If it is new we need to assign it to the
                // current gen and tell the diagram manager - so we pick the relationship digest from the
                // expanded relationship and wrap it in a traversal and issue a graph-changed event.
                // Similarly for the end entities - if the relationship was known we must already know the
                // entities - but if the relationship is new we need to check for the entities and either
                // assign them the next gen or allow them to retain their existing gens.
                // If the relationship was not new we need to update the relationship digest to retain the original gen.
                // This is so the gen remains as it was so that the relationship retains its location in the
                // user's exploration.
                // Whether it was already known or not we save the most recent information into the
                // root instance - the relationship content may have changed in the repo.
                // Whether new or known the focus is not this relationship so we issue a focus-relationship-changed
                // event.

                var serverName = newValue.expandedRelationship.serverName;

                var relationshipGUID = newValue.expandedRelationship.relationshipDigest.relationshipGUID;

                console.log("instance-retriever: relationshipGUID is "+relationshipGUID);
                // Determine whether relationship is already known ...
                var relationshipKnown = false;
                var gen;
                // Search the existing gens looking for guid  // TODO can use guidToGen
                for (var i=0; i< this.gens.length; i++) {
                    var igen = this.gens[i];
                    var igenRelationships = igen.relationships;
                    if (igenRelationships !== undefined) {
                        if (igenRelationships[relationshipGUID] !== undefined) {
                            relationshipKnown = true;
                            gen = i+1;
                            break;
                        }
                    }
                }
                console.log("instance-retriever: relationshipGUID search complete");

                if (relationshipKnown === false) {
                    console.log("instance-retriever: relationship is new");
                    // Advance the currentGen
                    this.advanceCurrentGen();
                    gen = this.currentGen;
                }


                // Store the expanded relationship into rootInstance, maintain the gen as above.
                // rootInstance may have been cleared (to undefined) due to deselection of the focus instance.
                if (this.rootInstance === undefined) {
                    this.rootInstance = {};
                }
                this.rootInstance.category = "relationship";
                this.rootInstance.expRelationship = newValue.expandedRelationship;
                this.rootInstance.expRelationship.relationshipDigest.gen = gen;
                this.rootInstance.expEntity = undefined;

                //this.logRootRelationshipToConsole();

                // If this is a relationship we have not already seen - tell the diagram manager
                if (relationshipKnown === false) {

                    /*
                     * For the diagram, construct a pair of entity digests (for the end entities) and a
                     * relationship digest in a traversal; add it the logically next gen and issue a graph-changed event.
                     * We can do this by fabricating a traversal for the relationship and entities.
                     * This should be formatted into the following form.
                     *
                     *   a map of entityGUID             --> { entityGUID, label, gen }
                     *   a map of relationshipGUID       --> { relationshipGUID, label, gen }
                     */

                     var rexTraversal             = {};
                     rexTraversal.operation = "getRelationship";

                     rexTraversal.relationships   = {};
                     rexTraversal.relationships[relationshipGUID] = this.rootInstance.expRelationship.relationshipDigest;
                     rexTraversal.entities        = {};

                      /*
                       * We need to retrieve the end entity digests from the expRelationship and find out
                       * whether each end entity is new or known, so they can either keep their gens or be
                       * assigned the next gen...
                       */

                     /*
                      * entityOne
                      */
                     var entityOneDigest = this.rootInstance.expRelationship.entityOneDigest;
                     var entityOneGUID = entityOneDigest.entityGUID;

                     /*
                      * Determine whether entityOne is already known. This could loop through the gens
                      * but it is slightly more efficient to use the guidToGen map as a direct index.
                      */
                     var entityOneKnown = false;
                     var e1gen;
                     if (this.guidToGen[entityOneGUID] !== undefined) {
                         entityOneKnown = true;
                         e1gen = this.guidToGen[entityOneGUID];
                     }
                     if (entityOneKnown === false) {
                         e1gen = this.currentGen;
                     }
                     entityOneDigest.gen = e1gen;

                     /*
                      * entityTwo
                      */
                     var entityTwoDigest = this.rootInstance.expRelationship.entityTwoDigest;
                     var entityTwoGUID = entityTwoDigest.entityGUID;

                     /*
                      * Determine whether entityTwo is already known. This could loop through the gens
                      * but it is slightly more efficient to use the guidToGen map as a direct index.
                      */
                     var entityTwoKnown = false;
                     var e2gen;
                     if (this.guidToGen[entityTwoGUID] !== undefined) {
                         entityTwoKnown = true;
                         e2gen = this.guidToGen[entityTwoGUID];
                     }
                     if (entityTwoKnown === false) {
                         e2gen = this.currentGen;
                     }
                     entityTwoDigest.gen = e2gen;

                     /*
                      * Add the entity digests to the traversal
                      */
                     rexTraversal.entities[entityOneGUID] = entityOneDigest;
                     rexTraversal.entities[entityTwoGUID] = entityTwoDigest;
                     rexTraversal.serverName              = serverName;

                     // Add the traversal to the sequence of gens in the graph.
                     // Only add the entities to guidToGen if they are not known.
                     this.gens.push(rexTraversal);
                     if (!entityOneKnown)
                         this.guidToGen[entityOneGUID] = e1gen;
                     if (!entityTwoKnown)
                         this.guidToGen[entityTwoGUID] = e2gen;
                     this.guidToGen[relationshipGUID] = this.currentGen;

                     // Generate the graph-changed event.
                     console.log("instance-retriever: generate graph-extended event");
                     this.outEvtGraphExtended();

                }

                 // Regardless of whether we have seen it before or it is new - it is now the focus....
                 this.outEvtFocusRelationshipChanged(relationshipGUID);

            }

            else {
                 // Failure
                 console.log("_getRelationshipRespChanged newValue has bad status code");
                 if (newValue.exceptionText) {
                     alert('Error occurred: ' +newValue.exceptionText);
                 }
                 else {
                     alert('Error occurred: no exception message given');
                 }
                 // Generate a failure to load event - this will allow the status to be reported
                 this.outEvtFocusRelationshipNotLoaded();

            }
        }
        else {
            console.log("_getRelationshipRespChanged newValue was null");
        }
    }



// TODO - can be deleted...
    /*
     * Observer to handle receipt of packaged instance data response from UI Application
     */
    processTraversalResult(newValue,oldValue) {
        //console.log("_traversalRespChanged invoked");
        if (newValue !== undefined && newValue !== null) {
            //console.log("_traversalRespChanged newValue : "+newValue);
            //console.log("_traversalRespChanged httpStatusCode : "+newValue.httpStatusCode);
            //console.log("_traversalRespChanged rexTraversal : "+newValue.rexTraversal);


            if (newValue.httpStatusCode == 200) {
                // Success

                /*
                 * The traversal results should have been formatted by the VS into the form needed by Rex.
                 * This means that it should have:
                 *   a map of entityGUID       --> { entityGUID, label, gen }
                 *   a map of relationshipGUID --> { relationshipGUID, end1GUID, end2GUID, idx, label, gen }
                 */

                 /*
                  * For each entity and relationship in the traversal response we need to determine whether it
                  * is known or new.
                  * Anything known is dropped from the traversal, which is then pushed to the logically next gen
                  */



                var rexTraversal = newValue.rexTraversal;
                rexTraversal.operation = "traversal";



                this.processTraversal(rexTraversal);

            }

            else {
                // Failure
                console.log("_traversalRespChanged newValue has bad status code");
                if (newValue.exceptionText) {
                    alert('Error occurred: ' +newValue.exceptionText);
                }
                else {
                    alert('Error occurred: no exception message given');
                }
                // Generate a failure to load event - this will allow the status to be reported
                var customEvent = new CustomEvent('traversal-not-loaded', { bubbles: true, composed: true, detail: {source: "instance-retriever"}  });
                this.dispatchEvent(customEvent);
            }
        }
        else {
            console.log("_traversalRespChanged: newValue was null");
        }
    }



    processTraversal(rexTraversal) {

        /*
         * The traversal results should have been formatted by the VS into the form needed by Rex.
         * This means that it should have:
         *   a map of entityGUID       --> { entityGUID, label, gen }
         *   a map of relationshipGUID --> { relationshipGUID, end1GUID, end2GUID, idx, label, gen }
         *
         * For each entity and relationship in the traversal response we need to determine whether it
         * is known or new. Anything known is dropped from the traversal, which is then pushed to the
         * logically next gen
         */



        /* Assume initially that the traversal contains new information; if so it will be added to
         * the next gen, calculated below. If nothing new is learned from the traversal then we
         * will not update the current gen.
         * Notice that the currentGen is NOT advanced at this stage - this will only happen if the
         * traversal contains new metadata objects.
         */
        var gen = this.currentGen + 1;

        /*
         * Process entities...
         * Anything that is known should be removed from the traversal.
         * Anything new can remain and should be assigned the next gen.
         */
        var entities = rexTraversal.entities;
        for (var entityGUID in entities) {

            /*
             * Determine whether entity is already known ...
             */
            var entityKnown = false;
            if (this.guidToGen[entityGUID] !== undefined) {
                entityKnown = true;
            }
            if (entityKnown === true) {
                // Remove the entity from the traversal
                delete rexTraversal.entities[entityGUID];
            }
            else {
                // Update the new entity's gen
                rexTraversal.entities[entityGUID].gen = gen;
                this.guidToGen[entityGUID] = gen;
            }
        }


        /*
         * Process relationships...
         * Anything that is known should be removed from the traversal.
         * Anything new can remain and should be assigned the next gen.
         */
        var relationships = rexTraversal.relationships;
        for (var relationshipGUID in relationships) {

            /*
             * Determine whether relationship is already known ...
             */
            var relationshipKnown = false;
            if (this.guidToGen[relationshipGUID] !== undefined) {
                relationshipKnown = true;
            }
            if (relationshipKnown === true) {
                // Remove the relationship from the traversal
                delete rexTraversal.relationships[relationshipGUID];
            }
            else {
                console.log("Relationship with GUID : "+relationshipGUID+" is new");
                // Update the new relationship's gen
                rexTraversal.relationships[relationshipGUID].gen = gen;
                this.guidToGen[relationshipGUID] = gen;
            }
        }


        /*
         * If there is anything new still in the traversal,
         *   -  each new instance has been assigned (above) the next gen; advance the value of currentGen.
         *   -  push the traversal into the appropriate position in gens
         *   -  generate a graph-changed event
         * else
         *   -  display a message saying that nothing new was learned.
         */
        var no_entities      = rexTraversal.entities      === undefined || Object.keys(rexTraversal.entities).length      === 0;
        var no_relationships = rexTraversal.relationships === undefined || Object.keys(rexTraversal.relationships).length === 0;
        // TODO CLEAN UP
        //if (rexTraversal.entities === undefined || (Object.keys(rexTraversal.entities).length === 0))
        //    no_entities      = true;
        //if (rexTraversal.relationships === undefined || (Object.keys(rexTraversal.relationships).length === 0))
        //    no_relationships = true;
        if (no_entities && no_relationships) {
            /*
             * This is not an error - it just means everything in the traversal was already known,
             * which can happen.
             * However, it is desirable to advise the user that nothing new was returned, which should explain why
             * there will be no visible change to the display.
             */
            alert("No additional objects were returned in the traversal");
        }

        else {

            /*
             * Because the traversal contains new information, advance the currentGen
             */
            this.advanceCurrentGen();
            /*
             * Add the traversal to the sequence of gens in the graph. Then generate the graph-changed event.
             * For what it's worth - set the gen at the traversal level (the contained objects already have
             * gen set)
             */
            rexTraversal.gen = gen;
            this.gens.push(rexTraversal);

            this.outEvtGraphExtended();

        }
    }






    _entitySearchRespChanged(newValue,oldValue) {
        //console.log("_entitySearchRespChanged invoked");
        if (newValue !== undefined && newValue !== null) {
            //console.log("_entitySearchRespChanged newValue : "+newValue);
            //console.log("_entitySearchRespChanged httpStatusCode : "+newValue.httpStatusCode);

            if (newValue.httpStatusCode == 200) {
                // Success

                var instances = [];
                for (var guid in newValue.entities) {
                    var entityDigest = newValue.entities[guid];
                    entityDigest.checked = false;
                    instances.push(entityDigest);
                }

                // Clear the filters from the dialog - we want to build a new one from the template...
                var containerForSearchResults = this.$.containerForSearchResults;
                while (containerForSearchResults.firstChild) {
                    containerForSearchResults.removeChild(containerForSearchResults.firstChild);
                }

                // Now add a new set of traversal filters to the container....
                var searchResults = document.createElement("entity-search-results");
                searchResults.instances = instances;
                this.searchText = newValue.searchText;
                this.serverName = newValue.serverName;
                this.searchCategory = newValue.searchCategory;
                containerForSearchResults.appendChild(searchResults);
                this.$.searchResultsDialog.open();
            }
            else{
                // Handle the failure case
                alert("Entity search did not find any matching instances");
            }
        }
    }


    _relationshipSearchRespChanged(newValue,oldValue) {
        //console.log("_relationshipSearchRespChanged invoked");
        if (newValue !== undefined && newValue !== null) {
            //console.log("_relationshipSearchRespChanged newValue : "+newValue);
            //console.log("_relationshipSearchRespChanged httpStatusCode : "+newValue.httpStatusCode);

            if (newValue.httpStatusCode == 200) {
                // Success
                var instances = [];
                for (var guid in newValue.relationships) {
                     var relationshipDigest = newValue.relationships[guid];
                     relationshipDigest.checked = false;
                     instances.push(relationshipDigest);
                }

                // Clear the results from the dialog - we want to build a new one from the template...
                var containerForSearchResults = this.$.containerForSearchResults;
                while (containerForSearchResults.firstChild) {
                     containerForSearchResults.removeChild(containerForSearchResults.firstChild);
                }

                // Now add a new set of results to the container....
                var searchResults = document.createElement("relationship-search-results");
                searchResults.instances = instances;
                this.searchText = newValue.searchText;
                this.serverName = newValue.serverName;
                this.searchCategory = newValue.searchCategory;
                containerForSearchResults.appendChild(searchResults);
                this.$.searchResultsDialog.open();
            }
            else{
                // Handle the failure case
                alert("Relationship search did not find any matching instances");
            }
        }
    }





    /*
     * Function for the diagram to call to retrieve the latest traversal data in the form it needs..
     * The subgraph should have been formatted by the VS into the form needed by Rex.
     * This means that it should have:
     *   a map of entityGUID       --> { entityGUID, label, gen }
     *   a map of relationshipGUID --> { relationshipGUID, end1GUID, end2GUID, idx, label, gen }
     */
    getLatestGen() {
        var genIdx = this.currentGen - 1;
        //console.log("instance-retriever retrieving latest gen from position "+genIdx);
        var returnMap = {};
        returnMap.numberOfGens = this.numberOfGens;
        returnMap.currentGen   = this.gens[genIdx];
        return returnMap;

    }

    /*
     * Function for the diagram to call to retrieve the latest traversal data in the form it needs..
     * The subgraph should have been formatted by the VS into the form needed by Rex.
     * This means that it should have:
     *   a map of entityGUID       --> { entityGUID, label, gen }
     *   a map of relationshipGUID --> { relationshipGUID, end1GUID, end2GUID, idx, label, gen }
     */
    getAllGens() {
        var genIdx = this.currentGen - 1;
        var returnMap = {};
        returnMap.numberOfGens = this.numberOfGens;
        returnMap.allGens   = this.gens;
        return returnMap;
   }


    /*
     * This function should be the only way that the currentGen and numberOfGens properties are updated
     */
    advanceCurrentGen() {
        this.currentGen = this.currentGen + 1;
        this.numberOfGens = this.currentGen;
    }

    /*
     * Return the current value of instanceGUID
     */
    getInstanceGUID() {
        return this.instanceGUID;
    }

    /*
     * Return the current value of currentGen
     */
    getCurrentGen() {
        return this.currentGen;
    }

    /*
     * Return the category of the current focus, if set
     */
    getFocusInstanceCategory() {
        if (this.selectedCategory === undefined) {
          return null;
        }
        return this.selectedCategory;
    }

    /*
     * Function to retrieve the current focus entity - if one exists
     * The returned object is the expanded entity - consisting of the entityDetail and entityDigest.
     */
    getFocusEntity() {
        if (this.rootInstance !== undefined) {
            if (this.rootInstance.category === "entity") {
                return this.rootInstance.expEntity;
            }
        }
        /* No focus set */
        return null;
    }


    /*
     * Function to retrieve the current focus relationship - if one exists
     * The returned object is the expanded relationship - consisting of the relationship and relationshipDigest.
     */
    getFocusRelationship() {
        if (this.rootInstance !== undefined) {
            if (this.rootInstance.category === "relationship") {
                //console.log("instance-retriever: getFocusRelationship has relationship with gen "+this.rootInstance.expRelationship.relationshipDigest.gen);
                return this.rootInstance.expRelationship;
            }
        }
        //console.log("getFocusRelationship invoked, but focus is not a relationship");
        return null;
    }



}

window.customElements.define('instance-retriever', InstanceRetriever);