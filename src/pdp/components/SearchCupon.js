import React from "react";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import styles from "./SearchCupon.css";
export default class SearchCupon extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      couponCode: this.props.couponCode ? this.props.couponCode : ""
    };
  }
  getValue(val) {
    if (this.props.getValue) {
      this.props.getValue(val);
    }
    this.setState({ couponCode: val });
  }

  onApply() {
    if (this.props.applyUserCoupon && this.props.couponCode) {
      this.props.applyUserCoupon(this.props.couponCode);
    }
  }
  componentWillReceiveProps(nextProps) {
    if (nextProps.couponCode !== this.state.couponCode) {
      this.setState({ couponCode: nextProps.couponCode });
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
              label={this.props.label}
              onClick={() => this.onApply()}
            />
          </div>
        </div>
        <div className={styles.inputHolder}>
          <Input2
            boxy={true}
            placeholder="Enter Coupon code"
            onChange={val => this.getValue(val)}
            value={this.state.couponCode}
            textStyle={{ fontSize: 14 }}
            height={35}
            background="#fff"
          />
        </div>
      </div>
    );
  }
}
SearchCupon.propTypes = {
  label: PropTypes.string,
  getValue: PropTypes.func,
  onApply: PropTypes.func
};
SearchCupon.defaultProps = {
  label: "Apply"
};
