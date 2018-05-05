import React from "react";
import PropTypes from "prop-types";
import styles from "./ProfileImage.css";
export const SIZE_1 = 1;
export const SIZE_2 = 2;
export const SIZE_3 = 3;
export const SIZE_4 = 4;
export default class ProfileImage extends React.Component {
  constructor(props) {
    super(props);
    this.styles = this.props.styles ? this.props.styles : styles;
  }
  render() {
    const initials = this.props.initials;
    let className = this.styles.base;
    if (this.props.size === SIZE_1) {
      className = this.styles.size1;
    }

    if (this.props.size === SIZE_2) {
      className = this.styles.size2;
    }

    if (this.props.size === SIZE_3) {
      className = this.styles.size3;
    }

    if (this.props.size === SIZE_4) {
      className = this.styles.size4;
    }
    const profile = this.props.image ? (
      <div
        className={styles.image}
        style={{
          backgroundColor: "#ebeced",
          backgroundImage: `url(${this.props.image})`,
          backgroundSize: "cover"
        }}
      />
    ) : (
      <div className={this.styles.initials}>{initials}</div>
    );
    return (
      <div
        className={className}
        style={{
          borderRadius: this.props.border.radius,
          borderWidth: this.props.border.width,
          borderColor: this.props.border.color,
          borderStyle: "solid"
        }}
      >
        {profile}
      </div>
    );
  }
}

ProfileImage.propTypes = {
  initials: PropTypes.string.isRequired,
  image: PropTypes.string,
  size: PropTypes.oneOf([SIZE_1, SIZE_2, SIZE_3, SIZE_4]),
  border: PropTypes.shape({
    color: PropTypes.string,
    radius: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    width: PropTypes.number
  })
};
ProfileImage.defaultProps = {
  initials: "NA",
  size: 2,
  border: {
    color: "transparent",
    width: 0,
    radius: "50%"
  }
};
