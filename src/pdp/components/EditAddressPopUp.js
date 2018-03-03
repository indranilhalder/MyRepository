import React from "react";
import styles from "./EditAddressPopUp.css";
import Input2 from "../../general/components/Input2.js";
import OrderReturn from "../../account/components/OrderReturn";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";

export default class EditAddressPopUp extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }
  onChange(val) {
    if (this.props.onChange) {
      this.props.onChange(val);
    }
  }
  cancelAddress() {
    if (this.props.onCancel) {
      this.props.onCancel();
    }
  }
  saveChanges(val) {
    if (this.props.saveChanges) {
      this.props.saveChanges(val);
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
              placeholder={this.props.contactNumber}
              boxy={true}
              textStyle={{ fontSize: 14 }}
              height={33}
              onChange={contactNumber => this.onChange({ contactNumber })}
            />
          </div>
          <div className={styles.container}>
            <Input2
              placeholder={this.props.line1}
              boxy={true}
              textStyle={{ fontSize: 14 }}
              height={33}
              onChange={line1 => this.onChange({ line1 })}
            />
          </div>
          <div className={styles.container}>
            <Input2
              placeholder={this.props.line2}
              boxy={true}
              textStyle={{ fontSize: 14 }}
              height={33}
              onChange={line2 => this.onChange({ line2 })}
            />
          </div>
          <div className={styles.container}>
            <Input2
              placeholder={this.props.postalCode}
              boxy={true}
              textStyle={{ fontSize: 14 }}
              height={33}
              onChange={postalCode => this.onChange({ postalCode })}
            />
          </div>
          <div className={styles.container}>
            <SelectBoxMobile
              value={this.props.state}
              arrowColour="black"
              height={33}
              onChange={state => this.onChange({ state })}
            />
          </div>
          <div className={styles.container}>
            <SelectBoxMobile
              value={this.props.country}
              arrowColour="black"
              height={33}
              onChange={country => this.onChange({ country })}
            />
          </div>
        </div>
        <div className={styles.buttonHolder}>
          <OrderReturn
            buttonLabel={this.props.buttonLabel}
            underlineButtonLabel={this.props.underlineButtonLabel}
            writeReview={() => this.cancelAddress()}
            replaceItem={() => this.saveChanges()}
          />
        </div>
      </div>
    );
  }
}
