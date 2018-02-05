import React from "react";
import styles from "./OfferCard.css";
import Counter from "./TimerCounter.js";
import { Icon } from "xelpmoc-core";

export default class OfferCard extends React.Component {
  handleClick(val) {
    if (this.props.onClick()) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.headingText}>
          {this.props.heading}
          <div className={styles.IconHolder}>
            <span className={styles.timer}>
              {" "}
              <Counter />
            </span>
            <div className={styles.timerHolder}>
              <Icon image={this.props.imageUrl} color="transparent" size={20} />
            </div>
          </div>
        </div>
        <div className={styles.headingDescription}>
          {this.props.description}
          <span className={styles.text}>{this.props.extra}</span>
        </div>
        <div className={styles.description}>{this.props.descriptionData}</div>
        <div className={styles.button} onClick={() => this.handleClick()}>
          {this.props.buttonText}
        </div>
      </div>
    );
  }
}
