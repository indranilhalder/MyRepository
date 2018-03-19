import React from "react";
import PropTypes from "prop-types";
import InformationHeader from "../../general/components/InformationHeader.js";
import ProfileMenuGrid from "../../blp/components/ProfileMenuGrid.js";
import AccountSetting from "./AccountSetting.js";
import TabHolder from "./TabHolder";
import TabData from "./TabData";
import BrandCoupons from "../../blp/components/BrandCoupons";
import ShippingCommenced from "../../blp/components/ShippingCommenced";
import MyCoupons from "../../blp/components/MyCoupons";
import styles from "./ProfileHome.css";
export default class ProfileHome extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isSelected: 0
    };
  }
  tabSelect(val) {
    this.setState({ isSelected: val });
  }
  render() {
    return (
      <div className={styles.base}>
        <ProfileMenuGrid />
        <div className={styles.accountHolder}>
          <AccountSetting
            image="http://tong.visitkorea.or.kr/cms/resource/58/1016958_image2_1.jpg"
            onClick={() => this.onClick()}
            heading="Anamika Dey"
          />
        </div>
        <div className={styles.tabHolder}>
          <TabHolder>
            <TabData
              width="40%"
              label="Recent Orders "
              selected={this.state.isSelected === 0}
              selectItem={() => this.tabSelect(0)}
            />
            <TabData
              width="40%"
              label="Alerts "
              selected={this.state.isSelected === 1}
              selectItem={() => this.tabSelect(1)}
            />
            <TabData
              width="40%"
              label="Coupons "
              selected={this.state.isSelected === 2}
              selectItem={() => this.tabSelect(2)}
            />
          </TabHolder>
        </div>
        <div className={styles.dataHolder}>
          {this.state.isSelected === 0 && (
            <div className={styles.ordersHolder}>
              <div className={styles.recentOrderHolder} />
            </div>
          )}
          {this.state.isSelected === 1 && (
            <div className={styles.alertsHolder}>
              <div className={styles.cardHolder}>
                <ShippingCommenced
                  heading="2 Rs. Voucher"
                  label="Shipping Commenced"
                />
              </div>
              <div className={styles.cardHolder}>
                <BrandCoupons
                  label="Min. 50% Off on top footwear brands. Shop from Red tape, Clarks & More"
                  heading="Walk Like you own the town"
                  image="http://wondermika.com/wp-content/uploads/2015/01/authentic-pandora2.jpg"
                />
              </div>
            </div>
          )}
          {this.state.isSelected === 2 && (
            <div className={styles.couponHolder}>
              <div className={styles.cardHolder}>
                <MyCoupons
                  heading="2 Rs. Voucher"
                  image="../../general/components/img/coupon-1.svg"
                  couponNumber="TEST3123"
                  label="(Long press here to copy the coupon code)"
                  maxRedemption="Max Redemption:"
                  maxRedemptionValue="1"
                  creationDate="Creation Date"
                  creationDateValue="Feb 22, 2018"
                  expiryDate="Expiry Date"
                  expiryDateValue="Feb 25,2018"
                />
              </div>
            </div>
          )}
        </div>
      </div>
    );
  }
}
