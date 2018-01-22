import React from "react";
import FeedComponent from "./FeedComponent";
import ProductVideo from "../../general/components/ProductVideo";
import { transformData } from "./utils.js";
export default class VideoProductCarousel extends React.Component {
  handleClick() {
    if (this.props.seeAll) {
      this.props.seeAll();
    }
  }
  render() {
    console.log(this.props);
    const feedComponentData = this.props.feedComponentData.data;
    let data = [];
    if (feedComponentData.items) {
      data = feedComponentData.items.map(transformData);
    }
    return (
      <FeedComponent
        banner={
          <ProductVideo
            url={feedComponentData.videoURL}
            logo={feedComponentData.imageURL}
            description={feedComponentData.description}
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
