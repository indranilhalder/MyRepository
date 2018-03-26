import React from "react";
import styles from "./GiftCard.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import TextArea from "../../general/components/TextArea";
import SelectBoxMobile from "../../general/components/SelectBoxMobile.js";
import PdfFooter from "../../pdp/components/PdpFooter.js";

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
    if (this.props.getGiftCardDetails) {
      this.props.getGiftCardDetails();
    }
    if (this.props.createGiftCardDetails) {
      let giftCardDetails = {};
      giftCardDetails.from = "ajkdnf";
      giftCardDetails.quantity = "1";
      giftCardDetails.messageOnCard = "Ksjdbfkjsd";
      giftCardDetails.productID = "MP000000000127263";
      giftCardDetails.priceSelectedByUserPerQuantity = "10000";
      giftCardDetails.receiverEmailID = "cdoshi@tataunistore.com";
      giftCardDetails.mobileNumber = "9769344954";
      this.props.createGiftCardDetails(giftCardDetails);
    }
  }
  selectAmount(val) {
    this.setState({ amountText: val });
  }
  getQuantity(val) {
    if (this.props.onChange) {
      this.props.onChange(val);
    }
  }
  onSave() {
    if (this.props.onSave) {
      this.props.onSave();
    }
  }
  onAddToBag(val) {
    if (this.props.onAddToBag) {
      this.props.onAddToBag(this.state);
    }
  }
  render() {
    // console.log(this.props.giftCards);
    return (
      <div className={styles.base}>
        <div className={styles.giftCardImageHolder}>
          {this.props.giftCards && (
            <div className={styles.giftCradImage}>
              <Image
                image={this.props.giftCards.giftCartImageUrl}
                fit="cover"
              />
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
            <span>{this.state.amountText}</span>
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
              {this.props.giftCards && (
                <div className={styles.amountHolder}>
                  {this.props.giftCards &&
                    this.props.giftCards.amountOptions &&
                    this.props.giftCards.amountOptions.options.map((val, i) => {
                      return (
                        <div
                          className={styles.amountSelect}
                          onClick={() =>
                            this.selectAmount(val.formattedValueNoDecimal)
                          }
                        >
                          {val.formattedValueNoDecimal}
                        </div>
                      );
                    })}
                </div>
              )}
            </div>
            <div className={styles.inputHolder}>
              <div className={styles.labelHeader}>Or</div>
              <Input2
                boxy={true}
                placeholder="Enter Customer Amount"
                value={
                  this.props.amountText
                    ? this.props.amountText
                    : this.state.amountText
                }
                onChange={amountText => this.setState({ amountText })}
                textStyle={{ fontSize: 14 }}
                height={33}
              />
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
              <SelectBoxMobile
                value="1"
                onChange={val => this.getQuantity(val)}
                options={this.props.quantity}
              />
            </div>
          </div>
        </div>
        <div className={styles.footer}>
          <PdfFooter
            onSave={() => this.onSave()}
            onAddToBag={() => this.onAddToBag()}
          />
        </div>
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
  )
};
GiftCard.defaultProps = {};
