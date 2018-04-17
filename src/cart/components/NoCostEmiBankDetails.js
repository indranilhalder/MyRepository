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
      showAll: false
    };
  }
  selectOtherBank(val) {
    this.setState({ isSelected: null });
    if (this.props.selectOtherBank) {
      this.props.selectOtherBank(val);
    }
  }
  itemBreakup() {
    if (this.props.itemBreakup) {
      this.props.itemBreakup();
    }
  }
  handleSelect(index) {
    this.setState({ isSelected: index, selectMonth: null });
    if (this.state.isSelected === index) {
      this.setState({ isSelected: null });
    }
    if (this.props.onBankSelect) {
      this.props.onBankSelect(index);
    }
  }
  termsAndCondition() {
    if (this.props.termsAndCondition) {
      this.props.termsAndCondition();
    }
  }
  onSelectMonth(index, val) {
    this.setState({ selectMonth: index });
    if (this.state.selectMonth === index) {
      this.setState({ selectMonth: null });
    }
    if (this.props.onSelectMonth) {
      this.props.onSelectMonth(val);
    }
  }
  renderMonthsPlan(val) {
    return (
      <div className={styles.monthsPlanDataHolder}>
        <div className={styles.amountPlaneForMonth}>
          {val.orderValue && (
            <div className={styles.amountData}>
              <div className={styles.amountLabel}>Order Value</div>
              <div className={styles.amount}>{`Rs.${val.orderValue}`}</div>
            </div>
          )}
          {val.interest && (
            <div className={styles.amountData}>
              <div className={styles.amountLabel}>
                Interest (charged by bank)
              </div>
              <div className={styles.amount}>{`Rs. ${val.interest}`}</div>
            </div>
          )}
          {val.emiDiscount && (
            <div className={styles.discount}>
              <div className={styles.amountLabel}>No Cost EMI Discount</div>
              <div className={styles.amount}>{`-Rs. ${val.emiDiscount}`}</div>
            </div>
          )}
        </div>
        <div className={styles.totalAmountDisplay}>
          {val.totalAmount && (
            <div className={styles.totalAmountLabel}>
              <div className={styles.amountLabel}>Total Amount Payable</div>
              <div className={styles.amount}>{`Rs. ${val.totalAmount}`}</div>
            </div>
          )}
          {val.emiPm && (
            <div className={styles.totalAmountLabel}>
              <div className={styles.amountLabel}>EMI p.m</div>
              <div className={styles.amount}>{`Rs. ${val.emiPm}`}</div>
            </div>
          )}
        </div>

        {val.totalAmount && (
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
                      value={val.imageUrl}
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
              * No cost EMI available only on 1 product
            </div>
            <div className={styles.monthsLabel}>Tenure (Months)</div>
            <div className={styles.monthsHolder}>
              {this.props.bankList[this.state.isSelected].monthsPlan &&
                this.props.bankList[this.state.isSelected].monthsPlan.map(
                  (val, i) => {
                    return (
                      <div
                        className={styles.monthWithCheckbox}
                        key={i}
                        onClick={() => this.onSelectMonth(i, val)}
                      >
                        <div className={styles.checkbox}>
                          <CheckBox selected={this.state.selectMonth === i} />
                        </div>
                        {val.month}
                      </div>
                    );
                  }
                )}
            </div>
          </div>
        )}
        {this.state.selectMonth !== null &&
          this.props.bankList[this.state.isSelected].monthsPlan &&
          this.renderMonthsPlan(
            this.props.bankList[this.state.isSelected].monthsPlan[
              this.state.selectMonth
            ]
          )}
      </div>
    );
  }
}
