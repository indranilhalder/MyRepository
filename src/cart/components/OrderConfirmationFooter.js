import React from "react";
import styles from "./OrderConfirmationFooter.css";
import PropTypes from "prop-types";
import FooterButton from "../../general/components/FooterButton.js";

export default class OrderConfirmationFooter extends React.Component {
  trackOrder() {
    if (this.props.trackOrder) {
      this.props.trackOrder();
    }
  }
  continueShopping() {
    if (this.props.continueShopping) {
      this.props.continueShopping();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        {!this.props.isEgvOrder && (
          <div>
            <div className={styles.footerButtonHolder}>
              <FooterButton
                borderColor="#ececec"
                label={this.props.viewFullDetailsText}
                onClick={() => this.trackOrder()}
              />
            </div>
            <div className={styles.footerButtonHolder}>
              <FooterButton
                backgroundColor="#ff1744"
                label={this.props.continueShoppingText}
                onClick={() => this.continueShopping()}
                labelStyle={{
                  color: "#fff",
                  fontSize: 14,
                  fontFamily: "semibold"
                }}
              />
            </div>}
          </div>
        )}
        {this.props.isEgvOrder && (
          <div className={styles.buttonHolder}>
            <FooterButton
              backgroundColor="#ff1744"
              onClick={() => this.continueShopping()}
              label={this.props.continueShoppingText}
              labelStyle={{
                color: "#fff",
                fontSize: 14,
                fontFamily: "semibold"
              }}
            />
          </div>
        )}
      </div>
    );
  }
}
OrderConfirmationFooter.propTypes = {
  cancel: PropTypes.func,
  update: PropTypes.func
};

OrderConfirmationFooter.defaultProps = {
  viewFullDetailsText: "View Full Details",
  continueShoppingText: "Continue Shopping"
};
