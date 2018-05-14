import React from "react";
import SlideModal from "../../general/components/SlideModal";
import PropTypes from "prop-types";
import styles from "./OfferModal.css";
export default class OfferModal extends React.Component {
  render() {
    return (
      <SlideModal closeModal={this.props.closeModal}>
        <div className={styles.base}>
          <div className={styles.header}>Offers</div>
          {this.props.potentialPromotions && (
            <div className={styles.content}>
              <div className={styles.headingText}>
                {this.props.potentialPromotions.title}
              </div>
              {this.props.potentialPromotions.description && (
                <div className={styles.section}>
                  <div
                    className={styles.description}
                    dangerouslySetInnerHTML={{
                      __html: this.props.potentialPromotions.description
                    }}
                  />
                </div>
              )}
              <div className={styles.section}>
                <div className={styles.timeSection}>
                  <div className={styles.subHeader}>Valid From</div>
                  {this.props.potentialPromotions.startDate}
                </div>
                <div className={styles.timeSection}>
                  <div className={styles.subHeader}>Valid Till</div>
                  {this.props.potentialPromotions.endDate}
                </div>
              </div>
            </div>
          )}
          {this.props.secondaryPromotions && (
            <div className={styles.content}>
              <div className={styles.border} />
              <div className={styles.headingText}>
                {this.props.secondaryPromotions.messageID}
              </div>
              {this.props.secondaryPromotions.messageDetails && (
                <div className={styles.section}>
                  <div
                    className={styles.description}
                    dangerouslySetInnerHTML={{
                      __html: this.props.secondaryPromotions.messageDetails
                    }}
                  />
                </div>
              )}
              <div className={styles.section}>
                <div className={styles.timeSection}>
                  <div className={styles.subHeader}>Valid From</div>
                  {this.props.secondaryPromotions.startDate}
                </div>
                <div className={styles.timeSection}>
                  <div className={styles.subHeader}>Valid Till</div>
                  {this.props.secondaryPromotions.endDate}
                </div>
              </div>
            </div>
          )}
        </div>
      </SlideModal>
    );
  }
}
OfferModal.propTypes = {
  potentialPromotions: PropTypes.shape({
    title: PropTypes.string,
    description: PropTypes.string,
    endDate: PropTypes.string,
    startDate: PropTypes.string
  }),
  secondaryPromotions: PropTypes.shape({
    messageId: PropTypes.string,
    messageDetails: PropTypes.string,
    endDate: PropTypes.string,
    startDate: PropTypes.string
  })
};
