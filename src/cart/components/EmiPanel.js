import React from "react";
import styles from "./EmiPanel.css";
import PropTypes from "prop-types";
import MenuDetails from "../../general/components/MenuDetails.js";
import eWalletIcon from "./img/netBanking.svg";
import NoCostEmi from "./NoCostEmi.js";
import NoCostEmiBankDetails from "./NoCostEmiBankDetails.js";
export default class EmiPanel extends React.Component {
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
  render() {
    return (
      <div className={styles.base}>
        <MenuDetails text="EMI" icon={eWalletIcon}>
          <div className={styles.subListHolder}>
            <NoCostEmi text="No Cost Emi">
              <NoCostEmiBankDetails
                onBankSelect={val => this.onBankSelect(val)}
                onSelectMonth={val => this.onSelectMonth(val)}
                bankList={this.props.bankList}
              />
            </NoCostEmi>
          </div>
          <div className={styles.subListHolder}>
            <NoCostEmi text="Standard Emi" />
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
