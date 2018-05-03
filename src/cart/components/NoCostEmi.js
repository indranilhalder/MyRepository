import React from "react";
import styles from "./NoCostEmi.css";
import PropTypes from "prop-types";
import { Collapse } from "react-collapse";
import { NO_COST_EMI_COUPON } from "../../lib/constants.js";
const STANDARD_EMI = "Standard Emi";
const NO_COST_EMI = "No Cost Emi";

export default class NoCostEmi extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isOpen: false
    };
  }

  openMenu(e) {
    const isOpen = !this.state.isOpen;
    this.setState({
      isOpen
    });
    if (this.props.onChangeEMIType) {
      if (isOpen) {
        this.props.onChangeEMIType(this.props.EMIText);
      } else {
        this.props.onChangeEMIType(null);
      }
    }
    if (
      isOpen &&
      this.props.EMIText === STANDARD_EMI &&
      !this.props.emiList &&
      this.props.getEmiBankDetails
    ) {
      let noCostEmiCouponCode = localStorage.getItem(NO_COST_EMI_COUPON);
      if (noCostEmiCouponCode) {
        this.props.removeNoCostEmi(noCostEmiCouponCode);
      }
      this.props.getEmiBankDetails();
    }
    if (
      isOpen &&
      this.props.EMIText === NO_COST_EMI &&
      this.props.getBankAndTenureDetails
    ) {
      this.props.getBankAndTenureDetails();
    }
  }
  componentWillReceiveProps(nextProps) {
    if (nextProps.isOpenSubEMI !== this.state.isOpen) {
      this.setState({ isOpen: nextProps.isOpenSubEMI });
    }
  }
  render() {
    let rotateIcon = styles.iconHolder;
    if (this.state.isOpen) {
      rotateIcon = styles.rot;
    }
    return (
      <div className={styles.base}>
        <div
          className={styles.holder}
          onClick={e => {
            this.openMenu(e);
          }}
        >
          <div className={rotateIcon} />
          <div className={styles.textHolder}>{this.props.EMIText}</div>
        </div>
        <Collapse isOpened={this.state.isOpen}>{this.props.children}</Collapse>
      </div>
    );
  }
}
NoCostEmi.propTypes = {
  EMIText: PropTypes.string,
  onOpenMenu: PropTypes.func
};
