import React, { Component } from "react";
import Recaptcha from "react-grecaptcha";
import Config from "../../lib/config";
import PropTypes from "prop-types";
const LOCALE_ENGLISH = "en";
const THEME = "light";
const CASH_ON_DELIVERY = "COD";
const env = process.env;

class Captcha extends Component {
  verifyCallback = response => {
    if (response) {
      if (this.props.binValidationForCOD) {
        this.props.verifyCallback(response);
        this.props.binValidationForCOD(CASH_ON_DELIVERY);
      }
    }
  };

  render() {
    if (this.props.isResetCaptcha) {
      window.grecaptcha && window.grecaptcha.reset && window.grecaptcha.reset();
    }
    return (
      <Recaptcha
        sitekey={env.REACT_APP_RECAPTCHA_SITE_KEY}
        callback={this.verifyCallback}
        locale={LOCALE_ENGLISH}
        data-theme={THEME}
        className="customClassName"
      />
    );
  }
}

Captcha.propTypes = {
  getCaptcha: PropTypes.func
};

export default Captcha;
