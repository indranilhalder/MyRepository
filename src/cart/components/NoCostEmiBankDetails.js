import React from "react";
import styles from "./NoCostEmiBankDetails.css";
import CheckBox from "../../general/components/CheckBox.js";
import BankSelect from "./BankSelect";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
import EmiDisplay from "./EmiDisplay";
import CreditCardForm from "./CreditCardForm";

export default class NoCostEmiBankDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedBankIndex: null,
      selectedMonth: null,
      showAll: false,
      selectedBankName: null,
      selectedBankCode: null,
      selectedCouponCode: null,
      selectedTenure: null
    };
  }
  selectOtherBank(val) {
    this.setState({ selectedBankIndex: null, bankName: val });
    if (this.props.selectOtherBank) {
      this.props.selectOtherBank(val);
    }
  }
  itemBreakup() {
    if (this.props.getItemBreakUpDetails) {
      this.props.getItemBreakUpDetails(this.state.selectedCouponCode);
    }
  }
  handleSelect(index) {
    if (this.state.selectedBankIndex === index) {
      this.setState({
        selectedBankIndex: null,
        selectedBankName: null,
        selectedBankCode: null
      });
    } else {
      this.setState({
        selectedBankIndex: index,
        selectedMonth: null,
        selectedBankName: this.props.bankList[index].bankName,
        selectedBankCode: this.props.bankList[index].code,
        bankName: null
      });
    }
  }
  termsAndCondition() {
    if (this.props.getEmiTermsAndConditionsForBank) {
      this.props.getEmiTermsAndConditionsForBank(
        this.state.selectedBankCode,
        this.state.selectedBankName
      );
    }
  }
  binValidation = (paymentMode, binNo) => {
    if (this.props.binValidation) {
      this.props.binValidation(paymentMode, binNo);
    }
  };

  softReservationForPayment = cardDetails => {
    if (this.props.softReservationForPayment) {
      this.props.softReservationForPayment(cardDetails);
    }
  };

  onSelectMonth(index, val) {
    if (this.state.selectedMonth === index) {
      this.setState({
        selectedMonth: null,
        selectedCouponCode: null,
        selectedTenure: null
      });
      this.props.removeNoCostEmi(val.emicouponCode);
    } else {
      if (val && this.props.applyNoCostEmi) {
        this.setState({
          selectedMonth: index,
          selectedCouponCode: val.emicouponCode,
          selectedTenure: val.tenure
        });
        this.props.applyNoCostEmi(val.emicouponCode);
      }
    }
  }
  renderMonthsPlan() {
    let noCostEmiDetails = this.props.noCostEmiDetails.cartAmount;
    return (
      <div className={styles.monthsPlanDataHolder}>
        <div className={styles.amountPlaneForMonth}>
          {noCostEmiDetails &&
            noCostEmiDetails.noCostEMIOrderValue &&
            noCostEmiDetails.noCostEMIOrderValue.value && (
              <div className={styles.amountData}>
                <div className={styles.amountLabel}>Order Value</div>
                <div className={styles.amount}>{`Rs.${Math.round(
                  noCostEmiDetails.noCostEMIOrderValue.value * 100
                ) / 100}`}</div>
              </div>
            )}
          {noCostEmiDetails &&
            noCostEmiDetails.noCostEMIInterestValue &&
            noCostEmiDetails.noCostEMIInterestValue.value && (
              <div className={styles.amountData}>
                <div className={styles.amountLabel}>
                  Interest (charged by bank)
                </div>
                <div className={styles.amount}>{`Rs. ${Math.round(
                  noCostEmiDetails.noCostEMIInterestValue.value * 100
                ) / 100}`}</div>
              </div>
            )}
          {noCostEmiDetails &&
            noCostEmiDetails.noCostEMIDiscountValue &&
            noCostEmiDetails.noCostEMIDiscountValue.value && (
              <div className={styles.discount}>
                <div className={styles.amountLabel}>No Cost EMI Discount</div>
                <div className={styles.amount}>{`-Rs. ${Math.round(
                  noCostEmiDetails.noCostEMIDiscountValue.value * 100
                ) / 100}`}</div>
              </div>
            )}
        </div>
        <div className={styles.totalAmountDisplay}>
          {noCostEmiDetails &&
            noCostEmiDetails.noCostEMITotalPayable &&
            noCostEmiDetails.noCostEMITotalPayable.value && (
              <div className={styles.totalAmountLabel}>
                <div className={styles.amountLabel}>Total Amount Payable</div>
                <div className={styles.amount}>{`Rs. ${Math.round(
                  noCostEmiDetails.noCostEMITotalPayable.value * 100
                ) / 100}`}</div>
              </div>
            )}
          {noCostEmiDetails &&
            noCostEmiDetails.noCostEMIPerMonthPayable &&
            noCostEmiDetails.noCostEMIPerMonthPayable.value && (
              <div className={styles.totalAmountLabel}>
                <div className={styles.amountLabel}>EMI p.m</div>
                <div className={styles.amount}>{`Rs. ${Math.round(
                  noCostEmiDetails.noCostEMIPerMonthPayable.value * 100
                ) / 100}`}</div>
              </div>
            )}
        </div>

        {noCostEmiDetails &&
          noCostEmiDetails.noCostEMIPerMonthPayable &&
          noCostEmiDetails.noCostEMIPerMonthPayable.value && (
            <div className={styles.itemLevelButtonHolder}>
              <div className={styles.itemLevelButton}>
                <UnderLinedButton
                  size="14px"
                  fontFamily="regular"
                  color="#000"
                  label="Item Level Breakup"
                  onClick={() => this.itemBreakup()}
                />
              </div>
            </div>
          )}
      </div>
    );
  }

  changeNoCostEmiPlan() {
    this.setState({
      selectedBankIndex: null,
      selectedMonth: null,
      showAll: false,
      selectedBankName: null,
      selectedBankCode: null,
      selectedCouponCode: null,
      selectedTenure: null
    });
    this.props.changeNoCostEmiPlan();
  }

  render() {
    let filteredBankListWithLogo =
      this.props.bankList &&
      this.props.bankList
        .filter((bank, i) => {
          return bank.logoUrl;
        })
        .slice(0, 4);

    let filteredBankListWithOutLogo =
      this.props.bankList &&
      this.props.bankList.filter(
        val => !filteredBankListWithLogo.includes(val)
      );

    return (
      <div className={styles.base}>
        {!this.props.isNoCostEmiProceeded && (
          <div>
            <div className={styles.bankLogoHolder}>
              {filteredBankListWithLogo &&
                filteredBankListWithLogo
                  .filter((val, i) => {
                    return !this.state.showAll ? i < 4 : true;
                  })
                  .map((val, i) => {
                    console.log(val.logoUrl);
                    return (
                      <div className={styles.bankLogo}>
                        <BankSelect
                          image={val.logoUrl}
                          value={val.code}
                          key={i}
                          selectItem={() => this.handleSelect(i)}
                          selected={this.state.selectedBankIndex === i}
                        />
                      </div>
                    );
                  })}
            </div>
            {filteredBankListWithOutLogo &&
              filteredBankListWithOutLogo.length > 0 && (
                <div className={styles.selectHolder}>
                  <SelectBoxMobile
                    height={33}
                    label={
                      this.state.bankName ? this.state.bankName : "Other Bank"
                    }
                    options={filteredBankListWithOutLogo.map((val, i) => {
                      return {
                        value: val.bankName,
                        label: val.bankName
                      };
                    })}
                    onChange={val => this.selectOtherBank(val)}
                  />
                </div>
              )}
            <div className={styles.itemLevelButtonHolder}>
              <div className={styles.itemLevelButton}>
                <UnderLinedButton
                  size="14px"
                  fontFamily="regular"
                  color="#000"
                  label="View T&C"
                  onClick={() => this.termsAndCondition()}
                />
              </div>
            </div>
            {this.state.selectedBankIndex !== null && (
              <div className={styles.emiDetailsPlan}>
                <div className={styles.labelHeader}>
                  * No cost EMI available only on 1 product
                </div>
                <div className={styles.monthsLabel}>Tenure (Months)</div>
                <div className={styles.monthsHolder}>
                  {this.props.bankList[this.state.selectedBankIndex]
                    .noCostEMICouponList &&
                    this.props.bankList[
                      this.state.selectedBankIndex
                    ].noCostEMICouponList.map((val, i) => {
                      return (
                        <div
                          className={styles.monthWithCheckbox}
                          key={i}
                          value={val.emicouponCode}
                          onClick={() => this.onSelectMonth(i, val)}
                        >
                          <div className={styles.checkbox}>
                            <CheckBox
                              selected={this.state.selectedMonth === i}
                            />
                          </div>
                          {`${val.tenure} Months`}
                        </div>
                      );
                    })}
                </div>
              </div>
            )}
            {this.state.selectedMonth !== null &&
              this.props.noCostEmiDetails &&
              this.renderMonthsPlan()}
          </div>
        )}

        {this.props.isNoCostEmiProceeded && (
          <React.Fragment>
            <EmiDisplay
              bankName={this.state.selectedBankName}
              term={this.state.selectedTenure}
              emiRate="No Cost"
              price={`Rs. ${Math.round(
                this.props.noCostEmiDetails.cartAmount.noCostEMIPerMonthPayable
                  .value * 100
              ) / 100}`}
              changePlan={() => this.changeNoCostEmiPlan()}
            />
            <CreditCardForm
              onChangeCvv={i => this.onChangeCvv(i)}
              binValidation={binNo => this.binValidation(binNo)}
              softReservationForPayment={cardDetails =>
                this.softReservationForPayment(cardDetails)
              }
              displayToast={this.props.displayToast}
            />
          </React.Fragment>
        )}
      </div>
    );
  }
}
