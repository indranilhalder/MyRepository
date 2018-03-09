import React from "react";
import styles from "./BankCoupons.css";
import CheckBox from "../../general/components/CheckBox.js";
import PropTypes from "prop-types";
import GridSelect from "../../general/components/GridSelect";
export default class BankCoupons extends React.Component {
  applyCoupons(val) {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }

  render() {
    return (
      <div className={styles.base}>
        {this.props.coupons &&
          this.props.coupons.map((coupon, i) => {
            return (
              <div className={styles.cuponCard} key={i} value={i}>
                <div className={styles.headerText}>
                  <span>{coupon.offerTitle}</span>
                  <div
                    className={styles.checkBoxHolder}
                    onClick={val => this.applyCoupons(val)}
                  >
                    <CheckBox selected={this.props.selected} />
                  </div>
                </div>
                <div className={styles.promotionDetailsText}>
                  {coupon.offerDescription && (
                    <div>{coupon.offerDescription}</div>
                  )}
                </div>

                <div className={styles.dataHolder}>
                  {coupon.offerCode && (
                    <div className={styles.amountExpireHolder}>
                      <div className={styles.dataHeader}>Valid till</div>
                      <div className={styles.dataInformation}>
                        {coupon.offerCode}
                      </div>
                    </div>
                  )}
                  {coupon.offerMinCartValue && (
                    <div className={styles.amountExpireHolder}>
                      <div className={styles.dataHeader}>Min.bag amount</div>
                      <div className={styles.dataInformation}>
                        Rs.
                        {coupon.offerMinCartValue}
                      </div>
                    </div>
                  )}
                </div>
              </div>
            );
          })}
      </div>
    );
  }
}
BankCoupons.propTypes = {
  coupons: PropTypes.arrayOf(
    PropTypes.shape({
      promotionTitle: PropTypes.string,
      promotionDetail: PropTypes.string,
      formattedDate: PropTypes.string,
      amount: PropTypes.string,
      selectItem: PropTypes.func,
      selected: PropTypes.bool
    })
  )
};
