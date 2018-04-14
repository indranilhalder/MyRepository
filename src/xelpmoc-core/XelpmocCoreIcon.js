import React from "react";
import PropTypes from "prop-types";
import styles from "./XelpmocCoreIcon.css";

export default class XelpmocCoreIcon extends React.Component {
  render() {
    return (
      <div
        className={styles.base}
        style={{ width: this.props.size, height: this.props.size }}
      >
        <div
          className={styles.image}
          style={{
            backgroundImage: `url(${this.props.image})`,
            backgroundSize: `${this.props.backgroundSize}`
          }}
        />
      </div>
    );
  }
}

XelpmocCoreIcon.propTypes = {
  size: PropTypes.number.isRequired,
  backgroundSize: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  image: PropTypes.string.isRequired
};

XelpmocCoreIcon.defaultProps = {
  size: 30,
  backgroundSize: "contain"
};
