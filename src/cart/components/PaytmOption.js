import React from "react";
import styles from "./PaytmOption.css";
import PropTypes from "prop-types";
import MenuDetails from "../../general/components/MenuDetails.js";
import CheckBox from "../../general/components/CheckBox.js";
import eWalletIcon from "./img/netBanking.svg";
import paytmIcon from "./img/paytm.png";
import Logo from "../../general/components/Logo";
import { E_WALLET } from "../../lib/constants";
export default class PaytmOption extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selected: false
    };
  }
  componentWillReceiveProps(nextProps) {
    if (!nextProps.paymentModeSelected) {
      this.setState({ selected: false });
    }
  }
  handleOnSelect() {
    if (this.state.selected) {
      this.props.binValidationForPaytm(false);
      this.setState({ selected: false });
    } else {
      if (this.props.binValidationForPaytm) {
        this.setState({ selected: true });
        this.props.binValidationForPaytm(true);
      }
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <MenuDetails
          text={E_WALLET}
          isOpen={this.props.currentPaymentMode === E_WALLET}
          onOpenMenu={currentPaymentMode =>
            this.props.onChange({ currentPaymentMode })
          }
          icon={eWalletIcon}
        >
          {this.props.isPtm && (
            <div className={styles.eWalletDetailsHolder}>
              <div
                className={styles.paytmLogoHolder}
                onClick={() => this.handleOnSelect()}
              >
                <div className={styles.checkboxHolder}>
                  <CheckBox selected={this.state.selected} />
                </div>
                <div className={styles.paytmIcon}>
                  <Logo image={paytmIcon} />
                </div>
              </div>
            </div>
          )}
          {this.props.insufficientBalance && (
            <div className={styles.eWalletDetailsHolder}>
              {this.props.balance && (
                <div className={styles.currentBalanceHolder}>
                  <div className={styles.currentBalanceText}>
                    Current wallet balance
                  </div>
                  <div className={styles.currentBalanceAmount}>{`Rs. ${
                    this.props.balance
                  }`}</div>
                </div>
              )}
              <div className={styles.messageTextHolder}>
                <div className={styles.messageHeader}>
                  You have insufficient balance
                </div>
                <div className={styles.messageText}>
                  Please use split payment or try other payment methods
                </div>
              </div>
            </div>
          )}
        </MenuDetails>
      </div>
    );
  }
}
PaytmOption.propTypes = {
  isPtm: PropTypes.bool,
  insufficientBalance: PropTypes.bool,
  balance: PropTypes.string,
  onSelect: PropTypes.func
};
PaytmOption.defaultProps = {
  isPtm: true
};
