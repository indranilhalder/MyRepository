import React from "react";
import PropTypes from "prop-types";
import styles from "./BrandsSubCategory.css";
export default class BrandsSubCategory extends React.Component {
  onClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    let className = styles.base;
    if (this.props.select) {
      className = styles.selected;
    }
    return (
      <div className={className} onClick={() => this.onClick()}>
        {this.props.label}
      </div>
    );
  }
}
BrandsSubCategory.propTypes = {
  label: PropTypes.string,
  select: PropTypes.bool
};
