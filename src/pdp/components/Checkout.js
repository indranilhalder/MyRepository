import React from "react";
import PropTypes from "prop-types";
import styles from "./Checkout.css";
import Button from "../../general/components/Button.js";
import infoIcon from "./img/Info.svg";
import { Icon } from "xelpmoc-core";
export default class Checkout extends React.Component {
  handleClick() {
    if (this.props.onCheckout) {
      this.props.onCheckout();
    }
  }
  render() {
    let classOffers = styles.informationAnswerHolder;
    if (this.props.offers) {
      classOffers = styles.apply;
    }
    return (
      <div className={styles.base}>
        <div className={styles.totalPriceButtonHolder}>
          <div className={styles.checkoutButtonHolder}>
            <Button
              borderRadius={25}
              backgroundColor="#ff1744"
              height={38}
              label="Checkout"
              width={121}
              textStyle={{ color: "#FFF", fontSize: 14 }}
              onClick={() => this.handleClick()}
            />
          </div>
          <div className={styles.totalPriceHeading}>Total</div>
          <div className={styles.amountHolder}>
            <div className={styles.amount}>Rs. {this.props.amount}</div>
            <div className={styles.infoIconholder}>
              <Icon image={infoIcon} size={22} />
            </div>
          </div>
        </div>
        <div className={styles.detailsHolder}>
          {this.props.bagTotal && (
            <div className={styles.informationHolder}>
              <div className={styles.informationQuestionHolder}>Bag Total</div>
              <div className={styles.informationAnswerHolder}>
                Rs.
                {this.props.bagTotal}
              </div>
            </div>
          )}
          {this.props.tax && (
            <div className={styles.informationHolder}>
              <div className={styles.informationQuestionHolder}>Tax</div>
              <div className={styles.informationAnswerHolder}>
                {this.props.tax}
              </div>
            </div>
          )}
          {this.props.delivery && (
            <div className={styles.informationHolder}>
              <div className={styles.informationQuestionHolder}>Delivery</div>
              <div className={styles.informationAnswerHolder}>
                {this.props.delivery}
              </div>
            </div>
          )}
          {this.props.offers && (
            <div className={styles.informationHolder}>
              <div className={styles.informationQuestionHolder}>Offers</div>
              <div className={classOffers}>{this.props.offers}</div>
            </div>
          )}
          {this.props.payable && (
            <div className={styles.informationHolder}>
              <div className={styles.informationQuestionHolder}>
                Total Payable
              </div>
              <div className={styles.informationAnswerHolder}>
                Rs.
                {this.props.payable}
              </div>
            </div>
          )}
        </div>
      </div>
    );
  }
}
Checkout.propTypes = {
  amount: PropTypes.string,
  bagTotal: PropTypes.string,
  tax: PropTypes.string,
  delivery: PropTypes.string,
  onCheckout: PropTypes.func,
  offers: PropTypes.string,
  payable: PropTypes.string
};
