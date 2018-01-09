import React, { Component } from "react";
import MediaQuery from "react-responsive";
import CoreButton from "./Button";
import { Icon } from "xelpmoc-core";
import CircleIconWhite from "./img/circle_plus_white.svg";
import CircleIconBlack from "./img/circle_plus_black.svg";

export default class FollowButton extends Component {
  render() {
    const { type, label, icon, ...other } = this.props;
    return (
      <div>
        <MediaQuery query="(min-device-width: 1025px)">
          <CoreButton
            type={"secondary"}
            label={"Start following"}
            icon={{
              element: <Icon image={CircleIconBlack} size={20} />,
              size: 20,
              offset: 10
            }}
            {...other}
          />
        </MediaQuery>
        <MediaQuery query="(max-device-width:1024px)">
          <CoreButton
            type={"primary"}
            label={"Start following"}
            icon={{
              element: <Icon image={CircleIconWhite} size={20} />,
              size: 20,
              offset: 10
            }}
            {...other}
          />
        </MediaQuery>
      </div>
    );
  }
}
