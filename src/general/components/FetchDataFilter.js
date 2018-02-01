import React from "react";
import FetchFilter from "./FetchFilter";
import styles from "./FetchDataFilter";
import PropTypes from "prop-types";

export default class FetchDataFilter extends React.Component {
  render() {
    let data = this.props.facetdata;
    return (
      <div className={styles.base}>
        {data.map((datum, i) => {
          return (
            <FetchFilter
              key={i}
              name={datum.name}
              selectedFilterCount={datum.selectedFilterCount}
              onClick={this.props.onClick}
            />
          );
        })}
      </div>
    );
  }
}

FetchDataFilter.propTypes = {
  selectedFilterCount: PropTypes.string,
  name: PropTypes.string,
  onClick: PropTypes.func
};
