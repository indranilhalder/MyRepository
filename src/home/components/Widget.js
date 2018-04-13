import React from "react";
import PropTypes from "prop-types";
import { SUCCESS } from "../../lib/constants";
import Observer from "@researchgate/react-intersection-observer";

export default class Widget extends React.Component {
  // componentDidMount() {
  //   console.log("COMPONENT DID MOUNT");
  //   if (
  //     this.props.isVisible &&
  //     this.props.feedComponentData.status !== SUCCESS
  //   ) {
  //     if (this.props.feedComponentData.fetchURL) {
  //       this.props.getComponentData(
  //         this.props.feedComponentData.fetchURL,
  //         this.props.positionInFeed,
  //         this.props.postData,
  //         this.props.feedComponentData.backupURL,
  //         this.props.feedComponentData.type
  //       );
  //     }
  //   }
  // }

  // componentDidUpdate() {
  //   if (
  //     this.props.isVisible &&
  //     this.props.feedComponentData.status !== SUCCESS
  //   ) {
  //     if (this.props.feedComponentData.fetchURL) {
  //       this.props.getComponentData(
  //         this.props.feedComponentData.fetchURL,
  //         this.props.positionInFeed,
  //         this.props.postData,
  //         this.props.feedComponentData.backupURL,
  //         this.props.feedComponentData.type
  //       );
  //     }
  //   }
  // }

  onChange = ({ isIntersecting, intersectionRatio }) => {
    if (isIntersecting && intersectionRatio >= 0.3) {
      if (this.props.feedComponentData.fetchURL) {
        this.props.getComponentData(
          this.props.feedComponentData.fetchURL,
          this.props.positionInFeed,
          this.props.postData,
          this.props.feedComponentData.backupURL,
          this.props.feedComponentData.type
        );
      }
    }
  };

  render() {
    return (
      <Observer onChange={this.onChange}>
        <div>{this.props.children(this.props)}</div>
      </Observer>
    );
  }
}

Widget.propTypes = {
  feedComponentData: PropTypes.object,
  positionInFeed: PropTypes.number,
  type: PropTypes.string,
  postData: PropTypes.object,
  disableGetComponentDataCall: PropTypes.bool
};

Widget.defaultProps = {
  disableGetComponentDataCall: false
};
