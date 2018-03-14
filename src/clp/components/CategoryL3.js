import React from "react";
import PropTypes from "prop-types";
import styles from "./CategoryL3.css";
export default class CategoryL3 extends React.Component {
  handleClick(val) {
    if (this.props.onClick) {
      this.props.onClick(val);
    }
  }
  render() {
    return (
      <div
        className={styles.base}
        onClick={() => {
          this.handleClick(this.props.url);
        }}
      >
        {this.props.label}
      </div>
    );
  }
}

CategoryL3.propTypes = {
  label: PropTypes.string,
  onClick: PropTypes.func,
  url: PropTypes.string
};
