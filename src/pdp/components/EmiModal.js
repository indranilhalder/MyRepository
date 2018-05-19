import React from "react";
import sortBy from "lodash.sortby";
import EmiCard from "./EmiCard";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import SlideModal from "../../general/components/SlideModal";
import Accordion from "../../general/components/Accordion";
import PropTypes from "prop-types";
import styles from "./EmiModal.css";
import {
  setDataLayerForPdpDirectCalls,
  SET_DATA_LAYER_FOR_EMI_BANK_EVENT
} from "../../lib/adobeUtils";
const EMI_INFO = "An EMI for this product is provided by the following banks";
export default class EmiModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      openIndex: null,
      showEmi: false
    };
  }
  handleOpen(index) {
    if (
      this.props.emiData.bankList[index] &&
      this.props.emiData.bankList[index].emiBank
    ) {
      const bankName = this.props.emiData.bankList[index].emiBank;
      setDataLayerForPdpDirectCalls(
        SET_DATA_LAYER_FOR_EMI_BANK_EVENT,
        bankName
      );
    }
    if (index === this.state.openIndex) {
      this.setState({ openIndex: null });
    } else {
      this.setState({ openIndex: index });
    }
  }
  toggleTermsView() {
    this.setState({ showEmi: !this.state.showEmi }, () => {
      if (this.state.showEmi) {
        let scroll = document.getElementById("viewTermsAndConditionEmi");
        scroll.scrollIntoView();
      }
    });
  }

  render() {
    return (
      <SlideModal closeModal={this.props.closeModal}>
        <div className={styles.base}>
          <div className={styles.header}>EMI details</div>
          <div className={styles.content}>
            <div className={styles.info}>{EMI_INFO}</div>
            {this.props.emiData &&
              this.props.emiData.bankList &&
              this.props.emiData.bankList.map((val, i) => {
                return (
                  <Accordion
                    controlled={true}
                    text={val.emiBank}
                    key={i}
                    offset={20}
                    activeBackground="#f8f8f8"
                    isOpen={this.state.openIndex === i}
                    onOpen={() => this.handleOpen(i)}
                  >
                    <EmiCard
                      options={sortBy(
                        val.emitermsrate,
                        item => item && parseInt(item.term, 10)
                      )}
                    />
                  </Accordion>
                );
              })}
          </div>
          <div className={styles.info} id="viewTermsAndConditionEmi">
            <UnderLinedButton
              label={
                this.state.showEmi
                  ? "Hide Terms & Conditions"
                  : "View Terms & Conditions"
              }
              onClick={() => {
                this.toggleTermsView();
              }}
              fontFamily="semibold"
              size={12}
            />
          </div>
          {this.state.showEmi && (
            <div className={styles.content}>
              {this.props.emiTerms &&
                this.props.emiTerms.data &&
                this.props.emiTerms.data.termAndConditions && (
                  <div
                    className={styles.termsAndConditions}
                    dangerouslySetInnerHTML={{
                      __html: this.props.emiTerms.data.termAndConditions[0]
                    }}
                  />
                )}
            </div>
          )}
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
