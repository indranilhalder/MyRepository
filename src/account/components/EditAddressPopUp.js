import React from "react";
import styles from "./EditAddressPopUp.css";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import OrderReturn from "../../account/components/OrderReturn";
import { SUCCESS, EDIT_YOUR_ADDRESS } from "../../lib/constants";
const CANCEL_TEXT = "Cancel";
const SAVE_CHANGES = "Save changes";
let addressDetails;
export default class EditAddressPopUp extends React.Component {
  constructor(props) {
    super(props);
    addressDetails = this.props.location.state.addressDetails;
    this.state = {
      countryIso: addressDetails.country.isocode,
      addressType: addressDetails.addressType,
      phone: addressDetails.phone,
      firstName: addressDetails.firstName,
      lastName: addressDetails.lastName,
      postalCode: addressDetails.postalCode,
      line1: addressDetails.line1,
      landmark: addressDetails.landmark,
      line2: addressDetails.line2,
      line3: "",
      town: addressDetails.town,
      addressId: addressDetails.id,
      defaultFlag: addressDetails.defaultAddress,
      state: addressDetails.state
    };
  }
  componentDidMount() {
    this.props.setHeaderText(EDIT_YOUR_ADDRESS);
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.getPinCodeDetails) {
      this.setState({
        state: nextProps.getPinCodeDetails.state.name,
        town: nextProps.getPinCodeDetails.cityName
      });
    }
  }
  componentWillUnmount() {
    this.props.resetAutoPopulateDataForPinCode();
  }
  componentDidUpdate(prevProps, prevState) {
    this.props.setHeaderText(EDIT_YOUR_ADDRESS);
    if (
      this.props.editAddressStatus !== prevProps.editAddressStatus &&
      this.props.editAddressStatus === SUCCESS
    ) {
      this.props.history.goBack();
    }
  }
  onChange(val) {
    this.setState(val);
  }
  onChangePinCode(postalCode) {
    if (postalCode.length <= 6) {
      this.setState({ postalCode });
    }
    if (postalCode.length === 6) {
      this.props.getPinCode(postalCode);
    }
  }
  editAddress(val) {
    if (this.props.editAddress) {
      let addressDetails = this.state;
      this.props.editAddress(addressDetails);
    }
  }

  cancelAddress = () => {
    this.props.history.goBack();
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.holder}>
          <div className={styles.formHolder}>
            <div className={styles.container}>
              <Input2
                value={this.state.firstName}
                placeholder="Name*"
                boxy={true}
                textStyle={{ fontSize: 14 }}
                height={33}
                onChange={firstName => this.onChange({ firstName })}
              />
            </div>
            <div className={styles.container}>
              <Input2
                type="number"
                value={this.state.phone}
                placeholder="Phone number*"
                boxy={true}
                textStyle={{ fontSize: 14 }}
                height={33}
                onChange={phone => this.onChange({ phone })}
              />
            </div>
            {this.state.landmark &&
              this.state.landmark !== "undefined" && (
                <div className={styles.container}>
                  <Input2
                    value={this.state.landmark}
                    placeholder="Landmark"
                    boxy={true}
                    textStyle={{ fontSize: 14 }}
                    height={33}
                    onChange={landmark => this.onChange({ landmark })}
                  />
                </div>
              )}

            <div className={styles.container}>
              <Input2
                value={this.state.line1}
                boxy={true}
                textStyle={{ fontSize: 14 }}
                height={33}
                onChange={line1 => this.onChange({ line1 })}
              />
            </div>
            {this.state.line2 &&
              this.state.line2 !== "undefined" && (
                <div className={styles.container}>
                  <Input2
                    value={this.state.line2}
                    boxy={true}
                    textStyle={{ fontSize: 14 }}
                    height={33}
                    onChange={line2 => this.onChange({ line2 })}
                  />
                </div>
              )}
            <div className={styles.container}>
              <Input2
                value={this.state.postalCode}
                placeholder="Enter a pincode/zipcode*"
                boxy={true}
                textStyle={{ fontSize: 14 }}
                height={33}
                onChange={postalCode => this.onChangePinCode(postalCode)}
              />
            </div>
            <div className={styles.container}>
              <Input2
                value={this.state.town}
                boxy={true}
                textStyle={{ fontSize: 14 }}
                height={33}
                onChange={town => this.onChange({ town })}
              />
            </div>
            <div className={styles.container}>
              <Input2
                value={this.state.state}
                boxy={true}
                textStyle={{ fontSize: 14 }}
                height={33}
                onChange={state => this.onChange({ state })}
              />
            </div>
          </div>
          <div className={styles.buttonHolder}>
            <OrderReturn
              isEditable={true}
              buttonLabel={CANCEL_TEXT}
              underlineButtonLabel={SAVE_CHANGES}
              writeReview={() => this.editAddress()}
              replaceItem={() => this.cancelAddress()}
            />
          </div>
        </div>
      </div>
    );
  }
}
EditAddressPopUp.propTypes = {
  country: PropTypes.string,
  buttonLabel: PropTypes.string,
  underlineButtonLabel: PropTypes.string,
  userName: PropTypes.string,
  contactNumber: PropTypes.string,
  line1: PropTypes.string,
  line2: PropTypes.string,
  postalCode: PropTypes.bool,
  state: PropTypes.string,
  cancelAddress: PropTypes.func,
  saveChanges: PropTypes.func,
  onChange: PropTypes.func
};
