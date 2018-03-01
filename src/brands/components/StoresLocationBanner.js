import React from "react";
import styles from "./StoresLocationBanner.css";
import PropTypes from "prop-types";
import StoresLocationCard from "./StoresLocationCard";
import BannerMobile from "../../general/components/BannerMobile";

export default class StoresLocationBanner extends React.Component {
  render() {
    let data = this.props.brandLocation;
    return (
      <div className={styles.base}>
        <BannerMobile>
          {data.map((datum, i) => {
            return (
              <StoresLocationCard
                headingText={datum.headingText}
                label={datum.label}
                image={datum.image}
                key={i}
              />
            );
          })}
        </BannerMobile>
      </div>
    );
  }
}

StoresLocationBanner.propTypes = {
  headingText: PropTypes.string,
  label: PropTypes.string,
  image: PropTypes.string,
  heading: PropTypes.string,
  StoresLocationBanner: PropTypes.arrayOf(
    PropTypes.shape({
      headingText: PropTypes.string,
      label: PropTypes.string,
      image: PropTypes.string,
      heading: PropTypes.string
    })
  )
};
