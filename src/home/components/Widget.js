import React from "react";
import PropTypes from "prop-types";
import { SUCCESS } from "../../lib/constants";
// import Observer from "@researchgate/react-intersection-observer";

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

  onChange = ({ isIntersecting, intersectionRatio }) => {
    if (isIntersecting) {
      this.setState({ isVisible: true });
      console.log("IS INTERSECTING");
    }
  };

  render() {
    return (
      // <Observer onChange={this.onChange}>
      // {this.state.isVisible ? (
      <div>{this.props.children(this.props)}</div>
      // ) : (
      // <div />
      // /        )}
      // </Observer>
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
