import React from "react";
import styles from "./LogInButtonStyles.css";
import { Button } from "xelpmoc-core";
import MediaQuery from "react-responsive";
import PropTypes from "prop-types";
export default class LogInButton extends React.Component {
  onClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    return (
      <div>
        <MediaQuery query="(min-device-width: 1024px)">
          <div className={styles.base}>
            <Button
              label={"Sign in"}
              width={150}
              height={40}
              borderRadius={20}
              backgroundColor={"#FF1744"}
              onClick={this.onClick}
              loading={this.props.loading}
              textStyle={{ color: "#FFF", fontSize: 14 }}
            />
          </div>
        </MediaQuery>
        <MediaQuery query="(max-device-width:1023px)">
          <div className={styles.base}>
            <Button
              backgroundColor={"#FF1744"}
              label={"Login"}
              width={100}
              height={40}
              borderRadius={20}
              onClick={this.onClick}
              loading={this.props.loading}
              textStyle={{ color: "#FFF", fontSize: 14 }}
            />
          </div>
        </MediaQuery>
      </div>
    );
  }
}
LogInButton.propTypes = {
  onClick: PropTypes.func,
  loading: PropTypes.bool
};
