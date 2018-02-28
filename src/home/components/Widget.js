import React from "react";
import PropTypes from "prop-types";

export default class Widget extends React.Component {
  componentDidMount() {
    if (!this.props.disableGetComponentDataCall) {
      this.props.getComponentData(
        this.props.feedComponentData.fetchURL,
        this.props.positionInFeed,
        this.props.postData
      );
    }
  }

  render() {
    return <React.Fragment>{this.props.children(this.props)}</React.Fragment>;
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
