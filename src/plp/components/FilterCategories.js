import React from "react";
import FilterTab from "./FilterTab";
import styles from "./FilterCategories.css";
import PropTypes from "prop-types";
const GLOBAL = "global";
const ADVANCE = "advance";
export default class FilterCategories extends React.Component {
  handleClick(val) {
    if (this.props.onClick) {
      this.props.onClick(val);
    }
  }
  render() {
    let data = this.props.data;
    let filterCount = [];
    console.log("SELECTED");
    console.log(this.props.selected);
    if (this.props.selected) {
      filterCount = this.props.selected.map(val => {
        let summer = 0;
        val.forEach(val => {
          if (val !== null) {
            summer++;
          }
        });

        return summer;
      });
    }
    return (
      <div className={styles.base}>
        {data.map((datum, i) => {
          return (
            <FilterTab
              key={i}
              name={datum.name}
              selectedFilterCount={filterCount[i]}
              onClick={val => this.handleClick(i)}
              selected={this.props.pageNumber === i}
              type={datum.isGlobalFilter ? GLOBAL : ADVANCE}
            />
          );
        })}
      </div>
    );
  }
}

FilterCategories.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      name: PropTypes.string,
      selectedFilterCount: PropTypes.number,
      isGlobalFilter: PropTypes.bool
    })
  ),
  pageNumber: PropTypes.number,
  onClick: PropTypes.func
};
