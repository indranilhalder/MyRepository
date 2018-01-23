import React from "react";
import Carousel from "./Carousel";
import PropTypes from "prop-types";
import styles from "./FollowBase.css";
import NewBrand from "./NewBrand.js";
export default class FollowBase extends React.Component {
  render() {
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
FollowBase.propTypes = {
  header: PropTypes.string,
  followBaseCarouselData: PropTypes.object
};
FollowBase.defaultProps = {
  header: " More brand from Tata Cliq"
};
