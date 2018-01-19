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
    const data = this.props.data;
    data.push({ url: this.props.circle, fit: "25px", onClick: this.newFollow });
    data.push({ url: this.props.circle, fit: "25px", onClick: this.newFollow });
    data.push({ url: this.props.circle, fit: "25px", onClick: this.newFollow });
    return (
      <Carousel header={BrandImage.defaultProps.header}>
        {data.map((datum, i) => {
          return (
            <BrandImage
              key={i}
              image={datum.url}
              value={datum.value}
              fit={datum.fit}
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
