import React from "react";
import Image from "../../xelpmoc-core/Image";
import styles from "./StoryWidget.css";

export default class StoryWidget extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      position: 0,
      length: this.props.children ? this.props.children.length : 0,
      totalTimer: 0,
      request: 0
    };
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.children !== nextProps.children) {
      this.setState({ length: nextProps.children.length });
      this.runTimer();
    }
  }
  runTimer = () => {
    let totalTimer = this.state.totalTimer + 1;

    if (totalTimer > 240) {
      totalTimer = 0;
      this.forward();
    }
    this.setState({ totalTimer }, () => {
      requestAnimationFrame(this.runTimer);
    });
  };

  forward = () => {
    if (this.state.position < this.state.length - 1) {
      const position = this.state.position + 1;
      this.setState({ position });
      this.setState({ totalTimer: 0 });
    } else {
      if (this.props.onEnd) {
        this.props.onEnd();
      }
    }
  };
  back = () => {
    if (this.state.position > 0) {
      const position = this.state.position - 1;
      this.setState({ position });
      this.setState({ totalTimer: 0 });
    }
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
  renderLoader() {
    if (this.props.feedComponentData.items.length > 0) {
      return null;
    } else {
      return <div className={styles.loader} />;
    }
  }

  render() {
    const translateAmount = this.state.position * -100;

    if (this.props.feedComponentData && this.props.feedComponentData.items) {
      return (
        <div className={styles.base}>
          <div className={styles.timerHolder}>
            <div className={styles.timer}>
              <div
                className={styles.timerProgress}
                style={{ transform: `scaleX(${this.state.totalTimer / 240})` }}
              />
            </div>
          </div>
          <div className={styles.brandSection}>
            <div className={styles.brandImage}>
              <Image image={this.props.image} />
            </div>
            <div className={styles.brandName}>{this.props.brandName}</div>
            <div className={styles.brandProducts}>{this.props.title}</div>
            <div
              className={styles.cancel}
              onClick={() => this.props.closeModal()}
            />
          </div>

          {this.props.feedComponentData.items.length > 0 && (
            <div
              className={styles.gallery}
              style={{ transform: `translateX(${translateAmount}%)` }}
              onTouchStart={evt => this.handleSwipeStart(evt)}
              onTouchMove={evt => this.handleSwipeMove(evt)}
              onTouchEnd={evt => this.handleSwipeEnd(evt)}
            >
              {this.props.children}
            </div>
          )}
          {this.renderLoader()}
        </div>
      );
    } else {
      return <div className={styles.loader} />;
    }
  }
}
