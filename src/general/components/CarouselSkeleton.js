import React from "react";
import styles from "./CarouselSkeleton.css";
export default class CarouselSkeleton extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.galleryHolder} />
        <div className={styles.galleryHolder} />
      </div>
    );
  }
}
