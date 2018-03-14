import React from "react";
import styles from "./EditAccountDetails.css";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
import MobileDatePicker from "../../general/components/MobileDatePicker";
export default class EditAccountDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      gender: props.gender ? props.gender : ""
    };
  }
  onChange(val) {
    if (this.props.onChange) {
      this.props.onChange(val);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.holder}>
          <div className={styles.container}>
            <Input2
              placeholder={this.props.userName}
              boxy={true}
              textStyle={{ fontSize: 14 }}
              height={33}
              onChange={userName => this.onChange({ userName })}
            />
          </div>
          <div className={styles.container}>
            <Input2
              placeholder={this.props.emailId}
              boxy={true}
              textStyle={{ fontSize: 14 }}
              height={33}
              onChange={emailId => this.onChange({ emailId })}
            />
          </div>
          <div className={styles.container}>
            <Input2
              placeholder={this.props.phoneNumber}
              boxy={true}
              textStyle={{ fontSize: 14 }}
              height={33}
              onChange={phoneNumber => this.onChange({ phoneNumber })}
            />
          </div>

          <div className={styles.container}>
            <SelectBoxMobile
              value={this.props.gender}
              options={[
                { label: "Female", value: "Female" },
                { label: "Male", value: "Male" }
              ]}
              selected={
                this.props.gender && {
                  label: this.props.gender,
                  value: this.props.gender
                }
              }
              arrowColour="grey"
              height={33}
              onChange={gender => this.onChange({ gender })}
            />
          </div>
          <div className={styles.container}>
            <MobileDatePicker onChange={date => this.onChange({ date })} />
          </div>
        </div>
      </div>
    );
  }
}
EditAccountDetails.propTypes = {
  emailId: PropTypes.string,
  phoneNumber: PropTypes.string,
  userName: PropTypes.string,
  gender: PropTypes.string,
  onChange: PropTypes.func
};
EditAccountDetails.defaultProps = {
  phoneNumber: "7358082465",
  emailId: "ananya_patel@123.com",
  userName: "Anaya Patel",
  gender: "Male"
};
