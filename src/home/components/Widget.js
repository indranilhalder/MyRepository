import React from "react";
import PropTypes from "prop-types";
import { SUCCESS } from "../../lib/constants";

export default class Widget extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isVisible: false
    };
  }
  componentDidMount() {
    if (
      this.props.feedComponentData.fetchURL &&
      this.props.feedComponentData.status !== SUCCESS
    ) {
      this.props.getComponentData(
        this.props.feedComponentData.fetchURL,
        this.props.positionInFeed,
        this.props.postData,
        this.props.feedComponentData.backupURL,
        this.props.feedComponentData.type
      );
    }
  }

  render() {
    return <div>{this.props.children(this.props)}</div>;
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
