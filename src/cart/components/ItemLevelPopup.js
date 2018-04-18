import React from "react";
import styles from "./ItemLevelPopup.css";
import LevelBreakupCard from "./LevelBreakupCard.js";
import PropTypes from "prop-types";
export default class ItemLevelPopup extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.cardOfferDisplay}>
          <div className={styles.cardName}>{`${this.props.cardName} for ${
            this.props.timeLimit
          }`}</div>
          <div className={styles.offerText}>{this.props.defaultText}</div>
        </div>
        <div className={styles.levelBreakupHolder}>
          {this.props.cardData &&
            this.props.cardData.map((val, i) => {
              return (
                <LevelBreakupCard
                  key={i}
                  productName={val.productName}
                  emiApplication={val.emiApplication}
                  quantity={val.quantity}
                  itemValue={val.itemValue}
                  Interest={val.Interest}
                  discount={val.discount}
                  totalAmount={val.totalAmount}
                  emiAmount={val.emiAmount}
                />
              );
            })}
        </div>
        <div className={styles.emiInformationHolder}>
          <div className={styles.emiInfoHeader}>Your EMI Information</div>
          <div className={styles.emiPlanTextHolder}>
            {this.props.emiOffer &&
              this.props.emiOffer.map((val, i) => {
                return <div className={styles.emiPlan}>{val.offerText}</div>;
              })}
          </div>
        </div>
      </div>
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
