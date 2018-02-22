import React from "react";
import styles from "./EmiDisplay.css";
import { Image } from "xelpmoc-core";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import image from "../../general/components/img/check.svg";
import PropTypes from "prop-types";
export default class EmiDisplay extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          {this.props.bankName}
          <div className={styles.button}>
            <UnderLinedButton label="Change plan" />
          </div>
        </div>
        <div className={styles.emiText}>
          {this.props.term} EMI @ {this.props.emiRate}% p.a{" "}
          <span className={styles.price}>{this.props.price}</span>/Month
        </div>

        {/* <span className={styles.month}>/ {this.props.emiRate}</span> */}
        <div className={styles.checkBoxHolder}>
          <Image image={image} />
        </div>
      </div>
    );
  }
}
EmiDisplay.propTypes = {
  term: PropTypes.string,
  emiRate: PropTypes.string,
  price: PropTypes.string
};
