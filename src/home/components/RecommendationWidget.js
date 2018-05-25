import React from "react";
import FeedComponent from "./FeedComponent";
import PropTypes from "prop-types";
import { transformData } from "./utils.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class RecommendationWidget extends React.Component {
  handleClick() {
    if (this.props.feedComponentData.webURL) {
      const urlSuffix = this.props.feedComponentData.webURL.replace(
        TATA_CLIQ_ROOT,
        "$1"
      );
      this.props.history.push(urlSuffix);
      if (this.props.setClickedElementId) {
        this.props.setClickedElementId();
      }
    }
  }

  componentDidUpdate() {
    if (
      this.props.feedComponentData.data &&
      this.props.feedComponentData.items.length === 0
    ) {
      this.props.getItems(
        this.props.positionInFeed,
        this.props.feedComponentData.data
      );
    }
  }

  render() {
    let feedComponentData = this.props.feedComponentData;
    if (!feedComponentData) {
      return null;
    }
    let carouselData;
    if (feedComponentData.items && feedComponentData.items.map) {
      carouselData = feedComponentData.items.map(transformData);
    }

    return (
      <FeedComponent
        carouselOptions={{
          header: this.props.feedComponentData.title,
          buttonText: this.props.feedComponentData.btnText,
          seeAll: () => {
            this.handleClick();
          }
        }}
        data={carouselData}
      />
    );
  }
}
RecommendationWidget.propTypes = {
  seeAll: PropTypes.func,
  feedComponentData: PropTypes.object
};
