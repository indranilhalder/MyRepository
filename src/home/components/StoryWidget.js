import React from "react";
import StoryProduct from "./StoryProduct";
import Image from "../../xelpmoc-core/Image";

import styles from "./StoryWidget.css";

export default class StoryWidget extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      position: 0,
      length: this.props.children.length,
      totalTimer: 0
    };
  }
  componentDidMount = () => {
    this.registerAnimationElement();
    this.runTimer();
  };
  runTimer = () => {
    const totalTimer = this.state.totalTimer + 1;

    // if (translate > 662 - HEIGHT_OF_CONTAINER) {
    //   translate = 0;
    // }
    this.setState({ totalTimer }, () => {
      requestAnimationFrame(this.runTimer);
    });
  };

  forward = () => {
    if (this.state.position < this.state.length) {
      const position = this.state.position + 1;
      this.setState({ position });
    }
  };
  back = () => {
    if (this.state.position > 0) {
      const position = this.state.position - 1;
      this.setState({ position });
    }
  };
  registerAnimationElement = () => {
    const element = document.getElementById("storyWidget");
    element.addEventListener("animationend", () => {
      this.revert();
    });
  };
  handleSwipeStart(evt) {
    evt.stopPropagation();
    this.setState({ touchStart: evt.touches[0].clientX });
  }
  handleSwipeMove(evt) {
    evt.stopPropagation();
    this.setState({ touchEnd: evt.touches[0].clientX });
  }
  handleSwipeEnd() {
    if (this.state.touchEnd) {
      if (this.state.touchStart - this.state.touchEnd < -30) {
        this.back();
      } else if (this.state.touchStart - this.state.touchEnd > 30) {
        this.forward();
      }
    }
  }
  // style={{ transform: `translateY(${-this.state.totalTranslate}px)`
  render() {
    // console.log(this.state.position);
    const translateAmount = this.state.position * -100;
    // console.log(translateAmount);
    return (
      <div className={styles.base}>
        <div className={styles.timerHolder}>
          <div className={styles.timer}>
            <div className={styles.timerProgress} />
          </div>
        </div>
        <div className={styles.brandSection}>
          <div className={styles.brandImage}>
            <Image image="http://img.tatacliq.com/images/i3/252Wx374H/MP000000002333309_252Wx374H_20180127024115.jpeg" />
          </div>
          <div className={styles.brandName}>Adidas</div>
          <div className={styles.brandProducts}>18 New Products</div>
        </div>
        <div
          className={styles.gallery}
          style={{ transform: `translateX(${translateAmount}%)` }}
          id={"storyWidget"}
          onTouchStart={evt => this.handleSwipeStart(evt)}
          onTouchMove={evt => this.handleSwipeMove(evt)}
          onTouchEnd={evt => this.handleSwipeEnd(evt)}
        >
          {this.props.children}
        </div>
      </div>
    );
  }
}
