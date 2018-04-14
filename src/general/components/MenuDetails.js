import React from "react";
import styles from "./MenuDetails.css";
import PropTypes from "prop-types";
import { Collapse } from "react-collapse";
import { Icon } from "xelpmoc-core";
import couponIcon from "./img/credit-card.svg";
import {
  setDataLayerForCheckoutDirectCalls,
  ADOBE_CALL_FOR_SELECTING_PAYMENT_MODES
} from "../../lib/adobeUtils";

export default class MenuDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isOpen: false
    };
  }

  openMenu() {
    let isOpenMenu = !this.state.isOpen;
    if (isOpenMenu) {
      setDataLayerForCheckoutDirectCalls(
        ADOBE_CALL_FOR_SELECTING_PAYMENT_MODES,
        this.props.text
      );
    }
    this.setState(
      {
        isOpen: isOpenMenu
      },
      () => {
        if (this.props.onOpenMenu) {
          this.props.onOpenMenu(this.state.isOpen);
        }
      }
    );

    if (isOpenMenu) {
      if (this.props.text === "Net banking" && !this.props.bankList) {
        this.props.getNetBankDetails();
      } else if (
        this.props.text === "Easy monthly installments" &&
        !this.props.emiList
      ) {
        this.props.getEmiBankDetails();
      }
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
