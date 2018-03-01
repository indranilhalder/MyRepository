import React from "react";
import FeedComponent from "./FeedComponent";
import PropTypes from "prop-types";
import { transformData } from "./utils.js";
import { PRODUCT_LISTINGS } from "../../lib/constants";
export default class RecommendationWidget extends React.Component {
  handleClick() {
    this.props.history.push(PRODUCT_LISTINGS);
  }

  componentDidUpdate() {
    if (
      this.props.feedComponentData.data &&
      this.props.feedComponentData.data.data &&
      this.props.feedComponentData.items.length === 0
    ) {
      const itemIds = this.props.feedComponentData.data.data;

      this.props.getItems(this.props.positionInFeed, itemIds);
    }
  }

  render() {
    let feedComponentData = this.props.feedComponentData;
    console.log("feed component data");
    console.log(feedComponentData);
    let carouselData;
    if (feedComponentData.items && feedComponentData.items instanceof Array) {
      carouselData = feedComponentData.items.map(transformData);
    }

    return (
      feedComponentData.items &&
      feedComponentData.items.length > 0 && (
        <FeedComponent
          backgroundColor="#e4e4e4"
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
