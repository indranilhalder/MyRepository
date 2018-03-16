import React from "react";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import Logo from "../../general/components/Logo";
import Input2 from "../../general/components/Input2.js";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import Button from "../../general/components/Button.js";
import cliqCashIcon from "./img/cliqcash.png";
import styles from "./CliqAndCash.css";
export default class CliqAndCash extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.logoHolder}>
          <Logo image={cliqCashIcon} />
        </div>
        <div className={styles.cliqCashBalanceHolder}>
          <div className={styles.balance}>{`Rs. ${this.props.balance}`}</div>
          <div className={styles.expiredBalanceText}>{`Balance as of ${
            this.props.date
          } ${this.props.time} Hrs`}</div>
          <div className={styles.informationText}>
            Once you validate your gift card, the value will automatically be
            added to your CLiQ Cash
          </div>
        </div>
      </div>
    );
  }
}
