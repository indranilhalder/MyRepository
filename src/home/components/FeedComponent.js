import React from "react";
import Carousel from "../../general/components/Carousel";
import ProductModule from "../../general/components/ProductModule";
import PropTypes from "prop-types";
import styles from "./FeedComponent.css";

export default class FeedComponent extends React.Component {
  render() {
    return (
      <div
        className={styles.base}
        style={{
          backgroundColor: this.props.backgroundColor,
          backgroundImage: `url(${this.props.backgroundImage})`
        }}
      >
        {this.props.banner && (
          <div className={styles.banner}>{this.props.banner}</div>
        )}
        <Carousel {...this.props.carouselOptions}>
          {this.props.data &&
            this.props.data.map((datum, i) => {
              return (
                <ProductModule
                  key={i}
                  isWhite={
                    this.props.carouselOptions
                      ? this.props.carouselOptions.isWhite
                        ? this.props.carouselOptions.isWhite
                        : false
                      : false
                  }
                  productImage={datum.image}
                  title={datum.title}
                  price={datum.price}
                  discountPrice={datum.discountPrice}
                  description={datum.description}
                  onDownload={datum.onDownload}
                  onClick={datum.onClick}
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
      productImage: PropTypes.string,
      title: PropTypes.string,
      price: PropTypes.number,
      discountPrice: PropTypes.number,
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
