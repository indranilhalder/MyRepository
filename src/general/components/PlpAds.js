import React from "react";
import styles from "./PlpAds.css";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
import Button from "../../general/components/Button";

export default class PlpAds extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.plpInnerBox}>
          <div className={styles.icon}>
            <Icon image={this.props.imageURL} size={40} />
          </div>
          <div className={styles.plpBox}>{this.props.text}</div>
          <div className={styles.buttonHolder}>
            <Button
              type="hollow"
              color="#fff"
              label={this.props.buttonText}
              onClick={() => this.handleClick()}
              width={100}
            />
          </div>
        </div>
      </div>
    );
  }
}
PlpAds.propTypes = {
  Text: PropTypes.string,
  imageURL: PropTypes.string,
  onClick: PropTypes.func
};
PlpAds.defaultProps = {
  text: "See the best sellers in Women Top Wear",
  buttonText: "See now"
};
