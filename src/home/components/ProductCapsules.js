import React from "react";
import Carousel from "../../general/components/Carousel";
import ProductCapsuleCircle from "../../general/components/ProductCapsuleCircle";
import PropTypes from "prop-types";
import styles from "./ProductCapsules.css";

export default class ProductCapsules extends React.Component {
  render() {
    const productCapsulesData = this.props.feedComponentData.data;
    const numberOfProducts = productCapsulesData
      ? productCapsulesData.length
      : 0;
    const subHeader = `You have ${numberOfProducts} products in your list`;
    return (
      <div className={styles.base}>
        <Carousel
          header={this.props.feedComponentData.title}
          subheader={subHeader}
          buttonText="See all"
          seeAll={this.props.feedComponentData.btnText}
          elementWidthMobile={30}
        >
          {productCapsulesData.length > 0 &&
            productCapsulesData.map((datum, i) => {
              return (
                <ProductCapsuleCircle
                  image={datum.imageURL}
                  label={datum.label}
                  key={i}
                />
              );
            })}
        </Carousel>
      </div>
    );
  }
}
ProductCapsules.propTypes = {
  header: PropTypes.string,
  subheader: PropTypes.string,
  data: PropTypes.arrayOf(PropTypes.shape({ image: PropTypes.string }))
};
ProductCapsules.defaultProps = {
  header: "Saved products"
};
