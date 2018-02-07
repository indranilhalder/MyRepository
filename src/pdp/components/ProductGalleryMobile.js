import React from "react";
import withTouchControls from "../../higherOrderComponents/withTouchControls";
import styles from "./ProductGalleryMobile.css";
class ProductGalleryMobile extends React.Component {
  handleSwipeStart(evt) {
    if (this.props.onTouchStart) {
      this.props.onTouchStart(evt);
    }
  }
  handleSwipeMove(evt) {
    if (this.props.onTouchStart) {
      this.props.onTouchMove(evt);
    }
  }
  handleSwipeEnd(evt) {
    if (this.props.onTouchEnd) {
      this.props.onTouchEnd(evt);
    }
  }
  render() {
    const translateAmount = this.props.position * -100;
    return (
      <div
        className={styles.base}
        onTouchStart={evt => this.handleSwipeStart(evt)}
        onTouchMove={evt => this.handleSwipeMove(evt)}
        onTouchEnd={evt => this.handleSwipeEnd(evt)}
      >
        <div
          className={styles.gallery}
          style={{ transform: `translateX(${translateAmount}%)` }}
        >
          {this.props.children &&
            this.props.children.map((child, i) => {
              return (
                <div className={styles.content} key={i}>
                  {child}
                </div>
              );
            })}
        </div>
        <div className={styles.navHolder}>
          {this.props.children &&
            this.props.children.map((val, i) => {
              return (
                <div
                  className={
                    this.props.position === i
                      ? styles.navActive
                      : styles.navButton
                  }
                  key={i}
                />
              );
            })}
        </div>
      </div>
    );
  }
}
export default withTouchControls(ProductGalleryMobile);
