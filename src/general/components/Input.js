import React from "react";
import styles from "./Input.css";
import { Input as CoreInput } from "xelpmoc-core";

export default class Input extends React.Component {
  render() {
    return (
      <div className={styles.container}>
        <CoreInput hollow={true} {...this.props} styles={styles} />
      </div>
    );
  }
}
