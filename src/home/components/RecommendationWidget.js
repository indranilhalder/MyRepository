import React from "react";
import FeedComponent from "./FeedComponent";
import PropTypes from "prop-types";
import { transformData } from "./utils.js";
import { PRODUCT_LISTINGS } from "../../lib/constants";
export default class RecommendationWidget extends React.Component {
  handleClick() {
    this.props.history.push(PRODUCT_LISTINGS);
  }

  render() {
    let feedComponentData = this.props.feedComponentData;
    let carouselData;
    if (feedComponentData.data.items instanceof Array) {
      carouselData = feedComponentData.data.items.map(transformData);
    }

    return (
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
    );
  }
}
RecommendationWidget.propTypes = {
  seeAll: PropTypes.func,
  feedComponentData: PropTypes.object
};
