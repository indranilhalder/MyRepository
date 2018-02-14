import React, { Component } from "react";
import Recaptcha from "react-grecaptcha";
import Config from "../../lib/config";
const verifyCallback = response => console.log(response);
const expiredCallback = () => {};
const LOCALE = "en";
const THEME = "light";
class Captcha extends Component {
  render() {
    return (
      <div>
        <Recaptcha
          sitekey={Config.reCaptChaSiteKey}
          callback={verifyCallback}
          expiredCallback={expiredCallback}
          locale={LOCALE}
          data-theme={THEME}
        />
      </div>
    );
  }
}

export default Captcha;
