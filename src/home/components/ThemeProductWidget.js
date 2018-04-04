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
        this.props.getItems(this.props.positionInFeed, data.itemIds);
      }
    }
  }

  handleThemeProductClick = url => {
    this.props.history.push(url);
  };

  render() {
    let items = [];
    let widgetData =
      this.props.feedComponentData.data && this.props.feedComponentData.data[0];
    if (!widgetData) {
      return null;
    }

    console.log("FEED DATA");
    console.log(this.props.feedComponentData);
    if (this.props.feedComponentData.items) {
      items = this.props.feedComponentData.items.map(transformData);
    }

    console.log("WIDGET DATA");
    console.log(widgetData);

    return (
      <div
        className={styles.base}
        style={{
          backgroundImage: `url(${widgetData.imageURL})`
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
              return (
                <ThemeProduct
                  image={datum.image}
                  label={datum.title}
                  price={datum.price}
                  discountPrice={datum.winningSellerMOP}
                  key={i}
                  onClick={this.handleThemeProductClick}
                  isWhite={true}
                  {...datum}
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
