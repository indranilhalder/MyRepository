import React from "react";
import styles from "./ErrorPage.css";
export default class ErrorPage extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.error}>404</div>
      </div>
    );
  }
}
