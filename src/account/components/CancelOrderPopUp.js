import React from "react";
import styles from "./CancelOrderPopUp.css";
import Button from "../../general/components/Button.js";
export default class CancelOrderPopUp extends React.Component {
  cancelProduct() {
    if (this.props.cancelProduct) {
      this.props.cancelProduct();
    }
  }

  cancelModal() {
    console.log(this.props);
    if (this.props.cancelModal) {
      this.props.cancelModal();
    }
  }

  render() {
    return (
      <div className={styles.base}>
        <div className={styles.cancelOrderTextHolder}>
          <div className={styles.cancelOrderText}>
            Are you sure you want to cancel your order ?
          </div>
        </div>
        <div className={styles.buttonHolderForCancelOrder}>
          <div className={styles.button}>
            <Button
              type="primary"
              backgroundColor="#ff1744"
              height={36}
              label="Yes"
              width={211}
              textStyle={{ color: "#FFF", fontSize: 14 }}
              onClick={() => this.cancelProduct()}
            />
          </div>
        </div>
        <div className={styles.buttonHolderForCloseModal}>
          <div className={styles.button}>
            <Button
              type="secondary"
              height={36}
              label="No"
              width={211}
              onClick={() => this.cancelModal()}
            />
          </div>
        </div>
      </div>
    );
  }
}
