import React from "react";
import AuthPopUp from "./AuthPopUp";
import Icon from "../../xelpmoc-core/Icon";
import Button from "../../xelpmoc-core/Button";
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
    console.log(this.props.userObj);
    if (this.props.resendOtp) {
      this.props.resendOtp(this.props.userObj);
    }
  };
  onSubmitOtp = otp => {
    if (this.props.submitOtp) {
      if (this.props.username && this.props.password) {
        this.props.submitOtp({
          username: this.props.username,
          password: this.props.password,
          otp: otp
        });
      } else {
        this.props.submitOtp(otp);
      }
    }
  };
  handleOtpInput(val) {
    if (val.length <= 6) {
      this.setState({ otp: val });
      if (val.length === 6) {
        this.onSubmitOtp(val);
      }
    }
  }
  onClickWrongNumber() {
    if (this.props.onClickWrongNumber) {
      this.props.onClickWrongNumber();
    }
  }

  render() {
    let mobileNumber;
    if (this.props.userObj && this.props.userObj.username) {
      mobileNumber = this.props.userObj.username;
    } else if (this.props.userObj && this.props.userObj.mobileNumber) {
      mobileNumber = this.props.userObj.mobileNumber;
    } else if (this.props.username) {
      mobileNumber = this.props.username;
    } else {
      mobileNumber = this.props.userObj;
    }

    return (
      <AuthPopUp>
        <MediaQuery query="(min-device-width: 1025px)">
          <div className={styles.header}>One last step</div>
          <div className={styles.content}>
            Please enter your OTP sent to {mobileNumber}.<span
              className={ownStyles.span}
            >
              Change number
            </span>
          </div>
          <div>
            <Input
              value={this.state.otp}
              placeholder={"Enter 6-digit code"}
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
                textStyle={{ color: "#212121", fontSize: 14 }}
                label={"Resend OTP"}
                onClick={() => this.resendOtp()}
              />
            </div>

            <div className={ownStyles.time}>30 sec</div>
          </div>
          {/* <div className={styles.button}>
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
          </div> */}
        </MediaQuery>
        <MediaQuery query="(max-device-width: 1024px)">
          <React.Fragment>
            <div className={ownStyles.iconHolder}>
              <Icon image={lockIcon} size={50} />
            </div>
            <div>
              <div className={ownStyles.content}>
                OTP sent to{" "}
                {mobileNumber.indexOf("@") !== -1
                  ? mobileNumber
                  : `+91${mobileNumber}`}.
                <span
                  className={ownStyles.span}
                  onClick={() => this.onClickWrongNumber()}
                >
                  Edit number
                </span>
              </div>
            </div>
            <div className={ownStyles.input}>
              <Input
                value={this.state.otp}
                placeholder={"Enter 6-digit code"}
                onChange={val => {
                  this.handleOtpInput(val);
                }}
                type="password"
              />
            </div>
            <div className={ownStyles.buttonHolder}>
              {/* <div className={ownStyles.left}>
                <Button
                  backgroundColor={"transparent"}
                  height={30}
                  textStyle={{ color: "#fff", fontSize: 14 }}
                  label={"Call to verify"}
                  onClick={() => this.callVerify()}
                />
              </div> */}
              <div className={ownStyles.right}>
                <Button
                  backgroundColor={"transparent"}
                  height={30}
                  textStyle={{ color: "#fff", fontSize: 14 }}
                  label={"Resend OTP"}
                  onClick={() => this.resendOtp()}
                />
                {/* <Button
                  backgroundColor={"transparent"}
                  height={30}
                  label={"Submit"}
                  textStyle={{ color: "#fff", fontSize: 14 }}
                  onClick={() => this.onSubmitOtp()}
                /> */}
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
  mobileNumber: "",
  userDetails: null
};
