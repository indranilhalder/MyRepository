import React from "react";
import styles from "./TravelInspiration.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import travelBackImage from "./img/background.jpg";
export default class TravelInspiration extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.travelHolder}>
          <Image image={this.props.travelImage} color="transparent" />
          <div className={styles.overlay}>
            <div className={styles.header}>{this.props.travelHeaderText}</div>
            <div className={styles.label}>
              {this.props.travelText}
              <div className={styles.buttonBox}>
                <div
                  className={styles.button}
                  onClick={() => {
                    this.handleClick();
                  }}
                >
                  {this.props.readMore}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
TravelInspiration.propTypes = {
  travelImage: PropTypes.string,
  travelHeaderText: PropTypes.string,
  travelText: PropTypes.string,
  readMore: PropTypes.string,
  onClick: PropTypes.func
};
TravelInspiration.defaultProps = {
  travelImage: travelBackImage,
  readMore: "Read More",
  travelHeaderText: "Parisian Chic",
  travelText:
    "The new trends thats caching up this summer.loremipsum lorem ipsum is a dummy text which is used to act as a placeholder. loremipsum lorem ipsum is a dummy text which is used to act as a placeholder."
};
