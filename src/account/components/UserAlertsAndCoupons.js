import React from "react";
import TabHolder from "./TabHolder";
import TabData from "./TabData";
import UserAlerts from "./UserAlerts";
import UserCoupons from "./UserCoupons";
import {
  MY_ACCOUNT_PAGE,
  MY_ACCOUNT_ALERTS_PAGE,
  MY_ACCOUNT_COUPON_PAGE
} from "../../lib/constants";
import * as styles from "./UserAlertsAndCoupons.css";

const URL_PATH_ALERTS = `${MY_ACCOUNT_PAGE}${MY_ACCOUNT_ALERTS_PAGE}`;
const URL_PATH_COUPONS = `${MY_ACCOUNT_PAGE}${MY_ACCOUNT_COUPON_PAGE}`;
const COUPONS = "coupons";
const ALERTS = "alerts";
export default class UserAlertsAndCoupons extends React.Component {
  componentDidMount() {
    this.props.getUserAlerts();
    this.props.getUserCoupons();
  }
  renderToAlerts() {
    this.props.history.push(URL_PATH_ALERTS);
  }

  renderToCoupons() {
    this.props.history.push(URL_PATH_COUPONS);
  }
  render() {
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
