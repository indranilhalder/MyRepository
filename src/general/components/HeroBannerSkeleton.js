import React from "react";
import styles from "./HeroBannerSkeleton.css";
export default class HeroBannerSkeleton extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.leftSide} />
        <div className={styles.rightSide} />
        <div className={styles.frontSkeliton} />
      </div>
    );
  }
}
