import React from "react";
import PropTypes from "prop-types";
import styles from "./DumbCarousel.css";
import VisibilityChild from "../../home/components/VisibilityChild.js";

export default class DumbCarousel extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        {this.props.headerComponent && this.props.headerComponent}
        <div className={styles.slider}>
          {this.props.children &&
            this.props.children.map((child, i) => {
              return (
                <div
                  className={styles.element}
                  style={{
                    width:
                      this.props.elementWidth === "auto"
                        ? "auto"
                        : `${this.props.elementWidth}%`
                  }}
                >
                  <VisibilityChild>{child}</VisibilityChild>
                </div>
              );
            })}
        </div>
      </div>
    );
  }
}
DumbCarousel.propTypes = {
  elementWidth: PropTypes.oneOfType([PropTypes.number, PropTypes.string])
};
