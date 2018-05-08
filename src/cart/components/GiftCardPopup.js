import React from "react";
import styles from "./GiftCardPopup.css";
import PropTypes from "prop-types";
import Icon from "../../xelpmoc-core/Icon";
import Button from "../../general/components/Button.js";
import Input2 from "../../general/components/Input2.js";
import giftImageURL from "../../general/components/img/Gift.svg";
export default class GiftCardPopup extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      cardNumber: this.props.voucherNumber ? this.props.voucherNumber : "",
      pinNumber: this.props.voucherPin ? this.props.voucherPin : ""
    };
  }
  addGiftCard() {
    if (this.props.addGiftCard) {
      this.props.addGiftCard(this.state);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.giftCardHeading}>
          <div className={styles.iconHolder}>
            <Icon image={giftImageURL} size={30} />
          </div>
          Gift Card Details
        </div>
        <div className={styles.inputHolder}>
          <Input2
            boxy={true}
            placeholder="Voucher number"
            value={
              this.props.voucherNumber
                ? this.props.voucherNumber
                : this.state.voucherNumber
            }
            onChange={cardNumber => this.setState({ cardNumber })}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.inputHolder}>
          <Input2
            boxy={true}
            placeholder="Voucher pin"
            value={
              this.props.voucherPin
                ? this.props.voucherPin
                : this.state.voucherPin
            }
            onChange={pinNumber => this.setState({ pinNumber })}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.termsAndConditionCheck}>
          Please read the Terms & Conditions before making your purchase
        </div>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <Button
              type="primary"
              backgroundColor="#ff1744"
              height={36}
              label="Add Gift Card"
              width={211}
              textStyle={{ color: "#FFF", fontSize: 14 }}
              onClick={() => this.addGiftCard()}
            />
          </div>
        </div>
      </div>
    );
  }
}
GiftCardPopup.propTypes = {
  voucherNumber: PropTypes.string,
  voucherPin: PropTypes.string,
  addGiftCard: PropTypes.func
};
