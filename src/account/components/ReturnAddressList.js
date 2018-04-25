import React from "react";
import styles from "./ReturnAddressList.css";
import PropTypes from "prop-types";
import CheckBox from "../../general/components/CheckBox.js";
import ConfirmAddress from "../../cart/components/ConfirmAddress.js";
import ReturnsFrame from "./ReturnsFrame.js";
import filter from "lodash.filter";
import ReturnDateTime from "./ReturnDateTime.js";
import AddDeliveryAddress from "../../cart/components/AddDeliveryAddress.js";
import * as Cookie from "../../lib/Cookie.js";
import ReturnSummary from "./ReturnSummary.js";
import Error from "../../general/components/Error.js";
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
  FAILURE,
  MY_ACCOUNT,
  ORDER,
  ORDER_CODE,
  YES,
  NO
} from "../../lib/constants";
const REG_X_FOR_ADDRESS = /address/i;
const REG_X_FOR_DATE_TIME = /dateTime/i;
const REG_X_FOR_NEW_ADDRESS = /addDeliveryLocation/i;
const REG_X_FOR_RETURN_SUMMARY = /returnSummary/i;
const PICK_UP_ADDRESS = "Select pick up Address";

export default class ReturnAddressList extends React.Component {
  constructor(props) {
    super(props);
    this.orderCode = props.location.pathname.split("/")[2];
    this.state = {
      selectedAddress: "",
      addressSelectedByUser: false,
      selectedDate: "",
      selectedTime: "",
      addNewAddress: false,
      errorMessage: "",
      error: false
    };
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.AddUserAddressStatus === SUCCESS) {
      if (this.state.addNewAddress === true) {
        this.setState({ addNewAddress: false });
        this.props.history.goBack();
      }
    }
    if (nextProps.returnInitiateStatus === SUCCESS) {
      this.props.history.push(
        `${MY_ACCOUNT}${ORDER}/?${ORDER_CODE}=${this.orderCode}`
      );
    } else if (nextProps.returnInitiateStatus === FAILURE) {
      this.setState({
        errorMessage: nextProps.returnInitiateError,
        error: true
      });
    }
    if (nextProps.returnPinCodeStatus === FAILURE) {
      this.setState({
        errorMessage: nextProps.returnPinCodeError,
        error: true
      });
      this.props.history.goBack();
    } else if (
      nextProps.returnPinCodeStatus === SUCCESS &&
      !this.state.addressSelectedByUser
    ) {
      this.setState({ addressSelectedByUser: true });
      this.props.history.push(
        `${RETURNS_PREFIX}/${
          this.orderCode
        }${RETURN_CLIQ_PIQ}${RETURN_CLIQ_PIQ_DATE}`
      );
    }
  }

  onSelectAddress(selectedAddress) {
    this.setState({ addressSelectedByUser: false });
    let addressSelected = filter(
      this.props.returnRequest.deliveryAddressesList,
      address => {
        return address.id === selectedAddress[0];
      }
    );

    let productObject = {};
    productObject.orderCode = this.props.returnProducts.orderProductWsDTO[0].sellerorderno;
    productObject.pinCode = addressSelected[0].postalCode;
    productObject.transactionId = this.props.returnProducts.orderProductWsDTO[0].transactionId;

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
      `${RETURNS_PREFIX}/${
        this.orderCode
      }${RETURN_CLIQ_PIQ}${RETURNS_NEW_ADDRESS}`
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
      `${RETURNS_PREFIX}/${
        this.orderCode
      }${RETURN_CLIQ_PIQ}${RETURN_CLIQ_PIQ_RETURN_SUMMARY}`
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
    let isCodOrder = NO;
    let reverseSealAvailable = "N";
    if (this.props.orderDetails.paymentMethod === "COD") {
      isCodOrder = YES;
    }
    if (
      this.props.data &&
      this.props.data.reverseSeal &&
      this.props.data.reverseSeal[0] === "Yes"
    ) {
      reverseSealAvailable = "Y";
    }

    let returnCliqAndPiqObject = {};
    returnCliqAndPiqObject.returnReasonCode = "01";
    returnCliqAndPiqObject.refundType = "R";
    returnCliqAndPiqObject.isCODorder = isCodOrder;
    returnCliqAndPiqObject.orderCode = this.props.returnProducts.orderProductWsDTO[0].sellerorderno;
    returnCliqAndPiqObject.transactionId = this.props.returnProducts.orderProductWsDTO[0].transactionId;
    returnCliqAndPiqObject.ussid = this.props.returnProducts.orderProductWsDTO[0].USSID;
    returnCliqAndPiqObject.transactionType = "01";
    returnCliqAndPiqObject.returnMethod = "schedule";
    returnCliqAndPiqObject.subReasonCode = this.props.subReasonCode;
    returnCliqAndPiqObject.comment = this.props.data.comment;
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
    returnCliqAndPiqObject.reverseSealAvailable = reverseSealAvailable;
    returnCliqAndPiqObject.isDefault = this.state.selectedAddress.returnPinCodeValues;
    returnCliqAndPiqObject.scheduleReturnDate = this.state.selectedDate;
    returnCliqAndPiqObject.scheduleReturnTime = this.state.selectedTime;
    if (isCodOrder === YES) {
      if (this.props.bankDetail) {
        returnCliqAndPiqObject.accountNumber = this.props.bankDetail.accountNumber;
        returnCliqAndPiqObject.reEnterAccountNumber = this.props.bankDetail.reEnterAccountNumber;
        returnCliqAndPiqObject.accountHolderName = this.props.bankDetail.userName;
        returnCliqAndPiqObject.bankName = this.props.bankDetail.bankName;
        returnCliqAndPiqObject.IFSCCode = this.props.bankDetail.code;
      }
    }
    this.props.newReturnInitial(
      returnCliqAndPiqObject,
      this.props.returnProductDetails.orderProductWsDTO[0]
    );
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
        returnProducts={this.props.returnProducts}
        returnRequest={this.props.returnRequest}
        orderDetails={this.props.orderDetails}
      />
    );
  };

  cancel = () => {
    this.props.history.goBack();
  };
  render() {
    if (this.props.loading) {
      this.props.showSecondaryLoader();
    } else {
      this.props.hideSecondaryLoader();
    }

    if (this.props.returnRequest && this.props.returnProducts) {
      const { pathname } = this.props.location;
      return (
        <div>
          <Error message={this.state.errorMessage} show={this.state.error} />
          <React.Fragment>
            {pathname.match(REG_X_FOR_ADDRESS) && this.renderAddress()}
            {pathname.match(REG_X_FOR_DATE_TIME) && this.renderDateTime()}
            {pathname.match(REG_X_FOR_NEW_ADDRESS) && this.renderNewAddress()}
            {pathname.match(REG_X_FOR_RETURN_SUMMARY) &&
              this.renderReturnSummary()}
          </React.Fragment>
        </div>
      );
    } else {
      return null;
    }
  }
}
ReturnAddressList.propTypes = {
  address: PropTypes.string,
  subAddress: PropTypes.string,
  addressType: PropTypes.string,
  isSelect: PropTypes.bool
};
