import React from "react";
import styles from "./ConnectKnowMore.css";
export default class ConnectKnowMore extends React.Component {
  handleClick() {
    if (this.props.showConnectModal) {
      this.props.showConnectModal();
    }
  }
  render() {
    return (
      <div
        className={styles.button}
        onClick={() => {
          this.handleClick();
        }}
      >
        know more
      </div>
    );
  }
}
