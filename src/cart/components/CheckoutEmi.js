import React from "react";
import emiIcon from "./img/emi.svg";
import PropTypes from "prop-types";
import EmiAccordion from "./EmiAccordion";
import MenuDetails from "../../general/components/MenuDetails.js";
import { SUCCESS } from "../../lib/constants";
import styles from "./CheckoutEmi.css";

export default class CheckoutEmi extends React.Component {
  binValidation = binNo => {
    if (this.props.binValidation) {
      this.props.binValidation(binNo);
    }
  };

  getEmiBankDetails = () => {
    if (this.props.getEmiBankDetails) {
      this.props.getEmiBankDetails();
    }
  };
  onChangeCardDetail = val => {
    if (this.props.onChangeCardDetail) {
      this.props.onChangeCardDetail(val);
    }
  };
  render() {
    return (
      <div>
        {this.props.cart.emiBankDetails &&
          this.props.cart.emiBankDetails.bankList && (
            <EmiAccordion
              emiList={this.props.cart.emiBankDetails.bankList}
              onChangeCvv={i => this.onChangeCvv(i)}
              binValidation={binNo => this.binValidation(binNo)}
              onChangeCardDetail={val => this.onChangeCardDetail(val)}
            />
          )}
        {!this.props.cart.emiBankDetails && (
          <div className={styles.errorText}>{this.props.cart.emiBankError}</div>
        )}
      </div>
    );
  }
}

CheckoutEmi.propTypes = {
  cart: PropTypes.arrayOf(
    PropTypes.shape({
      code: PropTypes.string,
      emiBank: PropTypes.string,
      emitermsrate: PropTypes.arrayOf(
        PropTypes.shape({
          interestPayable: PropTypes.string,
          interestRate: PropTypes.string,
          monthlyInstallment: PropTypes.string,
          term: PropTypes.string
        })
      )
    })
  ),
  onSelect: PropTypes.func,
  selected: PropTypes.arrayOf(PropTypes.string)
};
