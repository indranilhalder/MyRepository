import React from "react";
import styles from "./EmiPanel.css";
import PropTypes from "prop-types";
import MenuDetails from "../../general/components/MenuDetails.js";
import eWalletIcon from "./img/netBanking.svg";
import NoCostEmi from "./NoCostEmi.js";
import CheckoutEmi from "./CheckoutEmi.js";
import NoCostEmiBankDetails from "./NoCostEmiBankDetails.js";
import { EMI, NO_COST_EMI, STANDARD_DELIVERY } from "../../lib/constants";
const PAYMENT_MODE = "EMI";
export default class EmiPanel extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      currentSelectedEMIType: null
    };
  }

  componentDidMount = () => {
    if (
      this.props.getEmiEligibility &&
      !this.props.cart.emiEligibilityDetails
    ) {
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
    if (this.props.getItemBreakUpDetails) {
      this.props.getItemBreakUpDetails(couponCode);
    }
  };

  binValidation = binNo => {
    if (this.props.binValidation) {
      this.props.binValidation(PAYMENT_MODE, binNo);
    }
  };

  changeNoCostEmiPlan = () => {
    this.props.changeNoCostEmiPlan();
  };
  render() {
    return (
      <div className={styles.base}>
        <MenuDetails
          text={EMI}
          icon={eWalletIcon}
          isOpen={this.props.currentPaymentMode === EMI}
          onOpenMenu={currentPaymentMode =>
            this.props.onChange({ currentPaymentMode })
          }
        >
          {this.props.cart &&
            this.props.cart.emiEligibilityDetails &&
            this.props.cart.emiEligibilityDetails.isNoCostEMIEligible && (
              <div className={styles.subListHolder}>
                <NoCostEmi
                  EMIText={NO_COST_EMI}
                  isOpenSubEMI={
                    this.state.currentSelectedEMIType === NO_COST_EMI
                  }
                  onChangeEMIType={currentSelectedEMIType =>
                    this.setState({ currentSelectedEMIType })
                  }
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
                    isNoCostEmiProceeded={this.props.isNoCostEmiProceeded}
                    binValidation={binNo => this.binValidation(binNo)}
                    softReservationForPayment={cardDetails =>
                      this.softReservationForPayment(cardDetails)
                    }
                    displayToast={this.props.displayToast}
                    changeNoCostEmiPlan={() => this.changeNoCostEmiPlan()}
                  />
                </NoCostEmi>
              </div>
            )}
          <div className={styles.subListHolder}>
            <NoCostEmi
              isOpenSubEMI={
                this.state.currentSelectedEMIType === STANDARD_DELIVERY
              }
              EMIText={STANDARD_DELIVERY}
              onChangeEMIType={currentSelectedEMIType =>
                this.setState({ currentSelectedEMIType })
              }
              getEmiBankDetails={() => this.getEmiBankDetails()}
              emiList={
                this.props.cart.emiBankDetails &&
                this.props.cart.emiBankDetails.bankList
              }
            >
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
