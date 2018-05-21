import React, { Component } from "react";
import CuponDetails from "./CuponDetails.js";
import SlideModal from "../../general/components/SlideModal";
import arrowIcon from "../../general/components/img/down-arrow.svg";
import Icon from "../../xelpmoc-core/Icon";
import SearchCupon from "./SearchCupon.js";
import PropTypes from "prop-types";
import GridSelect from "../../general/components/GridSelect";
import StaticDarkHeader from "../../general/components/StaticDarkHeader";
import styles from "./ProductCouponDetails.css";
import * as Cookie from "../../lib/Cookie.js";
import { COUPON_COOKIE, SUCCESS, ERROR } from "../../lib/constants.js";
import {
  APPLY_USER_COUPON_FAILURE,
  RELEASE_USER_COUPON_FAILURE
} from "../../cart/actions/cart.actions";
import { LOGGED_IN_USER_DETAILS } from "../../lib/constants";
const REMOVE = "Remove";
const APPLY = "Apply";
const USER_COUPON_NOTE =
  "Note : Bank promotions  can be applied  during payment";

class ProductCouponDetails extends Component {
  constructor(props) {
    super(props);
    this.state = {
      previousSelectedCouponCode: Cookie.getCookie(COUPON_COOKIE)
        ? Cookie.getCookie(COUPON_COOKIE)
        : "",
      selectedCouponCode: Cookie.getCookie(COUPON_COOKIE)
        ? Cookie.getCookie(COUPON_COOKIE)
        : ""
    };
  }

  async applyUserCoupon() {
    if (this.state.selectedCouponCode) {
      if (
        this.state.selectedCouponCode !== this.state.previousSelectedCouponCode
      ) {
        if (this.state.previousSelectedCouponCode) {
          const applyNewBankOfferStatus = await this.props.releaseUserCoupon(
            this.state.previousSelectedCouponCode,
            this.state.selectedCouponCode
          );
          if (applyNewBankOfferStatus.status === SUCCESS) {
            localStorage.setItem(COUPON_COOKIE, this.state.selectedCouponCode);
            this.props.closeModal();
          } else {
            if (
              applyNewBankOfferStatus.status === ERROR &&
              applyNewBankOfferStatus.type === RELEASE_USER_COUPON_FAILURE
            ) {
              this.setState({
                selectedCouponCode: this.state.previousSelectedCouponCode
              });
            } else if (
              applyNewBankOfferStatus.status === ERROR &&
              applyNewBankOfferStatus.type === APPLY_USER_COUPON_FAILURE
            ) {
              localStorage.removeItem(COUPON_COOKIE);
              this.setState({
                previousSelectedCouponCode: "",
                selectedCouponCode: ""
              });
            }
          }
        } else {
          const applyNewCouponCode = await this.props.applyUserCoupon(
            this.state.selectedCouponCode
          );

          if (applyNewCouponCode.status === SUCCESS) {
            localStorage.setItem(COUPON_COOKIE, this.state.selectedCouponCode);
            this.props.closeModal();
          } else {
            this.setState({
              previousSelectedCouponCode: "",
              selectedCouponCode: ""
            });
          }
        }
      } else {
        const releaseBankOfferReq = await this.props.releaseUserCoupon(
          this.state.selectedCouponCode
        );
        if (releaseBankOfferReq.status === SUCCESS) {
          localStorage.removeItem(COUPON_COOKIE);
          this.setState({
            previousSelectedCouponCode: "",
            selectedCouponCode: ""
          });
        }
      }
    }
  }

  onSelectCouponCode = val => {
    if (val[0]) {
      this.setState({ selectedCouponCode: val[0] });
    } else {
      this.setState({ selectedCouponCode: "" });
    }
  };

  navigateToLogin() {
    if (this.props.navigateToLogin) {
      this.props.navigateToLogin(this.props.history.location.pathname);
    }
  }
  render() {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let coupons = [];
    let showLogOutUserCoupon = userDetails ? true : false;
    if (
      userDetails &&
      this.props.closedcouponsList &&
      this.props.closedcouponsList.length > 0
    ) {
      coupons = this.props.closedcouponsList.concat(this.props.opencouponsList);
    } else {
      if (this.props.opencouponsList) {
        coupons = this.props.opencouponsList;
      }
    }
    return (
      <SlideModal {...this.props}>
        <div className={styles.base}>
          <div className={styles.header}>
            <StaticDarkHeader text="Apply Coupon" />
          </div>
          <div className={styles.searchHolder}>
            <SearchCupon
              label={
                this.state.previousSelectedCouponCode &&
                this.state.previousSelectedCouponCode ===
                  this.state.selectedCouponCode
                  ? REMOVE
                  : APPLY
              }
              disableManualType={false}
              placeholder="Enter Coupon code"
              couponCode={this.state.selectedCouponCode}
              getValue={selectedCouponCode =>
                this.setState({ selectedCouponCode })
              }
              applyUserCoupon={() => this.applyUserCoupon()}
            />
          </div>
          <div className={styles.disclaimer}>{USER_COUPON_NOTE}</div>
          {!showLogOutUserCoupon && (
            <div className={styles.link} onClick={() => this.navigateToLogin()}>
              <div className={styles.linkArrow}>
                <Icon image={arrowIcon} size={10} />
              </div>Login to view personal coupons
            </div>
          )}
          <GridSelect
            elementWidthMobile={100}
            offset={0}
            limit={1}
            onSelect={val => this.onSelectCouponCode(val)}
            selected={[this.state.selectedCouponCode]}
          >
            {coupons &&
              coupons.map((value, i) => {
                return (
                  <CuponDetails
                    promotionTitle={value.couponName}
                    promotionDetail={value.description}
                    dateTime={value.couponExpiryDate}
                    amount={value.maxDiscount}
                    key={i}
                    couponType={value.couponType}
                    value={value.couponCode}
                  />
                );
              })}
          </GridSelect>
        </div>
      </SlideModal>
    );
  }
}

ProductCouponDetails.propTypes = {
  productOfferPromotion: PropTypes.array
};

export default ProductCouponDetails;
