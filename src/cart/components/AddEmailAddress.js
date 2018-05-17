import React from "react";
import styles from "./AddEmailAddress.css";
import Input2 from "../../general/components/Input2.js";
export default class AddEmailAddress extends React.Component {
  onChange(val) {
    if (this.props.onChange) {
      this.props.onChange(val);
    }
  }
  handleOnFocusInput() {
    if (this.props.onFocusInput) {
      this.props.onFocusInput();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>Add an Email Address</div>
        <div className={styles.subText}>
          We need your email ID to communicate all the order related details
        </div>
        <div className={styles.inputHolder}>
          <Input2
            placeholder="Email ID*"
            value={this.props.value}
            onChange={val => this.onChange(val)}
            textStyle={{ fontSize: 14 }}
            height={33}
            boxy={true}
            onFocus={() => {
              this.handleOnFocusInput();
            }}
          />
        </div>
        <div className={styles.noteText}>
          <span>Please Note:</span> You can also use this email id to login to
          your account
        </div>
      </div>
    );
  }
}
