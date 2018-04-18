import React from "react";
import styles from "./EmiDisplay.css";
import Image from "../../xelpmoc-core/Image";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import image from "../../general/components/img/check.svg";
import PropTypes from "prop-types";
export default class EmiDisplay extends React.Component {
  handleClick() {
    if (this.props.changePlan) {
      this.props.changePlan();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          {this.props.bankName}
          <div className={styles.button}>
            <UnderLinedButton
              label="Change plan"
              onClick={() => {
                this.handleClick();
              }}
            />
          </div>
        </div>
        <div className={styles.emiText}>
          {this.props.term} EMI @ {this.props.emiRate}% p.a{" "}
          <span className={styles.price}>{this.props.price}</span>/Month
        </div>
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
