import React from "react";
import FilterTab from "./FilterTab";
import styles from "./FilterCategories.css";
import PropTypes from "prop-types";

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
          let selected = false;
          if (this.props.pageNumber === i) {
            selected = true;
          }
          return (
            <FilterTab
              key={i}
              name={datum.name}
              selectedFilterCount={datum.selectedFilterCount}
              onClick={val => this.handleClick(i)}
              selected={selected}
              type={datum.isGlobalFilter ? "global" : "advance"}
            />
          );
        })}
      </div>
    );
  }
}

FilterCategories.propTypes = {
  selectedFilterCount: PropTypes.string,
  name: PropTypes.string,
  onClick: PropTypes.func
};
