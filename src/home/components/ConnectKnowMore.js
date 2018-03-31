import React from "react";
import styles from "./ConnectKnowMore.css";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import { withRouter } from "react-router-dom";

class ConnectKnowMore extends React.Component {
  handleClick() {
    const urlSuffix = this.props.url.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
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

export default withRouter(ConnectKnowMore);
