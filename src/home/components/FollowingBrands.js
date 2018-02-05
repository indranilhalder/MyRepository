import React from "react";
import BrandImage from "../../general/components/BrandImage";
import Carousel from "../../general/components/Carousel";
import styles from "./FollowingBrands.css";
import PropTypes from "prop-types";

export default class FollowingBrands extends React.Component {
  newFollow = () => {
    if (this.props.onFollow) {
      this.props.onFollow();
    }
  };

  handleClick() {
    this.props.history.push("/productListings");
  }

  render() {
    const followWidgetData = this.props.feedComponentData.data;
    return (
      <div className={styles.base}>
        <Carousel
          header={this.props.feedComponentData.title}
          buttonText="See All"
          seeAll={() => this.handleClick()}
        >
          {followWidgetData.items &&
            followWidgetData.items.map((datum, i) => {
              return (
                <BrandImage
                  key={i}
                  image={datum.imageURL}
                  value={datum.type}
                  fit={datum.type}
                  onClick={() => this.handleClick()}
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
