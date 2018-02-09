import React from "react";
import styles from "./CuponDetails.css";
import CheckBox from "../../general/components/CheckBox.js";
import PropTypes from "prop-types";
export default class CuponDetails extends React.Component {
  handleClick() {
    if (this.props.applyCupon) {
      this.props.applyCupon();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        {this.props.productOfferPromotion &&
          this.props.productOfferPromotion.map((promotion, i) => {
            return (
              <div
                className={styles.cuponCard}
                key={i}
                onClick={() => this.handleClick()}
              >
                <div className={styles.headerText}>
                  <span>{promotion.promotionTitle}</span>
                  {promotion.itemSelect && (
                    <div className={styles.checkBoxHolder}>
                      <CheckBox selected={true} />
                    </div>
                  )}
                </div>
                <div className={styles.promotionDetailsText}>
                  {promotion.promotionDetail}
                </div>

                <div className={styles.dataHolder}>
                  {promotion.validTill.formattedDate && (
                    <div className={styles.amountExpireHolder}>
                      <div className={styles.dataHeader}>Valid till</div>
                      <div className={styles.dataInformation}>
                        {promotion.validTill.formattedDate}
                      </div>
                    </div>
                  )}
                  {promotion.validTill.amount && (
                    <div className={styles.amountExpireHolder}>
                      <div className={styles.dataHeader}>Min.bag amount</div>
                      <div className={styles.dataInformation}>
                        Rs.
                        {promotion.validTill.amount}
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
CuponDetails.propTypes = {
  productOfferPromotion: PropTypes.arrayOf(
    PropTypes.shape({
      promotionTitle: PropTypes.string,
      promotionDetail: PropTypes.string,
      formattedDate: PropTypes.string,
      amount: PropTypes.string,
      itemSelect: PropTypes.bool,
      applyCupon: PropTypes.func
    })
  )
};
