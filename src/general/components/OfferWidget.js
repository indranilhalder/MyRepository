import React from "react";
import styles from "./OfferWidget.css";
import { Image } from "xelpmoc-core";
import Button from "./Button";
import Carousel from "../../general/components/Carousel";
import PropTypes from "prop-types";

export default class OfferWidget extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
    }
  };
  render() {
    var data = this.props.data;
    return (
      <Carousel elementWidthMobile={75} elementWidthDesktop={45}>
        {data.map((datum, i) => {
          return (
            <div className={styles.base}>
              <div className={styles.imageHolder}>
                <Image image={datum.image} key={i} />
              </div>
              <div className={styles.overlay} />{" "}
              <div className={styles.logo}>
                <div className={styles.text} key={i}>
                  {OfferWidget.defaultProps.text}
                </div>
              </div>
              <div className={styles.textHolder}>
                <div className={styles.textLine}>
                  {datum.textLine}
                  <div className={styles.buttonHolder}>
                    <Button
                      type={this.props.case}
                      color={this.props.color}
                      label={this.props.value}
                      onClick={() => this.handleClick()}
                      width={150}
                      padding-right={20}
                    />
                  </div>
                </div>
              </div>
            </div>
          );
        })}
      </Carousel>
    );
  }
}

OfferWidget.propTypes = {
  image: PropTypes.string,
  onClick: PropTypes.func,
  textLine: PropTypes.string
};

OfferWidget.defaultProps = {
  logo:
    " https://media.dcrainmaker.com/images/2016/05/Garmin-Vivomove-Rosegold-Sport-Faces1.jpg",
  case: "hollow",
  text: "Up to 40% off ",
  off: "40% off",
  textLine: "Beautiful watches at amazing prices",
  value: "Shop now",
  color: "#fff"
};
