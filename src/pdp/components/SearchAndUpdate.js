import React from "react";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import gpsIcon from "../../general/components/img/GPS.svg";
import { Icon, CircleButton } from "xelpmoc-core";
import styles from "./SearchAndUpdate.css";
export default class SearchAndUpdate extends React.Component {
  getValue(val) {
    if (this.props.getValue) {
      this.props.getValue(val);
    }
  }
  handleClick() {
    if (this.props.getLocation) {
      this.props.getLocation();
    }
  }
  onUpdate() {
    if (this.props.onUpdate) {
      this.props.onUpdate();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.inputHolder}>
          <Input2
            boxy={true}
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
                onClick={() => this.handleClick()}
              />
            }
          />
        </div>
        <div className={styles.buttonHolder}>
          <div className={styles.buttonCover}>
            <UnderLinedButton
              size="14px"
              fontFamily="regular"
              color="#000"
              label="Update"
              onClick={() => this.onUpdate()}
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
  onUpdate: PropTypes.func
};
