import React from "react";
import PropTypes from "prop-types";
import styles from "./HollowHeader.css";
import orderIcon from "./img/order-history.svg";
import backArrow from "./img/arrowBackblack.svg";
import downloadIcon from "../../pdp/components/img/Save.svg";
import { Icon } from "xelpmoc-core";
export default class HollowHeader extends React.Component {
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

HollowHeader.propTypes = {
  goBack: PropTypes.func,
  orderProduct: PropTypes.func,
  downloadProduct: PropTypes.func
};
