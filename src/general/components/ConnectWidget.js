import React from "react";
import styles from "./ConnectWidget.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import image from "./img/Connect_Small.svg";
import iconImageURL from "./img/Connect_Small.svg";

export default class ConnectWidget extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.ConnectWidgetHolder}>
          <div className={styles.ConnectWidgetIcon}>
            <Image image={this.props.ConnectWidgetImage} color="transparent" />
          </div>
          <div className={styles.headerTextBox}>{this.props.headerText}</div>
          <div className={styles.ConnectWidgetLabel}>{this.props.text}</div>
          <div className={styles.ConnectWidgetLabel}>{this.props.knowMore}</div>
          <div className={styles.ConnectWidgetKnowMoreAuto}>
            <div className={styles.ConnectWidgetKnowMore} />
          </div>
        </div>
      </div>
    );
  }
}
ConnectWidget.propTypes = {
  ConnectWidgetImage: PropTypes.string,
  headerText: PropTypes.string,
  text: PropTypes.string,
  knowMore: PropTypes.string
};
ConnectWidget.defaultProps = {
  ConnectWidgetImage: image,
  headerText: "Faster Delivery, Easier Returns.",
  text: "Introducing Connect Service",
  knowMore: "know more"
};
