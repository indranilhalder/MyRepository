import React from "react";
import { PulseLoader } from "react-spinners";
import styles from "./LoadingIndicator.css";

export default class LoadingIndicator extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <PulseLoader {...this.props} />
      </div>
    );
  }
}
