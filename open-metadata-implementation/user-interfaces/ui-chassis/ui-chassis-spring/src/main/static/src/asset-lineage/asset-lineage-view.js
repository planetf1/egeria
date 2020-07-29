/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from '@polymer/polymer/polymer-element.js';
import '../shared-styles.js';
import '../common/vis-graph.js';
import '@vaadin/vaadin-radio-button/vaadin-radio-button.js';
import '@vaadin/vaadin-radio-button/vaadin-radio-group.js';
import '@vaadin/vaadin-tabs/vaadin-tabs.js';
import '@vaadin/vaadin-select/vaadin-select.js';
import '@vaadin/vaadin-dropdown-menu/vaadin-dropdown-menu.js';
import '@vaadin/vaadin-item/vaadin-item.js';
import '@vaadin/vaadin-list-box/vaadin-list-box.js';
import {mixinBehaviors} from "@polymer/polymer/lib/legacy/class";
import {ItemViewBehavior} from "../common/item";

class AssetLineageView extends mixinBehaviors([ItemViewBehavior], PolymerElement) {
    constructor() {
        super();
    }
  static get template() {
    return html`
    <style include="shared-styles">
        :host {
          display: flex;
          flex-direction: column;
          margin:var(--egeria-view-margin);
          min-height: var(--egeria-view-min-height);
          max-height: var(--egeria-view-min-height);
        }
        
        #container {
          background-color: var( --egeria-background-color );
          display: flex;
          flex-direction: column;
          flex-grow: 1;
        }
        #useCases {
            color: var(--egeria-primary-color);
            width: fit-content;
            margin: auto;
        }
    </style>
    
    <app-route route="{{route}}" pattern="/:usecase/:guid" data="{{routeData}}" tail="{{tail}}"></app-route>
     
    <token-ajax id="tokenAjax" last-response="{{graphData}}"></token-ajax>
    <token-ajax id="tokenAjaxDetails" last-response="{{item}}" ></token-ajax>
    <div>
        <vaadin-tabs id ="useCases"  selected="[[ _getUseCase(routeData.usecase) ]]" >
          <vaadin-tab value="ultimateSource" >
            <a href="[[rootPath]]#/asset-lineage/ultimateSource/[[routeData.guid]]" tabindex="-1" rel="noopener"> 
                Ultimate Source
            </a>
          </vaadin-tab>
          <vaadin-tab value="endToEnd">
            <a href="[[rootPath]]#/asset-lineage/endToEnd/[[routeData.guid]]" tabindex="-1"  rel="noopener"> 
                End to End Lineage
            </a>
          </vaadin-tab>
          <vaadin-tab value="ultimateDestination">
            <a href="[[rootPath]]#/asset-lineage/ultimateDestination/[[routeData.guid]]" tabindex="-1"  rel="noopener"> 
                Ultimate Destination
            </a>
          </vaadin-tab>
          <vaadin-tab value="glossaryLineage">
            <a href="[[rootPath]]#/asset-lineage/glossaryLineage/[[routeData.guid]]" tabindex="-1" rel="noopener"> 
                Glossary Lineage
            </a>
          </vaadin-tab>
          <vaadin-tab value="sourceAndDestination">
            <a href="[[rootPath]]#/asset-lineage/sourceAndDestination/[[routeData.guid]]" tabindex="-1" rel="noopener"> 
                Source and Destination
            </a>
          </vaadin-tab>
        </vaadin-tabs>
        <div> 
            <vaadin-select id="processMenu" value="true" >
              <template>
                <vaadin-list-box>
                  <vaadin-item value="true" selected>With ETL Jobs</vaadin-item>
                  <vaadin-item value="false">Without ETL Jobs</vaadin-item>
                </vaadin-list-box>
                </template>
            </vaadin-select>
            <vaadin-select id="glossaryTermMenu" value="true" 
                hidden = "[[_hideIncludeGlossaryTerms(routeData.usecase)]]" >
              <template>
                <vaadin-list-box>
                  <vaadin-item value="true" selected>With Glossary Term</vaadin-item>
                  <vaadin-item value="false">Without Glossary Term</vaadin-item>
                </vaadin-list-box>  
              </template>
            </vaadin-select>
            <paper-button id = "closeLegendButton" on-tap="toggleLegend">Toggle legend</paper-button>
        </div>
    </div>

    <div id="container">
        <vis-graph id="visgraph" groups=[[groups]] data=[[graphData]]></vis-graph>
    </div>
    `;
  }

