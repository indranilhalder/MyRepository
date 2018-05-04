import React from "react";
import PropTypes from "prop-types";
import styles from "./StickyHeader.css";
import orderIcon from "./img/orderhistorywhite.svg";
import backArrow from "./img/arrowBack.svg";
import downloadIcon from "./img/downloadWhite.svg";
import Icon from "../../xelpmoc-core/Icon";
export default class StickyHeader extends React.Component {
  backPage() {
    if (this.props.goBack) {
      this.props.goBack();
    }
  }

  goToCartPage = () => {
    if (this.props.goToCart) {
      this.props.goToCart();
    }
  };

  goToWishList = () => {
    if (this.props.goToWishList) {
      this.props.goToWishList();
    }
  };

  render() {
    return (
      <div className={styles.base}>
        <span>{this.props.text}</span>
        <div className={styles.backArrowHolder} onClick={() => this.backPage()}>
          <Icon image={backArrow} size={20} />
        </div>
        <div className={styles.historyDownloadIcon}>
          <div
            className={styles.orderIconHolder}
            onClick={() => this.goToCartPage()}
          >
            <Icon image={orderIcon} size={20} />
          </div>
          <div
            className={styles.downloadIconHolder}
            onClick={() => this.goToWishList()}
          >
            <Icon image={downloadIcon} size={20} />
          </div>
        </div>
      </div>
    );
  }
}

StickyHeader.propTypes = {
  goBack: PropTypes.func,
  goToCart: PropTypes.func,
  goToWishList: PropTypes.func
};
