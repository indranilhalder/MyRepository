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
import { COUPON_COOKIE } from "../../lib/constants.js";
const REMOVE = "Remove";
const APPLY = "Apply";
const USER_COUPON_NOTE =
  "Note : Bank promotions  can be applied  during payment";

class ProductCouponDetails extends Component {
  constructor(props) {
    super(props);
    this.state = {
      previousCouponVal: Cookie.getCookie(COUPON_COOKIE),
      couponVal: Cookie.getCookie(COUPON_COOKIE)
    };
  }

  applyUserCoupon() {
    if (this.state.couponVal) {
      if (this.state.couponVal !== this.state.previousCouponVal) {
        if (this.state.previousCouponVal) {
          this.setState({
            previousCouponVal: this.state.couponVal
          });
          this.props.selecteBankOffer(this.state.couponVal);
          this.props.releasePreviousAndApplyNewBankOffer(
            this.state.previousCouponVal,
            this.state.couponVal
          );
        } else {
          this.props.selecteBankOffer(this.state.couponVal);
          this.props.applyBankOffer(this.state.couponVal);
          this.setState({
            previousCouponVal: this.state.couponVal
          });
        }
      } else {
        this.props.selecteBankOffer("");
        this.setState({
          previousCouponVal: "",
          couponVal: ""
        });
        this.props.releaseBankOffer(this.state.couponVal);
      }
    }
  }
  onSelectCouponCode = val => {
    this.setState({ couponVal: val[0] });
  };
  render() {
    return (
      <SlideModal {...this.props}>
        <div className={styles.base}>
          <div className={styles.header}>
            <StaticDarkHeader text="Apply Coupon" />
          </div>
          <div className={styles.searchHolder}>
            <SearchCupon
              label={
                this.state.previousCouponVal &&
                this.state.previousCouponVal === this.state.couponVal
                  ? REMOVE
                  : APPLY
              }
              couponCode={this.state.couponVal}
              getValue={couponVal => this.setState({ couponVal })}
              applyUserCoupon={() => this.applyUserCoupon()}
            />
          </div>
          <div className={styles.disclaimer}>
            <span>Note:</span> {USER_COUPON_NOTE}
          </div>
          <div className={styles.link}>
            <div className={styles.linkArrow}>
              <Icon image={arrowIcon} size={10} />
            </div>Login to view personal coupons
          </div>
          <GridSelect
            elementWidthMobile={100}
            offset={0}
            limit={1}
            onSelect={val => this.onSelectCouponCode(val)}
            selected={[this.state.couponVal]}
          >
            {this.props.opencouponsList &&
              this.props.opencouponsList.map((value, i) => {
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
