import React from "react";
import ProductModule from "./ProductModule";
import Carousel from "./Carousel";
import styles from "./BrandSection.css";
import Button from "./Button";
import Logo from "./Logo";
import PropTypes from "prop-types";

export default class BrandSection extends React.Component {
  follow() {
    if (this.props.follow) {
      this.props.follow();
    }
  }
  seeAll() {
    if (this.props.goToBrandStore) {
      this.props.goToBrandStore();
    }
  }
  render() {
    return (
      <div
        className={styles.base}
        style={{
          backgroundColor: this.props.backgroundColor,
          backgroundImage: `url(${this.props.backgroundImage})`
        }}
      >
        <div className={styles.heading}>{this.props.heading}</div>
        <div className={styles.brandLogo}>
          <Logo image={this.props.image} />
          <span className={styles.buttonHolder}>
            <Button
              type="hollow"
              color="#fff"
              label={this.props.btnLabel}
              onClick={() => this.follow()}
              width={100}
              color="#b2b2b2"
            />
          </span>
        </div>
        <div className={styles.description}>{this.props.description}</div>
        <Carousel
          buttonText={this.props.buttonText}
          seeAll={() => this.props.goToBrandStore()}
        >
          {this.props.data &&
            this.props.data.map((datum, i) => {
              return (
                <ProductModule
                  key={i}
                  productImage={datum.productImage}
                  title={datum.title}
                  price={datum.price}
                  description={datum.description}
                  descriptionText={datum.descriptionText}
                />
              );
            })}
        </Carousel>
      </div>
    );
  }
}

BrandSection.propTypes = {
  image: PropTypes.string,
  heading: PropTypes.string,
  buttonText: PropTypes.string,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      productImage: PropTypes.string,
      title: PropTypes.string,
      price: PropTypes.string,
      descriptionText: PropTypes.string,
      description: PropTypes.string
    })
  ),
  seeAll: PropTypes.func,
  onClick: PropTypes.func
};
