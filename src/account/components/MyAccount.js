import React from "react";
import PropTypes from "prop-types";
import InformationHeader from "../../general/components/InformationHeader.js";
import GetAllOrderContainer from "../containers/GetAllOrderContainer";
import UserCoupons from "./UserCoupons";
import UserAlerts from "./UserAlerts";
import ProfileMenuGrid from "../../blp/components/ProfileMenuGrid.js";
import AccountSetting from "./AccountSetting.js";
import TabHolder from "./TabHolder";
import TabData from "./TabData";
import styles from "./MyAccount.css";
import {
  MY_ACCOUNT_PAGE,
  MY_ACCOUNT_UPDATE_PROFILE_PAGE
} from "../../lib/constants";
export default class MyAccount extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isSelected: 0
    };
  }
  tabSelect(val) {
    this.setState({ isSelected: val });
  }
  renderToAccountSetting() {
    this.props.history.push(
      `${MY_ACCOUNT_PAGE}${MY_ACCOUNT_UPDATE_PROFILE_PAGE}`
    );
  }
  componentDidMount() {
    this.props.getUserDetails();
    this.props.getUserCoupons();
    this.props.getUserAlerts();
  }
  render() {
    return (
      <div className={styles.base}>
        <ProfileMenuGrid {...this.props} />
        <div className={styles.accountHolder}>
          <AccountSetting
            image="http://tong.visitkorea.or.kr/cms/resource/58/1016958_image2_1.jpg"
            onClick={() => this.renderToAccountSetting()}
            heading={
              this.props.userDetails &&
              `${this.props.userDetails.firstName} ${
                this.props.userDetails.lastName
              }`
            }
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
              <div className={styles.recentOrderHolder}>
                <GetAllOrderContainer />
              </div>
            </div>
          )}

          {this.state.isSelected === 1 && (
            <div className={styles.alertsHolder}>
              <UserAlerts userAlerts={this.props.userAlerts} />
            </div>
          )}
          {this.state.isSelected === 2 && (
            <div className={styles.couponHolder}>
              <UserCoupons userCoupons={this.props.userCoupons} />
            </div>
          )}
        </div>
      </div>
    );
  }
}
