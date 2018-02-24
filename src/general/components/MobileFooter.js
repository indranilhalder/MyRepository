import React from "react";
import styles from "./MobileFooter.css";
import withMultiSelect from "../../higherOrderComponents/withMultiSelect";
const MobileFooter = class MobileFooter extends React.Component {
  render() {
    return <div className={styles.base}>{this.props.children}</div>;
  }
};
export default withMultiSelect(MobileFooter);
