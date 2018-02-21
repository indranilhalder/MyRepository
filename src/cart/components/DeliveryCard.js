import React from "react";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import { Image } from "xelpmoc-core";
import checkIcon from "./img/check.svg";
import CheckOutHeader from "./CheckOutHeader.js";
import styles from "./DeliveryCard.css";
export default class DeliveryCard extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.buttonHolder}>
          <UnderLinedButton
            size="14px"
            fontFamily="regular"
            color="#000"
            label="Change"
            onClick={() => this.handleClick()}
          />
        </div>
        <div className={styles.checkIconHolder}>
          <Image image={checkIcon} fit="cover" />
        </div>
        <div className={styles.headerTextHolder}>
          <CheckOutHeader
            confirmTitle={this.props.confirmTitle}
            indexNumber={this.props.indexNumber}
          />
        </div>
        <div className={styles.productShippingTextHolder}>
          {this.props.children}
        </div>
      </div>
    );
  }
}
DeliveryCard.propTypes = {
  confirmTitle: PropTypes.string,
  indexNumber: PropTypes.string,
  onClick: PropTypes.func
};
