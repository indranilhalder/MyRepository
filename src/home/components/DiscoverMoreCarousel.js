import React from "react";
import Carousel from "../../general/components/Carousel";
import CircleProductImage from "../../general/components/CircleProductImage";
import PropTypes from "prop-types";
import styles from "./DiscoverMoreCarousel.css";

export default class DiscoverMoreCarousel extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <Carousel header={this.props.header}>
          {this.props.data &&
            this.props.data.map((datum, i) => {
              return (
                <CircleProductImage
                  image={datum.image}
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
DiscoverMoreCarousel.propTypes = {
  header: PropTypes.string,
  data: PropTypes.arrayOf(
    PropTypes.shape({ image: PropTypes.string, label: PropTypes.string })
  )
};
DiscoverMoreCarousel.defaultProps = {
  header: "Discover more from Tata Cliq"
};
