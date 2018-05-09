import React from "react";
import styles from "./LevelBreakupCard.css";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import PropTypes from "prop-types";
export default class LevelBreakupCard extends React.Component {
  moveToWishlist() {
    if (this.props.moveToWishlist) {
      this.props.moveToWishlist();
    }
  }
  render() {
    let emiText = "Standard EMI applicable";
    if (this.props.emiApplication) {
      emiText = "No Cost EMI applicable";
    }
    return (
      <div className={styles.base}>
        <div className={styles.productHeader}>{this.props.productName}</div>
        <div className={styles.emiOptionHolder}>
          {emiText}
          {/* {!this.props.emiApplication && (
            <div className={styles.moveToWishListButton}>
              <UnderLinedButton
                size="14px"
                fontFamily="regular"
                color="#000000"
                label="Move to Wishlist"
                onClick={() => this.moveToWishlist()}
              />
            </div>
          )} */}
        </div>
        <div className={styles.amountPlaneForMonth}>
          <div className={styles.quantity}>
            Qty <span>{this.props.quantity}</span>
          </div>
          <div className={styles.amountData}>
            <div className={styles.amountLabel}>Item Value</div>
            <div className={styles.amount}>{`Rs.${Math.round(
              this.props.itemValue * 100
            ) / 100}`}</div>
          </div>
          <div className={styles.amountData}>
            <div className={styles.amountLabel}>Interest (charged by bank)</div>
            <div className={styles.amount}>{`Rs.${Math.round(
              this.props.Interest * 100
            ) / 100}`}</div>
          </div>
          <div className={styles.discount}>
            <div className={styles.amountLabel}>No Cost EMI Discount</div>
            <div className={styles.amountDiscount}>
              {this.props.discount > 0
                ? `-Rs.${Math.round(this.props.discount * 100) / 100}`
                : 0}
            </div>
          </div>
        </div>
        <div className={styles.totalAmountDisplay}>
          <div className={styles.totalAmountLabel}>
            <div className={styles.amountLabel}>Total Amount Payable</div>
            <div className={styles.amount}>{`Rs.${Math.round(
              this.props.totalAmount * 100
            ) / 100}
            `}</div>
          </div>
          <div className={styles.amountData}>
            <div className={styles.amountLabel}>EMI p.m</div>
            <div className={styles.amountEmi}>{`Rs.${Math.round(
              this.props.emiAmount * 100
            ) / 100}`}</div>
          </div>
        </div>
      </div>
    );
  }
}
LevelBreakupCard.propTypes = {
  productName: PropTypes.string,
  emiApplication: PropTypes.string,
  moveToWishlist: PropTypes.func,
  quantity: PropTypes.number,
  itemValue: PropTypes.string,
  Interest: PropTypes.string,
  discount: PropTypes.string,
  totalAmount: PropTypes.string,
  emiAmount: PropTypes.string
};
