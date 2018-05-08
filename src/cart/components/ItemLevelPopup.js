import React from "react";
import styles from "./ItemLevelPopup.css";
import LevelBreakupCard from "./LevelBreakupCard.js";
import PropTypes from "prop-types";
import SlideModal from "../../general/components/SlideModal";
export default class ItemLevelPopup extends React.Component {
  render() {
    let emiItemDetails = this.props.emiItemDetails;
    return (
      <SlideModal closeModal={this.props.closeModal}>
        <div className={styles.base}>
          <div className={styles.cardOfferDisplay}>
            <div className={styles.cardName}>{`${emiItemDetails.bankName} for ${
              emiItemDetails.tenure
            } months`}</div>
            <div className={styles.offerText}>{this.props.defaultText}</div>
          </div>
          <div className={styles.levelBreakupHolder}>
            {emiItemDetails &&
              emiItemDetails.itemBreakUpDetailList &&
              emiItemDetails.itemBreakUpDetailList.map((val, i) => {
                return (
                  <LevelBreakupCard
                    key={i}
                    productName={val.productTitle}
                    emiApplication={val.isNoCostEMIEligible}
                    quantity={val.quantity}
                    itemValue={val.lineValue.value}
                    Interest={val.lineBankInterst.value}
                    discount={val.noCostEMILineDiscount.value}
                    totalAmount={val.lineTotalValue.value}
                    emiAmount={val.perMonthEMILineValue.value}
                  />
                );
              })}
          </div>
          <div className={styles.emiInformationHolder}>
            <div className={styles.emiInfoHeader}>Your EMI Information</div>
            <div className={styles.emiPlanTextHolder} />
            <div>
              {`\n\u2022  ${
                this.props.emiItemDetails.noCostEMIDiscountValue.formattedValue
              } has been given as No Cost EMI  discount (Interest applicable on 1 product in your cart)`}
            </div>
            <div>{`\n\u2022  ${
              this.props.emiItemDetails.cardBlockingAmount.formattedValue
            } will be blocked on your card now. It will be converted into EMI in 3-4 working days`}</div>
            <div>
              {` \n\u2022  You will pay ${
                this.props.emiItemDetails.noCostEMIPerMonthPayable
                  .formattedValue
              }  per month for ${
                emiItemDetails.tenure
              } months. Total amount paid to bank will be equal ro the value of products on offer.`}
            </div>
          </div>
        </div>
      </SlideModal>
    );
  }
}
ItemLevelPopup.propTypes = {
  cardName: PropTypes.string,
  timeLimit: PropTypes.string,
  cardData: PropTypes.arrayOf(
    PropTypes.shape({
      productName: PropTypes.string,
      emiApplication: PropTypes.string,
      quantity: PropTypes.number,
      itemValue: PropTypes.string,
      Interest: PropTypes.string,
      discount: PropTypes.string,
      totalAmount: PropTypes.string,
      emiAmount: PropTypes.string
    })
  )
};
ItemLevelPopup.defaultProps = {
  defaultText:
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis quis dapibus sem. Donec id aliquet arcu."
};
