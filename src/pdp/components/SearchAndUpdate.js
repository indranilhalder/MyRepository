import React from "react";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import gpsIcon from "../../general/components/img/GPS.svg";
import { Icon, CircleButton } from "xelpmoc-core";
import styles from "./SearchAndUpdate.css";
export default class SearchAndUpdate extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      pinCode: this.props.value,
      errorMessage: null
    };
  }
  getValue(pinCode) {
    if (pinCode.length <= 6) {
      this.setState({ pinCode });
    }
  }
  getLocation() {
    if (this.props.getLocation) {
      this.props.getLocation();
    }
  }

  onUpdate() {
    if (this.state.pinCode && this.state.pinCode.match(/^\d{6}$/)) {
      if (this.props.checkPinCodeAvailability) {
        this.props.checkPinCodeAvailability(this.state.pinCode);
      }
      this.setState({ errorMessage: null });
    } else {
      this.setState({ errorMessage: "Please enter a  valid pincode" });
    }
  }
  render() {
    return (
      <div className={styles.base}>
        {this.state.errorMessage && (
          <div className={styles.errorMessage}>{this.state.errorMessage}</div>
        )}
        <div className={styles.inputSearchHolder}>
          <div className={styles.buttonHolder}>
            <div className={styles.buttonCover}>
              <UnderLinedButton
                size="14px"
                fontFamily="regular"
                color="#000"
                label={this.props.labelText}
                onClick={() => this.onUpdate()}
              />
            </div>
          </div>
          <div className={styles.inputHolder}>
            <Input2
              boxy={true}
              value={this.state.pinCode}
              type="number"
              placeholder="Enter a pincode / zipcode"
              onChange={val => this.getValue(val)}
              textStyle={{ fontSize: 14 }}
              height={35}
              rightChildSize={35}
              rightChild={
                <CircleButton
                  size={35}
                  color={"transparent"}
                  icon={<Icon image={gpsIcon} size={20} />}
                  onClick={() => this.getLocation()}
                />
              }
            />
          </div>
        </div>
      </div>
    );
  }
}
SearchAndUpdate.propTypes = {
  getValue: PropTypes.func,
  getLocation: PropTypes.func,
  onUpdate: PropTypes.func,
  errorMessage: PropTypes.string
};

SearchAndUpdate.defaultProps = {
  labelText: "Update"
};
