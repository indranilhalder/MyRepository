import React, { Component } from "react";

import styles from "./SocialButtons.css";
import MediaQuery from "react-responsive";
import { CircleButton } from "xelpmoc-core";
import PropTypes from "prop-types";
class SocialButtons extends Component {
  render() {
    return (
      <div>
        <MediaQuery query="(min-device-width: 1024px)">
          <div className={styles.textHolder}>
            <p className={styles.title}>{this.props.titleText}</p>
          </div>
          <div className={styles.base}>
            <div className={styles.holder}>
              <CircleButton color={"rgba(0,0,0,0)"} size={50} />
            </div>

            <div className={styles.centerHolder}>
              <CircleButton color={"rgba(0,0,0,0)"} size={50} />
            </div>

            <div className={styles.holder}>
              <CircleButton color={"rgba(0,0,0,0)"} size={50} />
            </div>
          </div>
        </MediaQuery>
        <MediaQuery query="(max-device-width: 1023px)">
          <div className={styles.textHolder}>
            <p className={styles.title}>{this.props.titleText}</p>
          </div>
          <div className={styles.base}>
            <div className={styles.holder}>
              <CircleButton
                color={"rgba(0,0,0,0)"}
                borderColor={"#fff"}
                size={50}
              />
            </div>
            <div className={styles.centerHolder}>
              <CircleButton
                color={"rgba(0,0,0,0)"}
                borderColor={"#fff"}
                size={50}
              />
            </div>

            <div className={styles.holder}>
              <CircleButton
                color={"rgba(0,0,0,0)"}
                borderColor={"#fff"}
                size={50}
              />
            </div>
          </div>
        </MediaQuery>
      </div>
    );
  }
}

export default SocialButtons;
SocialButtons.propTypes = {
  titleText: PropTypes.string
};
SocialButtons.defaultProps = {
  titleText: " Sign in with your social account"
};
