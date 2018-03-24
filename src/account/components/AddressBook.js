import React from "react";
import styles from "./AddressBook.css";
import Button from "../../general/components/Button.js";
import AddressItemFooter from "./AddressItemFooter.js";
import MDSpinner from "react-md-spinner";
const ADDRESS_BOOK_HEADER = "Add a new address";
const DELETE_LABEL = "Delete";
const EDIT_LABEL = "Edit";
export default class AddressBook extends React.Component {
  componentDidMount() {
    this.props.getUserAddress();
  }

  removeAddress = addressId => {
    if (this.props.removeAddress) {
      this.props.removeAddress(addressId);
    }
  };

  renderLoader = () => {
    return (
      <div className={styles.loadingIndicator}>
        <MDSpinner />
      </div>
    );
  };

  renderAddressBook = () => {
    return (
      <div className={styles.base}>
        {this.props.userAddress &&
          this.props.userAddress.addresses.map(address => {
            return (
              <div className={styles.addressBlock}>
                <div className={styles.addressHolder}>
                  <div className={styles.name}>{`${address.firstName} ${
                    address.lastName
                  }`}</div>
                  <div className={styles.address}>{address.line1}</div>
                  <div className={styles.phoneNumber}>{`Ph. ${
                    address.phone
                  }`}</div>
                </div>
                <div className={styles.actionHolder}>
                  <AddressItemFooter
                    buttonLabel={DELETE_LABEL}
                    underlineButtonLabel={EDIT_LABEL}
                    editAddress={() =>
                      this.setState({
                        isEditable: true,
                        editableAddressId: address.id
                      })
                    }
                    removeAddress={() => this.removeAddress(address.id)}
                    isEditable={true}
                  />
                </div>
              </div>
            );
          })}

        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <Button
              type="hollow"
              height={40}
              label={ADDRESS_BOOK_HEADER}
              width={200}
              textStyle={{ color: "#212121", fontSize: 14 }}
              onClick={() => this.setState({ addNewAddress: true })}
            />
          </div>
        </div>
      </div>
    );
  };
  render() {
    if (this.props.renderLoader) {
      return this.renderLoader();
    }
    return this.renderAddressBook();
  }
}
