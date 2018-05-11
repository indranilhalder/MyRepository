import React from "react";
import PropTypes from "prop-types";
import * as styles from "./UserAlerts.css";
import ShippingCommenced from "../../blp/components/ShippingCommenced";
export default class UserAlerts extends React.Component {
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
