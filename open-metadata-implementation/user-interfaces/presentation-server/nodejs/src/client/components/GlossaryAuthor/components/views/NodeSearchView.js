/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect, useContext } from "react";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";
// import useDebounce from "./useDebounce";
// import Delete16 from "../../images/Egeria_delete_16";
// import Edit16 from "../../images/Egeria_edit_16";
import {
  Accordion,
  AccordionItem,
  DataTable,
  MultiSelect,
  Pagination,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableSelectRow,
  TableCell,
  TableHeader,
  TableBody,
} from "carbon-components-react";

// Responsible for issuing search requests on a node and displaying the results.
// - the search is issue with debounce
// - additional columns can be specified.
// - the search has pagination
// - the search results can be selected.
// @param {*} props
//
const NodeSearchView = (props) => {
  console.log("NodeSearchView " + props.refresh);
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);

  // properties that will be displayed be default for a node
  const mainProperties = [
    {
      key: "name",
      text: "Name",
    },
    {
      key: "description",
      text: "Description",
    },
    {
      key: "qualifiedName",
      text: "Qualified Name",
    },
  ];

  const paginationProps = () => ({
    disabled: false,
    page: props.pageNumber,
    pagesUnknown: true,
    pageInputDisabled: false,
    backwardText: "Previous page",
    forwardText: "Next page",
    totalItems: props.total,
    pageSize: props.pageSize,
    pageSizes: [5, 10, 50, 100],
    itemsPerPageText: "Items per page:",
    onChange: props.onPagination,
  });

  const [headerData, setHeaderData] = useState(mainProperties);
  const additionalProperties = calculateAdditionalProperties();
  let selectedAdditionalProperties = [];

  // calculate the results table header - this will be the default columns plus any additional coliumns the user has specified
  function calculateHeaderData() {
    let allProperties = mainProperties;
    if (
      selectedAdditionalProperties !== undefined &&
      selectedAdditionalProperties &&
      selectedAdditionalProperties.length > 0
    ) {
      console.log("selectedAdditionalProperties.selectedItems 1");
      console.log(selectedAdditionalProperties);
      allProperties = mainProperties.concat(selectedAdditionalProperties);
    }
    console.log("allProperties 1");
    console.log(allProperties);
    setHeaderData(allProperties);
  }

  const onSearchCriteria = (e) => {
    props.onSearchCriteria(e.target.value);
  }
  // Additional attributes can be selected so more columns can be shown
  // the additional attriniutes are in selectedAdditionalProperties
  const onAdditionalAttributesChanged = (items) => {
    console.log("onAdditionalAttributesChanged");
    console.log(items.selectedItems);
    selectedAdditionalProperties = [];
    const selectedItems = items.selectedItems;
    for (let i = 0; i < selectedItems.length; i++) {
      let item = {};
      item.key = selectedItems[i].id;
      item.text = selectedItems[i].text;
      selectedAdditionalProperties.push(item);
    }
    // render the table by recalculating the header state based on the new values
    calculateHeaderData();
  };
  // calculate the columns from the main attributes and the additional attributes.
  function calculateAdditionalProperties() {
    let items = [];
    glossaryAuthorContext.currentNodeType.attributes.map(function (attribute) {
      if (
        attribute.key != "name" &&
        attribute.key != "qualifiedName" &&
        attribute.key != "description"
      ) {
        let item = {};
        item.id = attribute.key;
        item.text = attribute.label;
        items.push(item);
      }
    });
    return items;
  }
  
  const onClickExactMatch = () => {
    console.log("onClickExactMatch");
    const checkBox = document.getElementById("exactMatch");
    props.onExactMatch(checkBox.checked);
  };
  const onSelectRow = (row) => {
    console.log("onSelectRow");
    props.onSelectRow(row);
  };
 
  return (
    <div className="top-search-container">
      <div className="top-search-item">
        <div className="search-container">
          <div data-search role="search" className="bx--search bx--search--l">
            <div className="search-item">
              <label
                id="search-input-label-1"
                className="bx--label"
                for="search__input-1"
              >
                Search
              </label>
              <input
                className="bx--search-input"
                type="text"
                id="search__input-1"
                onChange={onSearchCriteria}
                placeholder="Search"
              />
              <svg
                focusable="false"
                preserveAspectRatio="xMidYMid meet"
                xmlns="http://www.w3.org/2000/svg"
                className="bx--search-magnifier"
                width="16"
                height="16"
                viewBox="0 0 16 16"
                aria-hidden="true"
              >
                <path d="M15,14.3L10.7,10c1.9-2.3,1.6-5.8-0.7-7.7S4.2,0.7,2.3,3S0.7,8.8,3,10.7c2,1.7,5,1.7,7,0l4.3,4.3L15,14.3z M2,6.5  C2,4,4,2,6.5,2S11,4,11,6.5S9,11,6.5,11S2,9,2,6.5z"></path>
              </svg>
              <button
                className="bx--search-close bx--search-close--hidden"
                title="Clear search
            input"
                aria-label="Clear search input"
              >
                <svg
                  focusable="false"
                  preserveAspectRatio="xMidYMid meet"
                  xmlns="http://www.w3.org/2000/svg"
                  className="bx--search-clear"
                  width="20"
                  height="20"
                  viewBox="0 0 32 32"
                  aria-hidden="true"
                >
                  <path d="M24 9.4L22.6 8 16 14.6 9.4 8 8 9.4 14.6 16 8 22.6 9.4 24 16 17.4 22.6 24 24 22.6 17.4 16 24 9.4z"></path>
                </svg>
              </button>
            </div>
          </div>
          {glossaryAuthorContext.currentNodeType &&
            glossaryAuthorContext.currentNodeType.attributes.length > 3 && (
              <div className="search-item">
                <Accordion>
                  <AccordionItem title="Search options">
                    <label forHtml="exactMatch">Exact Match </label>
                    <input
                      type="checkbox"
                      id="exactMatch"
                      onClick={onClickExactMatch}
                    />
                    <div className="bx--form-item">
                      <div style={{ width: 150 }}>
                        <MultiSelect
                          onChange={onAdditionalAttributesChanged}
                          items={additionalProperties}
                          itemToString={(item) => (item ? item.text : "")}
                        />
                      </div>
                    </div>
                  </AccordionItem>
                </Accordion>
              </div>
            )}
          <div className="search-item">
            <DataTable
              radio
              key={props.tableKey}
              isSortable
              rows={props.tableRows}
              headers={headerData}
              render={({
                rows,
                headers,
                getHeaderProps,
                getSelectionProps,
                getRowProps,
              }) => (
                <TableContainer
                  title={glossaryAuthorContext.currentNodeType.typeName}
                >
                  <Table>
                    <TableHead>
                      <TableRow>
                        <TableHeader />
                        {headers.map((header) => (
                          <TableHeader
                            {...getHeaderProps({ header })}
                            key={header.key}
                          >
                            {header.text}
                          </TableHeader>
                        ))}
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {rows.map((row) => (
                        <TableRow {...getRowProps({ row })} key={row.id}>
                          <TableSelectRow
                            {...getSelectionProps({
                              row,
                              onClick: () => onSelectRow(row),
                            })}
                          />
                          {row.cells.map((cell) => (
                            <TableCell key={cell.id}>{cell.value}</TableCell>
                          ))}
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </TableContainer>
              )}
            />
          </div>
          <div className="search-item">
            <Pagination {...paginationProps()} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default NodeSearchView;
