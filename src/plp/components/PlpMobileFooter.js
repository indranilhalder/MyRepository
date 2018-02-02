import React from "react";
import { Icon } from "xelpmoc-core";
import SortImage from "./img/sort.svg";
import FilterImage from "./img/filter.svg";
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
            <div className={styles.refine} />
            Refine
          </div>
        </div>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <div
              className={styles.sort}
              onClick={() => {
                this.onSort();
              }}
            />Sort
          </div>
        </div>
      </div>
    );
  }
}
