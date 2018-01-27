import React from "react";
import styles from "./TravelInspiration.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import image from "./img/travelb.jpg";
import travelBackImage from "./img/backgroungt.jpg";
export default class TravelInspiration extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.travelMain}>
          <Image image={this.props.travelBackImage} color="transparent" />
          <div className={styles.travelHolder}>
            <Image image={this.props.travelImage} color="transparent" />
            <div className={styles.overlay}>
              <div className={styles.header}>{this.props.travelHeaderText}</div>
              <div className={styles.label}>{this.props.travelText}</div>
              <div className={styles.buttonBox}>
                <div className={styles.button}>{this.props.readMore}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
TravelInspiration.propTypes = {
  traveImage: PropTypes.string,
  travelBackImage: PropTypes.string,
  travelHeaderText: PropTypes.string,
  travelText: PropTypes.string,
  readMore: PropTypes.string
};
TravelInspiration.defaultProps = {
  travelImage: image,
  travelBackImage: travelBackImage,
  readMore: "Read More",
  travelHeaderText: "Parisian Chic",
  travelText:
    "The new trends thats caching up this summer.loremipsum lorem ipsum is a dummy text which is used to act as a placeholder. loremipsum lorem ipsum is a dummy text which is used to act as a placeholder."
};
