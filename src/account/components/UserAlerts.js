import React from "react";
import PropTypes from "prop-types";
import * as styles from "./UserAlerts.css";
import ShippingCommenced from "../../blp/components/ShippingCommenced";
import * as Cookie from "../../lib/Cookie";
import {
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN
} from "../../lib/constants";
export default class UserAlerts extends React.Component {
  componentDidMount() {
    if (this.props.getUserAlerts) {
      const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
      const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
      if (userDetails && customerCookie) {
        this.props.getUserAlerts();
      }
    }
  }
  render() {
    const { userAlerts } = this.props;
    return (
      <div className={styles.base}>
        {userAlerts && userAlerts.orderNotifications ? (
          userAlerts.orderNotifications.map(alert => (
            <div className={styles.cardHolder}>
              <ShippingCommenced
                heading={alert.orderStatus}
                label={alert.orderDetailStatus}
                orderNotificationPassDate={alert.orderNotificationPassDate}
              />
            </div>
          ))
        ) : (
          <div className={styles.noAlerts}>{"No Alerts"}</div>
        )}
      </div>
    );
  }
}
UserAlerts.propTypes = {
  userAlerts: PropTypes.shape({ orderNotifications: PropTypes.array })
};
