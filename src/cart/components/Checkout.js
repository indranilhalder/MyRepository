import React from "react";
import PropTypes from "prop-types";
import styles from "./Checkout.css";
import Button from "../../general/components/Button.js";
import infoIcon from "./img/Info.svg";
import Icon from "../../xelpmoc-core/Icon";
import {
  RUPEE_SYMBOL,
  DEFAULT_PIN_CODE_LOCAL_STORAGE
} from "../../lib/constants.js";
export default class Checkout extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showDetails: this.props.showDetails ? this.props.showDetails : false
    };
  }
  handleClick() {
    if (this.props.onCheckout) {
      this.props.onCheckout();
    }
  }
  handleShowDetail() {
    this.setState({ showDetails: !this.state.showDetails });
  }
  componentWillReceiveProps(nextProps) {
    if (nextProps.showDetails !== this.state.showDetails) {
      this.setState({ showDetails: nextProps.showDetails });
    }
  }
  handleFocusOnPinCode() {
    document.getElementById("searchAndUpdateInput").focus();
  }

  render() {
    let classOffers = styles.informationAnswerHolder;
    if (this.props.offers) {
      classOffers = styles.apply;
    }
    const defaultPinCode =
      localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE) &&
      localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE) !== "undefined"
        ? localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
        : null;
    return (
      <React.Fragment>
        <div className={styles.hiddenBase}>
          <div className={styles.totalPriceButtonHolder}>
            <div className={styles.checkoutButtonHolder}>
              <Button
                disabled={this.props.disabled}
                type="primary"
                backgroundColor="#ff1744"
                height={40}
                label={this.props.label}
                width={120}
                textStyle={{ color: "#FFF", fontSize: 14 }}
                onClick={() => this.handleClick()}
              />
            </div>
            <div className={styles.totalPriceHeading}>Total</div>
            <div className={styles.amountHolder}>
              <div className={styles.amount}>
                {RUPEE_SYMBOL}
                {this.props.amount}
              </div>
              <div className={styles.infoIconHolder}>
                <Icon image={infoIcon} size={22} />
              </div>
            </div>
          </div>
          {this.state.showDetails && (
            <div className={styles.detailsHolder}>
              {this.props.bagTotal && (
                <div className={styles.informationHolder}>
                  <div className={styles.informationQuestionHolder}>
                    Bag total
                  </div>
                  <div className={styles.informationAnswerHolder}>
                    {RUPEE_SYMBOL}
                    {this.props.bagTotal}
                  </div>
                </div>
              )}
              {this.props.totalDiscount && (
                <div className={styles.informationHolder}>
                  <div className={styles.informationQuestionHolder}>
                    Discount
                  </div>
                  <div className={styles.informationAnswerHolder}>
                    {RUPEE_SYMBOL}
                    {this.props.totalDiscount}
                  </div>
                </div>
              )}
              {this.props.tax && (
                <div className={styles.informationHolder}>
                  <div className={styles.informationQuestionHolder}>Taxes</div>
                  <div className={styles.informationAnswerHolder}>
                    {this.props.tax}
                  </div>
                </div>
              )}
              {this.props.delivery && (
                <div className={styles.informationHolder}>
                  <div className={styles.informationQuestionHolder}>
                    Shipping fee
                  </div>
                  <div className={styles.informationAnswerHolder}>
                    {RUPEE_SYMBOL}
                    {this.props.delivery}
                  </div>
                </div>
              )}
              {this.props.offers && (
                <div className={styles.informationHolder}>
                  <div className={styles.informationQuestionHolder}>Offers</div>
                  <div className={classOffers}>
                    {RUPEE_SYMBOL}
                    {this.props.offers}
                  </div>
                </div>
              )}
              {this.props.payable && (
                <div className={styles.informationHolder}>
                  <div className={styles.informationQuestionHolder}>
                    Total payable
                  </div>
                  <div className={styles.informationAnswerHolder}>
                    {RUPEE_SYMBOL}
                    {this.props.payable}
                  </div>
                </div>
              )}
            </div>
          )}
        </div>
        <div className={styles.base}>
          <div className={styles.totalPriceButtonHolder}>
            {defaultPinCode && (
              <div className={styles.checkoutButtonHolder}>
                <Button
                  disabled={this.props.disabled}
                  type="primary"
                  backgroundColor="#ff1744"
                  height={40}
                  label={this.props.label}
                  width={120}
                  textStyle={{ color: "#FFF", fontSize: 14 }}
                  onClick={() => this.handleClick()}
                />
              </div>
            )}
            {!defaultPinCode && (
              <div className={styles.checkoutButtonHolder}>
                <Button
                  type="primary"
                  backgroundColor="#ff1744"
                  height={40}
                  label={this.props.label}
                  width={120}
                  textStyle={{ color: "#FFF", fontSize: 14 }}
                  onClick={() => this.handleFocusOnPinCode()}
                />
              </div>
            )}
            <div className={styles.totalPriceHeading}>Total</div>
            <div className={styles.amountHolder}>
              <div className={styles.amount}>
                {RUPEE_SYMBOL}
                {this.props.amount}
              </div>
              <div
                className={styles.infoIconHolder}
                onClick={() => {
                  this.handleShowDetail();
                }}
              >
                <Icon image={infoIcon} size={22} />
              </div>
            </div>
          </div>
          {this.state.showDetails && (
            <div className={styles.detailsHolder}>
              {this.props.bagTotal && (
                <div className={styles.informationHolder}>
                  <div className={styles.informationQuestionHolder}>
                    Bag total
                  </div>
                  <div className={styles.informationAnswerHolder}>
                    {RUPEE_SYMBOL}
                    {this.props.bagTotal}
                  </div>
                </div>
              )}
              {this.props.totalDiscount && (
                <div className={styles.informationHolder}>
                  <div className={styles.informationQuestionHolder}>
                    Discount
                  </div>
                  <div className={styles.informationAnswerHolder}>
                    {RUPEE_SYMBOL}
                    {this.props.totalDiscount}
                  </div>
                </div>
              )}
              {this.props.discount && (
                <div className={styles.informationHolder}>
                  <div className={styles.informationQuestionHolder}>
                    Discount
                  </div>
                  <div className={styles.informationAnswerHolder}>
                    {RUPEE_SYMBOL}
                    {this.props.discount}
                  </div>
                </div>
              )}
              {this.props.delivery && (
                <div className={styles.informationHolder}>
                  <div className={styles.informationQuestionHolder}>
                    Shipping fee
                  </div>
                  <div className={styles.informationAnswerHolder}>
                    {RUPEE_SYMBOL}
                    {this.props.delivery}
                  </div>
                </div>
              )}

              {this.props.coupons && (
                <div className={styles.informationHolder}>
                  <div className={styles.informationQuestionHolder}>Coupon</div>
                  <div className={classOffers}>
                    {RUPEE_SYMBOL}
                    {this.props.coupons}
                  </div>
                </div>
              )}

              {this.props.noCostEmiEligibility && (
                <div className={styles.informationHolder}>
                  <div className={styles.informationQuestionHolder}>
                    No Cost EMI Discount
                  </div>
                  <div className={classOffers}>
                    {RUPEE_SYMBOL}
                    {this.props.noCostEmiDiscount}
                  </div>
                </div>
              )}
              {this.props.isCliqCashApplied && (
                <div className={styles.informationHolder}>
                  <div className={styles.informationQuestionHolder}>
                    Cliq Cash
                  </div>
                  <div className={classOffers}>
                    {RUPEE_SYMBOL}
                    {this.props.cliqCashPaidAmount}
                  </div>
                </div>
              )}

              {this.props.payable && (
                <div className={styles.informationHolder}>
                  <div className={styles.informationQuestionHolder}>
                    Total payable
                  </div>
                  <div className={styles.informationAnswerHolder}>
                    {RUPEE_SYMBOL}
                    {this.props.payable}
                  </div>
                </div>
              )}
            </div>
          )}
        </div>
      </React.Fragment>
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
  payable: PropTypes.string,
  label: PropTypes.string,
  disabled: PropTypes.bool
};
Checkout.defaultProps = {
  label: "Continue",
  disabled: false
};
