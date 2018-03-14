import React from "react";
import styles from "./CourierProduct.css";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";
export default class CourierProduct extends React.Component {
  downloadForm() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        {this.props.header && (
          <div className={styles.header}>
            {this.props.header}
            {this.props.indexNumber !== "0" && (
              <div className={styles.circleHolder}>
                <div className={styles.circle}>{this.props.indexNumber}</div>
              </div>
            )}
          </div>
        )}
        {this.props.text && (
          <div className={styles.text}>{this.props.text}</div>
        )}
        {this.props.subText && (
          <div className={styles.subText}>{this.props.subText}</div>
        )}
        {this.props.underlineButtonLabel && (
          <div className={styles.formHolder}>
            <div className={styles.form} onClick={() => this.downloadForm()}>
              <UnderLinedButton
                label={this.props.underlineButtonLabel}
                color={this.props.underlineButtonColour}
              />
            </div>
          </div>
        )}
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
  subText: PropTypes.string
};
