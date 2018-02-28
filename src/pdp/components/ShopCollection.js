import React from "react";
import styles from "./ShopCollection.css";
import { Image } from "xelpmoc-core";
import Button from "../../general/components/Button";
import PropTypes from "prop-types";

export default class ShopCollection extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder}>
          <Image image={this.props.image} />
        </div>
        <div className={styles.overlay} />
        <div className={styles.textLine}>
          <div className={styles.headingHolder}>
            <div className={styles.heading}>{this.props.beatsText}</div>
          </div>
          <div className={styles.title}>{this.props.title}</div>
          <div className={styles.buttonHolder}>
            <Button
              type="primary"
              color="#fff"
              label={this.props.btnText}
              onClick={() => this.handleClick()}
              width={180}
            />
          </div>
        </div>
      </div>
    );
  }
}
ShopCollection.propTypes = {
  onClick: PropTypes.func,
  textLine: PropTypes.string,
  title: PropTypes.string,
  beatsText: PropTypes.string
};
