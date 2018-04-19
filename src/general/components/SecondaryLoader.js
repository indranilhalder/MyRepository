import React from "react";
import styles from "./SecondaryLoader.css";
export default class SecondaryLoader extends React.Component {
  render() {
    if (this.props.display) {
      return (
        <div className={styles.base}>
          <div className={styles.loader} />
        </div>
      );
    } else {
      return null;
    }
  }
}

SecondaryLoader.defaultProps = {
  display: true
};
