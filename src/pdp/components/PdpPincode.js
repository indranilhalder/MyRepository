import React from "react";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import styles from "./PdpPincode.css";
export default class PdpPincode extends React.Component {
  onClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return this.props.hasPincode ? (
      <div className={styles.base}>
        <div className={styles.label}>{this.props.pincode}</div>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <UnderLinedButton
              label="Change"
              onClick={() => this.onClick()}
              color="#ff1744"
            />
          </div>
        </div>
      </div>
    ) : (
      <div className={styles.base}>
        <div className={styles.labelMessage}>
          Click to enter pin code & check delivery options
        </div>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <UnderLinedButton
              fontSize={14}
              label="Update Pincode"
              onClick={() => this.onClick()}
              color="#ff1744"
            />
          </div>
        </div>
      </div>
    );
  }
}
