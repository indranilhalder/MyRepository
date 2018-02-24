import React from "react";
import PropTypes from "prop-types";
import styles from "./BrandsCatagory.css";
export default class BrandsCatagory extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.brandIndex}>{this.props.index}</div>
        <div className={styles.listHolder}>
          <div className={styles.catagoryTetx}>{this.props.catagory}</div>
          {this.props.children}
        </div>
      </div>
    );
  }
}
BrandsCatagory.propTypes = {
  index: PropTypes.string,
  catagory: PropTypes.string
};
