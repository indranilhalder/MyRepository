import React from "react";
import styles from "./BankCoupons.css";
import CheckBox from "../../general/components/CheckBox.js";
import PropTypes from "prop-types";
export default class BankCoupons extends React.Component {
  applyCoupons(val) {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }

  render() {
    return (
      <div className={styles.base}>
        <div className={styles.cuponCard}>
          <div className={styles.headerText}>
            <span>{this.props.offerTitle}</span>
            <div
              className={styles.checkBoxHolder}
              onClick={val => this.applyCoupons(val)}
            >
              <CheckBox selected={this.props.selected} />
            </div>
          </div>
          <div className={styles.promotionDetailsText}>
            {this.props.offerDescription && (
              <div>{this.props.offerDescription}</div>
            )}
          </div>

          <div className={styles.dataHolder}>
            {this.props.offerMaxDiscount && (
              <div className={styles.amountExpireHolder}>
                <div className={styles.dataHeader}>Max Discount</div>
                <div className={styles.dataInformation}>
                  Rs.
                  {this.props.offerMaxDiscount}
                </div>
              </div>
            )}
            {this.props.offerMinCartValue && (
              <div className={styles.amountExpireHolder}>
                <div className={styles.dataHeader}>Min.bag amount</div>
                <div className={styles.dataInformation}>
                  Rs.
                  {this.props.offerMinCartValue}
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    );
  }
}
BankCoupons.propTypes = {
  coupons: PropTypes.arrayOf(
    PropTypes.shape({
      offerTitle: PropTypes.string,
      offerDescription: PropTypes.string,
      offerCode: PropTypes.string,
      offerMinCartValue: PropTypes.string
    })
  )
};
