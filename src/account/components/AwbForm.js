import React from "react";
import styles from "./AwbForm.css";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import Input2 from "../../general/components/Input2.js";
import Button from "../../general/components/Button";
import ImageUpload from "./ImageUpload";
export default class AwbForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      awbNumber: this.props.awbNumber ? this.props.awbNumber : "",
      logisticsPartner: this.props.logisticsPartner
        ? this.props.logisticsPartner
        : "",
      courierCharge: this.props.courierCharge ? this.props.courierCharge : ""
    };
  }
  updateLater() {
    if (this.props.updateLater) {
      this.props.updateLater();
    }
  }
  onUpdate() {
    if (this.props.onUpdate) {
      this.props.onUpdate(this.state);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.inputHolder}>
          <Input2
            boxy={true}
            placeholder="AWB number"
            value={
              this.props.awbNumber ? this.props.awbNumber : this.state.awbNumber
            }
            onChange={awbNumber => this.setState({ awbNumber })}
            textStyle={{ fontSize: 14 }}
            height={35}
          />
        </div>
        <div className={styles.inputHolder}>
          <Input2
            boxy={true}
            placeholder="Logistics partner"
            value={
              this.props.logisticsPartner
                ? this.props.logisticsPartner
                : this.state.logisticsPartner
            }
            onChange={logisticsPartner => this.setState({ logisticsPartner })}
            textStyle={{ fontSize: 14 }}
            height={35}
          />
        </div>
        <div className={styles.inputHolder}>
          <Input2
            boxy={true}
            placeholder="Courier charges in Rs."
            value={
              this.props.courierCharge
                ? this.props.courierCharge
                : this.state.courierCharge
            }
            onChange={courierCharge => this.setState({ courierCharge })}
            textStyle={{ fontSize: 14 }}
            onlyNumber={true}
            height={35}
          />
        </div>

        <div className={styles.inputHolder}>
          <ImageUpload onChange={file => this.setState({ file })} />
        </div>

        {this.props.isShowButton && (
          <div className={styles.buttonHolder}>
            {this.props.updateLater && (
              <div className={styles.doItLaterHolder}>
                <UnderLinedButton
                  size="14px"
                  fontFamily="regular"
                  color="#000"
                  label="Do it later"
                  onClick={() => this.updateLater()}
                />
              </div>
            )}

            <div className={styles.updateHolder}>
              <UnderLinedButton
                size="14px"
                fontFamily="regular"
                color="#ff1744"
                label="Update"
                onClick={() => this.onUpdate()}
              />
            </div>
          </div>
        )}
        <div className={styles.PopUpbuttonHolder}>
          <div className={styles.PopUpbutton}>
            <Button
              width={176}
              type="primary"
              label="Submit"
              onClick={() => this.onUpdate()}
            />
          </div>
        </div>
      </div>
    );
  }
}
AwbForm.propTypes = {
  doItLater: PropTypes.func,
  updateLater: PropTypes.func,
  awbNumber: PropTypes.string,
  logisticsPartner: PropTypes.string,
  courierCharge: PropTypes.string,
  onChange: PropTypes.func,
  isUpload: PropTypes.bool,
  isShowButton: PropTypes.bool
};