    ready() {
        super.ready();
        var thisElement = this;
        this.$.tokenAjax.addEventListener('error', () =>
            thisElement.$.visgraph.importNodesAndEdges([],[]));
        this.$.processMenu.addEventListener('value-changed', () =>
            this._reload(this.$.useCases.items[this.$.useCases.selected].value, this.$.processMenu.value));

        this.$.glossaryTermMenu.addEventListener('value-changed', () =>
            this._reload(this.$.useCases.items[this.$.useCases.selected].value, this.$.processMenu.value));

    }

    static get properties() {
        return {
            usecases:{
                type: Array,
                value:['ultimateSource','endToEnd','ultimateDestination','glossaryLineage','sourceAndDestination' ]
            },
            graphData: {
                type: Object,
                observer: '_graphDataChanged'
            },
            graphInteraction: {
                type: Object,
                value: {
                    hideEdgesOnDrag: true
                }
            },
            graphLayout: {
                type: Object,
                value: {
                    hierarchical: {
                        enabled: true,
                        levelSeparation: 300,
                        direction: 'LR'
                    }
                }
            },
            groups : {
                type: Object,
                value: {
                    GlossaryTerm: {
                        color: '#f0e442',
                        icon: 'vaadin:records'
                    },
                    Column: {
                        color: '#009e73',
                        icon: 'vaadin:grid-h'
                    },
                    RelationalColumn: {
                        color: '#73c0ee',
                        icon: 'vaadin:road-branches'
                    },
                    TabularColumn: {
                        color: '#cc79a7',
                        icon: 'vaadin:tab'
                    },
                    RelationalTable: {
                        color: '#50c67a',
                        icon: 'vaadin:table'
                    },
                    Process: {
                        color: '#ffff00',
                        icon: 'vaadin:file-process'
                    },
                    subProcess: {
                        color: '#ffff00',
                        icon: 'vaadin:cogs'
                    },
                    condensedNode: {
                        color: '#faa43a',
                        icon: 'vaadin:cogs'
                    }
                }
            }
        }
    }

    connectedCallback() {
        super.connectedCallback();
        this.$.visgraph.options.groups = this.groups;
        this.$.visgraph.options.interaction = this.graphInteraction;
        this.$.visgraph.options.layout = this.graphLayout;
        this.$.visgraph.options.physics = false;
    }

    static get observers() {
        return [
            '_routeChanged(route)'
        ];
    }

    _routeChanged(route){
        if (this.route.prefix === '/asset-lineage') {
            this.$.tokenAjaxDetails.url = '/api/assets/' + this.routeData.guid;
            this.$.tokenAjaxDetails._go();
            this._reload(this.routeData.usecase, this.$.processMenu.value);
        }
    }

    _parseProperties(props){
        var obj = {};
        Object.keys(props).forEach(
            (key)=> {
                var newKey = key.split("vertex--").join("");
                obj[newKey] = ""+props[key];
            }
        );
        return obj;
    }

