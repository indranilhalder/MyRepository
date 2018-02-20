import React from "react";
import styles from "./OrderBanner.css";
import PropTypes from "prop-types";
import Button from "../../general/components/Button";
export default class OrderBanner extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.orderInnerBox}>
          <div className={styles.orderHeadong}>{this.props.headingText}</div>
          <div className={styles.orderLabel}>{this.props.label}</div>
          <div className={styles.buttonHolder}>
            <Button
              type="hollow"
              color="#fff"
              label={this.props.buttonText}
              width={150}
              onClick={() => this.handleClick(console.log("pay now"))}
            />
          </div>
        </div>
      </div>
    );
  }
}
OrderBanner.propTypes = {
  headingText: PropTypes.string,
  label: PropTypes.string,
  buttonText: PropTypes.string
};
