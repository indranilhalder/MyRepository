import React from "react";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import styles from "./PickUpDetails.css";
import Button from "../../general/components/Button";

export default class PickUpDetails extends React.Component {
  getValue(val) {
    if (this.props.getValue) {
      this.props.getValue(val);
    }
  }
  handleClick() {
    if (this.props.onSubmit) {
      this.props.onSubmit();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>Pickup details</div>
        <div className={styles.subHeader}>
          (Changing pickup person details will overwrite the previous pick up
          person details)
        </div>
        <div className={styles.inputHolder}>
          <Input2
            placeholder="Full name"
            boxy={true}
            onChange={name => this.getValue({ name })}
            textStyle={{ fontSize: 14 }}
            height={33}
            value={this.props.name ? this.props.name : ""}
          />
        </div>
        <div className={styles.inputHolder}>
          <Input2
            placeholder="Phone number"
            type="tel"
            boxy={true}
            onChange={mobile => this.getValue({ mobile })}
            textStyle={{ fontSize: 14 }}
            height={33}
            value={this.props.mobile ? this.props.mobile : ""}
          />
        </div>
        <div
          className={styles.buttonContainer}
          onClick={() => this.handleClick()}
        >
          <Button type="primary" label="Continue" color="#fff" width={121} />
        </div>
      </div>
    );
  }
}
PickUpDetails.propTypes = {
  getValue: PropTypes.func,
  onSubmit: PropTypes.func
};
