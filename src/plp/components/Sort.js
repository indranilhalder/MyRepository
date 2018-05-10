import React from "react";
import SortTab from "./SortTab.js";
import InformationHeader from "../../general/components/InformationHeader";
import PropTypes from "prop-types";
import styles from "./Sort.css";
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
    let searchText = "";
    let icid2 = null;
    let cid = null;
    if (this.props.onClick) {
      if (this.props.location.search) {
        const parsedQueryString = queryString.parse(this.props.location.search);
        if (parsedQueryString.icid2) {
          icid2 = parsedQueryString.icid2;
        }
        if (parsedQueryString.cid) {
          cid = parsedQueryString.cid;
        }
        if (parsedQueryString.q) {
          searchText = parsedQueryString.q;
        } else if (parsedQueryString.text) {
          searchText = parsedQueryString.text;
        }
      }

      const url = applySortToUrl(
        searchText,
        this.props.location.pathname,
        val,
        icid2,
        cid
      );
      this.props.history.push(url, {
        isFilter: false
      });
      this.props.onClick();
      this.props.setIfSortHasBeenClicked();
    }
  }
  handleCloseClick = () => {
    this.props.onCloseSort();
  };
  render() {
    let data = this.props.sortList;
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          <InformationHeader text="Sort by" goBack={this.handleCloseClick} />
        </div>
        {this.props.sortList &&
          this.props.sortList.length > 0 &&
          data.map((datum, i) => {
            return (
              <SortTab
                label={datum.name}
                value={datum.code}
                selected={datum.selected}
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
