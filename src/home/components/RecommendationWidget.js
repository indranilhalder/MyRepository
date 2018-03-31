import React from "react";
import FeedComponent from "./FeedComponent";
import PropTypes from "prop-types";
import { transformData } from "./utils.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class RecommendationWidget extends React.Component {
  handleClick() {
    const urlSuffix = this.props.feedComponentData.webURL.replace(
      TATA_CLIQ_ROOT,
      "$1"
    );
    this.props.history.push(urlSuffix);
  }

  componentDidUpdate() {
    if (
      this.props.feedComponentData.data &&
      this.props.feedComponentData.items.length === 0
    ) {
      const itemIds = this.props.feedComponentData.data;

      this.props.getItems(this.props.positionInFeed, [
        "MP000000000155861",
        "MP000000000114700",
        "MP000000000169248",
        "MP000000000113243"
      ]);
    }
  }

  render() {
    let feedComponentData = this.props.feedComponentData;
    if (!feedComponentData) {
      return null;
    }
    let carouselData;
    if (feedComponentData.items && feedComponentData.items instanceof Array) {
      carouselData = feedComponentData.items.map(transformData);
    }

    return (
      feedComponentData.items instanceof Array &&
      feedComponentData.items.length > 0 && (
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
      )
    );
  }
}
RecommendationWidget.propTypes = {
  seeAll: PropTypes.func,
  feedComponentData: PropTypes.object
};
