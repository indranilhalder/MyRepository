import React from "react";
import styles from "./ConnectWidget.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import image from "./img/Connect_Small.svg";
import iconImageURL from "./img/Connect_Small.svg";

export default class ConnectWidget extends React.Component {
  handleClick(val) {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.ConnectHolder}>
          <div className={styles.icon}>
            <Image image={this.props.ConnectWidgetImage} color="transparent" />
          </div>
          <div className={styles.textBox}>{this.props.headerText}</div>
          <div className={styles.label}>{this.props.text}</div>
          <div className={styles.text} onClick={() => this.handleClick()}>
            {this.props.knowMore}
          </div>
          <div className={styles.knowMore}>
            <div className={styles.knowMoreselect} />
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
