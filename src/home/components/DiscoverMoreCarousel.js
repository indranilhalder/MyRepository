import React from "react";
import Carousel from "../../general/components/Carousel";
import CircleProductImage from "../../general/components/CircleProductImage";
import PropTypes from "prop-types";
import styles from "./DiscoverMoreCarousel.css";

export default class DiscoverMoreCarousel extends React.Component {
  render() {
    const discoverMoreCarouselData = this.props.feedComponentData.data;
    return (
      <div className={styles.base}>
        <Carousel header={this.props.feedComponentData.title}>
          {discoverMoreCarouselData.data &&
            discoverMoreCarouselData.data.map((datum, i) => {
              return (
                <CircleProductImage
                  image={datum.imageURL}
                  label={datum.title}
                  key={i}
                />
              );
            })}
        </Carousel>
      </div>
    );
  }
}
DiscoverMoreCarousel.propTypes = {
  header: PropTypes.string,
  discoverMoreCarouselData: PropTypes.object
};
DiscoverMoreCarousel.defaultProps = {
  header: "Discover more from Tata Cliq"
};
