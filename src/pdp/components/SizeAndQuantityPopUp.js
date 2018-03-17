import React from "react";
import styles from "./SizeAndQuantityPopUp.css";
import PropTypes from "prop-types";
export default class SizeAndQuantityPopUp extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      gender: props.gender ? props.gender : ""
    };
  }
  onChange(val) {
    if (this.props.onChange) {
      this.props.onChange(val);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.textHolder}>{this.props.data}</div>
      </div>
    );
  }
}
SizeAndQuantityPopUp.propTypes = {
  data: PropTypes.string
};
SizeAndQuantityPopUp.defaultProps = {
  data: "Please select a size & quantity to continue"
};
