import React from "react";
import SlideModal from "../../general/components/SlideModal";
import styles from "./OrderModal.css";
export default class OrderModal extends React.Component {
  render() {
    return (
      <SlideModal closeModal={this.props.closeModal}>
        <div className={styles.base}>
          <div class={styles.header}>Order #{this.props.data.orderCode}</div>
          {this.props.data &&
            this.props.data.shippingList &&
            this.props.data.shippingList.map(val => {
              return (
                <div className={styles.step}>
                  <div className={styles.row}>
                    {val.date} , {val.time}
                  </div>
                  <div className={styles.row}>{val.statusDescription}</div>
                </div>
              );
            })}
        </div>
      </SlideModal>
    );
  }
}
