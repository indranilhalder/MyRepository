import React, { Component } from "react";
import FilterCategoryDetails from "./FilterCategoryDetails";
import FilterCategorySubList from "./FilterCategorySubList";
import PropTypes from "prop-types";
import Grid from "../../general/components/Grid";
import styles from "./FilterCategorySection.css";
export default class FilterCategorySection extends Component {
  onSelect(val) {
    if (this.props.onCategorySelect) {
      this.props.onCategorySelect(val);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        {this.props.categoryTypeList &&
          this.props.categoryTypeList[0].childFilters.map((val, i) => {
            return (
              <FilterCategoryDetails
                category={val.categoryName}
                categoryCount={val.quantity}
                key={i}
              >
                <Grid limit={1} offset={0} elementWidthMobile={100}>
                  {val.childFilters.map((data, i) => {
                    return (
                      <FilterCategorySubList
                        subListItem={data.categoryName}
                        key={i}
                        value={data.categoryCode}
                        subListCount={data.quantity}
                        onSelect={value => this.onSelect(value)}
                      />
                    );
                  })}
                </Grid>
              </FilterCategoryDetails>
            );
          })}
      </div>
    );
  }
}
FilterCategorySection.propTypes = {
  category: PropTypes.string,
  categoryCount: PropTypes.number,
  childFilters: PropTypes.arrayOf(
    PropTypes.shape({
      subListItem: PropTypes.string,
      subListCount: PropTypes.number
    })
  )
};
