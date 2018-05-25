import React from "react";
import Carousel from "../../general/components/Carousel";
import MediaQuery from "react-responsive";
import ProductModule from "../../general/components/ProductModule";
import PropTypes from "prop-types";
import styles from "./FeedComponent.css";
import { withRouter } from "react-router";

class FeedComponent extends React.Component {
  onClick = val => {
    this.props.history.push(val);
    if (this.props.setClickedElementId) {
      this.props.setClickedElementId();
    }
  };
  render() {
    const {
      data,
      carouselOptions,
      banner,
      backgroundColor,
      backgroundImage,
      ...rest
    } = this.props;
    if (!(data instanceof Array)) {
      return null;
    }

    return (
      <div
        className={
          this.props.positionInFeed === 1 ? styles.firstItemBase : styles.base
        }
        style={{
          backgroundColor: backgroundColor,
          backgroundImage: `url(${backgroundImage})`
        }}
      >
        <MediaQuery query="(max-device-width: 1024px)">
          {banner && <div className={styles.banner}>{banner}</div>}
        </MediaQuery>
        <div
          className={
            backgroundColor ? styles.productHolder : styles.bannerProductHolder
          }
        >
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
                    productCode={datum.productListingId}
                    showWishListButton={false}
                    ussId={datum.winningUssID}
                    onClick={this.onClick}
                    {...rest}
                    {...datum}
                  />
                );
              })}
          </Carousel>
        </div>
      </div>
    );
  }
}

export default withRouter(FeedComponent);
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
