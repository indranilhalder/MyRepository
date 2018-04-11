import React from "react";
import SlideModal from "../../general/components/SlideModal";
import styles from "./OfferModal.css";
export default class OfferModal extends React.Component {
  render() {
    console.log(this.props);
    return (
      <SlideModal closeModal={this.props.closeModal}>
        <div className={styles.base}>
          <div className={styles.header}>Offer</div>
          <div className={styles.content}>lol</div>
        </div>
      </SlideModal>
    );
  }
}
