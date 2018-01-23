import React from "react";
import NewBrand from "../../general/components/NewBrand.js";
import Carousel from "../../general/components/Carousel.js";
import PropTypes from "prop-types";
import styles from "./FollowBase.css";

export default class FollowBase extends React.Component {
  render() {
    console.log(this.props);
    return (
      <div className={styles.base}>
        <Carousel elementWidthMobile={80} elementWidthDesktop={33.333}>
          {this.props.data.length > 0 &&
            this.props.data.map((datum, i) => {
              return (
                <NewBrand
                  image={datum.image}
                  logo={datum.logo}
                  label={datum.label}
                  key={i}
                />
              );
            })}
        </Carousel>
      </div>
    );
  }
}
