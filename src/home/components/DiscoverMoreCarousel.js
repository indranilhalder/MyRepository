import React from "react";
import Carousel from "../../general/components/Carousel";
import CircleProductImage from "../../general/components/CircleProductImage";
import PropTypes from "prop-types";
import styles from "./DiscoverMoreCarousel.css";

export default class DiscoverMoreCarousel extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <Carousel
          header={this.props.header}
          elementWidthMobile={30}
          elementWidthDesktop={30}
        >
          <CircleProductImage
            label="A Laptop"
            image={
              "https://static.pexels.com/photos/157627/watch-fashion-accessories-clothes-157627.jpeg"
            }
          />
          <CircleProductImage
            label="An Icecream"
            image={
              "https://truffle-assets.imgix.net/67ce1315-104-icecream-dishland2.jpg"
            }
          />
          <CircleProductImage
            label="A Mobile"
            image={
              "https://images.pexels.com/photos/248528/pexels-photo-248528.jpeg?h=350&auto=compress&cs=tinysrgb"
            }
          />
          <CircleProductImage
            label="A Laptop"
            image={
              "https://static.pexels.com/photos/157627/watch-fashion-accessories-clothes-157627.jpeg"
            }
          />
          <CircleProductImage
            label="An Icecream"
            image={
              "https://truffle-assets.imgix.net/67ce1315-104-icecream-dishland2.jpg"
            }
          />
          <CircleProductImage
            label="A Mobile"
            image={
              "https://images.pexels.com/photos/248528/pexels-photo-248528.jpeg?h=350&auto=compress&cs=tinysrgb"
            }
          />
        </Carousel>
      </div>
    );
  }
}
DiscoverMoreCarousel.defaultProps = {
  header: "Discover more from Tata Cliq"
};
