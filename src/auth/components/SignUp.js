import React, { Component } from "react";
import PropTypes from "prop-types";
import { Button } from "xelpmoc-core";
import MediaQuery from "react-responsive";
import Input from "../../general/components/Input";
import PasswordInput from "./PasswordInput";
import styles from "./SignUp.css";

class SignUp extends Component {
  onClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };

  render() {
    return (
      <div>
        <div>
          <Input placeholder={"Name"} />
          <Input placeholder={"Email or phone number"} />
          <PasswordInput placeholder={"Password"} />
        </div>
        <div className={styles.buttonLogin}>
          <div className={styles.buttonHolder}>
            <MediaQuery query="(min-device-width: 1024px)">
              <Button
                label={"Sign Up"}
                width={200}
                height={40}
                borderColor={"#000000"}
                borderRadius={20}
                backgroundColor={"#ffffff"}
                onClick={this.onClick}
                loading={this.props.loading}
                textStyle={{
                  color: "#000000",
                  fontSize: 14,
                  fontFamily: "regular"
                }}
              />
            </MediaQuery>
            <MediaQuery query="(max-device-width:1023px)">
              <Button
                backgroundColor={"#FF1744"}
                label={"Sign Up"}
                width={150}
                height={40}
                borderRadius={20}
                onClick={this.onClick}
                loading={this.props.loading}
                textStyle={{
                  color: "#FFFFFF",
                  fontSize: 14,
                  fontFamily: "regular"
                }}
              />
            </MediaQuery>
          </div>
        </div>
      </div>
    );
  }
}

SignUp.propTypes = {
  onClick: PropTypes.func,
  loading: PropTypes.bool
};

SignUp.defaultProps = {
  loading: false
};

export default SignUp;
