import React from "react";
import styles from "./OrderReturn.css";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import ColourButton from "../../general/components/ColourButton";
import PropTypes from "prop-types";
export default class OrderReturn extends React.Component {
  replaceItem() {
    if (this.props.replaceItem) {
      this.props.replaceItem();
    }
  }
  writeReview() {
    if (this.props.writeReview) {
      this.props.writeReview();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.replaceHolder}>
          <div className={styles.replace}>
            <ColourButton
              label={this.props.buttonLabel}
              onClick={() => this.replaceItem()}
            />
          </div>
        </div>
        {this.props.isEditable && (
          <div className={styles.reviewHolder}>
            <div
              className={
                this.props.isDisabled ? styles.reviewDesible : styles.review
              }
              onClick={() => this.writeReview()}
            >
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
OrderReturn.propTypes = {
  underlineButtonColour: PropTypes.string,
  underlineButtonLabel: PropTypes.string,
  buttonLabel: PropTypes.string,
  replaceItem: PropTypes.func,
  writeReview: PropTypes.func,
  isEditable: PropTypes.bool,
  isDisabled: PropTypes.bool
};
OrderReturn.defaultProps = {
  underlineButtonLabel: "Write a review",
  buttonLabel: "Return or Replace",
  underlineButtonColour: "#ff1744",
  isEditable: false,
  isDisabled: false
};
