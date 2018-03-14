import React from "react";
import styles from "./AddressBook.css";
import OrderReturn from "./OrderReturn.js";
import PropTypes from "prop-types";
export default class AddressBook extends React.Component {
  deleteItem() {
    if (this.props.deleteItem) {
      this.props.deleteItem();
    }
  }
  onEdit() {
    if (this.props.onEdit) {
      this.props.onEdit();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.addressHolder}>
          <div className={styles.name}>{this.props.name}</div>
          <div className={styles.address}>{this.props.address}</div>
          <div className={styles.phoneNumber}>{`Ph. ${
            this.props.phoneNumber
          }`}</div>
        </div>
        <div className={styles.actionHolder}>
          <OrderReturn
            replaceItem={() => this.deleteItem()}
            buttonLabel="Delete"
            underlineButtonLabel="Edit"
            writeReview={() => this.onEdit()}
            isEditable={true}
          />
        </div>
      </div>
    );
  }
}
AddressBook.propTypes = {
  name: PropTypes.string,
  address: PropTypes.string,
  phoneNumber: PropTypes.string,
  deleteItem: PropTypes.func,
  onEdit: PropTypes.func
};
