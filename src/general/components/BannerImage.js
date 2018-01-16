import React from "react";
import { Image } from "xelpmoc-core";
import styles from "./BannerImage.css";
import Button from "./Button";

import MediaQuery from "react-responsive";
import PropTypes from "prop-types";
export default class Banner extends React.Component {
  render() {
    return (
      <div>
        <MediaQuery query="(min-device-width: 1025px)">
          <div className={styles.base}>
            <div className={styles.imageHolder}>
              <Image image={this.props.backgroundImage} />
            </div>
            <div className={styles.overlay}>
              <div className={styles.logoAndText}>
                <div className={styles.logo}>
                  <Image color={"transparent"} />
                </div>
                <div className={styles.title}>{this.props.title}</div>
                <div className={styles.button}>
                  <Button
                    height={50}
                    width={200}
                    label={this.props.buttonLabel}
                    type={"primary"}
                    borderRadius={25}
                  />
                </div>
              </div>
            </div>
          </div>
        </MediaQuery>
        <MediaQuery query="(max-device-width: 1024px)">
          <div className={styles.base}>
            <div className={styles.imageHolder}>
              <Image image={this.props.backgroundImage} />
            </div>
            <div className={styles.overlay}>
              <div className={styles.logoAndText}>
                <div className={styles.logo}>
                  <Image color={"transparent"} />
                </div>
                <div className={styles.title}>{this.props.title}</div>
              </div>
            </div>
          </div>
        </MediaQuery>
      </div>
    );
  }
}
Banner.propTypes = {
  backgroundImage: PropTypes.string,
  title: PropTypes.string,
  logo: PropTypes.string,
  buttonLabel: PropTypes.string
};
Banner.defaultProps = {
  backgroundImage: "",
  title: "",
  logo: ""
};
