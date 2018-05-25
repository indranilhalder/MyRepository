import React from "react";
import Carousel from "../../general/components/Carousel";
import ThemeProduct from "../../general/components/ThemeProduct";
import Logo from "../../general/components/Logo";
import PropTypes from "prop-types";
import styles from "./ThemeProductWidget.css";
import { transformData } from "./utils.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import { MSD_WIDGET_PLATFORM } from "../../lib/config.js";

export default class ThemeProductWidget extends React.Component {
  handleClick() {
    let widgetData = this.props.feedComponentData;
    if (
      widgetData.postParams &&
      widgetData.postParams.widgetPlatform === MSD_WIDGET_PLATFORM
    ) {
      widgetData = widgetData.data[0];
    }

    if (widgetData.webURL) {
      const urlSuffix = widgetData.webURL.replace(TATA_CLIQ_ROOT, "$1");
      this.props.history.push(urlSuffix);
      if (this.props.setClickedElementId) {
        this.props.setClickedElementId();
      }
    }
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
    if (this.props.setClickedElementId) {
      this.props.setClickedElementId();
    }
  };

  render() {
    let items = [];
    let widgetData = this.props.feedComponentData;
    if (
      widgetData.postParams &&
      widgetData.widgetPlatform === MSD_WIDGET_PLATFORM
    ) {
      widgetData = widgetData.data;
    }
    if (!widgetData) {
      return null;
    }

    if (this.props.feedComponentData.items) {
      items = this.props.feedComponentData.items.map(transformData);
    }

    return (
      <div
        className={styles.base}
        style={{
          backgroundImage: `url(${
            widgetData.data ? widgetData.data[0].imageURL : widgetData.imageURL
          })`,
          backgroundSize: "cover",
          backgroundPosition: "center"
        }}
      >
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
