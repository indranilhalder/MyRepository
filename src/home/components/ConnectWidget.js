import React from "react";
import styles from "./ConnectWidget.css";
import Icon from "../../xelpmoc-core/Icon";
import PropTypes from "prop-types";
import iconImageURL from "./img/Connect_Small.svg";
import MediaQuery from "react-responsive";
import ConnectKnowMoreContainer from "../containers/ConnectKnowMoreContainer";
import ConnectKnowMore from "./ConnectKnowMore";
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
      <div
        className={styles.holder}
        style={{
          backgroundImage: `linear-gradient(165deg, ${
            this.props.feedComponentData.startHexCode
          } ,${this.props.feedComponentData.endHexCode})`
        }}
      >
        <MediaQuery query="(min-device-width: 1025px)">
          <ConnectBaseWidget {...this.props.feedComponentData} />
        </MediaQuery>
        <MediaQuery query="(max-device-width: 1024px)">
          <div className={className}>
            <div
              className={styles.buffer}
              style={{
                backgroundImage: `url(${
                  this.props.feedComponentData.backgroundImageURL
                }`,
                backgroundRepeat: "no-repeat",
                backgroundSize: "contain",
                backgroundPosition: "center"
              }}
            >
              <div className={styles.content}>
                {this.props.feedComponentData.iconImageURL && (
                  <div className={styles.icon}>
                    <Icon
                      image={this.props.feedComponentData.iconImageURL}
                      size={40}
                    />
                  </div>
                )}
                <div className={styles.dataHolder}>
                  <div className={styles.connectBox}>
                    {this.props.feedComponentData.title}
                  </div>
                  <div className={styles.label}>
                    {this.props.feedComponentData.description}
                  </div>
                  {this.props.feedComponentData.btnText && (
                    <div className={styles.buttonBox}>
                      <ConnectKnowMore
                        url={this.props.feedComponentData.webURL}
                        btnText={this.props.feedComponentData.btnText}
                      />
                    </div>
                  )}
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
  knowMore: PropTypes.string,
  onClick: PropTypes.func
};
ConnectWidget.defaultProps = {
  header: "Faster Delivery, Easier Returns.",
  text: "Introducing Connect Service"
};
