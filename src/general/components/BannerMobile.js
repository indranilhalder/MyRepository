import React from "react";
import PropTypes from "prop-types";
import styles from "./BannerMobile.css";
export default class BannerMobile extends React.Component {
  constructor(props) {
    super(props);
    const itemArray = React.Children.map(this.props.children, (child, i) => {
      return child;
    });
    const lastItem = itemArray.pop();
    itemArray.unshift(lastItem);
    this.items = itemArray;
    this.state = {
      children: this.props.children,
      visibleItems: this.props.children
        ? itemArray.filter((child, i) => {
            return i < 3;
          })
        : null,
      numberOfItems: React.Children.count(this.props.children),
      position: 0,
      absolutePosition: 0
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
  handleSwipe(val) {
    if (this.props.onSwipe) {
      this.props.onSwipe(val);
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.children) {
      const itemArray = React.Children.map(nextProps.children, (child, i) => {
        return child;
      });
      const lastItem = itemArray.pop();
      itemArray.unshift(lastItem);
      this.items = itemArray;
      this.setState({
        children: nextProps.children,
        visibleItems: itemArray.filter((child, i) => {
          return i < 3;
        })
      });
    }
  }
  slideForward = () => {
    const visibleItems = [];
    visibleItems.push(this.state.visibleItems[0]);
    visibleItems.push(this.state.visibleItems[1]);
    visibleItems.push(
      this.items[
        this.state.position - 1 >= 0
          ? (this.state.position - 1) % this.state.numberOfItems
          : this.state.numberOfItems - 1
      ]
    );
    this.setState({ visibleItems, direction: "forward" });
  };
  slideBack = () => {
    const visibleItems = [];
    visibleItems.push(
      this.items[(this.state.position + 3) % this.state.numberOfItems]
    );
    visibleItems.push(this.state.visibleItems[1]);
    visibleItems.push(this.state.visibleItems[2]);

    this.setState({ visibleItems, direction: "back" });
  };
  swapForward = () => {
    const visibleItems = [];
    let absolutePosition =
      this.state.absolutePosition - 1 >= 0
        ? this.state.absolutePosition - 1
        : this.state.numberOfItems - 1;
    const position = absolutePosition % this.state.numberOfItems;

    this.setState({ position, absolutePosition }, () => {
      visibleItems.push(this.items[this.state.position]);
      visibleItems.push(
        this.items[
          this.state.position + 1 < this.state.numberOfItems
            ? this.state.position + 1
            : 0
        ]
      );
      visibleItems.push(
        this.items[
          this.state.position + 2 < this.state.numberOfItems
            ? this.state.position + 2
            : this.state.position + 2 - this.state.numberOfItems
        ]
      );
      this.setState({ visibleItems }, () => {
        this.setState({ direction: "" });
      });
      this.handleSwipe(absolutePosition);
    });
  };

  swapBack = () => {
    const visibleItems = [];
    const position =
      (this.state.absolutePosition + 1) % this.state.numberOfItems;
    let absolutePosition = this.state.absolutePosition + 1;

    this.setState({ position, absolutePosition }, () => {
      visibleItems.push(this.items[this.state.position]);
      visibleItems.push(
        this.items[
          this.state.position + 1 < this.state.numberOfItems
            ? this.state.position + 1
            : 0
        ]
      );
      visibleItems.push(
        this.items[
          this.state.position + 2 < this.state.numberOfItems
            ? this.state.position + 2
            : this.state.position + 2 - this.state.numberOfItems
        ]
      );
      this.setState({ visibleItems }, () => {
        this.setState({ direction: "" });
      });
    });
    this.handleSwipe(absolutePosition);
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
        style={{ height: this.props.bannerHeight }}
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
BannerMobile.propTypes = {
  bannerHeight: PropTypes.string,
  onSwipe: PropTypes.func
};
BannerMobile.defaultProps = {
  bannerHeight: "66.33vw"
};
