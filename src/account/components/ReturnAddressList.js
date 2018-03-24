import React from "react";
import styles from "./ReturnAddressList.css";
import PropTypes from "prop-types";
import CheckBox from "../../general/components/CheckBox.js";
import ConfirmAddress from "../../cart/components/ConfirmAddress.js";
import ReturnsFrame from "./ReturnsFrame.js";
import filter from "lodash/filter";
import ReturnDateTime from "./ReturnDateTime.js";
import AddDeliveryAddress from "../../cart/components/AddDeliveryAddress.js";
import * as Cookie from "../../lib/Cookie.js";
import ReturnSummary from "./ReturnSummary.js";
import {
  RETURN_CLIQ_PIQ,
  RETURN_CLIQ_PIQ_DATE,
  RETURNS_PREFIX,
  RETURNS_NEW_ADDRESS,
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  RETURN_CLIQ_PIQ_RETURN_SUMMARY,
  SUCCESS,
  FAILURE
} from "../../lib/constants";
const REG_X_FOR_ADDRESS = /address/i;
const REG_X_FOR_DATE_TIME = /dateTime/i;
const REG_X_FOR_NEW_ADDRESS = /addDeliveryLocation/i;
const REG_X_FOR_RETURN_SUMMARY = /returnSummary/i;
const ORDER_ID = "345-34534534";
const PICK_UP_ADDRESS = "Select pick up Address";

