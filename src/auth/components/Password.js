import React from "react";
import PropTypes from "prop-types";
import show_password from "../../general/components/img/show_pwd.svg";
import hide_password from "../../general/components/img/hide_pwd.svg";
import Input from "../../general/components/Input";
import { CircleButton, Icon } from "xelpmoc-core";

class Password extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isPasswordVisible: this.props.passwordVisible,
      password: "",
      type: this.props.type,
      img: show_password
    };
    this.styles = this.props.styles;
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
    return (
      <Input
        {...this.props}
        type={this.state.type}
        placeholder={"Password"}
        value={this.state.password}
        rightChild={
          <CircleButton
            size={20}
            color={"transparent"}
            icon={<Icon image={this.state.img} />}
            onClick={() => this.onPress()}
          />
        }
      />
    );
  }
}

Password.propTypes = {
  passwordVisible: PropTypes.bool,
  type: PropTypes.string
};

Password.defaultProps = {
  passwordVisible: false,
  type: "Password"
};

export default Password;
