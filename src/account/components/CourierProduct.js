import React from "react";
import styles from "./CourierProduct.css";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import CheckOutHeader from "../../cart/components/CheckOutHeader.js";
export default class CourierProduct extends React.Component {
  downloadForm() {
    if (this.props.selfCourierDocumentLink) {
      window.open(this.props.selfCourierDocumentLink);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        {this.props.header && (
          <div className={styles.header}>
            <CheckOutHeader
              confirmTitle={this.props.header}
              indexNumber={this.props.indexNumber}
            />
          </div>
        )}
        {this.props.text && (
          <div className={styles.text}>{this.props.text}</div>
        )}
        {this.props.subText && (
          <div className={styles.subText}>{this.props.subText}</div>
        )}
        {this.props.selfCourierDocumentLink &&
          this.props.underlineButtonLabel && (
            <div className={styles.formHolder}>
              <div className={styles.form} onClick={() => this.downloadForm()}>
                <UnderLinedButton
                  label={this.props.underlineButtonLabel}
                  color={this.props.underlineButtonColour}
                />
              </div>
            </div>
          )}
        {this.props.children}
      </div>
    );
  }
}
CourierProduct.propTypes = {
  underlineButtonLabel: PropTypes.string,
  underlineButtonColour: PropTypes.string,
  text: PropTypes.string,
  indexNumber: PropTypes.string,
  header: PropTypes.string,
  subText: PropTypes.string,
  onClick: PropTypes.func
};
