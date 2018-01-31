import React from "react";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import styles from "./ConnectButton.css";
import { Icon, CircleButton } from "xelpmoc-core";
import iconImageURL from "../../home/components/img/Connect_Small.svg";
export default class ConnectButton extends React.Component {
  render() {
    return (
      <div className={styles.connectButtonHolder}>
        <CircleButton
          color={"transparent"}
          size={50}
          icon={<Icon image={iconImageURL} size={30} />}
        />
      </div>
    );
  }
}
