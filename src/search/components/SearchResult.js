import React from "react";
import PropTypes from "prop-types";
import styles from "./SearchResult.css";
export default class SearchResult extends React.Component {
  handleClick() {
    if (this.props.onClick && this.props.value) {
      this.props.onClick(this.props.value);
    }
  }
  render() {
    return (
      <div
        className={styles.base}
        onClick={() => {
          this.handleClick();
        }}
      >
        {this.props.text}
      </div>
    );
  }
}
SearchResult = {
  text: PropTypes.string,
  onClick: PropTypes.func,
  value: PropTypes.string
};
