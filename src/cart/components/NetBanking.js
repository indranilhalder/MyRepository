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
import induslandBankIcon from "./img/indusind.svg";
import kotakBankIcon from "./img/kotak.svg";
const axisBankName = "Axis Bank";
const hdfcBankName = "HDFC BANK, LTD.";
const iciciBankName = "ICICI Bank";
const sbiBankName = "State Bank of India";
const kotakBankName = "Kotak Bank";
const induslandBankName = "IndusInd Bank";
const axisBankCodeDummy = "Dummy Bank";
const SHOW_DEFAULT_BANK_LIST = [
  axisBankName,
  hdfcBankName,
  iciciBankName,
  sbiBankName,
  kotakBankName,
  induslandBankName
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
    this.setState({ bankCode: val, bankName: val });
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
      this.props.binValidationForNetBank(bankName);
    }
    this.props.onSelectBankForNetBanking(bankCode);
  }

  render() {
    return (
      <div>
        {this.props.bankList && (
          <Grid limit={1} offset={30} elementWidthMobile={33.33}>
            {this.props.bankList.find(bank => {
              return (
                bank.bankName === axisBankName ||
                bank.bankName === axisBankCodeDummy
              );
            }) ? (
              <BankSelect
                selectItem={() => this.handleSelectForIcon(axisBankName)}
                image={axisBankIcon}
                selected={this.state.bankName === axisBankName}
                name="Axis Bank"
              />
            ) : null}
            {this.props.bankList.find(bank => {
              return bank.bankName === hdfcBankName;
            }) ? (
              <BankSelect
                selectItem={() => this.handleSelectForIcon(hdfcBankName)}
                image={hdfcBankIcon}
                selected={this.state.bankName === hdfcBankName}
                name="HDFC BANK, LTD."
              />
            ) : null}
            {this.props.bankList.find(bank => {
              return bank.bankName === iciciBankName;
            }) ? (
              <BankSelect
                selectItem={() => this.handleSelectForIcon(iciciBankName)}
                image={iciciBankIcon}
                selected={this.state.bankName === iciciBankName}
                name="ICICI Bank"
              />
            ) : null}
            {this.props.bankList.find(bank => {
              return bank.bankName === sbiBankName;
            }) ? (
              <BankSelect
                selectItem={() => this.handleSelectForIcon(sbiBankName)}
                image={sbiBankIcon}
                selected={this.state.bankName === sbiBankName}
                name="State Bank of India"
              />
            ) : null}
            {this.props.bankList.find(bank => {
              return bank.bankName === kotakBankName;
            }) ? (
              <BankSelect
                selectItem={() => this.handleSelectForIcon(kotakBankName)}
                image={kotakBankIcon}
                selected={this.state.bankName === kotakBankName}
                name="Kotak Bank"
              />
            ) : null}
            {this.props.bankList.find(bank => {
              return bank.bankName === induslandBankName;
            }) ? (
              <BankSelect
                selectItem={() => this.handleSelectForIcon(induslandBankName)}
                image={induslandBankIcon}
                selected={this.state.bankName === induslandBankName}
                name="IndusInd Bank"
              />
            ) : null}
          </Grid>
        )}
        <div className={styles.bankDropDown}>
          <SelectBoxMobile2
            height={33}
            placeholder={"Other Bank"}
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
