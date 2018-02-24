import React from "react";
import Checkout from "./Checkout";
import PropTypes from "prop-types";
import styles from "./CheckoutFrame.css";
export default class CheckoutFrame extends React.Component {
  handleCancel = () => {
    if (this.props.onCancel) {
      this.props.onCancel();
    }
  };
  handleSubmit = () => {
    if (this.props.onSubmit) {
      this.props.onSubmit();
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          <div className={styles.cross} onClick={this.handleCancel} />
          Checkout
        </div>
        <div className={styles.content}>{this.props.children}</div>
        <Checkout
          amount={this.props.amount}
          bagTotal={this.props.bagTotal}
          tax={this.props.tax}
          offers={this.props.offers}
          delivery={this.props.delivery}
          payable={this.props.payable}
          onCheckout={this.handleSubmit}
        />
      </div>
    );
  }
}

CheckoutFrame.propTypes = {
  amount: PropTypes.string,
  bagTotal: PropTypes.string,
  tax: PropTypes.string,
  delivery: PropTypes.string,
  onCheckout: PropTypes.func,
  offers: PropTypes.string,
  payable: PropTypes.string
};
