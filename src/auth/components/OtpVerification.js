import React from "react";
import AuthPopUp from "./AuthPopUp";
import { Icon, Button } from "xelpmoc-core";
import MediaQuery from "react-responsive";
import PropTypes from "prop-types";
import lockIcon from "./img/otpLock.svg";
import ownStyles from "./OtpVerificationStyles.css";
import { default as styles } from "./AuthPopUp.css";
import Input from "../../general/components/Input";
export default class OtpVerification extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      otp: ""
    };
  }
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
  onSubmitOtp = () => {
    if (this.props.submitOtp) {
      this.props.submitOtp(this.state.otp);
    }
  };
  handleOtpInput(val) {
    this.setState({ otp: val });
  }
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
                textStyle={{ color: "#212121", fontSize: 14 }}
                label={"Resend OTP"}
                onClick={() => this.callVerify()}
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
                  fontSize: 14,
                  fontFamily: "bolder"
                }}
                onClick={() => this.onSubmitOtp()}
              />
            </div>
          </div>
        </MediaQuery>
        <MediaQuery query="(max-device-width: 1023px)">
          <React.Fragment>
            <div className={ownStyles.iconHolder}>
              <Icon image={lockIcon} size={50} />
            </div>
            <div>
              <div className={ownStyles.content}>
                Please enter the OTP sent to {this.props.mobileNumber}.
                <span className={ownStyles.span}>Wrong number?</span>
              </div>
            </div>
            <div className={ownStyles.input}>
              <Input
                placeholder={"Enter 4-digit code"}
                onChange={val => {
                  this.handleOtpInput(val);
                }}
                type="tel"
              />
            </div>
            <div className={ownStyles.buttonHolder}>
              <div className={ownStyles.left}>
                <Button
                  backgroundColor={"transparent"}
                  height={30}
                  textStyle={{ color: "#fff", fontSize: 14 }}
                  label={"Call to verify"}
                  onClick={() => this.callVerify()}
                />
              </div>
              <div className={ownStyles.right}>
                <Button
                  backgroundColor={"transparent"}
                  height={30}
                  textStyle={{ color: "#fff", fontSize: 14 }}
                  label={"Resend OTP"}
                  onClick={() => this.resendOtp()}
                />
                <Button
                  backgroundColor={"transparent"}
                  height={30}
                  label={"Submit"}
                  textStyle={{ color: "#fff", fontSize: 14 }}
                  onClick={() => this.onSubmitOtp()}
                />
                <div className={ownStyles.time}>30 sec</div>
              </div>
            </div>
          </React.Fragment>
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
  mobileNumber: ""
};
