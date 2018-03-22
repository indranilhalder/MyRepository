import React from "react";
import styles from "./FollowedBrand.css";
import Button from "../../general/components/Button";
import PropTypes from "prop-types";
export default class FollowedBrand extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.headerHolder}>
          <div className={styles.heading}>{this.props.header}</div>
          <div className={styles.subHeading}>{this.props.subHeader}</div>
        </div>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <Button
              borderRadius={14}
              label={this.props.btnText}
              backgroundColor={this.props.backgroundColor}
              onClick={() => this.handleClick()}
              width={89}
              height={27}
              borderColor={this.props.borderColor}
              textStyle={{
                color: this.props.color,
                fontSize: 14,
                fontFamily: this.props.fontFamily
              }}
            />
          </div>
        </div>
      </div>
    );
  }
}
FollowedBrand.propTypes = {
  onClick: PropTypes.func,
  btnText: PropTypes.string,
  backgroundColor: PropTypes.string,
  fontFamily: PropTypes.string,
  borderColor: PropTypes.string,
  header: PropTypes.string,
  subHeader: PropTypes.string,
  color: PropTypes.string
};
FollowedBrand.defaultProps = {
  fontFamily: "semibold",
  color: "#000",
  btnText: "Edit",
  backgroundColor: "#ececec",
  borderColor: "#000000"
};
