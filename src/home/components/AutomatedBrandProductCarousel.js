import React from "react";
import FeedComponent from "./FeedComponent";
import PropTypes from "prop-types";
import ProductImageHeader from "../../general/components/ProductImageHeader";
import Logo from "../../general/components/Logo";
export default class AutomatedBrandProductCarousel extends React.Component {
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
            description={this.props.bannerDescription}
            logo={<Logo image={this.props.bannerLogo} />}
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
AutomatedBrandProductCarousel.propTypes = {
  bannerImage: PropTypes.string,
  bannerLogo: PropTypes.string,
  bannerDescription: PropTypes.string
};
