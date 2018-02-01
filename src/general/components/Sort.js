import React from "react";
import styles from "./Sort.css";
import SortByBase from "./SortTab.js";
export default class Sort extends React.Component {
  render() {
    let data = this.props.data;
    return (
      <div className={styles.base}>
        {this.props.data.length > 0 &&
          data.map((datum, i) => {
            return (
              <SortByBase
                label={datum.label}
                value={datum.value}
                key={i}
                onClick={this.props.onClick}
              />
            );
          })}
      </div>
    );
  }
}
