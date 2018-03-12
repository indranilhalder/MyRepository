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
      buttonLabel: props.feedComponentData.buttonLabel
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
    let { feedComponentData } = this.props;
    return (
      <div className={styles.base}>
        <div className={styles.container}>
          <div className={styles.imageHolder}>
            <Image
              image={feedComponentData && feedComponentData.items[0].imageURL}
            />
            <div className={styles.textAndLogoContainer}>
              <div className={styles.logo}>
                <Logo
                  image={
                    feedComponentData && feedComponentData.items[0].brandLogo
                  }
                />
              </div>
              <div className={styles.text}>
                {feedComponentData && feedComponentData.items[0].title}
              </div>
            </div>
            <div className={styles.buttonHolder}>
              {/* Need to be uncommented when the follow and unFollow api will work */}

              {/* <div className={styles.button}>
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
              </div> */}
            </div>
          </div>
        </div>
      </div>
    );
  }
}
BrandCardHeader.propTypes = {
  backgroundImageURL: PropTypes.string,
  description: PropTypes.string,
  logoImage: PropTypes.string,
  buttonLabel: PropTypes.string,
  onClick: PropTypes.func
};
BrandCardHeader.defaultProps = {
  image: "",
  text: "",
  logo: "",
  buttonLabel: "Unfollow"
};
