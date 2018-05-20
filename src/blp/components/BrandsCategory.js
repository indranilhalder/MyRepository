import React from "react";
import PropTypes from "prop-types";
import styles from "./BrandsCategory.css";
export default class BrandsCategory extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.brandIndex}>{this.props.index}</div>
        <div className={styles.listHolder}>{this.props.children}</div>
      </div>
    );
  }
}
BrandsCategory.propTypes = {
  index: PropTypes.string,
  catagory: PropTypes.string
};
