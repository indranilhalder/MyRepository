import React from "react";
import styles from "./BankDetails.css";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import OrderReturn from "../../account/components/OrderReturn";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
export default class BankDetails extends React.Component {
  onChange(val) {
    if (this.props.onChange) {
      this.props.onChange(val);
    }
  }

  render() {
    return (
      <div className={styles.base}>
        <div className={styles.holder}>
          <div className={styles.header}>Bank Details</div>
          <div className={styles.container}>
            <Input2
              placeholder={this.props.accountNumber}
              boxy={true}
              textStyle={{ fontSize: 14 }}
              height={33}
              onChange={accountNumber => this.onChange({ accountNumber })}
            />
          </div>
          <div className={styles.container}>
            <Input2
              placeholder={this.props.reEnterAccountNumber}
              boxy={true}
              textStyle={{ fontSize: 14 }}
              height={33}
              onChange={reEnterAccountNumber =>
                this.onChange({ reEnterAccountNumber })
              }
            />
          </div>
          <div className={styles.container}>
            <Input2
              placeholder={this.props.userName}
              boxy={true}
              textStyle={{ fontSize: 14 }}
              height={33}
              onChange={userName => this.onChange({ userName })}
            />
          </div>
          <div className={styles.container}>
            <SelectBoxMobile
              value={this.props.mode}
              arrowColour="black"
              height={33}
              onChange={mode => this.onChange({ mode })}
            />
          </div>
          <div className={styles.container}>
            <Input2
              placeholder={this.props.bankName}
              boxy={true}
              textStyle={{ fontSize: 14 }}
              height={33}
              onChange={bankName => this.onChange({ bankName })}
            />
          </div>
          <div className={styles.container}>
            <Input2
              placeholder={this.props.code}
              boxy={true}
              textStyle={{ fontSize: 14 }}
              height={33}
              onChange={code => this.onChange({ code })}
            />
          </div>
        </div>
      </div>
    );
  }
}
BankDetails.propTypes = {};
BankDetails.defaultProps = {
  accountNumber: "Account number",
  reEnterAccountNumber: "Re-enter account number",
  userName: "Account holder name",
  mode: "Refund mode",
  bankName: "Bank name",
  code: "IFSC Code"
};
