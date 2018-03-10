import React from "react";
import styles from "./CategorySelect.css";

export default class CategorySelect extends React.Component {
  render() {
    return (
      <div className={this.props.selected ? styles.selected : styles.base}>
        {this.props.name}
        <div className={styles.count}>{this.props.count}</div>
      </div>
    );
  }
}
