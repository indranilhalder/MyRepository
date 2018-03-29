import React from "react";
import ContentCard from "./ContentCard";

import styles from "./ContentWidget.css";
const data = [
  {
    image:
      "https://i.pinimg.com/originals/f2/81/28/f2812844b37d5bb2d5608a6d816e586b.jpg"
  },
  {
    image:
      "https://i.pinimg.com/564x/f1/fc/3e/f1fc3e0d33594f02ccb8d2e06d200f25.jpg"
  },
  {
    image:
      "https://i.pinimg.com/564x/47/d0/b0/47d0b0338ac894e59b7518aa54af02d1.jpg"
  }
];

export default class ContentWidget extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      goLeft: false,
      goRight: false,
      finishedLeft: false,
      data: data,
      position: 0
    };
  }
  forward = () => {
    this.goLeft();
  };
  back = () => {
    this.goRight();
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
    if (this.state.touchStart < this.state.touchEnd) {
      this.back();
    } else {
      this.forward();
    }
  }
  goLeft() {
    if (!this.state.goLeft || !this.state.goRight) {
      this.setState({ goLeft: true });
    }
  }
  goRight() {
    if (!this.state.goLeft || !this.state.goRight) {
      this.setState({ goRight: true });
    }
  }

  animationEnd(evt) {
    evt.stopPropagation();
    this.revert();
  }
  componentDidMount = () => {
    this.registerAnimationELement();
  };
  componentWillReceiveProps(props) {
    if (this.state.position < props.position) {
      this.setState({ position: props.position });
      this.goLeft();
    } else if (this.state.position > props.position) {
      this.setState({ position: props.position });
      this.goRight();
    }
  }
  registerAnimationELement = () => {
    const element = document.getElementById("animation-root");
    element.addEventListener("animationend", () => {
      console.log("please dont crash");
      this.revert();
    });
  };

  revert = () => {
    const currentData = this.state.data;
    let data = [];
    if (this.state.goLeft) {
      data[0] = currentData[2];
      data[1] = currentData[0];
      data[2] = currentData[1];
    } else if (this.state.goRight) {
      data[0] = currentData[1];
      data[1] = currentData[2];
      data[2] = currentData[0];
    }

    this.setState({ data, goLeft: false, goRight: false });
  };
  render() {
    let direction = styles.base;
    if (this.state.goLeft) {
      direction = styles.goingLeft;
    } else if (this.state.goRight) {
      direction = styles.goingRight;
    }
    return (
      <div
        className={direction}
        onTouchStart={evt => this.handleSwipeStart(evt)}
        onTouchMove={evt => this.handleSwipeMove(evt)}
        onTouchEnd={evt => this.handleSwipeEnd(evt)}
        onClick={() => {
          this.goRight();
        }}
      >
        {this.state.data.map((val, i) => {
          let className = styles.card;
          if (this.state.goLeft) {
            switch (i) {
              case 0:
                className = styles.leftRight;
                break;
              case 1:
                className = styles.rightCenter;
                break;
              case 2:
                className = styles.centerLeft;
                break;
              default:
                break;
            }
          } else if (this.state.goRight) {
            switch (i) {
              case 0:
                className = styles.leftCenter;
                break;
              case 1:
                className = styles.rightLeft;
                break;
              case 2:
                className = styles.centerRight;
                break;
              default:
                break;
            }
          }

          return (
            <div className={className} id={i === 0 ? "animation-root" : ""}>
              <ContentCard image={val.image} />
            </div>
          );
        })}
      </div>
    );
  }
}