    _graphDataChanged(data, newData) {
        if (data === null || data === undefined) {
            if (newData && newData != null) {
                data = newData;
            } else {
                data = {
                    nodes: [],
                    edges: []
                };
            }
        }
        for (var i = 0; i < data.nodes.length; i++) {
            const egeriaColor = getComputedStyle(this).getPropertyValue('--egeria-primary-color');
            let displayName;
            if(data.nodes[i].properties && data.nodes[i].properties!==null && data.nodes[i].properties!==undefined ){
                data.nodes[i].properties = this._parseProperties(data.nodes[i].properties);

                if (data.nodes[i].properties['tableDisplayName'] != null
                    && data.nodes[i].properties['tableDisplayName'] != undefined) {
                    displayName = data.nodes[i].properties['tableDisplayName']
                }
            }
            data.nodes[i].displayName = data.nodes[i].label;
            data.nodes[i].type = data.nodes[i].group;

            data.nodes[i].label = '<b>'+data.nodes[i].label+'</b>';
            data.nodes[i].label += ' \n\n Type : ' + this._camelCaseToSentence(data.nodes[i].group);
            if (displayName != null) {

                data.nodes[i].label += ' \n\n From : ' + displayName;
            }
            if (data.nodes[i].id === this.routeData.guid){
                data.nodes[i].group='';
                data.nodes[i].color=egeriaColor;
                data.nodes[i].font= {color:'white'};
                data.nodes[i].shape= 'circle';
            }
        }
        if (!this._hideIncludeGlossaryTerms(this.routeData.usecase) && this.$.glossaryTermMenu.value === "false" ) {
            var filteredNodes = [];
            var nodesToRemove = [];
            var filteredEdges = [];
            for (var i = 0; i < data.nodes.length; i++) {
                if (data.nodes[i].group !== "GlossaryTerm") {
                    filteredNodes.push(data.nodes[i]);
                } else {
                    nodesToRemove.push(data.nodes[i])
                }
            }
            for (var j = 0; j < data.edges.length; j++) {
                var edgeRemoved = false;
                for (var i = 0; i < nodesToRemove.length; i++) {
                    if (data.edges[j].from === nodesToRemove[i].id) {
                        for (var k = 0; k < data.edges.length; k++) {
                            if (data.edges[k].to === nodesToRemove[i].id) {
                                edgeRemoved = true;
                                filteredEdges.push({
                                    to : data.edges[j].to,
                                    from : data.edges[k].from,
                                    label : data.edges[k].label
                                })
                            }
                        }
                    }
                }
                if (edgeRemoved === false) {
                    filteredEdges.push(data.edges[j])
                }
            }
            data.nodes = filteredNodes;
            data.edges = filteredEdges;
        }
        this.$.visgraph.importNodesAndEdges(data.nodes, data.edges);
    }

    _ultimateSource(guid, includeProcesses) {
    if (includeProcesses === null || includeProcesses === undefined) {
     includeProcesses  = "true";
    }
    this.$.visgraph.options.groups = this.groups;
    this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/ultimate-source?includeProcesses=' + includeProcesses;
    this.$.tokenAjax._go();
    }

    _endToEndLineage(guid, includeProcesses){
      if (includeProcesses === null || includeProcesses === undefined) {
          includeProcesses  = "true";
      }
      this.$.visgraph.options.groups = this.groups;
      this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/end2end?includeProcesses=' + includeProcesses;
      this.$.tokenAjax._go();
    }

    _ultimateDestination(guid, includeProcesses){
      if (includeProcesses === null || includeProcesses === undefined) {
          includeProcesses  = "true";
      }
      this.$.visgraph.options.groups = this.groups;
      this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/ultimate-destination?includeProcesses=' + includeProcesses;
      this.$.tokenAjax._go();
    }

    _glossaryLineage(guid, includeProcesses){
      if (includeProcesses === null || includeProcesses === undefined) {
          includeProcesses  = "true";
      }
      this.$.visgraph.options.groups = this.groups;
      this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/glossary-lineage?includeProcesses=' + includeProcesses;
      this.$.tokenAjax._go();
    }

    _sourceAndDestination(guid, includeProcesses){
      if (includeProcesses === null || includeProcesses === undefined) {
          includeProcesses  = "true";
      }
      this.$.visgraph.options.groups = this.groups;
      this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/source-and-destination?includeProcesses=' + includeProcesses;
      this.$.tokenAjax._go();
    }

    _reload(usecase, includeProcesses) {
    switch (usecase) {
        case 'ultimateSource':
            this._ultimateSource(this.routeData.guid, includeProcesses);
            break;
        case 'endToEnd':
            this._endToEndLineage(this.routeData.guid, includeProcesses);
            break;
        case 'ultimateDestination':
            this._ultimateDestination(this.routeData.guid, includeProcesses);
            break;
        case 'glossaryLineage':
            this._glossaryLineage(this.routeData.guid, includeProcesses);
            break;
        case 'sourceAndDestination':
            this._sourceAndDestination(this.routeData.guid, includeProcesses);
            break;
    }
    }

    toggleLegend() {
        this.$$('vis-graph').toggleLegend();
    }

    _getUseCase(usecase){
        return this.usecases.indexOf(usecase);
    }

    _hideIncludeGlossaryTerms(usecase) {
        // return !("ultimateDestination" === usecase || "ultimateSource" === usecase) ;
        return true;
    }
}

window.customElements.define('asset-lineage-view', AssetLineageView);
