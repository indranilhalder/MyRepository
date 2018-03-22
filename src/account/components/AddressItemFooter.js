import React from "react";
import styles from "./OrderReturn.css";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import ColourButton from "../../general/components/ColourButton";
import PropTypes from "prop-types";
export default class OrderReturn extends React.Component {
  editAddress() {
    if (this.props.editAddress) {
      this.props.editAddress();
    }
  }
  removeAddress() {
    if (this.props.removeAddress) {
      this.props.removeAddress();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.replaceHolder}>
          <div className={styles.replace}>
            <ColourButton
              label={this.props.buttonLabel}
              onClick={() => this.removeAddress()}
            />
          </div>
        </div>
        {this.props.isEditable && (
          <div className={styles.reviewHolder}>
            <div className={styles.review} onClick={() => this.editAddress()}>
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
  isEditable: PropTypes.bool
};
OrderReturn.defaultProps = {
  underlineButtonLabel: "Delete",
  buttonLabel: "Edit",
  underlineButtonColour: "#ff1744",
  isEditable: false
};
