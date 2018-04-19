import React, { Component } from "react";
import FilterCategoryDetails from "./FilterCategoryDetails";
import FilterCategorySubList from "./FilterCategorySubList";
import PropTypes from "prop-types";

import styles from "./FilterCategory.css";
export default class FilterCategory extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selected: []
    };
  }

  render() {
    return (
      <div className={styles.base}>
        {this.props.categoryTypeList.map((val, i) => {
          return (
            <FilterCategoryDetails
              category={val.categoryName}
              categoryCount={val.quantity}
              key={i}
              value={val.categoryCode}
              onClick={this.props.onClick}
            >
              {val.childFilters &&
                val.childFilters.map((data, i) => {
                  return (
                    <FilterCategorySubList
                      subListItem={data.categoryName}
                      key={i}
                      value={data.categoryCode}
                      onClick={this.props.onL3Click}
                      subListCount={data.subListCount}
                    />
                  );
                })}
            </FilterCategoryDetails>
          );
        })}
      </div>
    );
  }
}
FilterCategory.propTypes = {
  category: PropTypes.string,
  categoryCount: PropTypes.number,
  typeList: PropTypes.arrayOf(
    PropTypes.shape({
      subListItem: PropTypes.string,
      subListCount: PropTypes.number
    })
  )
};
