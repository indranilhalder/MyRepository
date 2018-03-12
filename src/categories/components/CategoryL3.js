import React from "react";
import styles from "./CategoryL3.css";
export default class CategoryL3 extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div
        className={styles.base}
        onClick={() => {
          this.handleClick(this.props.value);
        }}
      >
        {this.props.label}
      </div>
    );
  }
}
