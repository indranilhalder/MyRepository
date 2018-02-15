import React from "react";
import styles from "./DeliveryAdressCopy.css";
import CheckBox from "../../general/components/CheckBox.js";

export default class DeliveryAdressCopy extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.confirm}>
          {this.props.confirmTitle}
          <div className={styles.circle}> 1</div>
        </div>
        <div className={styles.titleContainer}>
          <div className={styles.titleAdress}>{this.props.adressTitle}</div>
          <div className={styles.titleDescription}>
            {this.props.adressDescription}
            <div className={styles.checkCircle}>
              <CheckBox selected={this.props.selected} />
            </div>
          </div>
        </div>
      </div>
    );
  }
}
