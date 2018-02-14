import React from "react";
import EmiCard from "./EmiCard";
import SlideModal from "../../general/components/SlideModal";
import Accordion from "../../general/components/Accordion";
import PropTypes from "prop-types";
import styles from "./EmiModal.css";
const EMI_INFO = "EMI for the product is provided by the following banks";
export default class EmiModal extends React.Component {
  render() {
    console.log(this.props);
    return (
      <SlideModal closeModal={this.props.closeModal}>
        <div className={styles.base}>
          <div className={styles.header}>EMI details</div>
          <div className={styles.content}>
            <div className={styles.info}>{EMI_INFO}</div>
            {this.props.emiData &&
              this.props.emiData.bankList.map((val, i) => {
                return (
                  <Accordion text={val.emiBank} key={i}>
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
EmiModal.propTypes = {
  emiData: PropTypes.shape({
    bankList: PropTypes.arrayOf(
      PropTypes.shape({
        emiBank: PropTypes.string,
        emitermsrate: PropTypes.arrayOf(
          PropTypes.shape({
            interestPayable: PropTypes.string,
            interestRate: PropTypes.string,
            monthlyInstallment: PropTypes.string,
            term: PropTypes.string
          })
        )
      })
    )
  })
};
