import React from "react";
import Carousel from "../../general/components/Carousel";
import CircleProductImage from "../../general/components/CircleProductImage";
import PropTypes, { instanceOf } from "prop-types";
import styles from "./DiscoverMoreCarousel.css";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import { withRouter } from "react-router";

class DiscoverMoreCarousel extends React.Component {
  handleClick = webUrl => {
    const urlSuffix = webUrl.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
    if (this.props.setClickedElementId) {
      this.props.setClickedElementId();
    }
  };
  render() {
    const discoverMoreCarouselData = this.props.feedComponentData;
    return (
      <div className={styles.base}>
        <Carousel header={discoverMoreCarouselData.title}>
          {discoverMoreCarouselData.data &&
            discoverMoreCarouselData.data.map &&
            discoverMoreCarouselData.data.map((datum, i) => {
              return (
                <CircleProductImage
                  image={datum.imageURL}
                  label={datum.title}
                  key={i}
                  value={datum.webURL}
                  onClick={this.handleClick}
                />
              );
            })}
        </Carousel>
      </div>
    );
  }
}

export default withRouter(DiscoverMoreCarousel);
DiscoverMoreCarousel.propTypes = {
  header: PropTypes.string,
  discoverMoreCarouselData: PropTypes.object
};
DiscoverMoreCarousel.defaultProps = {
  header: "Discover more from Tata Cliq"
};
