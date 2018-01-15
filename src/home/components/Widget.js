import React from "react";
import PropTypes from "prop-types";
import { PulseLoader } from "react-spinners";
import styles from "./Widget.css";

export default class Widget extends React.Component {
  componentDidMount() {
    this.props.getComponentData(
      this.props.feedComponentData.fetchURL,
      this.props.positionInFeed
    );
  }

  render() {
    return <React.Fragment>{this.props.children(this.props)}</React.Fragment>;
  }
}

Widget.propTypes = {
  feedComponentData: PropTypes.object,
  positionInFeed: PropTypes.number
};
