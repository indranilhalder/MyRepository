import React, { Component } from "react";
import PropTypes from "prop-types";
import CoreButton from "./Button";

export default class Follow extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isFollow: this.props.follow
    };
  }

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
    this.setState({ isFollow: !this.state.isFollow });
  }

  render() {
    if (this.state.isFollow) {
      this.onFollowClick();
    } else {
      this.onUnFollowClick();
    }

    return (
      <CoreButton
        width={80}
        height={36}
        backgroundColor={"transparent"}
        borderRadius={100}
        borderColor={"#FFFFFF"}
        label={"Follow"}
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
  follow: false
};
