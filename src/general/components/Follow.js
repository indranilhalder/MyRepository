import React, { Component } from "react";
import PropTypes from "prop-types";
import CoreButton from "./Button";

export default class Follow extends Component {
  onFollowClick() {
    if (this.props.onFollowClick) {
      this.props.onFollowClick();
    }
  }

  onUnFollowClick() {
    if (this.props.onUnFollowClick) {
      this.props.onUnFollowClick();
    }
  }

  handleClick() {
    if (this.props.follow) {
      this.onFollowClick();
    } else {
      this.onUnFollowClick();
    }
  }

  render() {
    let text = this.props.text;
    let width = this.props.width;
    if (this.props.follow) {
      text = "Following";
      width = 100;
    } else {
      text = "Follow";
      width = 80;
    }

    return (
      <CoreButton
        width={width}
        height={36}
        backgroundColor={"transparent"}
        borderRadius={100}
        borderColor={"#FFFFFF"}
        label={text}
        textStyle={{
          color: "#FFFFFF",
          fontSize: 14,
          fontFamily: "regular"
        }}
        onClick={() => this.handleClick()}
      />
    );
  }
}

Follow.PropTypes = {
  onFollowClick: PropTypes.func,
  onUnFollowClick: PropTypes.func,
  follow: PropTypes.bool
};

Follow.defaultProps = {
  follow: false,
  text: "Follow",
  width: 80
};
