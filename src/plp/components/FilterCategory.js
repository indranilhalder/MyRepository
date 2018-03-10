import React, { Component } from "react";
import FilterCategoryDetails from "./FilterCategoryDetails";
import FilterCategorySubList from "./FilterCategorySubList";
import PropTypes from "prop-types";

import styles from "./FilterCategory.css";
export default class FilterCategory extends Component {
  render() {
    console.log(this.props);
    return (
      <div className={styles.base}>
        {this.props.categoryTypeList.map((val, i) => {
          console.log(val);
          return (
            <FilterCategoryDetails
              category={val.categoryName}
              categoryCount={val.quantity}
              key={i}
            >
              {val.childFilters.map((data, i) => {
                console.log(data);
                return (
                  <FilterCategorySubList
                    subListItem={data.categoryName}
                    key={i}
                    value={i}
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
