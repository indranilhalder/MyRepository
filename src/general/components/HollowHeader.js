import React from "react";
import PropTypes from "prop-types";
import styles from "./HollowHeader.css";
import orderIcon from "./img/cart-with-bg.svg";
import backArrow from "./img/back-with-bg.svg";
import downloadIcon from "./img/save-with-bg.svg";
import Icon from "../../xelpmoc-core/Icon";
import companyLogo from "./img/companylogo.svg";
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
  redirectToHome() {
    if (this.props.redirectToHome) {
      this.props.redirectToHome();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.backArrowHolder} onClick={() => this.backPage()}>
          <Icon image={backArrow} size={30} />
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
            <Icon image={orderIcon} size={30} />
            {this.props.bagCount > 0 && (
              <div className={styles.bagCount}>{this.props.bagCount}</div>
            )}
          </div>
          <div
            className={styles.downloadIconHolder}
            onClick={() => this.goToWishList()}
          >
            <Icon image={downloadIcon} size={30} />
          </div>
        </div>
      </div>
    );
  }
}

HollowHeader.propTypes = {
  goBack: PropTypes.func,
  orderProduct: PropTypes.func,
  downloadProduct: PropTypes.func,
  isShowCompanyLogo: PropTypes.bool
};
HollowHeader.defaultProps = {
  isShowCompanyLogo: false
};
