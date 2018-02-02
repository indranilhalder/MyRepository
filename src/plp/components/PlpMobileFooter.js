import React from "react";
import { Icon } from "xelpmoc-core";
import SortImage from "./img/sort.svg";
import FilterImage from "./img/filter.svg";
import styles from "./PlpMobileFooter.css";
export default class PlpMobileFooter extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <div className={styles.refine} />
            Refine
          </div>
        </div>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <div className={styles.sort} />Sort
          </div>
        </div>
      </div>
    );
  }
}
