import React from "react";
import Carousel from "../../general/components/Carousel";
import MediaQuery from "react-responsive";
import ProductModule from "../../general/components/ProductModule";
import PropTypes from "prop-types";
import styles from "./FeedComponent.css";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class FeedComponent extends React.Component {
  render() {
    const {
      data,
      carouselOptions,
      banner,
      backgroundColor,
      backgroundImage,
      ...rest
    } = this.props;
    return (
      <div
        className={styles.base}
        style={{
          backgroundColor: backgroundColor,
          backgroundImage: `url(${backgroundImage})`
        }}
      >
        <MediaQuery query="(max-device-width: 1024px)">
          {banner && <div className={styles.banner}>{banner}</div>}
        </MediaQuery>
        <Carousel
          {...carouselOptions}
          banner={banner}
          bannerWidth="42%"
          elementWidthDesktop={33.333}
        >
          {data &&
            data.map((datum, i) => {
              return (
                <ProductModule
                  key={i}
                  isWhite={
                    carouselOptions
                      ? carouselOptions.isWhite
                        ? carouselOptions.isWhite
                        : false
                      : false
                  }
                  productImage={datum.image}
                  title={datum.title}
                  price={datum.price}
                  discountPrice={datum.discountPrice}
                  description={datum.description}
                  onDownload={datum.onDownload}
                  webURL={datum.webURL}
                  {...rest}
                />
              );
            })}
        </Carousel>
      </div>
    );
  }
}
FeedComponent.propTypes = {
  backgroundColor: PropTypes.string,
  banner: PropTypes.element,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      image: PropTypes.string,
      title: PropTypes.string,
      price: PropTypes.string,
      discountPrice: PropTypes.string,
      description: PropTypes.string,
      onDownload: PropTypes.func,
      onClick: PropTypes.func
    })
  ),
  carouselOptions: PropTypes.shape({
    elementWidthDesktop: PropTypes.number,
    elementWidthMobile: PropTypes.number,
    buttonText: PropTypes.string,
    header: PropTypes.string,
    isWhite: PropTypes.bool,
    seeAll: PropTypes.func
  })
};
