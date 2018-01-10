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
      isPasswordVisible: this.props.passwordVisible
    };
  }

  onPress = () => {
    this.setState({ isPasswordVisible: !this.state.isPasswordVisible });
  };

  render() {
    let scalerClass = this.styles.scaler;
    let type = this.props.type;

    if (this.state.isPasswordVisible) {
      scalerClass = this.styles.scalerHolder;
      type = "text";
    }
    return (
      <Input
        {...this.props}
        type={type}
        value={this.props.password}
        rightChild={
          <div className={styles.passWordButton}>
            <CircleButton
              color={"transparent"}
              icon={<Icon image={this.props.img} size={20} />}
              onClick={this.onPress}
            />
            <div className={scalerClass} />
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
