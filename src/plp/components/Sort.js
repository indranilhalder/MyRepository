import React from "react";
import SortTab from "./SortTab.js";
import PropTypes from "prop-types";
import styles from "./Sort.css";
import InformationHeader from "../../general/components/InformationHeader";
import queryString from "query-string";

export const ARRAY_OF_SORTS = [
  "relevance",
  "isProductNew",
  "name-asc",
  "name-desc",
  "price-asc",
  "price-desc",
  "isDiscountedPrice",
  "promotedpriority-asc"
];

export default class Sort extends React.Component {
  onClick(val) {
    console.log("SORT CLICK");
    console.log(this.props);

    if (this.props.onClick) {
      const parsedQueryString = queryString.parse(this.props.location.search);
      let searchText = parsedQueryString.q;
      console.log("INITIAL SEARCH TEXT");
      console.log(searchText);
      if (!searchText) {
        searchText = val;
      } else {
        if (searchText.startsWith(":")) {
          searchText = `${val}${searchText}`;
        } else {
          searchText = `${searchText}${val}`;
        }
      }

      console.log("SEARCH TEXT");
      console.log(searchText);

      /*

      So Cases

      1. If q starts with a :, prepend the sort.
        Except if I can find the sort in the string, then redirect to the same url.
      2. If q does not start with a :, then I know I can just append it to q and set.
      3. If there is no q, then take the sort and do q=:sort: and redirect.
      */
    }
  }
  handleCloseClick = () => {
    this.props.onCloseSort();
  };
  render() {
    let data = this.props.sortList;
    return (
      <div className={styles.base}>
        <InformationHeader text="Sort" onClick={this.handleCloseClick} />
        {this.props.sortList &&
          this.props.sortList.length > 0 &&
          data.map((datum, i) => {
            return (
              <SortTab
                label={datum.name}
                value={datum.code}
                key={i}
                onClick={() => {
                  this.onClick(datum.code);
                }}
              />
            );
          })}
      </div>
    );
  }
}

Sort.PropTypes = {
  sortList: PropTypes.shape({ name: PropTypes.string, code: PropTypes.string }),
  onClick: PropTypes.func
};
