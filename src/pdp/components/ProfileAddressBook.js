import React from "react";
import styles from "./ProfileAddressBook.css";
import OrderReturn from "../../account/components/OrderReturn";
import PropTypes from "prop-types";

export default class ProfileAddressBook extends React.Component {
  editAddress() {
    if (this.props.editAddress) {
      this.props.editAddress();
    }
  }
  deleteAddress() {
    if (this.props.deleteAddress) {
      this.props.deleteAddress();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.container}>
          <div className={styles.header}>{this.props.userName}</div>
          <div className={styles.address}>{this.props.address}</div>
          <div className={styles.contact}>{this.props.contactNumber}</div>
        </div>
        {this.props.underlineButtonLabel &&
          this.props.buttonLabel && (
            <div className={styles.buttonHolder}>
              <OrderReturn
                underlineButtonLabel={this.props.underlineButtonLabel}
                buttonLabel={this.props.buttonLabel}
                writeReview={() => this.editAddress()}
                replaceItem={() => this.deleteAddress()}
                isEditable={true}
              />
            </div>
          )}
      </div>
    );
  }
}

OrderReturn.propTypes = {
  userName: PropTypes.string,
  underlineButtonLabel: PropTypes.string,
  buttonLabel: PropTypes.string,
  address: PropTypes.string,
  contactNumber: PropTypes.string,
  editAddress: PropTypes.func,
  deleteAddress: PropTypes.func
};
