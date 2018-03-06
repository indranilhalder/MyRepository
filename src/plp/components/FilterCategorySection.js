import React, { Component } from "react";
import FilterCategoryDetails from "./FilterCategoryDetails";
import FilterCategorySubList from "./FilterCategorySubList";
import PropTypes from "prop-types";
import Grid from "../../general/components/Grid";
import styles from "./FilterCategorySection.css";
export default class FilterCategorySection extends Component {
  constructor(props) {
    super(props);
    const selectedCategoryCodes = [];
    props.categoryTypeList[0].childFilters.forEach(val => {
      val.childFilters.forEach(filter => {
        selectedCategoryCodes.push(filter.categoryCode);
      });
    });
    this.state = {
      selectedCategoryCodes
    };
  }
  onSelect(val) {
    const selectedCategoryCodes = this.state.selectedCategoryCodes;
    // if the val is in selectedCategoryCodes, then we need to reverse the render
    if (selectedCategoryCodes[val]) {
      selectedCategoryCodes[val] = !selectedCategoryCodes[val];
    } else {
      selectedCategoryCodes[val] = true;
    }

    this.setState({ selectedCategoryCodes });
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
                        selected={
                          this.state.selectedCategoryCodes[data.categoryCode]
                        }
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
