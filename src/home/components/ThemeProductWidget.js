import React from "react";
import Carousel from "../../general/components/Carousel";
import ThemeProduct from "../../general/components/ThemeProduct";
import Logo from "../../general/components/Logo";
import PropTypes from "prop-types";
import styles from "./ThemeProductWidget.css";
import { PRODUCT_LISTINGS } from "../../lib/constants";
import { transformData } from "./utils.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class ThemeProductWidget extends React.Component {
  handleClick() {
    const urlSuffix = this.props.feedComponentData.data[0].webURL.replace(
      TATA_CLIQ_ROOT,
      "$1"
    );
    this.props.history.push(urlSuffix);
  }

  componentDidUpdate() {
    const data =
      this.props.feedComponentData.data && this.props.feedComponentData.data[0];
    if (data) {
      if (
        this.props.feedComponentData.items.length === 0 &&
        data.itemIds &&
        data.itemIds.length > 0
      ) {
        this.props.getItems(this.props.positionInFeed, [
          "MP000000000155861",
          "MP000000000114700",
          "MP000000000169248",
          "MP000000000113243"
        ]);
      }
    }
  }
  render() {
    let items = [];
    console.log(this.props.feedComponentData);
    let widgetData =
      this.props.feedComponentData.data && this.props.feedComponentData.data[0];
    if (!widgetData) {
      return null;
    }
    if (this.props.feedComponentData.items) {
      items = this.props.feedComponentData.items.map(transformData);
    }

    console.log("WIDGET DATA");
    console.log(widgetData);

    return (
      <div
        className={styles.base}
        style={{
          backgroundImage: `url(${widgetData.backgroundImageURL})`
        }}
      >
        <div className={styles.overlay} />
        <div className={styles.logo}>
          <Logo image={widgetData.brandLogo} />
        </div>
        <Carousel
          {...this.props}
          header={this.props.feedComponentData.title}
          buttonText={this.props.feedComponentData.btnText}
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
                  discountPrice={datum.winningSellerMOP}
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
