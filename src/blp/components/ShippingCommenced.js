import React from "react";
import styles from "./ShippingCommenced.css";
import Icon from "../../xelpmoc-core/Icon";
import PropTypes from "prop-types";
import couponIcon from "../../general/components/img/order-history.svg";
export default class ShippingCommenced extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        {this.props.heading && (
          <div className={styles.couponInnerBox}>
            <div className={styles.couponIcon}>
              <Icon image={couponIcon} size={25} />
            </div>
            <div className={styles.headingText}>{this.props.heading}</div>
          </div>
        )}

        <div className={styles.lebelText}>{this.props.label}</div>
        {this.props.orderNotificationPassDate && (
          <div className={styles.dayAgo}>{`${
            this.props.orderNotificationPassDate
          } ago`}</div>
        )}
      </div>
    );
  }
}
ShippingCommenced.propTypes = {
  label: PropTypes.string,
  image: PropTypes.string,
  heading: PropTypes.string
};
