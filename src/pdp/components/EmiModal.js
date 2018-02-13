import React from "react";
import EmiCard from "./EmiCard";
import SlideModal from "../../general/components/SlideModal";
import Accordion from "../../general/components/Accordion";
import styles from "./EmiModal.css";
export default class EmiModal extends React.Component {
  render() {
    return (
      <SlideModal closeModal={this.props.closeModal}>
        <div className={styles.base}>
          <div className={styles.header}>EMI details</div>
          <div className={styles.content}>
            <div className={styles.info}>
              EMI for the product is provided by the followng banks
            </div>
            {this.props.data &&
              this.props.data.bankList.map((val, i) => {
                return (
                  <Accordion text={val.emiBank}>
                    <EmiCard options={val.emitermsrate} />
                  </Accordion>
                );
              })}
          </div>
        </div>
      </SlideModal>
    );
  }
}
