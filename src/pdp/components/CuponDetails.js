import React from "react";
import styles from "./CuponDetails.css";
import CheckBox from "../../general/components/CheckBox.js";
import PropTypes from "prop-types";
const COUPON_TYPE = "COUPON";
export default class CuponDetails extends React.Component {
  handleClick(val) {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div
          className={styles.cuponCard}
          onClick={val => this.handleClick(val)}
        >
          {this.props.couponType === COUPON_TYPE && (
            <div className={styles.headerText}>
              <span>{this.props.promotionTitle}</span>
              {this.props.selectItem && (
                <div className={styles.checkBoxHolder}>
                  <CheckBox selected={this.props.selected} />
                </div>
              )}
            </div>
          )}
          <div
            className={styles.promotionDetailsText}
            dangerouslySetInnerHTML={
              this.props.promotionDetail && {
                __html: this.props.promotionDetail
                  .replace("<p>", "")
                  .replace("</p>", "")
              }
            }
          />

          <div className={styles.dataHolder}>
            {this.props.formattedDate && (
              <div className={styles.amountExpireHolder}>
                <div className={styles.dataHeader}>Valid till</div>
                <div className={styles.dataInformation}>
                  {this.props.formattedDate}
                </div>
              </div>
            )}
            {this.props.amount && (
              <div className={styles.amountExpireHolder}>
                <div className={styles.dataHeader}>Min.bag amount</div>
                <div className={styles.dataInformation}>
                  Rs.
                  {this.props.amount}
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    );
  }
}
CuponDetails.propTypes = {
  productOfferPromotion: PropTypes.arrayOf(
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
