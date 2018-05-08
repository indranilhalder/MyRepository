import React from "react";
import styles from "./EmiPanel.css";
import PropTypes from "prop-types";
import MenuDetails from "../../general/components/MenuDetails.js";
import eWalletIcon from "./img/netBanking.svg";
import NoCostEmi from "./NoCostEmi.js";
import CheckoutEmi from "./CheckoutEmi.js";
import NoCostEmiBankDetails from "./NoCostEmiBankDetails.js";
import { EMI, NO_COST_EMI, STANDARD_EMI } from "../../lib/constants";
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
  applyNoCostEmi = (couponCode, bankName) => {
    if (this.props.applyNoCostEmi) {
      this.props.applyNoCostEmi(couponCode, bankName);
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

  onChangeCardDetail = val => {
    this.props.onChangeCardDetail(val);
  };

  componentWillReceiveProps(nextProps) {
    if (
      nextProps.currentPaymentMode !== EMI &&
      this.state.currentSelectedEMIType !== null
    ) {
      this.setState({ currentSelectedEMIType: null });
    }
  }
  onChangeEMIType(currentSelectedEMIType) {
    this.props.changeSubEmiOption(currentSelectedEMIType);
    this.setState({ currentSelectedEMIType });
  }
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
                    this.onChangeEMIType(currentSelectedEMIType)
                  }
                  getBankAndTenureDetails={() => this.getBankAndTenureDetails()}
                  onChangeCardDetail={val => this.onChangeCardDetail(val)}
                >
                  <NoCostEmiBankDetails
                    selectedEMIType={this.state.currentSelectedEMIType}
                    onBankSelect={val => this.onBankSelect(val)}
                    onSelectMonth={val => this.onSelectMonth(val)}
                    bankList={
                      this.props.cart &&
                      this.props.cart.bankAndTenureDetails &&
                      this.props.cart.bankAndTenureDetails.bankList
                    }
                    noCostEmiProductCount={
                      this.props.cart &&
                      this.props.cart.bankAndTenureDetails &&
                      this.props.cart.bankAndTenureDetails.numEligibleProducts
                    }
                    totalProductCount={this.props.totalProductCount}
                    getEmiTermsAndConditionsForBank={(code, bankName) =>
                      this.getEmiTermsAndConditionsForBank(code, bankName)
                    }
                    applyNoCostEmi={(couponCode, bankName) =>
                      this.applyNoCostEmi(couponCode, bankName)
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
                    onChangeCardDetail={val => this.onChangeCardDetail(val)}
                  />
                </NoCostEmi>
              </div>
            )}
          <div className={styles.subListHolder}>
            <NoCostEmi
              isOpenSubEMI={this.state.currentSelectedEMIType === STANDARD_EMI}
              EMIText={STANDARD_EMI}
              onChangeEMIType={currentSelectedEMIType =>
                this.onChangeEMIType(currentSelectedEMIType)
              }
              getEmiBankDetails={() => this.getEmiBankDetails()}
              emiList={
                this.props.cart.emiBankDetails &&
                this.props.cart.emiBankDetails.bankList
              }
              onChangeCardDetail={val => this.onChangeCardDetail(val)}
            >
              <CheckoutEmi
                {...this.props}
                selectedEMIType={this.state.currentSelectedEMIType}
              />
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
