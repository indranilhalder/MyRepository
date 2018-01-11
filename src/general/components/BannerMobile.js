import React from "react";
import styles from "./BannerMobile.css";
export default class BannerMobile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      visibleItems: this.props.children
        ? React.Children.map(this.props.children, (child, i) => {
            if (i < 3) {
              return child;
            }
          })
        : null,
      numberOfItems: React.Children.count(this.props.children),
      position: 2
    };
    this.items = this.props.children
      ? React.Children.map(this.props.children, (child, i) => {
          return child;
        })
      : null;
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
    const visibleItems = [];

    const position = (this.state.position - 1) % this.state.numberOfItems;
    let normalPosition = position;
    if (normalPosition < 0) {
      normalPosition = this.state.numberOfItems + normalPosition;
    }
    this.setState({ position: normalPosition }, () => {
      visibleItems.push(this.state.visibleItems[2]);
      visibleItems.push(this.state.visibleItems[0]);
      visibleItems.push(this.items[this.state.position]);
      this.setState({ visibleItems }, () => {
        this.setState({ direction: "" });
      });
      console.log(this.state.position);
    });
  };

  swapBack = () => {
    const visibleItems = [];
    const position = (this.state.position + 1) % this.state.numberOfItems;
    this.setState({ position }, () => {
      visibleItems.push(this.state.visibleItems[1]);
      visibleItems.push(this.state.visibleItems[2]);
      // if (this.items[this.state.position] && this.state.position > 2) {
      //   visibleItems.push(this.items[this.state.position]);
      // } else {
      //   visibleItems.push(this.state.visibleItems[0]);
      // }
      visibleItems.push(this.items[this.state.position]);
      this.setState({ visibleItems }, () => {
        this.setState({ direction: "" });
      });
      console.log(this.state.position);
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
          {this.state.visibleItems.map((child, i) => {
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
