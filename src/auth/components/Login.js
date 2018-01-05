import React, { Component } from "react";
import { Button } from "xelpmoc-core";
import MediaQuery from "react-responsive";
import Input from "../../general/components/Input";
import PasswordInput from "./PasswordInput";
import styles from "./Login.css";
import LoginButton from "./LogInButton";

class Login extends Component {
  render() {
    return (
      <div>
        <div>
          <Input placeholder={"Email or phone number"} />
          <PasswordInput placeholder={"Password"} />

          <div className={styles.forgotButton}>
            <MediaQuery query="(min-device-width: 1024px)">
              <Button
                backgroundColor={"transparent"}
                label={"FORGOT PASSWORD?"}
                textStyle={{
                  color: "#d00",
                  fontSize: 12,
                  fontFamily: "regular"
                }}
              />
            </MediaQuery>

            <MediaQuery query="(max-device-width:1023px)">
              <div className={styles.forgotButtonPosition}>
                <Button
                  backgroundColor={"transparent"}
                  label={"FORGOT PASSWORD?"}
                  textStyle={{
                    color: "#000",
                    fontSize: 12,
                    fontFamily: "regular"
                  }}
                />
              </div>
            </MediaQuery>
          </div>
        </div>
        <div className={styles.buttonLogin}>
          <div className={styles.buttonHolder}>
            <LoginButton />
          </div>
        </div>
      </div>
    );
  }
}

export default Login;
