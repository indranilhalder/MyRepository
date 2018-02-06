import React from "react";
import styles from "./ProductGalleryMobile.css";
export default class ProductGalleryMobile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      position: 0
    };
  }
  forward = () => {
    if (this.state.position < this.props.children.length - 1) {
      const position = this.state.position + 1;
      this.setState({ position: position });
    }
  };
  back = () => {
    if (this.state.position > 0) {
      const position = this.state.position - 1;
      this.setState({ position: position });
    }
  };
  swipeStart(evt) {
    evt.stopPropagation();
    this.setState({ touchStart: evt.touches[0].clientX });
  }
  swipeMove(evt) {
    evt.stopPropagation();
    this.setState({ touchEnd: evt.touches[0].clientX });
  }
  swipeEnd() {
    if (this.state.touchStart < this.state.touchEnd) {
      this.back();
    } else {
      this.forward();
    }
  }
  renderNav() {}
  render() {
    const translateAmount = this.state.position * -100;

    return (
      <div
        className={styles.base}
        onTouchStart={evt => this.swipeStart(evt)}
        onTouchMove={evt => this.swipeMove(evt)}
        onTouchEnd={evt => this.swipeEnd(evt)}
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
                    this.state.position === i
                      ? styles.navActive
                      : styles.navButton
                  }
                />
              );
            })}
        </div>
      </div>
    );
  }
}
