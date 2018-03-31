import React from "react";
import BrandImage from "../../general/components/BrandImage";
import Carousel from "../../general/components/Carousel";
import styles from "./FollowingBrands.css";
import PropTypes from "prop-types";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class FollowingBrands extends React.Component {
  newFollow = () => {
    if (this.props.onFollow) {
      this.props.onFollow();
    }
  };

  handleClick() {
    const urlSuffix = this.props.feedComponentData.webURL.replace(
      TATA_CLIQ_ROOT,
      "$1"
    );
    this.props.history.push(urlSuffix);
  }

  handleBrandImageClick = url => {
    const urlSuffix = url.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
  };

  render() {
    const followWidgetData = this.props.feedComponentData;
    if (!(followWidgetData instanceof Array)) {
      return null;
    }
    return (
      <div className={styles.base}>
        <Carousel header={this.props.feedComponentData.title}>
          {followWidgetData.data &&
            followWidgetData.data.map((datum, i) => {
              return (
                <BrandImage
                  key={i}
                  image={datum.imageURL}
                  value={datum.webURL}
                  fit={datum.type}
                  onClick={this.handleBrandImageClick}
                />
              );
            })}
        </Carousel>
      </div>
    );
  }
}
BrandImage.propTypes = {
  image: PropTypes.string,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      url: PropTypes.string,
      value: PropTypes.string
    })
  )
};
BrandImage.defaultProps = {
  header: "Following Brands"
};
