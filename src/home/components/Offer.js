import React from "react";
import styles from "./Offer.css";
import MediaQuery from "react-responsive";
import PropTypes from "prop-types";
import Button from "../../general/components/Button";
import { Image } from "xelpmoc-core";

export default class Offer extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick(this.props.datum.webURL);
    }
  };
  render() {
    const { datum, key } = this.props;
    return (
      <div className={styles.base} key={key}>
        <div className={styles.imageHolder}>
          <Image image={datum.imageURL} key={key} />
        </div>
        <MediaQuery query="(min-device-width: 1025px)">
          <div className={styles.overlay} onClick={this.handleClick} />
        </MediaQuery>
        <MediaQuery query="(max-device-width: 1024px)">
          <div className={styles.overlay} />
        </MediaQuery>
        <div className={styles.ovalImage}>
          <div className={styles.text} key={key}>
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
                onClick={this.handleClick}
                width={130}
              />
            </div>
          </MediaQuery>
        </div>
      </div>
    );
  }
}
