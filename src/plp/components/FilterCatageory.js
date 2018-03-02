import React, { Component } from "react";
import FilterCatageoryDetails from "./FilterCatageoryDetails";
import FilterCatageorySubList from "./FilterCatageorySubList";
import PropTypes from "prop-types";
import GridSelect from "../../general/components/GridSelect";
import styles from "./FilterCatageory.css";
export default class FilterCatageory extends Component {
  render() {
    return (
      <div className={styles.base}>
        {this.props.categoryTypeList.map((val, i) => {
          return (
            <FilterCatageoryDetails
              category={val.category}
              categoryCount={val.categoryCount}
              key={i}
            >
              <GridSelect limit={1} offset={0} elementWidthMobile={100}>
                {val.typeList.map((data, i) => {
                  return (
                    <FilterCatageorySubList
                      subListItem={data.subListItem}
                      key={i}
                      value={i}
                      subListCount={data.subListCount}
                    />
                  );
                })}
              </GridSelect>
            </FilterCatageoryDetails>
          );
        })}
      </div>
    );
  }
}
FilterCatageory.propTypes = {
  category: PropTypes.string,
  categoryCount: PropTypes.number,
  typeList: PropTypes.arrayOf(
    PropTypes.shape({
      subListItem: PropTypes.string,
      subListCount: PropTypes.number
    })
  )
};
