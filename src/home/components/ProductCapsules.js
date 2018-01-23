import React from "react";
import Carousel from "../../general/components/Carousel";
import ProductCapsuleCircle from "../../general/components/ProductCapsuleCircle";
import PropTypes from "prop-types";
import styles from "./ProductCapsules.css";
export default class ProductCapsules extends React.Component {
  render() {
    const productCapsulesData = this.props.feedComponentData;
    const numberOfProducts = productCapsulesData.data.items
      ? productCapsulesData.data.items.length
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
          withFooter={false}
        >
          {this.props.feedComponentData.data.items &&
            this.props.feedComponentData.data.items.map((datum, i) => {
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
  header: PropTypes.string
};
ProductCapsules.defaultProps = {
  header: "Saved products"
};
