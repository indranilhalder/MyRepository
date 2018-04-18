import React from "react";
import styles from "./EmiPanel.css";
import PropTypes from "prop-types";
import MenuDetails from "../../general/components/MenuDetails.js";
import eWalletIcon from "./img/netBanking.svg";
import NoCostEmi from "./NoCostEmi.js";
import CheckoutEmi from "./CheckoutEmi.js";
import NoCostEmiBankDetails from "./NoCostEmiBankDetails.js";
export default class EmiPanel extends React.Component {
  componentDidMount = () => {
    if (this.props.getEmiEligibility) {
      this.props.getEmiEligibility();
    }
  };
  onBankSelect(val) {
    if (this.props.onBankSelect) {
      this.props.onBankSelect(val);
    }
  }
  onSelectMonth(val) {
    if (this.props.onSelectMonth) {
      this.props.onSelectMonth(val);
    }
  }
  getEmiBankDetails = () => {
    if (this.props.getEmiBankDetails) {
      this.props.getEmiBankDetails();
    }
  };
  getBankAndTenureDetails = () => {
    if (this.props.getBankAndTenureDetails) {
      this.props.getBankAndTenureDetails();
    }
  };
  getEmiTermsAndConditionsForBank = (code, bankName) => {
    if (this.props.getEmiTermsAndConditionsForBank) {
      this.props.getEmiTermsAndConditionsForBank(code, bankName);
    }
  };
  applyNoCostEmi = couponCode => {
    if (this.props.applyNoCostEmi) {
      this.props.applyNoCostEmi(couponCode);
    }
  };

  removeNoCostEmi = couponCode => {
    if (this.props.removeNoCostEmi) {
      this.props.removeNoCostEmi(couponCode);
    }
  };
  getItemBreakUpDetails = couponCode => {
    console.log(couponCode);
    if (this.props.getItemBreakUpDetails) {
      this.props.getItemBreakUpDetails(couponCode);
    }
  };

  render() {
    return (
      <div className={styles.base}>
        <MenuDetails text="EMI" icon={eWalletIcon}>
          {this.props.cart &&
            this.props.cart.emiEligibilityDetails &&
            this.props.cart.emiEligibilityDetails.isNoCostEMIEligible && (
              <div className={styles.subListHolder}>
                <NoCostEmi
                  text="No Cost Emi"
                  getBankAndTenureDetails={() => this.getBankAndTenureDetails()}
                >
                  <NoCostEmiBankDetails
                    onBankSelect={val => this.onBankSelect(val)}
                    onSelectMonth={val => this.onSelectMonth(val)}
                    bankList={
                      this.props.cart &&
                      this.props.cart.bankAndTenureDetails &&
                      this.props.cart.bankAndTenureDetails.bankList
                    }
                    productCount={
                      this.props.cart &&
                      this.props.cart.bankAndTenureDetails &&
                      this.props.cart.bankAndTenureDetails.numEligibleProducts
                    }
                    getEmiTermsAndConditionsForBank={(code, bankName) =>
                      this.getEmiTermsAndConditionsForBank(code, bankName)
                    }
                    applyNoCostEmi={couponCode =>
                      this.applyNoCostEmi(couponCode)
                    }
                    removeNoCostEmi={couponCode =>
                      this.removeNoCostEmi(couponCode)
                    }
                    noCostEmiDetails={this.props.cart.noCostEmiDetails}
                    getItemBreakUpDetails={couponCode =>
                      this.getItemBreakUpDetails(couponCode)
                    }
                  />
                </NoCostEmi>
              </div>
            )}
          <div className={styles.subListHolder}>
            <NoCostEmi
              text="Standard Emi"
              getEmiBankDetails={() => this.getEmiBankDetails()}
              emiList={
                this.props.cart.emiBankDetails &&
                this.props.cart.emiBankDetails.bankList
              }
            >
              {" "}
              <CheckoutEmi {...this.props} />
            </NoCostEmi>
          </div>
        </MenuDetails>
      </div>
    );
  }
}
EmiPanel.propTypes = {
  onBankSelect: PropTypes.func,
  onSelectMonth: PropTypes.func
};
