import React from "react";
import PropTypes from "prop-types";
import TabHolder from "./TabHolder";
import TabData from "./TabData";
import UserAlerts from "./UserAlerts";
import UserCoupons from "./UserCoupons";
import Loader from "../../general/components/Loader";
import * as Cookie from "../../lib/Cookie";
import {
  MY_ACCOUNT_PAGE,
  MY_ACCOUNT_ALERTS_PAGE,
  MY_ACCOUNT_COUPON_PAGE,
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN,
  LOGIN_PATH,
  ALERTS_COUPON
} from "../../lib/constants";

import * as styles from "./UserAlertsAndCoupons.css";
import {
  setDataLayer,
  ADOBE_MY_ACCOUNT_ALERTS,
  ADOBE_MY_ACCOUNT_COUPONS
} from "../../lib/adobeUtils";

const URL_PATH_ALERTS = `${MY_ACCOUNT_PAGE}${MY_ACCOUNT_ALERTS_PAGE}`;
const URL_PATH_COUPONS = `${MY_ACCOUNT_PAGE}${MY_ACCOUNT_COUPON_PAGE}`;
const COUPONS = "coupons";
const ALERTS = "alerts";
export default class UserAlertsAndCoupons extends React.Component {
  componentDidMount() {
    const { pathname } = this.props.history.location;
    if (pathname === URL_PATH_ALERTS) {
      setDataLayer(ADOBE_MY_ACCOUNT_ALERTS);
    } else if (pathname === URL_PATH_COUPONS) {
      setDataLayer(ADOBE_MY_ACCOUNT_COUPONS);
    }
    this.props.setHeaderText(ALERTS_COUPON);
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

    if (userDetails && customerCookie) {
      this.props.getUserAlerts();
      this.props.getUserCoupons();
    } else {
      this.props.history.push(LOGIN_PATH);
    }
  }
  componentDidUpdate() {
    this.props.setHeaderText(ALERTS_COUPON);
  }
  renderToAlerts() {
    this.props.history.push(URL_PATH_ALERTS);
  }

  renderToCoupons() {
    this.props.history.push(URL_PATH_COUPONS);
  }
  renderLoader() {
    return <Loader />;
  }

  render() {
    if (this.props.loadingForUserCoupons || this.props.loadingForUserAlerts) {
      return this.renderLoader();
    }
    const { pathname } = this.props.history.location;
    let currentActivePath;
    if (pathname === URL_PATH_ALERTS) {
      currentActivePath = ALERTS;
    } else if (pathname === URL_PATH_COUPONS) {
      currentActivePath = COUPONS;
    }
    return (
      <div className={styles.base}>
        <div className={styles.tabHeader}>
          <TabHolder>
            <TabData
              width="50%"
              label="Alerts"
              selected={currentActivePath === ALERTS}
              selectItem={() => this.renderToAlerts()}
            />
            <TabData
              width="50%"
              label="Coupons "
              selected={currentActivePath === COUPONS}
              selectItem={() => this.renderToCoupons()}
            />
          </TabHolder>
        </div>
        <div className={styles.tabBody}>
          {currentActivePath === ALERTS && (
            <UserAlerts userAlerts={this.props.userAlerts} />
          )}
          {currentActivePath === COUPONS && (
            <UserCoupons userCoupons={this.props.userCoupons} />
          )}
        </div>
      </div>
    );
  }
}
UserAlertsAndCoupons.propTypes = {
  loadingForUserAlerts: PropTypes.bool,
  loadingForUserCoupons: PropTypes.bool,
  userAlerts: PropTypes.shape({ orderNotifications: PropTypes.array }),
  userCoupons: PropTypes.shape({ unusedCouponsList: PropTypes.array }),
  getUserAlerts: PropTypes.func,
  getUserCoupons: PropTypes.func
};
