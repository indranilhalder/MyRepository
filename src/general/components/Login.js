import React, { Component } from "react";
import { Input, Button } from "xelpmoc-core";
import MediaQuery from "react-responsive";
import styles from "./Login.css";
import Password from "./Password";

class Login extends Component {
  render() {
    return (
      <div className={styles.container}>
        <div className={styles.subContainer}>
          <Input placeholder={"Email or phone number"} styles={styles} />
          <Password placeholder={"Password"} />

          <div className={styles.forgotButton}>
            <MediaQuery query="(min-device-width: 1024px)">
              <Button
                className={styles.forgotText}
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
              <Button
                className={styles.forgotText}
                backgroundColor={"transparent"}
                label={"FORGOT PASSWORD?"}
                textStyle={{
                  color: "#000",
                  fontSize: 12,
                  fontFamily: "regular"
                }}
              />
            </MediaQuery>
          </div>
        </div>
        <div className={styles.buttonContainer}>
          <MediaQuery query="(min-device-width: 1024px)">
            <Button
              label={"Sign In"}
              backgroundColor={"#FF1744"}
              width={100}
              borderRadius={20}
            />
          </MediaQuery>

          <MediaQuery query="(max-device-width:1023px)">
            <Button
              label={"Login"}
              backgroundColor={"#FF1744"}
              width={100}
              borderRadius={20}
            />
          </MediaQuery>
        </div>
      </div>
    );
  }
}

export default Login;
