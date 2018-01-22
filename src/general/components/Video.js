import React from "react";
import ReactPlayer from "react-player";

export default class Video extends React.Component {
  render() {
    return (
      <ReactPlayer
        url={this.props.url}
        playing={this.props.playing}
        width="100%"
        height="100%"
      />
    );
  }
}
