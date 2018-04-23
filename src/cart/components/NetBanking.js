import React from "react";
import Icon from "../../xelpmoc-core/Icon";
import SelectBoxMobile2 from "../../general/components/SelectBoxMobile2";
import GridSelect from "../../general/components/GridSelect";
import BankSelect from "./BankSelect";
import styles from "./NetBanking.css";
import Button from "../../general/components/Button";
import PropTypes from "prop-types";
import axisBankIcon from "./img/pwa_NB_DUMMY.svg";
import hdfcBankIcon from "./img/pwa_NB_HDFC.svg";
import iciciBankIcon from "./img/pwa_NB_ICICI.svg";
import sbiBankIcon from "./img/pwa_NB_SBI.svg";

const axisBankCode = "NB_AXIS";
const hdfcBankCode = "NB_HDFC";
const iciciBankCode = "NB_ICICI";
const sbiBankCode = "NB_SBI";
const axisBankCodeDummy = "NB_DUMMY";
const SHOW_DEFAULT_BANK_LIST = [
  axisBankCode,
  hdfcBankCode,
  iciciBankCode,
  sbiBankCode
];
export default class NetBanking extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      bankName: "",
      bank: ""
    };
  }
  handleSelect(val) {
    const code = val.value;
    const label = val.label;
    this.setState({ bankName: code, bank: label });
    if (this.props.binValidationForNetBank) {
      this.props.binValidationForNetBank(code);
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
        {this.props.bankList && (
          <GridSelect
            limit={1}
            offset={30}
            elementWidthMobile={25}
            onSelect={val => this.handleSelect(val)}
            selected={this.props.selected}
          >
            {this.props.bankList.find(bank => {
              return (
                bank.bankCode === axisBankCode ||
                bank.bankCode === axisBankCodeDummy
              );
            }) ? (
              <Icon image={axisBankIcon} size={60} value={axisBankCode} />
            ) : null}
            {this.props.bankList.find(bank => {
              return bank.bankCode === hdfcBankCode;
            }) ? (
              <Icon image={hdfcBankIcon} size={60} value={hdfcBankCode} />
            ) : null}
            {this.props.bankList.find(bank => {
              return bank.bankCode === iciciBankCode;
            }) ? (
              <Icon image={iciciBankIcon} size={60} value={iciciBankCode} />
            ) : null}
            {this.props.bankList.find(bank => {
              return bank.bankCode === sbiBankCode;
            }) ? (
              <Icon image={sbiBankIcon} size={60} value={sbiBankCode} />
            ) : null}
          </GridSelect>
        )}
        <div className={styles.bankDropDown}>
          <SelectBoxMobile2
            height={33}
            label={this.state.bank ? this.state.bank : "Other Bank"}
            options={
              this.props.bankList &&
              this.props.bankList
                .filter(bank => !SHOW_DEFAULT_BANK_LIST.includes(bank.bankCode))
                .map((val, i) => {
                  return { value: val.bankCode, label: val.bankName };
                })
            }
            onChange={val => this.handleSelect(val)}
          />
        </div>
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
