import React from "react";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import Logo from "../../general/components/Logo";
import Input2 from "../../general/components/Input2.js";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import Button from "../../general/components/Button.js";
import cliqCashIcon from "./img/cliqcash.png";
import styles from "./CliqAndCash.css";
export default class CliqAndCash extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      cardNumber: this.props.cardNumber ? this.props.cardNumber : "",
      pinNumber: this.props.pinNumber ? this.props.cardNumber : ""
    };
  }

  componentDidMount() {
    if (this.props.getCliqCashDetails) {
      this.props.getCliqCashDetails();
    }
  }
  gitCard() {
    if (this.props.gitCard) {
      this.props.gitCard();
    }
  }
  addBalance(val) {
    if (this.props.addBalance) {
      this.props.addBalance(this.state);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.logoHolder}>
          <Logo image={cliqCashIcon} />
        </div>
        <div className={styles.cliqCashBalanceHolder}>
          <div className={styles.balance}>{`Rs. ${this.props.balance}`}</div>
          <div className={styles.expiredBalanceText}>{`Balance as of ${
            this.props.date
          } ${this.props.time} Hrs`}</div>
          <div className={styles.informationText}>
            Once you validate your gift card, the value will automatically be
            added to your CLiQ Cash
          </div>
        </div>
        <div className={styles.formHolder}>
          <div className={styles.addBlanceHeader}>
            Add Balance from Gift Card
          </div>
          <div className={styles.inputBoxHolder}>
            <div className={styles.labelHeader}>Card Number</div>
            <div className={styles.inputTextHolder}>
              <Input2
                boxy={true}
                type="number"
                placeholder="Enter 16 digit card number"
                value={
                  this.props.cardNumber
                    ? this.props.cardNumber
                    : this.state.cardNumber
                }
                onChange={cardNumber => this.setState({ cardNumber })}
                textStyle={{ fontSize: 14 }}
                height={33}
              />
            </div>
          </div>
          <div className={styles.inputBoxHolder}>
            <div className={styles.labelHeader}>Card Pin</div>
            <div className={styles.inputTextHolder}>
              <Input2
                boxy={true}
                type="number"
                placeholder="Enter 6 digit number"
                value={
                  this.props.pinNumber
                    ? this.props.pinNumber
                    : this.state.pinNumber
                }
                onChange={pinNumber => this.setState({ pinNumber })}
                textStyle={{ fontSize: 14 }}
                height={33}
              />
            </div>
          </div>
        </div>
        <div className={styles.buttonHolder}>
          {this.props.isGiftCard && (
            <div className={styles.giftCardButtonHolder}>
              <UnderLinedButton
                size="14px"
                fontFamily="regular"
                color="#000000"
                label="Buy new Gift Card"
                onClick={() => this.gitCard()}
              />
            </div>
          )}

          <div className={styles.addBalanceHolder}>
            <Button
              type="primary"
              backgroundColor="#ff1744"
              height={40}
              label="Add Balance"
              width={120}
              textStyle={{ color: "#FFF", fontSize: 14 }}
              onClick={() => this.addBalance()}
            />
          </div>
        </div>
      </div>
    );
  }
}
CliqAndCash.propTypes = {
  balance: PropTypes.string,
  date: PropTypes.string,
  time: PropTypes.string,
  cardNumber: PropTypes.number,
  pinNumber: PropTypes.number,
  isGiftCard: PropTypes.bool,
  gitCard: PropTypes.func,
  addBalance: PropTypes.func
};
CliqAndCash.defaultProps = {
  isGiftCard: true
};
