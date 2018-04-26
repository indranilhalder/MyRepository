import React from "react";
import SlideModal from "../../general/components/SlideModal";
import styles from "./EmiModal.css";

export default class PriceBreakupModal extends React.Component {
  render() {
    return (
      <SlideModal>
        <div className={styles.base}>
          <div className={styles.header}>Price breakup</div>
          <div className={styles.content} />
        </div>
      </SlideModal>
    );
  }
}
