import React from "react";
import SortTab from "./SortTab.js";
import PropTypes from "prop-types";
import styles from "./Sort.css";
import InformationHeader from "../../general/components/InformationHeader";
import queryString from "query-string";
import { applySortToUrl } from "./SortUtils.js";

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
    if (this.props.onClick) {
      const parsedQueryString = queryString.parse(this.props.location.search);
      console.log("PARSED QUERY STRING");
      console.log(parsedQueryString);
      let searchText;
      if (parsedQueryString.q) {
        searchText = parsedQueryString.q;
      } else if (parsedQueryString.text) {
        searchText = parsedQueryString.text;
      }

      const url = applySortToUrl(searchText, this.props.location.pathname, val);
      this.props.history.push(url, {
        isFilter: false
      });
      this.props.onClick();
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
