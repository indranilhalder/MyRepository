import React from "react";
import TabHolder from "./TabHolder";
import TabData from "./TabData";
import * as styles from "./UserAlertsAndCoupons.css";

export default class UserAlertsAndCoupons extends React.Component {
  componentDidMount() {
    this.props.getUserAlerts();
    this.props.getUserCoupons();
  }
  render() {
    console.log(this.props);
    return (
      <div className={styles.base}>
        <div className={styles.tabHead}>
          <TabHolder>
            <TabData
              width="50%"
              label="Alerts"
              selected={true}
              selectItem={() => this.tabSelect(0)}
            />
            <TabData
              width="50%"
              label="Coupons "
              selected={true}
              selectItem={() => this.tabSelect(1)}
            />
          </TabHolder>
        </div>
        <div className={styles.tabBody}>mainsd</div>
      </div>
    );
  }
}
