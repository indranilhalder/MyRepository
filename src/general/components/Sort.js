import React from "react";
import styles from "./Sort.css";
import SortTab from "./SortTab.js";
export default class Sort extends React.Component {
  onClick(val) {
    if (this.props.onClick) {
      this.props.onClick(val);
    }
  }
  render() {
    let data = this.props.sortList;
    return (
      <div className={styles.base}>
        {this.props.sortList &&
          this.props.sortList.length > 0 &&
          data.map((datum, i) => {
            return (
              <SortTab
                label={datum.name}
                value={datum.code}
                key={i}
                onClick={() => {
                  this.onClick(datum.code);
                }}
              />
            );
          })}
      </div>
    );
  }
}
