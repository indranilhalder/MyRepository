import React from "react";
import styles from "./Banner.css";
export default class Banner extends React.Component {
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
    this.setState({ touchStart: evt.touches[0].clientX });
  }
  swipeMove(evt) {
    this.setState({ touchEnd: evt.touches[0].clientX });
  }
  swipeEnd() {
    if (this.state.touchStart < this.state.touchEnd) {
      console.log("forward");
      this.slideForward();
    } else {
      this.slideBack();
    }
  }
  slideForward = () => {
    const items = [];
    items.push(this.state.items[2]);
    items.push(this.state.items[0]);
    items.push(this.state.items[1]);
    this.setState({ items, direction: "forward" });
  };
  slideBack = () => {
    const items = [];
    items.push(this.state.items[1]);
    items.push(this.state.items[2]);
    items.push(this.state.items[0]);
    this.setState({ items, direction: "back" });
  };
  animationEnd() {
    this.setState({ direction: "" });
  }

  render() {
    console.log(this.state);
    return (
      <div
        className={styles.base}
        onTouchStart={evt => this.swipeStart(evt)}
        onTouchMove={evt => this.swipeMove(evt)}
        onTouchEnd={evt => this.swipeEnd(evt)}
      >
        <div
          className={styles.slider}
          onAnimationEnd={() => {
            this.animationEnd();
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