export default class ReturnAddressList extends React.Component {
  state = {
    selectedAddress: "",
    addressSelected: false,
    selectedDate: "",
    selectedTime: "",
    addNewAddress: false
  };
  componentDidMount() {
    if (this.props.getReturnRequest) {
      this.props.getReturnRequest("180314-000-111548", "273570000120027");
    }
    this.props.returnProductDetails();
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.AddUserAddressStatus === SUCCESS) {
      if (this.state.addNewAddress === true) {
        this.setState({ addNewAddress: false });
        this.props.history.goBack();
      }
    }
    if (nextProps.returnPinCodeStatus === FAILURE) {
      this.props.history.goBack();
    } else if (nextProps.returnPinCodeStatus === SUCCESS) {
      this.props.history.push(
        `${RETURNS_PREFIX}/${ORDER_ID}${RETURN_CLIQ_PIQ}${RETURN_CLIQ_PIQ_DATE}`
      );
    }
  }

  onSelectAddress(selectedAddress) {
    let addressSelected = filter(
      this.props.returnRequest.deliveryAddressesList,
      address => {
        return address.id === selectedAddress[0];
      }
    );

    let productObject = {};
    productObject.orderCode = "180314-000-111548";
    productObject.pinCode = addressSelected[0].postalCode;
    productObject.transactionId = "273570000120027";

    if (this.props.returnPinCode) {
      this.props.returnPinCode(productObject);
    }

    this.setState({
      selectedAddress: addressSelected[0]
    });
  }

  addNewAddress = () => {
    this.setState({ addNewAddress: true });
    this.props.history.push(
      `${RETURNS_PREFIX}/${ORDER_ID}${RETURN_CLIQ_PIQ}${RETURNS_NEW_ADDRESS}`
    );
  };
  renderAddress = () => {
    if (this.props.returnRequest) {
      return (
        <ReturnsFrame
          headerText={PICK_UP_ADDRESS}
          onCancel={() => this.cancel()}
        >
          <div className={styles.addInitialAddAddress}>
            <ConfirmAddress
              address={
                this.props.returnRequest.deliveryAddressesList &&
                this.props.returnRequest.deliveryAddressesList.map(address => {
                  return {
                    addressTitle: address.addressType,
                    addressDescription: `${address.line1} ${address.town} ${
                      address.city
                    }, ${address.state} ${address.postalCode}`,
                    value: address.id,
                    selected: address.defaultAddress
                  };
                })
              }
              onNewAddress={() => this.addNewAddress()}
              onSelectAddress={address => this.onSelectAddress(address)}
            />
          </div>
        </ReturnsFrame>
      );
    } else {
      return null;
    }
  };

  addAddress = address => {
    if (this.props.addUserAddress) {
      let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
      let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
      let cartDetailsLoggedInUser = Cookie.getCookie(
        CART_DETAILS_FOR_LOGGED_IN_USER
      );
      this.props.addUserAddress(address, true);
      this.setState({ addNewAddress: false });
    }
  };

  renderNewAddress = () => {
    return (
      <div className={styles.base}>
        <AddDeliveryAddress
          addUserAddress={address => this.addAddress(address)}
          {...this.state}
          onChange={val => this.onChange(val)}
        />
      </div>
    );
  };

  renderAddressCard = () => {
    return (
      <div className={styles.cardOffset}>
        <div className={styles.content}>
          <div className={styles.checkBoxHolder}>
            <CheckBox selected={true} />
          </div>

          <div className={styles.home}>
            {this.state.selectedAddress.addressType}
          </div>

          <div className={styles.addressDetails}>
            {this.state.selectedAddress.formattedAddress}
          </div>

          <div className={styles.address}>
            {this.state.selectedAddress.state}
          </div>
        </div>
      </div>
    );
  };

  onSelectTime = val => {
    this.setState({ selectedTime: val });
    this.props.history.push(
      `${RETURNS_PREFIX}/${ORDER_ID}${RETURN_CLIQ_PIQ}${RETURN_CLIQ_PIQ_RETURN_SUMMARY}`
    );
  };
  renderDateTime = () => {
    if (this.props.returnRequest) {
      return (
        <ReturnDateTime
          timeSlot={this.props.returnRequest.returnTimeSlots}
          dateSlot={this.props.returnRequest.returnDates}
          selectedAddress={this.state.selectedAddress}
          onDateSelect={val => this.setState({ selectedDate: val })}
          onTimeSelect={val => this.onSelectTime(val)}
          onCancel={() => this.cancel()}
        />
      );
    } else {
      return null;
    }
  };

  newReturnInitiate = () => {
    let returnCliqAndPiqObject = {};
    returnCliqAndPiqObject.returnReasonCode = "01";
    returnCliqAndPiqObject.refundType = "R";
    returnCliqAndPiqObject.isCODorder = "N";
    returnCliqAndPiqObject.orderCode = this.props.returnProducts.orderProductWsDTO[0].sellerorderno;
    returnCliqAndPiqObject.transactionId = this.props.returnProducts.orderProductWsDTO[0].transactionId;
    returnCliqAndPiqObject.ussid = this.props.returnProducts.orderProductWsDTO[0].USSID;
    returnCliqAndPiqObject.transactionType = "01";
    returnCliqAndPiqObject.returnMethod = "schedule";
    returnCliqAndPiqObject.subReasonCode = "JEW2S2";
    returnCliqAndPiqObject.comment = "Test";
    returnCliqAndPiqObject.addressType = this.state.selectedAddress.addressType;
    returnCliqAndPiqObject.firstName = this.state.selectedAddress.firstName;
    returnCliqAndPiqObject.lastName = this.state.selectedAddress.lastName;
    returnCliqAndPiqObject.addrLine1 = this.state.selectedAddress.line1;
    returnCliqAndPiqObject.addrLine2 = "";
    returnCliqAndPiqObject.addrLine3 = "";
    returnCliqAndPiqObject.landMark = this.state.selectedAddress.landmark;
    returnCliqAndPiqObject.pincode = this.state.selectedAddress.postalCode;
    returnCliqAndPiqObject.phoneNumber = this.state.selectedAddress.phone;
    returnCliqAndPiqObject.city = this.state.selectedAddress.town;
    returnCliqAndPiqObject.state = this.state.selectedAddress.state;
    returnCliqAndPiqObject.country = this.state.selectedAddress.country.name;
    returnCliqAndPiqObject.reverseSealAvailable = "N";
    returnCliqAndPiqObject.isDefault = this.state.selectedAddress.returnPinCodeValues;
    returnCliqAndPiqObject.scheduleReturnDate = this.state.selectedDate;
    returnCliqAndPiqObject.scheduleReturnTime = this.state.selectedTime;
    this.props.newReturnInitiateForCliqAndPiq(returnCliqAndPiqObject);
  };
  renderReturnSummary = () => {
    return (
      <ReturnSummary
        onCancel={() => this.cancel()}
        onContinue={() => this.newReturnInitiate()}
        selectedAddress={this.state.selectedAddress}
        dateSelected={this.state.dateSelected}
        timeSelected={this.state.timeSelected}
        onChangeAddress={() => this.cancel()}
      />
    );
  };

  cancel = () => {
    this.props.history.goBack();
  };
  render() {
    const { pathname } = this.props.location;
    return (
      <div>
        <React.Fragment>
          {pathname.match(REG_X_FOR_ADDRESS) && this.renderAddress()}
          {pathname.match(REG_X_FOR_DATE_TIME) && this.renderDateTime()}
          {pathname.match(REG_X_FOR_NEW_ADDRESS) && this.renderNewAddress()}
          {pathname.match(REG_X_FOR_RETURN_SUMMARY) &&
            this.renderReturnSummary()}
        </React.Fragment>
      </div>
    );
  }
}
ReturnAddressList.propTypes = {
  address: PropTypes.string,
  subAddress: PropTypes.string,
  addressType: PropTypes.string,
  isSelect: PropTypes.bool
};
