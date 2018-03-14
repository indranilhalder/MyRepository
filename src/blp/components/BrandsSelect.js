import React from "react";
import PropTypes from "prop-types";
import withMultiSelect from "../../higherOrderComponents/withMultiSelect";
import styles from "./BrandsSelect.css";
const BrandsSelect = class BrandsSelect extends React.Component {
  render() {
    return <div className={styles.base}>{this.props.children}</div>;
  }
};
export default withMultiSelect(BrandsSelect);
