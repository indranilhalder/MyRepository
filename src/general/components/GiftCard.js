import React from "react";
import styles from "./GiftCard.css";
import Icon from "../../xelpmoc-core/Icon";
import PropTypes from "prop-types";
import giftImageURL from "./img/Gift.svg";

export default class GiftCard extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.giftInnerBox}>
          <div className={styles.giftIcon}>
            <Icon image={giftImageURL} size={30} />
          </div>
          <div className={styles.headingBox}>
            <div className={styles.headingText}>{this.props.heading}</div>
            <div className={styles.text}>{this.props.lable}</div>
          </div>
        </div>
      </div>
    );
  }
}
GiftCard.propTypes = {
  giftImageURL: PropTypes.string,
  heading: PropTypes.string,
  lable: PropTypes.string
};
