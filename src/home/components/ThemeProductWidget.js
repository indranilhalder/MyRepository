import React from "react";
import Carousel from "../../general/components/Carousel";
import ThemeProduct from "../../general/components/ThemeProduct";
import Logo from "../../general/components/Logo";
import PropTypes from "prop-types";
import styles from "./ThemeProductWidget.css";
import { PRODUCT_LISTINGS } from "../../lib/constants";
import { transformData } from "./utils.js";
export default class ThemeProductWidget extends React.Component {
  handleClick() {
    this.props.history.push(PRODUCT_LISTINGS);
  }
  render() {
    let items = [];
    let feedComponentData = this.props.feedComponentData;
    if (feedComponentData.items) {
      items = feedComponentData.items.map(transformData);
    }

    return (
      <div
        className={styles.base}
        style={{
          backgroundImage: `url(${feedComponentData.backgroundImageURL})`
        }}
      >
        <div className={styles.overlay} />
        <div className={styles.logo}>
          <Logo image={feedComponentData.brandLogo} />
        </div>
        <Carousel
          {...this.props}
          header={feedComponentData.title}
          buttonText={"See All"}
          seeAll={() => this.handleClick()}
          elementWidthMobile={45}
          withFooter={false}
          isWhite={true}
        >
          {items &&
            items.map((datum, i) => {
              console.log("DATUM");
              console.log(datum);
              return (
                <ThemeProduct
                  image={datum.image}
                  label={datum.title}
                  price={datum.price}
                  key={i}
                  isWhite={true}
                />
              );
            })}
        </Carousel>
      </div>
    );
  }
}
ThemeProductWidget.propTypes = {
  feedComponentData: PropTypes.shape({
    data: PropTypes.shape({
      backgroundImageURL: PropTypes.string,
      brandLogo: PropTypes.string,
      btnText: PropTypes.string,
      items: PropTypes.arrayOf(
        PropTypes.shape({
          title: PropTypes.string,
          mrpPrice: PropTypes.string,
          discountedPrice: PropTypes.string,
          imageURL: PropTypes.string
        })
      )
    })
  })
};
