import React from "react";
import ContentCard from "./ContentCard";

import styles from "./ContentWidget.css";
const allData = [
  {
    image:
      "https://i.pinimg.com/originals/f2/81/28/f2812844b37d5bb2d5608a6d816e586b.jpg",
    header: "One",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
  },
  {
    image:
      "https://i.pinimg.com/564x/f1/fc/3e/f1fc3e0d33594f02ccb8d2e06d200f25.jpg",
    header: "Two",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
  },
  {
    image:
      "https://i.pinimg.com/564x/47/d0/b0/47d0b0338ac894e59b7518aa54af02d1.jpg",
    header: "Three",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
  },
  {
    image:
      "https://i.pinimg.com/564x/bd/25/6e/bd256e562cdacc9e24d54dcfa6c420ed.jpg",
    header: "Four",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
  },
  {
    image:
      "https://i.pinimg.com/564x/87/42/44/874244cf20a10178a41c485c5fcfbc65.jpg",
    header: "Five",
    description:
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
  }
];

export default class ContentWidget extends React.Component {
  constructor(props) {
    console.log("remounted");
    super(props);
    this.state = {
      goLeft: false,
      goRight: false,
      data: [allData[4], allData[1], allData[0]],
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
      console.log("go left");
      const position = this.state.position + 1;
      const currentData = this.state.data;
      this.setState({ goLeft: true, position }, () => {
        let data = [];
        data[0] = allData[(1 + this.state.position) % 5];
        data[1] = currentData[1];
        data[2] = currentData[0];
        this.setState({ data });
      });
    }
  }
  goRight() {
    if (!this.state.goLeft || !this.state.goRight) {
      console.log("go right");
      let position = this.state.position - 1;
      if (position < 0) {
        position = 5 + position;
      }
      console.log(position);
      const currentData = this.state.data;
      this.setState({ goRight: true, position }, () => {
        let data = [];
        data[0] = allData[position % 5];
        data[1] = currentData[1];
        data[2] = currentData[0];
        this.setState({ data });
      });
    }
  }

  animationEnd(evt) {
    evt.stopPropagation();
    this.revert();
  }
  componentDidMount = () => {
    this.registerAnimationELement();
    allData.forEach(val => {
      console.log(val.image);
      new Image().src = val.image;
    });
    // (new Image()).src = "http://www.example.com/myimage.jpg"
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
              <ContentCard
                image={val.image}
                header={val.header}
                description={val.description}
              />
            </div>
          );
        })}
      </div>
    );
  }
}
