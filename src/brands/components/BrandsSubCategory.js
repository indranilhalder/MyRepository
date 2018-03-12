import React from "react";
import PropTypes from "prop-types";
import styles from "./BrandsSubCategory.css";
export default class BrandsSubCategory extends React.Component {
  render() {
    let className = styles.base;
    if (this.props.select) {
      className = styles.selected;
    }
    return <div className={className}>{this.props.label}</div>;
  }
}
BrandsSubCategory.propTypes = {
  label: PropTypes.string,
  select: PropTypes.bool
};
