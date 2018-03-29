import React from "react";
import DiscoverMore500 from "./DiscoverMore500.js";
import DiscoverMoreCarousel from "./DiscoverMoreCarousel.js";
import PropTypes from "prop-types";

export default class DiscoverMore extends React.Component {
  render() {
    if (this.props.feedComponentData.data) {
      if (this.props.feedComponentData.data.type !== "L1") {
        console.log("BLARGH");
        return (
          <DiscoverMore500
            feedComponentData={this.props.feedComponentData.data}
          />
        );
      } else {
        return (
          <DiscoverMoreCarousel
            feedComponentData={this.props.feedComponentData.data}
          />
        );
      }
    }

    return null;
  }
}

DiscoverMore.propTypes = {
  feedComponentData: PropTypes.shape({
    title: PropTypes.string,
    data: PropTypes.shape({
      data: PropTypes.arrayOf(
        PropTypes.shape({
          imageURL: PropTypes.string,
          title: PropTypes.string
        })
      )
    })
  })
};
