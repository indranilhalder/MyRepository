import React from "react";
import { Icon } from "xelpmoc-core";
import chashIcon from "./img/cod.svg";
import arrowIcon from "../../general/components/img/down";
import styles from "./CodUnavailable.css";
export default class CodUnavailable extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.icon}>
          <Icon image={chashIcon} size={25} />
        </div>
        <div className={styles.head}>Cash On Delivery</div>
        <div className={styles.message}>({this.props.message})</div>
        <div className={styles.arrow}>
          <Icon image={chashIcon} size={25} />
        </div>
      </div>
    );
  }
}

CodUnavailable.defaultProps = {
  message: "Not allowed for CiQ and PiQ option"
};
