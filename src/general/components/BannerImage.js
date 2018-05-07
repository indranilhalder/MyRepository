import React from "react";
import styles from "./BannerImage.css";
import Button from "./Button";
import Logo from "./Logo";
import MediaQuery from "react-responsive";
import PropTypes from "prop-types";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
export default class Banner extends React.Component {
  onClick = () => {
    const urlSuffix = this.props.url.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
  };
  render() {
    return (
      <React.Fragment>
        <MediaQuery query="(min-device-width: 1025px)">
          <div className={styles.base} onClick={this.onClick}>
            <div
              className={styles.imageHolder}
              style={{ backgroundImage: `url(${this.props.image})` }}
            />
            <div className={styles.overlay}>
              <div className={styles.logoAndText}>
                <div className={styles.logo}>
                  <Logo image={this.props.logo} />
                </div>
                <div className={styles.title}>{this.props.title}</div>
                <div className={styles.button}>
                  <Button
                    height={50}
                    label={this.props.buttonLabel}
                    type={"primary"}
                  />
                </div>
              </div>
            </div>
          </div>
        </MediaQuery>
        <MediaQuery query="(max-device-width: 1024px)">
          <div className={styles.base} onClick={this.onClick}>
            <div
              className={styles.imageHolder}
              style={{ backgroundImage: `url(${this.props.image})` }}
            />
            <div className={styles.overlay}>
              <div className={styles.logoAndText}>
                <div className={styles.logo}>
                  <Logo
                    image={this.props.logo}
                    imageHeight="auto"
                    imageWidth="100%"
                  />
                </div>
                {this.props.title && (
                  <div className={styles.title}>{this.props.title}</div>
                )}
                {this.props.subTitle && (
                  <div className={styles.subTitle}>{this.props.subTitle}</div>
                )}
              </div>
            </div>
          </div>
        </MediaQuery>
      </React.Fragment>
    );
  }
}
Banner.propTypes = {
  image: PropTypes.string,
  title: PropTypes.string,
  logo: PropTypes.string,
  buttonLabel: PropTypes.string
};
Banner.defaultProps = {
  image: "",
  title: "",
  logo: "",
  buttonLabel: "Shop Now"
};
