import React from "react";
import styles from "./NoCostEmiBankDetails.css";
import CheckBox from "../../general/components/CheckBox.js";
import BankSelect from "./BankSelect";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
export default class NoCostEmiBankDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isSelected: null,
      selectMonth: null,
      showAll: false,
      selectedBankName: null,
      selectedBankCode: null,
      selectedCouponCode: null
    };
  }
  selectOtherBank(val) {
    this.setState({ isSelected: null });
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
    this.setState({
      isSelected: index,
      selectMonth: null,
      selectedBankName: this.props.bankList[index].bankName,
      selectedBankCode: this.props.bankList[index].code
    });
    if (this.state.isSelected === index) {
      this.setState({
        isSelected: null,
        selectedBankName: null,
        selectedBankCode: null
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
  onSelectMonth(index, val) {
    this.setState({ selectMonth: index, selectedCouponCode: val });
    if (this.state.selectMonth === index) {
      this.setState({ selectMonth: null, selectedCouponCode: null });
      this.props.removeNoCostEmi(val);
    }
    if (val && this.props.applyNoCostEmi) {
      this.props.applyNoCostEmi(val);
    }
  }
  renderMonthsPlan(val) {
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
                  noCostEmiDetails.noCostEMIOrderValue.value
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
                  noCostEmiDetails.noCostEMIInterestValue.value
                ) / 100}`}</div>
              </div>
            )}
          {noCostEmiDetails &&
            noCostEmiDetails.noCostEMIDiscountValue &&
            noCostEmiDetails.noCostEMIDiscountValue.value && (
              <div className={styles.discount}>
                <div className={styles.amountLabel}>No Cost EMI Discount</div>
                <div className={styles.amount}>{`Rs. ${Math.round(
                  noCostEmiDetails.noCostEMIDiscountValue.value
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
                  noCostEmiDetails.noCostEMITotalPayable.value
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
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.bankLogoHolder}>
          {this.props.bankList &&
            this.props.bankList
              .filter((val, i) => {
                return !this.state.showAll ? i < 4 : true;
              })
              .map((val, i) => {
                return (
                  <div className={styles.bankLogo}>
                    <BankSelect
                      image={val.imageUrl}
                      value={val.code}
                      key={i}
                      selectItem={() => this.handleSelect(i)}
                      selected={this.state.isSelected === i}
                    />
                  </div>
                );
              })}
        </div>
        <div className={styles.selectHolder}>
          <SelectBoxMobile
            height={33}
            label="Other Bank"
            options={
              this.props.bankList &&
              this.props.bankList.map((val, i) => {
                return {
                  value: val.bankName,
                  label: val.bankName
                };
              })
            }
            onChange={val => this.selectOtherBank(val)}
          />
        </div>
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
        {this.state.isSelected !== null && (
          <div className={styles.emiDetailsPlan}>
            <div className={styles.labelHeader}>
              `* No cost EMI available only on ${this.props.productCount}{" "}
              product`
            </div>
            <div className={styles.monthsLabel}>Tenure (Months)</div>
            <div className={styles.monthsHolder}>
              {this.props.bankList[this.state.isSelected].noCostEMICouponList &&
                this.props.bankList[
                  this.state.isSelected
                ].noCostEMICouponList.map((val, i) => {
                  return (
                    <div
                      className={styles.monthWithCheckbox}
                      key={i}
                      value={val.emicouponCode}
                      onClick={() => this.onSelectMonth(i, val.emicouponCode)}
                    >
                      <div className={styles.checkbox}>
                        <CheckBox selected={this.state.selectMonth === i} />
                      </div>
                      {val.tenure}
                    </div>
                  );
                })}
            </div>
          </div>
        )}
        {this.state.selectMonth !== null &&
          this.props.noCostEmiDetails &&
          this.renderMonthsPlan(
            this.props.bankList[this.state.isSelected].noCostEMICouponList[
              this.state.selectMonth
            ]
          )}
      </div>
    );
  }
}
