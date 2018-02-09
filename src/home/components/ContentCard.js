import React from "react";
import styles from "./ContentCard.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";

export default class ContentCard extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.content}>
          <Image image={this.props.image} color="transparent" />
          <div className={styles.overlay}>
            <div className={styles.header}>{this.props.header}</div>
            <div className={styles.label}>
              {this.props.description}
              <div className={styles.buttonBox}>
                <div
                  className={styles.button}
                  onClick={() => {
                    this.handleClick();
                  }}
                >
                  Read More
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
ContentCard.propTypes = {
  image: PropTypes.string,
  header: PropTypes.string,
  description: PropTypes.string,
  onClick: PropTypes.func
};
ContentCard.defaultProps = {
  travelHeaderText: "Parisian Chic",
  travelText:
    "The new trends thats caching up this summer.loremipsum lorem ipsum is a dummy text which is used to act as a placeholder. loremipsum lorem ipsum is a dummy text which is used to act as a placeholder."
};
