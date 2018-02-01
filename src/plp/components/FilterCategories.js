import React from "react";
import FilterTab from "./FilterTab";
import styles from "./FilterCategories.css";
import PropTypes from "prop-types";

export default class FilterCategories extends React.Component {
  render() {
    console.log(this.props.data);
    let data = this.props.data;
    return (
      <div className={styles.base}>
        {data.map((datum, i) => {
          return (
            <FilterTab
              key={i}
              name={datum.name}
              selectedFilterCount={datum.selectedFilterCount}
              onClick={this.props.onClick}
              selected={true}
              type="advance"
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
