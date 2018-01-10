import React from "react";
import PropTypes from "prop-types";
import show_password from "../../general/components/img/show_pwd.svg";
import Input from "../../general/components/Input";
import { CircleButton, Icon } from "xelpmoc-core";
import styles from "./PasswordInput.css";

class PasswordInput extends React.Component {
  constructor(props) {
    super(props);
    this.styles = this.props.styles ? this.props.styles : styles;
    this.state = {
      isPasswordVisible: this.props.passwordVisible,
      type: this.props.type,
      scalerClass: this.styles.scaler
    };
  }

  onPress = () => {
    if (!this.state.isPasswordVisible) {
      this.setState({
        type: "text",
        isPasswordVisible: true,
        scalerClass: this.styles.scalerHolder
      });
    } else {
      this.setState({
        type: "password",
        isPasswordVisible: false,
        scalerClass: this.styles.scaler
      });
    }
  };

  render() {
    return (
      <Input
        {...this.props}
        type={this.state.type}
        value={this.props.password}
        rightChild={
          <div className={styles.passWordButton}>
            <CircleButton
              color={"transparent"}
              icon={<Icon image={this.props.img} size={20} />}
              onClick={this.onPress}
            />
            <div className={this.state.scalerClass} />
          </div>
        }
      />
    );
  }
}

PasswordInput.propTypes = {
  passwordVisible: PropTypes.bool,
  type: PropTypes.string,
  password: PropTypes.string,
  img: PropTypes.string
};

PasswordInput.defaultProps = {
  passwordVisible: false,
  type: "Password",
  password: "",
  img: show_password
};

export default PasswordInput;
