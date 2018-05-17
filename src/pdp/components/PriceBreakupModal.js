import React from "react";
import SlideModal from "../../general/components/SlideModal";
import styles from "./PriceBreakupModal.css";

export default class PriceBreakupModal extends React.Component {
  render() {
    return (
      <SlideModal closeModal={this.props.closeModal}>
        <div className={styles.base}>
          <div className={styles.header}>Price breakup</div>
          <div className={styles.content}>
            {this.props.data.map(val => {
              return (
                <div className={styles.row}>
                  {val.name &&
                    val.name !== "Gross Weight" && (
                      <div className={styles.name}>{val.name}</div>
                    )}
                  {val.price &&
                    val.name !== "Gross Weight" && (
                      <div className={styles.price}>
                        {val.price.formattedValue}
                      </div>
                    )}
                  {val.weightRateList &&
                    val.name !== "Gross Weight" &&
                    val.weightRateList.length > 0 &&
                    val.weightRateList.map(val => {
                      return <div className={styles.detail}>{val}</div>;
                    })}
                </div>
              );
            })}
          </div>
        </div>
      </SlideModal>
    );
  }
}
