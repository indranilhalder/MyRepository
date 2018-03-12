import React from "react";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import styles from "./SearchCupon.css";
export default class SearchCupon extends React.Component {
  getValue(val) {
    if (this.props.getValue) {
      this.props.getValue(val);
    }
  }

  onApply() {
    if (this.props.applyUserCoupon) {
      this.props.applyUserCoupon(this.props.couponCode);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.buttonHolder}>
          <div className={styles.buttonCover}>
            <UnderLinedButton
              size="14px"
              fontFamily="regular"
              color="#000"
              label="Apply"
              onClick={() => this.onApply()}
            />
          </div>
        </div>
        <div className={styles.inputHolder}>
          <Input2
            boxy={true}
            placeholder="Enter Coupon code"
            onChange={val => this.getValue(val)}
            value={this.props.couponCode}
            textStyle={{ fontSize: 14 }}
            height={35}
          />
        </div>
      </div>
    );
  }
}
SearchCupon.propTypes = {
  getValue: PropTypes.func,
  onApply: PropTypes.func
};
