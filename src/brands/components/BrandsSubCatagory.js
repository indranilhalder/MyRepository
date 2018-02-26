import React from "react";
import PropTypes from "prop-types";
import styles from "./BrandsSubCatagory.css";
export default class BrandsSubCatagory extends React.Component {
  render() {
    let className = styles.base;
    if (this.props.select) {
      className = styles.selected;
    }
    return <div className={className}>{this.props.label}</div>;
  }
}
BrandsSubCatagory.propTypes = {
  label: PropTypes.string,
  select: PropTypes.bool
};
