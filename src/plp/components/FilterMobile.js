import React from "react";
import FilterTab from "./FilterTab";
import FilterSelect from "./FilterSelect";
import styles from "./FilterMobile.css";

export default class FilterMobile extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.tabHolder}>
          <div className={styles.slider}>
            <FilterTab name="1" />
            <FilterTab name="2" />
            <FilterTab name="3" />
            <FilterTab name="4" />
            <FilterTab name="5" />
            <FilterTab name="6" />
            <FilterTab name="7" />
            <FilterTab name="8" />
            <FilterTab name="9" />
            <FilterTab name="10" />
            <FilterTab name="11" />
            <FilterTab name="12" />
            <FilterTab name="13" />
            <FilterTab name="14" />
            <FilterTab name="15" />
            <FilterTab name="16" />
            <FilterTab name="17" />
            <FilterTab name="18" />
          </div>
        </div>
        <div className={styles.contenHolder}>
          <div className={styles.slider} />
        </div>
      </div>
    );
  }
}
