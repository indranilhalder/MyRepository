import React from "react";
import PropTypes from "prop-types";
import Toggle from "../../general/components/Toggle";
import styles from "./CliqCashToggle.css";
export default class CliqCashToggle extends React.Component {
  onToggle(val) {
    if (this.props.onToggle) {
      this.props.onToggle(val);
    }
  }
  render() {
    let toggleDisable = this.props.value === 0 ? true : false;

    return (
      <div className={styles.base}>
        <div className={styles.cashBalanceTextHolder}>
          <div className={styles.casBalanceText}>{this.props.cashText}</div>
          <div className={styles.cashRupyText}>{`Rs. ${
            this.props.price
          } available`}</div>
        </div>
        <div className={styles.toggleButtonHolder}>
          <div className={styles.toggleButton}>
            <Toggle
              active={this.props.active}
              onToggle={val => this.onToggle(val)}
              disabled={toggleDisable}
            />
          </div>
        </div>
      </div>
    );
  }
}
CliqCashToggle.propTypes = {
  onToggle: PropTypes.func,
  cashText: PropTypes.string,
  price: PropTypes.string,
  active: PropTypes.bool
};
CliqCashToggle.defaultProps = {
  active: false
};
