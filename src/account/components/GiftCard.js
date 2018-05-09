import React from "react";
import styles from "./GiftCard.css";
import Image from "../../xelpmoc-core/Image.js";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import TextArea from "../../general/components/TextArea";
import FooterButton from "../../general/components/FooterButton.js";
import { Redirect } from "react-router-dom";

import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  LOGIN_PATH,
  CHECKOUT_ROUTER,
  GIFT_CARD
} from "../../lib/constants";
import { SUCCESS } from "../../lib/constants.js";
import * as Cookie from "../../lib/Cookie";
const PRODUCT_ID = "MP000000000127263";
const QUANTITY = "1";
const MOBILE_NUMBER = "999999999";
const MINIMUM_PRICE = 15;
const MAXIMUM_PRICE = 10000;
export default class GiftCard extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      email: this.props.email ? this.props.email : "",
      senderName: this.props.senderName ? this.props.senderName : "",
      message: this.props.message ? this.props.message : "",
      amountText: this.props.amountText ? this.props.amountText : ""
    };
  }
  componentDidMount() {
    this.props.setHeaderText(GIFT_CARD);
    if (this.props.getGiftCardDetails) {
      this.props.getGiftCardDetails();
    }
  }
  componentDidUpdate() {
    this.props.setHeaderText(GIFT_CARD);
  }
  componentWillReceiveProps(nextProps) {
    if (nextProps.giftCardDetailsStatus === SUCCESS) {
      this.props.history.push({
        pathname: CHECKOUT_ROUTER,
        state: {
          isFromGiftCard: true,
          egvCartGuid: nextProps.giftCardDetails.egvCartGuid,
          amount: this.state.amountText
        }
      });
    }
  }
  componentWillMount() {
    if (this.props.clearGiftCardStatus) {
      this.props.clearGiftCardStatus();
    }
  }

  selectAmount(val, amount) {
    this.setState({ amountText: amount });
  }

  onSubmitDetails() {
    if (this.props.createGiftCardDetails) {
      const EMAIL_REGULAR_EXPRESSION = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
      if (this.props.createGiftCardDetails) {
        const giftCardDetails = {};
        giftCardDetails.from = this.state.senderName;
        giftCardDetails.quantity = QUANTITY;
        giftCardDetails.messageOnCard = this.state.message;
        giftCardDetails.productID = PRODUCT_ID;
        giftCardDetails.priceSelectedByUserPerQuantity = this.state.amountText;
        giftCardDetails.receiverEmailID = this.state.email;
        giftCardDetails.mobileNumber = MOBILE_NUMBER;
        if (!this.state.amountText) {
          this.props.displayToast("Please select the amount");
          return false;
        }
        if (
          !(
            this.state.amountText <= MAXIMUM_PRICE &&
            this.state.amountText >= MINIMUM_PRICE
          )
        ) {
          this.props.displayToast(
            `Amount Should be less then ${MAXIMUM_PRICE} and greater than ${MINIMUM_PRICE} `
          );
          return false;
        }

        if (!this.state.email) {
          this.props.displayToast("Please fill recipient e-mail address");
          return false;
        }
        if (!EMAIL_REGULAR_EXPRESSION.test(this.state.email)) {
          this.props.displayToast("Please fill valid  e-mail address");
          return false;
        }
        if (!this.state.senderName) {
          this.props.displayToast("Please enter sender name");
          return false;
        } else {
          this.props.createGiftCardDetails(giftCardDetails);
        }
      }
    }
  }
  navigateToLogin() {
    return <Redirect to={LOGIN_PATH} />;
  }
  render() {
    if (this.props.loadingForGiftCardDetails) {
      this.props.showSecondaryLoader();
    } else {
      this.props.hideSecondaryLoader();
    }

    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (!userDetails || !customerCookie) {
      return this.navigateToLogin();
    }
    const giftCards = this.props.giftCardsDetails;
    return (
      <div className={styles.base}>
        <div className={styles.giftCardImageHolder}>
          {giftCards && (
            <div className={styles.giftCradImage}>
              <Image image={giftCards.giftCartImageUrl} fit="cover" />
            </div>
          )}
        </div>
        <div className={styles.giftCardDataHolder}>
          <div className={styles.displayMessageHolder}>
            {this.state.message === "" && (
              <span>Your message appears here</span>
            )}
            <span>{this.state.message}</span>
          </div>
          <div className={styles.displayAmountHolder}>
            {this.state.amountText === "" && (
              <span>Rs. 0 (Please select the amount from below)</span>
            )}
            {this.state.amountText !== "" && (
              <span className={styles.amountSign}>{this.state.amountText}</span>
            )}
          </div>
          <div className={styles.giftCardTextHolder}>
            <div className={styles.infoHeder}>Gift Card</div>
            <div className={styles.infoText}>
              Enter details for your gift card
            </div>
          </div>
          <div className={styles.formCard}>
            <div className={styles.formHeader}>1. Amount</div>
            <div className={styles.amountCardHolder}>
              <div className={styles.labelHeader}>
                Select Amount from below{" "}
              </div>
              <div className={styles.amountHolder}>
                {giftCards &&
                  giftCards.amountOptions &&
                  giftCards.amountOptions.options.map((val, i) => {
                    return (
                      <div
                        className={styles.amountSelect}
                        onClick={() =>
                          this.selectAmount(
                            val.formattedValueNoDecimal,
                            val.value
                          )
                        }
                      >
                        {val.formattedValueNoDecimal}
                      </div>
                    );
                  })}
              </div>
            </div>
            <div className={styles.inputHolder}>
              <div className={styles.labelHeader}>Or</div>
              <div className={styles.enterAmountHolder}>
                {this.state.amountText !== "" && (
                  <div className={styles.rupyLabel} />
                )}
                <Input2
                  boxy={true}
                  placeholder="Enter Customer Amount"
                  value={
                    this.props.amountText
                      ? this.props.amountText
                      : this.state.amountText
                  }
                  onChange={amountText =>
                    this.setState({ amountText: amountText })
                  }
                  onlyNumber={true}
                  textStyle={{ fontSize: 14 }}
                  height={33}
                  leftChildSize={this.state.amountText !== "" ? 33 : 10}
                />
              </div>
            </div>
          </div>
          <div className={styles.formCard}>
            <div className={styles.formHeader}>2. Sender/ Reciever details</div>
            <div className={styles.inputHolder}>
              <div className={styles.labelHeader}>To</div>
              <Input2
                boxy={true}
                placeholder="Enter recipient e-mail address"
                value={this.props.email ? this.props.email : this.state.email}
                onChange={email => this.setState({ email })}
                textStyle={{ fontSize: 14 }}
                height={33}
              />
            </div>
            <div className={styles.amountHolder}>
              <div className={styles.labelHeader}>From</div>
              <Input2
                boxy={true}
                placeholder="Sender Name"
                value={
                  this.props.senderName
                    ? this.props.senderName
                    : this.state.senderName
                }
                onChange={senderName => this.setState({ senderName })}
                textStyle={{ fontSize: 14 }}
                height={33}
              />
            </div>
          </div>
          <div className={styles.formCard}>
            <div className={styles.formHeader}>3. Message & Quantity</div>
            <div className={styles.inputHolder}>
              <div className={styles.labelHeader}>Message</div>
              <TextArea
                boxy={true}
                value={
                  this.props.message ? this.props.message : this.state.message
                }
                placeholder="Type the message that you want to appear on the card"
                onChange={message => this.setState({ message })}
                height={121}
              />
            </div>
            <div className={styles.selectHolder}>
              <div className={styles.labelHeader}>Quantity</div>
              <div className={styles.quantityValue}>1</div>
            </div>
          </div>
        </div>
        <div className={styles.textHolder}>
          <div className={styles.textHeader}>QwikCilver</div>
          <div className={styles.text}>
            Sold by QwikCilver Solutions pvt ltd
          </div>
        </div>
        {giftCards &&
          giftCards.isWalletCreated &&
          giftCards.isWalletOtpVerified && (
            <div className={styles.buttonHolder}>
              <FooterButton
                backgroundColor="#ff1744"
                onClick={() => this.onSubmitDetails()}
                label="Buy Now"
                labelStyle={{
                  color: "#fff",
                  fontSize: 14,
                  fontFamily: "semibold"
                }}
              />
            </div>
          )}
      </div>
    );
  }
}

GiftCard.propTypes = {
  giftCardImage: PropTypes.obj,
  email: PropTypes.string,
  senderName: PropTypes.string,
  message: PropTypes.string,
  amountText: PropTypes.string,
  quantity: PropTypes.arrayOf(
    PropTypes.shape({
      value: PropTypes.number
    })
  ),
  getGiftCardDetails: PropTypes.func,
  createGiftCardDetails: PropTypes.func
};
