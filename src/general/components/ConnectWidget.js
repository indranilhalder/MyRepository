import React from "react";
import styles from "./ConnectWidget.css";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
import iconImageURL from "./img/Connect_Small.svg";
export default class ConnectWidget extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.innerBox}>
          <div className={styles.icon}>
            <Icon image={iconImageURL} size={40} />
          </div>
          <div className={styles.connectBox}>{this.props.header}</div>
          <div className={styles.label}>{this.props.text}</div>
          <div className={styles.buttonBox}>
            <div
              className={styles.button}
              onClick={() => {
                this.handleClick();
              }}
            >
              {this.props.knowMore}
            </div>
          </div>
        </div>
      </div>
    );
  }
}
ConnectWidget.propTypes = {
  ConnectWidgetImage: PropTypes.string,
  header: PropTypes.string,
  text: PropTypes.string,
  knowMore: PropTypes.string,
  onClick: PropTypes.func
};
ConnectWidget.defaultProps = {
  ConnectWidgetImage: Icon,
  header: "Faster Delivery, Easier Returns.",
  text: "Introducing Connect Service",
  knowMore: "know more"
};
