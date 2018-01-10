import React from "react";
import styles from "./BannerMobile.css";
export default class BannerMobile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      items: this.props.children
        ? React.Children.map(this.props.children, (child, i) => {
            return child;
          })
        : null
    };
  }
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
      this.slideForward();
    } else {
      this.slideBack();
    }
  }
  slideForward = () => {
    this.setState({ direction: "forward" });
  };
  slideBack = () => {
    this.setState({ direction: "back" });
  };
  swapForward = () => {
    const items = [];
    items.push(this.state.items[2]);
    items.push(this.state.items[0]);
    items.push(this.state.items[1]);
    this.setState({ items }, () => {
      this.setState({ direction: "" });
    });
  };
  swapBack = () => {
    const items = [];
    items.push(this.state.items[1]);
    items.push(this.state.items[2]);
    items.push(this.state.items[0]);
    this.setState({ items }, () => {
      this.setState({ direction: "" });
    });
  };
  animationEnd(evt) {
    evt.stopPropagation();
    if (evt.target.classList.contains(styles.slider)) {
      if (this.state.direction === "forward") {
        this.swapForward();
      }
      if (this.state.direction === "back") {
        this.swapBack();
      }
    }
  }

  render() {
    let transformStyle = "";
    let sliderClass = styles.slider;
    if (this.state.direction === "") {
      transformStyle = "";
      sliderClass = styles.slider;
    }
    if (this.state.direction === "forward") {
      transformStyle = `translateX(${83}%)`;
      sliderClass = styles.forward;
    }
    if (this.state.direction === "back") {
      sliderClass = styles.back;
      transformStyle = `translateX(${-83}%)`;
    }
    return (
      <div
        className={styles.base}
        onTouchStart={evt => this.swipeStart(evt)}
        onTouchMove={evt => this.swipeMove(evt)}
        onTouchEnd={evt => this.swipeEnd(evt)}
      >
        <div
          className={sliderClass}
          style={{ transform: transformStyle }}
          onTransitionEnd={evt => {
            this.animationEnd(evt);
          }}
        >
          {this.state.items.map((child, i) => {
            return (
              <div className={styles.item} key={i}>
                {child}
              </div>
            );
          })}
        </div>
      </div>
    );
  }
}
