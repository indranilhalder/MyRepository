import React from "react";
import ReactPlayer from "react-player";
import PropTypes from "prop-types";
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
Video.propTypes = {
  url: PropTypes.string,
  playing: PropTypes.bool
};
