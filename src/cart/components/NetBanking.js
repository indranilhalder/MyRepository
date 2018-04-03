import React from "react";
import GridSelect from "../../general/components/GridSelect";
import BankSelect from "./BankSelect";
import styles from "./NetBanking.css";
import Button from "../../general/components/Button";
import PropTypes from "prop-types";
export default class NetBanking extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      bankName: ""
    };
  }
  handleSelect(val) {
    this.setState({ bankName: val });
    if (this.props.binValidationForNetBank) {
      this.props.binValidationForNetBank(val);
    }
  }

  payBill = () => {
    if (this.props.isFromGiftCard) {
      this.props.createJusPayOrderForGiftCardNetBanking(this.state.bankName);
    } else {
      this.props.softReservationPaymentForNetBanking(this.state.bankName);
    }
  };
  render() {
    return (
      <div>
        <GridSelect
          limit={1}
          offset={30}
          elementWidthMobile={33.3333}
          onSelect={val => this.handleSelect(val)}
          selected={this.props.selected}
        >
          {this.props.bankList &&
            this.props.bankList.map((val, i) => {
              return (
                <BankSelect image={val.image} value={val.bankCode} key={i} />
              );
            })}
        </GridSelect>
        <div className={styles.cardFooterText}>
          <div className={styles.buttonHolder}>
            <Button
              type="primary"
              color="#fff"
              label="Pay now"
              width={120}
              onClick={() => this.payBill()}
            />
          </div>
        </div>
      </div>
    );
  }
}
NetBanking.propTypes = {
  bankList: PropTypes.arrayOf(
    PropTypes.shape({
      bankCode: PropTypes.string,
      bankName: PropTypes.string,
      isAvailable: PropTypes.string,
      priority: PropTypes.string
    })
  ),
  onSelect: PropTypes.func
};
