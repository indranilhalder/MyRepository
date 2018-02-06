import React from "react";
import styles from "./ConnectWidget.css";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
import iconImageURL from "./img/Connect_Small.svg";
import MediaQuery from "react-responsive";
import ConnectKnowMoreContainer from "../containers/ConnectKnowMoreContainer";
import ConnectBaseWidget from "./ConnectBaseWidget";
export default class ConnectWidget extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    let className = styles.base;
    if (this.props.feedComponentData["sub-type"] === "bannerInCard") {
      className = styles.inCard;
    }

    return (
      <div className={styles.holder}>
        <MediaQuery query="(min-device-width: 1025px)">
          <ConnectBaseWidget {...this.props.feedComponentData.data} />
        </MediaQuery>
        <MediaQuery query="(max-device-width: 1024px)">
          <div className={className}>
            <div className={styles.buffer}>
              <div className={styles.content}>
                <div className={styles.icon}>
                  <Icon image={iconImageURL} size={40} />
                </div>
                <div className={styles.connectBox}>{this.props.header}</div>
                <div className={styles.label}>{this.props.text}</div>
                <div className={styles.buttonBox}>
                  <ConnectKnowMoreContainer
                    data={this.props.feedComponentData.data}
                  />
                </div>
              </div>
            </div>
          </div>
        </MediaQuery>
      </div>
    );
  }
}
ConnectWidget.propTypes = {
  header: PropTypes.string,
  text: PropTypes.string,
  knowMore: PropTypes.string,
  onClick: PropTypes.func
};
ConnectWidget.defaultProps = {
  header: "Faster Delivery, Easier Returns.",
  text: "Introducing Connect Service"
};
