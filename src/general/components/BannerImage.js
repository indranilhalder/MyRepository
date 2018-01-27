import React from "react";
import styles from "./BannerImage.css";
import Button from "./Button";
import Logo from "./Logo";
import MediaQuery from "react-responsive";
import PropTypes from "prop-types";
export default class Banner extends React.Component {
  render() {
    return (
      <React.Fragment>
        <MediaQuery query="(min-device-width: 1025px)">
          <div className={styles.base}>
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
          <div className={styles.base}>
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
