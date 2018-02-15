import React, { Component } from "react";
import Recaptcha from "react-grecaptcha";
import Config from "../../lib/config";
import PropTypes from "prop-types";
const LOCALE = "en";
const THEME = "light";
class Captcha extends Component {
  verifyCallback = response => {
    if (this.props.getCaptcha) {
      this.props.getCaptcha(response);
    }
  };
  render() {
    return (
      <div>
        <Recaptcha
          sitekey={Config.reCaptChaSiteKey}
          callback={this.verifyCallback}
          locale={LOCALE}
          data-theme={THEME}
        />
      </div>
    );
  }
}

Captcha.propTypes = {
  verifyCallback: PropTypes.func,
  getCaptcha: PropTypes.func
};

export default Captcha;
