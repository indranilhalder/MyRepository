import React from "react";

import { Button } from "xelpmoc-core";
import MediaQuery from "react-responsive";
export default class LogInButton extends React.Component {
  render() {
    return (
      <div className="App">
        <MediaQuery query="(min-device-width: 1024px)">
          <Button
            label={"Sign in"}
            width={150}
            backgroundColor={"#FF1744"}
            textStyle={{ color: "#FFF", fontSize: 14 }}
          />
        </MediaQuery>
        <MediaQuery query="(max-device-width:1023px)">
          <Button
            backgroundColor={"#FF1744"}
            label={"Login"}
            width={100}
            textStyle={{ color: "#FFF", fontSize: 14 }}
          />
        </MediaQuery>
      </div>
    );
  }
}
