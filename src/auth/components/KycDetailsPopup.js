import React from "react";
import { Icon } from "xelpmoc-core";
import ColourButton from "../../general/components/ColourButton";
import PropTypes from "prop-types";
import lockBlackIcon from "./img/lockBlackIcon.svg";
import Styles from "./KycDetailsPopup.css";
import Input2 from "../../general/components/Input2";
export default class KycDetailsPopup extends React.Component {
  callVerify() {
    if (this.props.callVerify) {
      this.props.callVerify();
    }
  }
  resendOtp() {
    if (this.props.resendOtp) {
      this.props.resendOtp();
    }
  }

  handleOtpInput(val) {
    this.setState({ otp: val });
  }
  wrongNumber() {
    if (this.props.wrongNumber) {
      this.props.wrongNumber();
    }
  }
  render() {
    return (
      <div className={Styles.base}>
        <div className={Styles.header}>
          Verify Phone number to complete KYC Details
        </div>
        <div className={Styles.iconHolder}>
          <Icon image={lockBlackIcon} size={50} />
        </div>
        <div className={Styles.content}>
          Waiting to automatically detect an SMS sent to
          <span className={Styles.number}> {this.props.mobileNumber}.</span>
          <span className={Styles.span} onClick={() => this.wrongNumber()}>
            Wrong number?
          </span>
        </div>
        <div className={Styles.input}>
          <Input2
            borderColor="#fff"
            placeholder={"Enter 4-digit code"}
            styles={{ color: "#000000" }}
            onChange={val => {
              this.handleOtpInput(val);
            }}
            type="number"
          />
        </div>
        <div className={Styles.buttonHolder}>
          <div className={Styles.leftButton}>
            <ColourButton
              label={"Call to verify"}
              onClick={() => this.callVerify()}
            />
          </div>
          <div className={Styles.rightButton}>
            <ColourButton
              label={"Resend OTP"}
              onClick={() => this.resendOtp()}
            />
            <div className={Styles.time}>30 sec</div>
          </div>
        </div>
      </div>
    );
  }
}
KycDetailsPopup.propTypes = {
  mobileNumber: PropTypes.string,
  wrongNumber: PropTypes.func,
  callVerify: PropTypes.func,
  resendOtp: PropTypes.func
};

KycDetailsPopup.defaultProps = {
  mobileNumber: ""
};
