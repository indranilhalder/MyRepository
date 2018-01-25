import React from "react";
import Grid from "../../general/components/Grid";
import ProductModule from "../../general/components/ProductModule";
import PropTypes from "prop-types";
import styles from "./FlashSale.css";
export default class FlashSale extends React.Component {
  render() {
    const data = this.props.feedComponentData.data;
    return (
      <div
        className={styles.base}
        style={{
          backgroundImage: this.props.backgroundImage
            ? `url(${this.props.backgroundImage})`
            : "linear-gradient(204deg, #fd2c7a, #ff7255)"
        }}
      >
        <div className={styles.header}>
          <div className={styles.headingText}>{this.props.headingText}</div>
          <div className={styles.offerTime}>
            <div className={styles.clock} />
          </div>
        </div>
        <div className={styles.subheader}>{this.props.subHeader}</div>
        <Grid offset={20}>
          {data.items &&
            data.items.map((datum, i) => {
              return (
                <ProductModule
                  key={i}
                  isWhite={true}
                  productImage={datum.imageURL}
                  title={datum.title}
                  price={datum.price}
                  description={datum.description}
                />
              );
            })}
        </Grid>
      </div>
    );
  }
}
FlashSale.propTypes = {
  offerTime: PropTypes.string,
  headingText: PropTypes.string,
  subHeader: PropTypes.string,
  icon: PropTypes.string,
  backgroundImage: PropTypes.string,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      imageURL: PropTypes.string,
      title: PropTypes.string,
      description: PropTypes.string,
      price: PropTypes.string
    })
  )
};
FlashSale.defaultProps = {
  headingText: "Exclusive offers",
  subHeader: "Grab these offers for a limited time only!"
};
