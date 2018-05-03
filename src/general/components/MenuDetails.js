import React from "react";
import styles from "./MenuDetails.css";
import PropTypes from "prop-types";
import { Collapse } from "react-collapse";
import Icon from "../../xelpmoc-core/Icon";
import couponIcon from "./img/credit-card.svg";
import {
  setDataLayerForCheckoutDirectCalls,
  ADOBE_CALL_FOR_SELECTING_PAYMENT_MODES
} from "../../lib/adobeUtils";
import {
  EASY_MONTHLY_INSTALLMENTS,
  NET_BANKING_PAYMENT_MODE
} from "../../lib/constants";

export default class MenuDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isOpen: false
    };
  }

  openMenu() {
    let isOpen = !this.state.isOpen;
    if (isOpen) {
      setDataLayerForCheckoutDirectCalls(
        ADOBE_CALL_FOR_SELECTING_PAYMENT_MODES,
        this.props.text
      );
    }
    this.setState({ isOpen });
    if (this.props.onOpenMenu) {
      if (isOpen) {
        this.props.onOpenMenu(this.props.text);
      } else {
        this.props.onOpenMenu(null);
      }
    }
    if (isOpen) {
      if (
        this.props.text === NET_BANKING_PAYMENT_MODE &&
        !this.props.bankList
      ) {
        this.props.getNetBankDetails();
      } else if (
        this.props.text === EASY_MONTHLY_INSTALLMENTS &&
        !this.props.emiList
      ) {
        this.props.getEmiBankDetails();
      }
    }
  }
  componentWillReceiveProps(nextProps) {
    if (nextProps.isOpen !== this.state.isOpen) {
      this.setState({ isOpen: nextProps.isOpen });
    }
  }

  render() {
    let iconActive = styles.icon;
    if (this.state.isOpen) {
      iconActive = styles.iconup;
    }
    return (
      <div className={styles.base}>
        <div
          className={styles.holder}
          onClick={() => {
            this.openMenu();
          }}
        >
          <div className={styles.debitCardIcon}>
            <Icon image={this.props.icon} size={25} />
          </div>
          <div className={styles.textBox}>
            {this.props.text}
            <div className={iconActive} />
          </div>
        </div>
        <Collapse isOpened={this.state.isOpen}>{this.props.children}</Collapse>
      </div>
    );
  }
}
MenuDetails.propTypes = {
  text: PropTypes.string,
  icon: PropTypes.string,
  onOpenMenu: PropTypes.bool
};

MenuDetails.defaultProps = {
  icon: couponIcon
};
