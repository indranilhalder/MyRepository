import React from "react";
import BannerDesktop from "./BannerDesktop";
import BannerMobile from "./BannerMobile";
import MediaQuery from "react-responsive";
export default class Banner extends React.Component {
  render() {
    return (
      <React.Fragment>
        <MediaQuery query="(min-device-width: 1025px)">
          <BannerDesktop>{this.props.children}</BannerDesktop>
        </MediaQuery>
        <MediaQuery query="(max-device-width: 1024px)">
          <BannerMobile>{this.props.children}</BannerMobile>
        </MediaQuery>
      </React.Fragment>
    );
  }
}
