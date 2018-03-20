import React from "react";
import FeedComponent from "./FeedComponent";
import PropTypes from "prop-types";
import ProductImageHeader from "../../general/components/ProductImageHeader";
import { transformData } from "./utils.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class BannerProductCarousal extends React.Component {
  handleClick() {
    const urlSuffix = this.props.feedComponentData.webURL.replace(
      TATA_CLIQ_ROOT,
      "$1"
    );
    this.props.history.push(urlSuffix);
  }

  componentDidUpdate() {
    const data = this.props.feedComponentData;
    if (data.items.length === 0 && data.itemIds && data.itemIds.length > 0) {
      this.props.getItems(this.props.positionInFeed, data.itemIds);
    }
  }
  render() {
    const feedComponentData = this.props.feedComponentData;

    let data = [];
    if (feedComponentData.items) {
      data = feedComponentData.items.map(transformData);
    }
    return (
      <FeedComponent
        banner={
          <ProductImageHeader
            image={feedComponentData.imageURL}
            name={feedComponentData.title}
            label={feedComponentData.description}
          />
        }
        backgroundColor="#e4e4e4"
        carouselOptions={{
          buttonText: "See All",
          seeAll: () => {
            this.handleClick();
          }
        }}
        data={data}
      />
    );
  }
}
BannerProductCarousal.propTypes = {
  seeAll: PropTypes.func,
  bannerImage: PropTypes.string,
  bannerHeading: PropTypes.string,
  bannerDescription: PropTypes.string
};
