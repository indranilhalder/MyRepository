import React from "react";
import SelectBoxMobile from "./SelectBoxMobile";
import PropTypes from "prop-types";
import styles from "./MobileSelectWithError.css";
export default class MobileSelectWithError extends React.Component {
  render() {
    return (
      <div className={this.props.hasError ? styles.error : styles.base}>
        <SelectBoxMobile theme="hollowBox" {...this.props} />
      </div>
    );
  }
}
MobileSelectWithError.propTypes = {
  hasError: PropTypes.bool
};
