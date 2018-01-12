import React from "react";
import FeedComponent from "./FeedComponent";
import PropTypes from "prop-types";
import ProductImageHeader from "../../general/components/ProductImageHeader";
export default class BannerProductCarousal extends React.Component {
  handleClick() {
    if (this.props.seeAll) {
      this.props.seeAll();
    }
  }
  render() {
    return (
      <FeedComponent
        banner={
          <ProductImageHeader
            image={this.props.bannerImage}
            name={this.props.bannerHeading}
            label={this.props.bannerDescription}
          />
        }
        backgroundColor="#e4e4e4"
        carouselOptions={{
          buttonText: "See All",
          seeAll: () => {
            this.handleClick();
          }
        }}
        {...this.props}
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
