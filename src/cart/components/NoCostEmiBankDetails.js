import React from "react";
import styles from "./NoCostEmiBankDetails.css";
import CheckBox from "../../general/components/CheckBox.js";
import BankSelect from "./BankSelect";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import SelectBoxMobile2 from "../../general/components/SelectBoxMobile2";
import EmiDisplay from "./EmiDisplay";
import CreditCardForm from "./CreditCardForm";
import { NO_COST_EMI, RUPEE_SYMBOL, SUCCESS } from "../../lib/constants";

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
      selectedTenure: null,
      selectedFromDropDown: false,
      noCostEmiText: ""
    };
  }

  componentDidUpdate(prevProps, prevState) {
    if (this.props.noCostEmiProductCount !== prevProps.noCostEmiProductCount) {
      if (this.props.noCostEmiProductCount > 0) {
        if (
          parseInt(this.props.noCostEmiProductCount, 10) ===
          this.props.totalProductCount
        ) {
          this.setState({
            noCostEmiText: ``
          });
        } else {
          this.setState({
            noCostEmiText: `*No Cost EMI available only on ${
              this.props.noCostEmiProductCount
            } product(s). Standard EMI will apply to products, if any, bought along with it.`
          });
        }
      }
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.selectedEMIType !== NO_COST_EMI) {
      this.setState({
        selectedBankIndex: null,
        selectedMonth: null,
        showAll: false,
        selectedBankName: null,
        selectedCode: null,
        selectedCouponCode: null,
        selectedTenure: null,
        selectedFromDropDown: false
      });
    }
  }
  selectOtherBank(val) {
    const selectedBankName = val.label;
    const selectedBankIndex = val.value;
    const selectedBankCodeObj = this.props.bankList.find(
      bank => bank.bankName === val.label
    );
    let selectedCode;
    if (selectedBankCodeObj) {
      selectedCode = selectedBankCodeObj.code;
    }
    this.setState({
      selectedBankIndex: selectedBankIndex,
      selectedBankName: selectedBankName,
      selectedCode,
      selectedFromDropDown: true,
      selectedMonth: null
    });
  }
  itemBreakup() {
    if (this.props.getItemBreakUpDetails) {
      this.props.getItemBreakUpDetails(
        this.state.selectedCouponCode,
        this.state.noCostEmiText,
        this.props.noCostEmiProductCount
      );
    }
  }
  handleSelect(index, code) {
    if (this.state.selectedFromDropDown === true) {
      this.setState({
        selectedBankIndex: null,
        selectedBankName: null,
        selectedCode: null,
        selectedFromDropDown: false,
        selectedTenure: null
      });
    }
    if (this.state.selectedBankIndex === index) {
      this.setState({
        selectedBankIndex: null,
        selectedBankName: null,
        selectedCode: null,
        selectedFromDropDown: false,
        selectedTenure: null
      });
    } else {
      let selectedBankCodeObj = this.props.bankList.find(
        bank => bank.code === code
      );

      this.setState({
        selectedBankIndex: index,
        selectedMonth: null,
        selectedBankName: selectedBankCodeObj.bankName,
        selectedCode: selectedBankCodeObj.code,
        selectedBankCode: selectedBankCodeObj.bankCode,
        bankName: null,
        selectedFromDropDown: false
      });
    }
  }
  termsAndCondition() {
    if (this.props.getEmiTermsAndConditionsForBank) {
      this.props.getEmiTermsAndConditionsForBank(
        this.state.selectedCode,
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

  onChangeCardDetail = card => {
    if (this.props.onChangeCardDetail) {
      this.props.onChangeCardDetail(card);
    }
  };

  async onSelectMonth(index, val) {
    console.log(val);
    console.log(this.state);
    if (this.state.selectedBankName !== "Other Bank") {
      if (this.state.selectedMonth === index) {
        this.setState({
          selectedMonth: null,
          selectedCouponCode: null,
          selectedTenure: null
        });

        this.props.removeNoCostEmi(val.emicouponCode);
      } else {
        if (val && this.props.applyNoCostEmi) {
          const applyNoCostEmiReponse = await this.props.applyNoCostEmi(
            val.emicouponCode,
            this.state.selectedBankName
          );
          if (applyNoCostEmiReponse.status === SUCCESS) {
            this.setState({
              selectedMonth: index,
              selectedCouponCode: val.emicouponCode,
              selectedTenure: val.tenure
            });
            this.onChangeCardDetail({
              is_emi: true,
              emi_bank: this.state.selectedBankCode,
              emi_tenure: val.tenure
            });
          }
        }
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
                <div className={styles.amountDiscount}>{`-Rs. ${Math.round(
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
                <div className={styles.amountPayble}>Total Amount Payable</div>
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
                <div className={styles.amountEmi}>{`Rs. ${Math.round(
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
    let modifiedBankList;
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

    if (this.state.selectedCode) {
      modifiedBankList = this.props.bankList.find(
        bank => bank.code === this.state.selectedCode
      );
    }

    return (
      <div className={styles.base}>
        <div className={styles.bankText}>
          Tata CLiQ does not levy any charges on EMIs taken. Charges, if any,
          are levied by your bank
        </div>
        {!this.props.isNoCostEmiProceeded && (
          <div>
            <div className={styles.bankLogoHolder}>
              {filteredBankListWithLogo &&
                filteredBankListWithLogo
                  .filter((val, i) => {
                    return !this.state.showAll ? i < 4 : true;
                  })
                  .map((val, i) => {
                    return (
                      <div className={styles.bankLogo}>
                        <BankSelect
                          image={val.logoUrl}
                          value={val.code}
                          name={val.bankName}
                          key={i}
                          selectItem={() => this.handleSelect(i, val.code)}
                          selected={this.state.selectedCode === val.code}
                        />
                      </div>
                    );
                  })}
            </div>
            {filteredBankListWithOutLogo &&
              filteredBankListWithOutLogo.length > 0 && (
                <div className={styles.selectHolder}>
                  <SelectBoxMobile2
                    height={33}
                    placeholder={"Other Bank"}
                    backgroundColor="#fff"
                    isEnable={this.state.selectedFromDropDown}
                    options={filteredBankListWithOutLogo.map((val, i) => {
                      return {
                        value: i,
                        label: val.bankName
                      };
                    })}
                    onChange={val => this.selectOtherBank(val)}
                  />
                </div>
              )}

            {this.state.selectedBankIndex !== null && (
              <div className={styles.emiDetailsPlan}>
                {this.state.noCostEmiText !== "" && (
                  <div className={styles.labelHeader}>
                    {this.state.noCostEmiText}
                  </div>
                )}
                <div className={styles.monthsLabel}>Tenure (Months)</div>
                <div className={styles.monthsHolder}>
                  {modifiedBankList &&
                    modifiedBankList.noCostEMICouponList &&
                    modifiedBankList.noCostEMICouponList.map((val, i) => {
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
            {this.state.selectedBankCode &&
              this.state.selectedBankIndex !== null && (
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
              )}
          </div>
        )}

        {this.props.isNoCostEmiProceeded && (
          <React.Fragment>
            <EmiDisplay
              bankName={this.state.selectedBankName}
              term={this.state.selectedTenure}
              emiRate="No Cost"
              price={
                this.props.noCostEmiDetails.cartAmount &&
                this.props.noCostEmiDetails.cartAmount
                  .noCostEMIPerMonthPayable &&
                `${RUPEE_SYMBOL} ${Math.round(
                  this.props.noCostEmiDetails.cartAmount
                    .noCostEMIPerMonthPayable.value * 100
                ) / 100}`
              }
              changePlan={() => this.changeNoCostEmiPlan()}
            />
            <CreditCardForm
              onFocusInput={this.props.onFocusInput}
              onChangeCvv={i => this.onChangeCvv(i)}
              binValidation={binNo => this.binValidation(binNo)}
              onChangeCardDetail={cardDetails =>
                this.onChangeCardDetail(cardDetails)
              }
              displayToast={this.props.displayToast}
            />
          </React.Fragment>
        )}
      </div>
    );
  }
}
