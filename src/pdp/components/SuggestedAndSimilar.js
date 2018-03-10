import React from "react";
import PropTypes from "prop-types";
import Carousel from "../../general/components/Carousel";
import ProductModule from "../../genaral/components/ProductModule";
import styles from "./SuggestedAndSimilar.css";

export default class SuggestedAndSimilar extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.content}>
          <Carousel>
            {this.props.suggested.map((val, i) => {
              return (
                <ProductModule
                  productImage={val.imageURL}
                  title={val.name}
                  description={val.description}
                  price={val.mrp}
                  discountPrice={val.discountPrice}
                />
              );
            })}
          </Carousel>
        </div>
        <div className={styles.content}>
          <Carousel>
            {this.props.similar.map((val, i) => {
              return (
                <ProductModule
                  productImage={val.imageURL}
                  title={val.name}
                  description={val.description}
                  price={val.mrp}
                  discountPrice={val.discountPrice}
                />
              );
            })}
          </Carousel>
        </div>
      </div>
    );
  }
}

SuggestedAndSimilar.propTypes = {
  suggested: PropTypes.arrayOf(
    PropTypes.shape({
      imageURL: PropTypes.string,
      name: PropTypes.string,
      description: PropTypes.string,
      discountPrice: PropTypes.string,
      mrp: PropTypes.string
    })
  ),
  similar: PropTypes.arrayOf(
    PropTypes.shape({
      imageURL: PropTypes.string,
      name: PropTypes.string,
      description: PropTypes.string,
      discountPrice: PropTypes.string,
      mrp: PropTypes.string
    })
  )
};
