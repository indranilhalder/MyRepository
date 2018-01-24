import React from "react";
import styles from "./ConnectWidgetModal.css";
import { Icon } from "xelpmoc-core";
import delivery from "./img/delivery.svg";
import PropTypes from "prop-types";
export default class ConnectWidgetModal extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.manage}>
          <div className={styles.icon}>
            <Icon image={delivery} size={30} />
          </div>
          <div className={styles.title}>{this.props.title}</div>
          <div className={styles.description}>{this.props.description}</div>
        </div>
      </div>
    );
  }
}

ConnectWidgetModal.PropsTypes = {
  title: PropTypes.string,
  description: PropTypes.string,
  image: PropTypes.string
};
