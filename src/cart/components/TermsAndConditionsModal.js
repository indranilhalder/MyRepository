import React from "react";
import SlideModal from "../../general/components/SlideModal";
import styles from "../../pdp/components/EmiModal.css";
const TERMS_AND_CONDITION_TEXT = "Terms & Conditions";
export default class TermsAndConditionsModal extends React.Component {
  render() {
    return (
      <SlideModal closeModal={this.props.closeModal}>
        <div className={styles.base}>
          <div className={styles.header}>{TERMS_AND_CONDITION_TEXT}</div>
          <div className={styles.bankNameHeader}>
            {this.props.emiTermsAndConditions.bankName}
          </div>
          <div className={styles.content}>
            {this.props.emiTermsAndConditions &&
              this.props.emiTermsAndConditions.termsAndCondition && (
                <div
                  className={styles.termsAndConditions}
                  dangerouslySetInnerHTML={{
                    __html: this.props.emiTermsAndConditions.termsAndCondition
                  }}
                />
              )}
          </div>
        </div>
      </SlideModal>
    );
  }
}
