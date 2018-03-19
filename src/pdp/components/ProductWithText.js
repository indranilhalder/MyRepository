import React from "react";
import Carousel from "../../general/components/Carousel";
import CircleProduct from "./CircleProduct.js";
import PropTypes from "prop-types";
import styles from "./ProductWithText.css";

export default class ProductWithText extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <Carousel elementWidthMobile={45}>
          {this.props.data &&
            this.props.data.map((datum, i) => {
              return (
                <CircleProduct
                  key={i}
                  productImage={datum.productImage}
                  productName={datum.productName}
                />
              );
            })}
        </Carousel>
      </div>
    );
  }
}
ProductWithText.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      productImage: PropTypes.string,
      productName: PropTypes.string
    })
  )
};
