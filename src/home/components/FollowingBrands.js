import React from "react";
import BrandImage from "../../general/components/BrandImage";
import Carousel from "../../general/components/Carousel";
import PropTypes from "prop-types";

export default class FollowingBrands extends React.Component {
  newFollow = () => {
    if (this.props.onFollow) {
      this.props.onFollow();
    }
  };
  render() {
    const followWidgetData = this.props.feedComponentData.data;
    return (
      <Carousel header={this.props.feedComponentData.title}>
        {followWidgetData.length > 0 &&
          followWidgetData.map((datum, i) => {
            return (
              <BrandImage
                key={i}
                image={datum.imageURL}
                value={datum.type}
                fit={datum.type}
                onClick={datum.onClick}
              />
            );
          })}
      </Carousel>
    );
  }
}
BrandImage.propTypes = {
  image: PropTypes.string,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      url: PropTypes.string,
      value: PropTypes.string
    })
  )
};
BrandImage.defaultProps = {
  header: "Following Brands"
};
