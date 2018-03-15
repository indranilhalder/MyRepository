import React from "react";
import PropTypes from "prop-types";
import styles from "./SearchResultItem.css";
export default class SearchResultItem extends React.Component {
  handleClick(val) {
    if (this.props.onClick && val) {
      this.props.onClick(val);
    }
  }
  render() {
    return (
      <div
        className={styles.base}
        onClick={val => {
          this.handleClick(val);
        }}
      >
        <span className={styles.bolder}>{this.props.suggestedText}</span> in{" "}
        {this.props.text}
      </div>
    );
  }
}
SearchResultItem = {
  text: PropTypes.string,
  onClick: PropTypes.func,
  value: PropTypes.string
};
