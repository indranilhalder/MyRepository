import React from "react";
import PropTypes from "prop-types";
import DeliveryCard from "./DeliveryCard.js";
import styles from "./DeliveryMode.css";
export default class DeliveryMode extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <DeliveryCard
          onClick={() => this.handleClick()}
          confirmTitle="Delivery Mode"
          indexNumber="2"
        >
          {this.props.productDelivery.map((data, i) => {
            return (
              <div className={styles.deliveryModeTextHolder} key={i}>
                <div className={styles.productName}>{data.productName}</div>
                <div className={styles.deliveryWay}>{data.deliveryWay}</div>
              </div>
            );
          })}
        </DeliveryCard>
      </div>
    );
  }
}
DeliveryMode.propTypes = {
  productDelivery: PropTypes.arrayOf(
    PropTypes.shape({
      productName: PropTypes.string,
      deliveryWay: PropTypes.string
    })
  ),
  onClick: PropTypes.func
};
