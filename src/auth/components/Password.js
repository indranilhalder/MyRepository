import React from "react";
import PropTypes from "prop-types";
import styles from "./Password.css";
import show_password from "../../general/components/img/show_pwd.svg";
import hide_password from "../../general/components/img/hide_pwd.svg";
import { Input, CircleButton } from "xelpmoc-core";

class Password extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isPasswordVisible: this.props.passwordVisible,
      password: "",
      disabled: this.props.disabled,
      focused: false
    };
    this.styles = this.props.styles ? this.props.styles : styles;
  }

  handleChange(event) {
    if (this.props.onChange) {
      this.props.onChange(event.target.value);
    }
    this.setState({ value: event.target.value });
  }
  handleFocus(event) {
    if (this.props.onFocus) {
      this.props.onFocus(event);
    }
    this.setState({ focused: true });
  }
  handleBlur(event) {
    if (this.props.onBlur) {
      this.props.onBlur(event);
    }
    this.setState({ focused: false });
  }

  render() {
    var { rightChild, placeholder, ...other } = this.props;
    let className = this.styles.input;
    if (this.state.focused) {
      className = this.styles.focused;
    }
    if (this.state.disabled) {
      className = this.styles.disabled;
    }
    return (
      <div className={styles.container}>
        <div>
          {this.state.isPasswordVisible ? (
            <Input
              type={"text"}
              placeholder={"Email or phone number"}
              value={this.state.password}
              placeholder={placeholder}
            />
          ) : (
            <Input
              type={"password"}
              placeholder={"Email or phone number"}
              styles={styles}
              value={this.state.password}
              placeholder={placeholder}
            />
          )}
          <div
            className={styles.button}
            onClick={() =>
              this.setState({
                isPasswordVisible: !this.state.isPasswordVisible
              })
            }
          >
            {this.state.isPasswordVisible ? (
              <CircleButton
                color={"transparent"}
                icon={<img src={hide_password} width="20" height="20" />}
              />
            ) : (
              <CircleButton
                color={"transparent"}
                icon={<img src={show_password} width="20" height="20" />}
              />
            )}{" "}
          </div>
        </div>
      </div>
    );
  }
}

Password.propTypes = {
  onFocus: PropTypes.func,
  onBlur: PropTypes.func,
  onChange: PropTypes.func,
  disabled: PropTypes.bool,
  maxLength: PropTypes.number,
  value: PropTypes.string,
  passwordVisible: PropTypes.bool
};

Password.defaultProps = {
  disabled: false,
  passwordVisible: false
};

export default Password;
