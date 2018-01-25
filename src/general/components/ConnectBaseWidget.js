import React from "react";
import styles from "./ConnectBothWidget.css";
import ConnectBothWidget from "./ConnectBothWidget";
import Connect from "./img/Connect_Small.svg";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
export default class ConnectBaseWidget extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    let data = this.props.data;
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          <div className={styles.iconBase}>
            <div className={styles.iconHolder}>
              <Icon image={Connect} size={50} />
            </div>
            <div className={styles.text}>{this.props.text}</div>
          </div>
          <div className={styles.heading}>{this.props.heading}</div>
        </div>
        {data.map((datum, i) => {
          return (
            <ConnectBothWidget
              key={i}
              title={datum.title}
              description={datum.description}
              ImageURL={datum.ImageURL}
            />
          );
        })}
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
    );
  }
}

ConnectBaseWidget.propTypes = {
  text: PropTypes.string,
  image: PropTypes.string,
  heading: PropTypes.string,
  onClick: PropTypes.func,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      title: PropTypes.string,
      description: PropTypes.string
    })
  )
};
