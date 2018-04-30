import React from "react";
import Icon from "../../xelpmoc-core/Icon";
import SelectBoxMobile2 from "../../general/components/SelectBoxMobile2";
import Grid from "../../general/components/Grid";
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
      bankCode: ""
    };
  }
  handleSelectForIcon(val) {
    this.setState({ bankCode: val });
    if (this.props.binValidationForNetBank) {
      this.props.binValidationForNetBank(val);
    }
    this.props.onSelectBankForNetBanking(val);
  }
  handleSelect(val) {
    const bankCode = val.value;
    const bankName = val.label;
    this.setState({ bankCode: bankCode, bankName: bankName });
    if (this.props.binValidationForNetBank) {
      this.props.binValidationForNetBank(bankCode);
    }
    this.props.onSelectBankForNetBanking(bankCode);
  }

  render() {
    return (
      <div>
        {this.props.bankList && (
          <Grid limit={1} offset={30} elementWidthMobile={25}>
            {this.props.bankList.find(bank => {
              return (
                bank.bankCode === axisBankCode ||
                bank.bankCode === axisBankCodeDummy
              );
            }) ? (
              <BankSelect
                selectItem={() => this.handleSelectForIcon(axisBankCode)}
                image={axisBankIcon}
                selected={this.state.bankCode === axisBankCode}
              />
            ) : null}
            {this.props.bankList.find(bank => {
              return bank.bankCode === hdfcBankCode;
            }) ? (
              <BankSelect
                selectItem={() => this.handleSelectForIcon(hdfcBankCode)}
                image={hdfcBankIcon}
                selected={this.state.bankCode === hdfcBankCode}
              />
            ) : null}
            {this.props.bankList.find(bank => {
              return bank.bankCode === iciciBankCode;
            }) ? (
              <BankSelect
                selectItem={() => this.handleSelectForIcon(iciciBankCode)}
                image={iciciBankIcon}
                selected={this.state.bankCode === iciciBankCode}
              />
            ) : null}
            {this.props.bankList.find(bank => {
              return bank.bankCode === sbiBankCode;
            }) ? (
              <BankSelect
                selectItem={() => this.handleSelectForIcon(sbiBankCode)}
                image={sbiBankIcon}
                selected={this.state.bankCode === sbiBankCode}
              />
            ) : null}
          </Grid>
        )}
        <div className={styles.bankDropDown}>
          <SelectBoxMobile2
            height={33}
            label={this.state.bankName ? this.state.bankName : "Other Bank"}
            value={this.state.bankCode ? this.state.bankCode : ""}
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
