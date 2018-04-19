import React from "react";
import styles from "./HomeSkeleton.css";
import HeroBannerSkeleton from "./HeroBannerSkeleton";
import CarouselSkeleton from "./CarouselSkeleton";
import VideoSkeleton from "./VideoSkeleton";
export default class HomeSkeleton extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.skeletonHolder}>
          <div className={styles.overlaySkeleton} />
          <HeroBannerSkeleton />
        </div>
        <div className={styles.skeletonHolder}>
          <div className={styles.overlaySkeleton} />
          <VideoSkeleton />
        </div>
        <div className={styles.dataSkeletonHolder}>
          <div className={styles.dataSkeleton}>
            <div className={styles.overlaySkeleton} />
          </div>
        </div>
        <div className={styles.skeletonHolder}>
          <div className={styles.overlaySkeleton} />
          <CarouselSkeleton />
        </div>
      </div>
    );
  }
}
