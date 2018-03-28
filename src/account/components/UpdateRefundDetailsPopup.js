import React from "react";
import PropTypes from "prop-types";
import AwbForm from "./AwbForm.js";
import styles from "./UpdateRefundDetailsPopup.css";
import BottomSlideModal from "../../general/components/BottomSlideModal.js";
export default class UpdateRefundDetailsPopup extends React.Component {
  onUpdate(val) {
    if (this.props.onUpdate) {
      this.props.onUpdate(val);
    }
  }
  render() {
    return (
      <BottomSlideModal>
        <div className={styles.base}>
          <div className={styles.headerText}>Update Refund Detials</div>
          <AwbForm onUpdate={val => this.onUpdate(val)} />
        </div>
      </BottomSlideModal>
    );
  }
}
