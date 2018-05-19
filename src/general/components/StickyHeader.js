import React from "react";
import PropTypes from "prop-types";
import styles from "./StickyHeader.css";
import orderIcon from "./img/orderhistorywhite.svg";
import backArrow from "./img/arrowBack.svg";
import downloadIcon from "./img/downloadWhite.svg";
import Icon from "../../xelpmoc-core/Icon";
import companyLogo from "./img/group.svg";
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
  redirectToHome() {
    if (this.props.redirectToHome) {
      this.props.redirectToHome();
    }
  }
  render() {
    let base = styles.base;
    if (this.props.isShowCompanyLogo) {
      base = styles.logoWithBase;
    }
    return (
      <div className={base}>
        <div className={styles.productDisplayingText}>{this.props.text}</div>
        <div className={styles.backArrowHolder} onClick={() => this.backPage()}>
          <Icon image={backArrow} size={20} />
        </div>
        {this.props.isShowCompanyLogo && (
          <div
            className={styles.logoHolder}
            onClick={() => this.redirectToHome()}
          >
            <Icon image={companyLogo} size={35} />
          </div>
        )}
        <div className={styles.historyDownloadIcon}>
          <div
            className={styles.orderIconHolder}
            onClick={() => this.goToCartPage()}
          >
            <Icon image={orderIcon} size={20} />
            {this.props.bagCount > 0 && (
              <div className={styles.bagCount}>{this.props.bagCount}</div>
            )}
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
  goToWishList: PropTypes.func,
  isShowCompanyLogo: PropTypes.bool
};
StickyHeader.defaultProps = {
  isShowCompanyLogo: false
};
