import React from "react";
import { Image } from "xelpmoc-core";
import styles from "./BrandCardHeader.css";
import Logo from "../../general/components/Logo";
import CoreButton from "../../general/components/Button";
import PropTypes from "prop-types";
export default class BrandCardHeader extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      buttonLabel: this.props.buttonLabel
    };
  }
  handleClick() {
    if (this.state.buttonLabel === "Unfollow") {
      this.setState({ buttonLabel: "Follow" }, () =>
        this.props.onClickFollow()
      );
    } else {
      this.setState({ buttonLabel: "Unfollow" }, () =>
        this.props.onClickUnfollow()
      );
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.container}>
          <div className={styles.imageHolder}>
            <Image image={this.props.image} />
            <div className={styles.textAndLogoContainer}>
              <div className={styles.logo}>
                <Logo image={this.props.logo} />
              </div>
              <div className={styles.text}>{this.props.text}</div>
            </div>
            <div className={styles.buttonHolder}>
              <div className={styles.button}>
                <CoreButton
                  width={100}
                  height={36}
                  backgroundColor={"transparent"}
                  borderRadius={100}
                  borderColor={"#FFFFFF"}
                  label={this.state.buttonLabel}
                  textStyle={{
                    color: "#FFFFFF",
                    fontSize: 14,
                    fontFamily: "semibold"
                  }}
                  onClick={() => this.handleClick()}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
BrandCardHeader.propTypes = {
  image: PropTypes.string,
  text: PropTypes.string,
  logo: PropTypes.string,
  buttonLabel: PropTypes.string,
  onClickFollow: PropTypes.func,
  onClickUnfollow: PropTypes.func
};
BrandCardHeader.defaultProps = {
  image: "",
  text: "",
  logo: "",
  buttonLabel: "Unfollow"
};
