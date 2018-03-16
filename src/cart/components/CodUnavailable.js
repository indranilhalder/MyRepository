import React from "react";
import { Icon } from "xelpmoc-core";
import cashIcon from "./img/cod.svg";
import PropTypes from "prop-types";
import arrowIcon from "../../general/components/img/down-arrow.svg";
import styles from "./CodUnavailable.css";
export default class CodUnavailable extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.icon}>
          <Icon image={cashIcon} size={30} />
        </div>
        <div className={styles.head}>Cash On Delivery</div>
        <div className={styles.message}>({this.props.message})</div>
        <div className={styles.arrow}>
          <Icon image={arrowIcon} size={12} />
        </div>
      </div>
    );
  }
}

CodUnavailable.defaultProps = {
  message: "Not allowed for CiQ and PiQ option"
};
CodUnavailable.propTyps = {
  message: PropTypes.string
};
