import React from "react";
import AuthPopUp from "./AuthPopUp";
import PropTypes from "prop-types";
import Button from "../../xelpmoc-core/Button";
import Icon from "../../xelpmoc-core/Icon";
import lockIcon from "./img/lock.svg";
import Input from "../../general/components/Input";
import { default as styles } from "./AuthPopUp.css";
import { default as ownStyles } from "./RestorePassword.css";
export default class RestorePassword extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userId: ""
    };
  }

  handleCancelClick() {
    if (this.props.handleCancel) {
      this.props.handleCancel();
    }
  }

  handleRestoreClick() {
    if (this.props.handleRestoreClick) {
      this.props.handleRestoreClick(this.state.userId);
    }
  }
  enterPassword(val) {
    if (val === "Enter") {
      this.handleRestoreClick();
    }
  }
  render() {
    return (
      <AuthPopUp>
        <div className={ownStyles.iconHolder}>
          <div className={ownStyles.icon}>
            <Icon image={lockIcon} size={30} />
          </div>
        </div>
        <div className={styles.header}>Reset your password</div>
        <div className={styles.content}>
          Please enter your email address or phone number to reset your password
        </div>
        <div className={styles.input}>
          <Input
            hollow={true}
            placeholder="Email or phone number"
            onChange={val => this.setState({ userId: val })}
            onKeyUp={event => {
              this.enterPassword(event.key);
            }}
          />
        </div>
        <div className={styles.button}>
          <div className={ownStyles.submit}>
            <Button
              label={"Reset"}
              width={150}
              height={40}
              borderRadius={20}
              backgroundColor={"#FF1744"}
              onClick={() => this.handleRestoreClick()}
              loading={this.props.loading}
              textStyle={{ color: "#FFF", fontSize: 14 }}
            />
          </div>
        </div>
        <div className={styles.button}>
          <div className={ownStyles.cancel}>
            <Button
              label={"Cancel"}
              onClick={() => this.handleCancelClick()}
              backgroundColor="transparent"
              width={100}
              height={40}
              borderRadius={20}
              loading={this.props.loading}
              textStyle={{ color: "#FFF", fontSize: 14 }}
            />
          </div>
        </div>
      </AuthPopUp>
    );
  }
}

RestorePassword.propTypes = {
  handleCancel: PropTypes.func,
  handleRestoreClick: PropTypes.func
};
