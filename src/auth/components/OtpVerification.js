import React from "react";
import AuthPopUp from "./AuthPopUp";
import { Icon, Button } from "xelpmoc-core";
import MediaQuery from "react-responsive";
import PropTypes from "prop-types";
import lockIcon from "./img/facebook.svg";
import styles from "./OtpVerificationStyles.css";
export default class OtpVerification extends React.Component {
  callVerify = () => {
    if (this.props.callVerify) {
      this.props.callVerify();
    }
  };
  resendOtp = () => {
    if (this.props.resendOtp) {
      this.props.resendOtp();
    }
  };
  render() {
    return (
      <AuthPopUp>
        <MediaQuery query="(min-device-width: 1024px)">
          <div>
            <div>Please enter your OTP sent to {this.props.mobileNumber}</div>
          </div>
        </MediaQuery>
        <MediaQuery query="(max-device-width: 1023px)">
          <div>
            <div style={{ textAlign: "center", alignSelf: "center" }}>
              <Icon image={lockIcon} size={30} />
            </div>
            <div>
              Waiting to automatically detect an SMS sent to{" "}
              {this.props.mobileNumber}
            </div>
            <div className={styles.buttonHolder}>
              <Button
                backgroundColor={"transparent"}
                height={30}
                width={"auto"}
                textStyle={{ color: "#fff" }}
                label={"Call to verify"}
                onClick={this.callVerify}
              />
              <Button
                backgroundColor={"transparent"}
                height={30}
                width={"auto"}
                textStyle={{ color: "#fff" }}
                label={"Resend OTP"}
                onClick={this.resendOtp}
              />
            </div>
            <div style={{ float: "right" }}>
              <div>30 sec</div>
            </div>
          </div>
        </MediaQuery>
      </AuthPopUp>
    );
  }
}
OtpVerification.propTypes = {
  mobileNumber: PropTypes.string,
  callVerify: PropTypes.func,
  resendOtp: PropTypes.func
};

OtpVerification.defaultProps = {
  mobileNumber: "9999999999"
};
