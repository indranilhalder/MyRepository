import React from "react";
import styles from "./BannerDesktop.css";
export default class BannerDesktop extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      position: 0
    };
  }
  changePosition(val) {
    this.setState({ position: val });
  }
  autoRun = () => {
    setTimeout(() => {
      const i = (this.state.position + 1) % 3;
      this.changePosition(i);
      this.autoRun();
    }, this.props.interval);
  };
  componentDidMount() {
    if (this.props.interval) {
      this.autoRun();
    }
  }
  render() {
    const navValues = [0, 1, 2];
    return (
      <div className={styles.base}>
        {this.props.children.map((child, i) => {
          if (this.state.position === i) {
            return (
              <div className={styles.item} key={i}>
                {child}
              </div>
            );
          }
        })}
        <div className={styles.nav}>
          {navValues.map(val => {
            let navClass = styles.navButton;
            if (val === this.state.position) {
              navClass = styles.active;
            }
            return (
              <div
                className={navClass}
                key={val}
                onClick={() => {
                  this.changePosition(val);
                }}
              />
            );
          })}
        </div>
      </div>
    );
  }
}
