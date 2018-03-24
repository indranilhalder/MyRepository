import React from "react";
import ReturnsFrame from "./ReturnsFrame";
import PropTypes from "prop-types";
import CourierProduct from "./CourierProduct.js";
import styles from "./SelfCourier.css";
export default class SelfCourier extends React.Component {
  onCancel() {
    if (this.props.onCancel) {
      this.props.onCancel();
    }
  }
  onContinue() {
    if (this.props.onContinue) {
      this.props.onContinue();
    }
  }
  downloadForm() {
    if (this.props.downloadForm) {
      this.props.downloadForm();
    }
  }
  render() {
    return (
      <ReturnsFrame
        headerText="Steps for self courier"
        onContinue={() => this.onContinue()}
        buttonText="Initiate Return"
        onCancel={() => this.onCancel()}
      >
        <div className={styles.card}>
          <CourierProduct
            indexNumber="1"
            header="Courier the product"
            text="Use any courier services to ship back the products to our address. "
            subText="Please use the form sent along with the invoice or
            Re-download the form again from below"
            underlineButtonLabel="Download form"
            underlineButtonColour="#ff1744"
            downloadForm={() => this.downloadForm()}
          />
        </div>
        <div className={styles.card}>
          <CourierProduct indexNumber="2" header="Update the AWB number">
            <div className={styles.awbText}>
              Please update the AWB number provided by the courier service in
              the <span>Order history</span> section of My Accountagainst the
              order{" "}
            </div>
          </CourierProduct>
        </div>
      </ReturnsFrame>
    );
  }
}
SelfCourier.propTypes = {
  onCancel: PropTypes.func,
  onContinue: PropTypes.func,
  downloadForm: PropTypes.func
};
