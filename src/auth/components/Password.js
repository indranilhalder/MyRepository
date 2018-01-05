import React from "react";
import PropTypes from "prop-types";
import styles from "./Password.css";
import show_password from "../../general/components/img/show_pwd.svg";
import hide_password from "../../general/components/img/hide_pwd.svg";
import Input from "../../general/components/Input";
import { CircleButton } from "xelpmoc-core";

class Password extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isPasswordVisible: this.props.passwordVisible,
      password: "",
      disabled: this.props.disabled,
      focused: false,
      type: this.props.type,
      img: show_password
    };
    this.styles = this.props.styles ? this.props.styles : styles;
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

  onPress() {
    if (!this.state.isPasswordVisible) {
      this.setState({
        type: "text",
        isPasswordVisible: true,
        img: hide_password
      });
    } else {
      this.setState({
        type: "password",
        isPasswordVisible: false,
        img: show_password
      });
    }
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
          <Input
            type={this.state.type}
            placeholder={"Password"}
            value={this.state.password}
            placeholder={placeholder}
            onFocus={event => this.handleFocus(event)}
            onBlur={event => this.handleBlur(event)}
            rightChild={
              <div className={styles.button} onClick={() => this.onPress()}>
                <CircleButton
                  color={"transparent"}
                  icon={<img src={this.state.img} width="20" height="20" />}
                />
              </div>
            }
          />
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
  passwordVisible: PropTypes.bool,
  type: PropTypes.string
};

Password.defaultProps = {
  disabled: false,
  passwordVisible: false,
  type: "Password"
};

export default Password;
