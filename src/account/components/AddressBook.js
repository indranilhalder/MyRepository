import React from "react";
import styles from "./AddressBook.css";
import Button from "../../general/components/Button.js";
import AddressItemFooter from "./AddressItemFooter.js";
import Loader from "../../general/components/Loader";
import {
  MY_ACCOUNT_PAGE,
  MY_ACCOUNT_ADDRESS_EDIT_PAGE,
  MY_ACCOUNT_ADDRESS_ADD_PAGE,
  ADDRESS_BOOK
} from "../../lib/constants.js";

const ADDRESS_BOOK_HEADER = "Add a new address";
const DELETE_LABEL = "Delete";
const EDIT_LABEL = "Edit";
const NO_ADDRESS_TEXT = "No Saved Address";

export default class AddressBook extends React.Component {
  componentDidMount() {
    this.props.setHeaderText(ADDRESS_BOOK);
    this.props.getUserAddress();
  }
  componentDidUpdate() {
    this.props.setHeaderText(ADDRESS_BOOK);
  }
  removeAddress = addressId => {
    if (this.props.removeAddress) {
      this.props.removeAddress(addressId);
    }
  };

  renderLoader = () => {
    return <Loader />;
  };

  editAddress = address => {
    this.props.history.push({
      pathname: `${MY_ACCOUNT_PAGE}${MY_ACCOUNT_ADDRESS_EDIT_PAGE}`,
      state: {
        addressDetails: address
      }
    });
  };

  addAddress = () => {
    this.props.history.push({
      pathname: `${MY_ACCOUNT_PAGE}${MY_ACCOUNT_ADDRESS_ADD_PAGE}`
    });
  };
  renderAddressBook = () => {
    return (
      <div className={styles.base}>
        {this.props.userAddress &&
          this.props.userAddress.addresses &&
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
                    editAddress={() => this.editAddress(address)}
                    removeAddress={() => this.removeAddress(address.id)}
                    isEditable={true}
                  />
                </div>
              </div>
            );
          })}
        {this.props.userAddress &&
          !this.props.userAddress.addresses && (
            <div className={styles.noAddressBlock}>{NO_ADDRESS_TEXT}</div>
          )}
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <Button
              type="hollow"
              height={40}
              label={ADDRESS_BOOK_HEADER}
              width={200}
              textStyle={{ color: "#212121", fontSize: 14 }}
              onClick={() => this.addAddress()}
            />
          </div>
        </div>
      </div>
    );
  };
  render() {
    if (this.props.loading) {
      this.props.showSecondaryLoader();
    } else {
      this.props.hideSecondaryLoader();
    }

    return this.renderAddressBook();
  }
}
