import React from "react";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import gpsIcon from "../../general/components/img/GPS.svg";
import { Icon, CircleButton } from "xelpmoc-core";
import styles from "./SearchLocationByPincode.css";
export default class SearchLocationByPincode extends React.Component {
  getValue(val) {
    if (this.props.changePincode) {
      this.props.changePincode(val);
    }
  }
  handleClick() {
    if (this.props.getLocation) {
      this.props.getLocation();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>{this.props.header}</div>
        <div className={styles.inputHolder}>
          <Input2
            placeholder={`Your pincode :${this.props.pincode}`}
            type="number"
            boxy={true}
            onChange={val => this.getValue(val)}
            textStyle={{ fontSize: 14 }}
            height={35}
            rightChildSize={35}
            rightChild={
              <CircleButton
                size={35}
                color={"transparent"}
                icon={<Icon image={gpsIcon} size={20} />}
                onClick={() => this.handleClick()}
              />
            }
          />
        </div>
      </div>
    );
  }
}
SearchLocationByPincode.propTypes = {
  header: PropTypes.string,
  pincode: PropTypes.string,
  getLocation: PropTypes.func,
  changePincode: PropTypes.func
};
