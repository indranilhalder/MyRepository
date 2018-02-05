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
    return (
      <div className={styles.base}>
        {data.map((datum, i) => {
          let accumulator = 0;
          if (this.props.selected[i]) {
            this.props.selected[i].forEach(val => {
              if (val !== null) {
                accumulator++;
              }
            });
          }
          return (
            <FilterTab
              key={i}
              name={datum.name}
              selectedFilterCount={accumulator}
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
