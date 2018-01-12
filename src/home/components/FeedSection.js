import React from "react";
import Carousel from "../../general/components/Carousel";
import ProductModule from "../../general/components/ProductModule";
import styles from "./FeedSection.css";

export default class FeedSection extends React.Component {
  render() {
    return (
      <div
        className={styles.base}
        style={{ background: this.props.background }}
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
                />
              );
            })}
        </Carousel>
      </div>
    );
  }
}
