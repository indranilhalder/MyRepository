import React from "react";
import styles from "./OfferWidget.css";
import { Image } from "xelpmoc-core";
import Button from "../../general/components/Button";
import Carousel from "../../general/components/Carousel";
import MediaQuery from "react-responsive";
import PropTypes from "prop-types";

export default class OfferWidget extends React.Component {
  handleClick = () => {
    this.props.history.push("/productListings");
  };
  render() {
    const data = this.props.feedComponentData.data.items
      ? this.props.feedComponentData.data.items
      : false;

    return (
      <div className={styles.holder}>
        <Carousel
          elementWidthMobile={90}
          elementWidthDesktop={33.33}
          header={this.props.feedComponentData.title}
        >
          {data &&
            data.map((datum, i) => {
              return (
                <div className={styles.base} key={i}>
                  <div className={styles.imageHolder}>
                    <Image image={datum.imageURL} key={i} />
                  </div>
                  <MediaQuery query="(min-device-width: 1025px)">
                    <div
                      className={styles.overlay}
                      onClick={() => this.handleClick()}
                    />
                  </MediaQuery>
                  <MediaQuery query="(max-device-width: 1024px)">
                    <div className={styles.overlay} />
                  </MediaQuery>
                  <div className={styles.ovalImage}>
                    <div className={styles.text} key={i}>
                      {datum.discountText}
                    </div>
                  </div>
                  <div className={styles.textLine}>
                    {datum.title}
                    <MediaQuery query="(max-device-width: 1024px)">
                      <div className={styles.buttonHolder}>
                        <Button
                          type="hollow"
                          color="#fff"
                          label={datum.btnText}
                          onClick={() => this.handleClick()}
                          width={130}
                        />
                      </div>
                    </MediaQuery>
                  </div>
                </div>
              );
            })}
        </Carousel>
      </div>
    );
  }
}
OfferWidget.propTypes = {
  feedComponentData: PropTypes.shape({
    title: PropTypes.string,
    items: PropTypes.arrayOf(
      PropTypes.shape({
        imageURL: PropTypes.string,
        title: PropTypes.string,
        discountText: PropTypes.string,
        btnText: PropTypes.string
      })
    )
  }),
  onClick: PropTypes.func,
  textLine: PropTypes.string
};
