import React from "react";
import FeedComponent from "./FeedComponent";
import ProductVideo from "../../general/components/ProductVideo";
import { transformData } from "./utils.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class VideoProductCarousel extends React.Component {
  handleClick() {
    const urlSuffix = this.props.feedComponentData.webURL.replace(
      TATA_CLIQ_ROOT,
      "$1"
    );
    this.props.history.push(urlSuffix);
    this.props.setClickedElementId();
  }

  componentDidUpdate() {
    const data = this.props.feedComponentData;

    if (data.items.length === 0 && data.itemIds && data.itemIds.length > 0) {
      this.props.getItems(this.props.positionInFeed, data.itemIds);
    }
  }

  render() {
    const feedComponentData = this.props.feedComponentData;

    if (!feedComponentData) {
      return null;
    }

    let data = [];
    if (feedComponentData.items) {
      data = feedComponentData.items.map(transformData);
    }
    return (
      <FeedComponent
        banner={
          <ProductVideo
            url={feedComponentData.videoURL}
            image={feedComponentData.imageURL}
            logo={feedComponentData.brandLogo}
            description={feedComponentData.description}
          />
        }
        carouselOptions={{
          buttonText: feedComponentData.btnText,
          seeAll: () => {
            this.handleClick();
          }
        }}
        setClickedElementId={this.props.setClickedElementId}
        data={data}
      />
    );
  }
}
