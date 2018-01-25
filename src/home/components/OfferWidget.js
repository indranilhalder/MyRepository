import React from "react";
import styles from "./OfferWidget.css";
import { Image } from "xelpmoc-core";
import Button from "../../general/components/Button";
import Carousel from "../../general/components/Carousel";
import PropTypes from "prop-types";

export default class OfferWidget extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
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
                <div className={styles.base}>
                  <div className={styles.imageHolder}>
                    <Image image={datum.imageURL} key={i} />
                  </div>
                  <div className={styles.overlay} />
                  <div className={styles.ovalImage}>
                    <div className={styles.text} key={i}>
                      {datum.discountText}
                    </div>
                  </div>
                  <div className={styles.textHolder}>
                    <div className={styles.textLine}>
                      {datum.title}
                      <div className={styles.buttonHolder}>
                        <Button
                          type={this.props.case}
                          color={this.props.color}
                          label={datum.btnText}
                          onClick={() => this.handleClick()}
                          width={130}
                        />
                      </div>
                    </div>
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

OfferWidget.defaultProps = {
  case: "hollow",
  off: "40% off",
  textLine: "Beautiful watches at amazing prices",
  value: "Shop now",
  color: "#fff"
};
