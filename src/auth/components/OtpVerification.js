import React from "react";
import AuthPopUp from "./AuthPopUp";
import { Icon, Button } from "xelpmoc-core";
import MediaQuery from "react-responsive";
import PropTypes from "prop-types";
import lockIcon from "./img/facebook.svg";
import ownStyles from "./OtpVerificationStyles.css";
import { default as styles } from "./AuthPopUp.css";
import Input from "../../general/components/Input";
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
  submit = () => {
    if (this.props.submit) {
      this.props.submit();
    }
  };
  render() {
    return (
      <AuthPopUp>
        <MediaQuery query="(min-device-width: 1024px)">
          <div className={styles.header}>One last step</div>
          <div className={styles.content}>
            Please enter your OTP sent to {this.props.mobileNumber}.<span
              className={ownStyles.span}
            >
              Change number
            </span>
          </div>
          <div className={ownStyles.buttonHolder}>
            <div className={ownStyles.left}>
              <Button
                backgroundColor={"transparent"}
                height={30}
                width={"auto"}
                textStyle={{ color: "#212121", fontSize: "14px" }}
                label={"Resend OTP"}
                onClick={this.callVerify}
              />
            </div>

            <div className={ownStyles.time}>30 sec</div>
          </div>
          <div className={styles.button}>
            <div className={ownStyles.submit}>
              <Button
                backgroundColor={"transparent"}
                height={40}
                width={180}
                borderColor={"#212121"}
                borderRadius={20}
                label={"Submit"}
                textStyle={{
                  color: "#212121",
                  fontSize: "14px",
                  fontFamily: "bolder"
                }}
                onClick={this.submit}
              />
            </div>
          </div>
        </MediaQuery>
        <MediaQuery query="(max-device-width: 1023px)">
          <div>
            <div className={ownStyles.iconHolder}>
              <Icon image={lockIcon} size={30} />
            </div>
            <div>
              <div className={ownStyles.content}>
                Waiting to automatically detect an SMS sent to{" "}
                {this.props.mobileNumber}.
                <span className={ownStyles.span}>Wrong number?</span>
              </div>
            </div>
            <div className={ownStyles.input}>
              <Input placeholder={"Enter 4-digit code"} />
            </div>
            <div className={ownStyles.buttonHolder}>
              <div className={ownStyles.left}>
                <Button
                  backgroundColor={"transparent"}
                  height={30}
                  width={"auto"}
                  textStyle={{ color: "#fff", fontSize: "14px" }}
                  label={"Call to verify"}
                  onClick={this.callVerify}
                />
              </div>
              <div className={ownStyles.right}>
                <Button
                  backgroundColor={"transparent"}
                  height={30}
                  width={"auto"}
                  textStyle={{ color: "#fff", fontSize: "14px" }}
                  label={"Resend OTP"}
                  onClick={this.resendOtp}
                />
                <div className={ownStyles.time}>30 sec</div>
              </div>
            </div>
          </div>
        </MediaQuery>
      </AuthPopUp>
    );
  }
}
OtpVerification.propTypes = {
  mobileNumber: PropTypes.string,
  submit: PropTypes.func,
  callVerify: PropTypes.func,
  resendOtp: PropTypes.func
};

OtpVerification.defaultProps = {
  mobileNumber: "9999999999"
};
