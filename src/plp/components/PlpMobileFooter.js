import React from "react";
import PropTypes from "prop-types";
import styles from "./PlpMobileFooter.css";
export default class PlpMobileFooter extends React.Component {
  onFilter = () => {
    if (this.props.onFilter) {
      this.props.onFilter();
    }
  };
  onSort() {
    if (this.props.onSort) {
      this.props.onSort();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.buttonHolder}>
          <div className={styles.button} onClick={this.onFilter}>
            {this.props.hasFilters ? (
              <div className={styles.refineSelected} />
            ) : (
              <div className={styles.refine} />
            )}
            <span className={this.props.hasFilters && styles.selectedText}>
              Refine
            </span>
          </div>
        </div>
        <div className={styles.buttonHolder}>
          <div
            className={styles.button}
            onClick={() => {
              this.onSort();
            }}
          >
            {this.props.hasSort ? (
              <div className={styles.activeSort} />
            ) : (
              <div className={styles.sort} />
            )}
            <span className={this.props.hasSort && styles.selectedText}>
              Sort
            </span>
          </div>
        </div>
      </div>
    );
  }
}

PlpMobileFooter.propTypes = {
  onFilter: PropTypes.func,
  onSort: PropTypes.func
};
