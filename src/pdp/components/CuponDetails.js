import React from "react";
import styles from "./CuponDetails.css";
import PropTypes from "prop-types";
export default class CuponDetails extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        {this.props.productOfferPromotion &&
          this.props.productOfferPromotion.map((promotion, i) => {
            return (
              <div className={styles.cuponCard} key={i}>
                <div className={styles.headerText}>
                  {promotion.promotionTitle}
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
