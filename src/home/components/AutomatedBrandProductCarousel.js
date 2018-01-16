import React from "react";
import FeedComponent from "./FeedComponent";
import PropTypes from "prop-types";
import ProductImageHeader from "../../general/components/ProductImageHeader";
import Logo from "../../general/components/Logo";
import { transformData } from "./utils.js";
export default class AutomatedBrandProductCarousel extends React.Component {
  handleClick() {
    if (this.props.seeAll) {
      this.props.seeAll();
    }
  }
  render() {
    const componentData = this.props.feedComponentData.data;
    let carouselData;
    console.log("COMPONENT DATA");
    console.log(componentData);
    if (componentData.items instanceof Array) {
      carouselData = componentData.items.map(transformData);
    }

    return (
      <FeedComponent
        banner={
          <ProductImageHeader
            image={componentData.imageURL}
            description={componentData.description}
            logo={<Logo image={componentData.brandLogo} />}
          />
        }
        backgroundColor="#e4e4e4"
        carouselOptions={{
          buttonText: "See All",
          seeAll: () => {
            this.handleClick();
          }
        }}
        data={carouselData}
      />
    );
  }
}
AutomatedBrandProductCarousel.propTypes = {
  bannerImage: PropTypes.string,
  bannerLogo: PropTypes.string,
  bannerDescription: PropTypes.string,
  seeAll: PropTypes.func
};
