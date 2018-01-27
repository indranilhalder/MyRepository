import React from "react";
import NewBrand from "../../general/components/NewBrand.js";
import Carousel from "../../general/components/Carousel.js";
import PropTypes from "prop-types";
import styles from "./FollowBase.css";

export default class FollowBase extends React.Component {
  render() {
    const feedComponentData = this.props.feedComponentData;
    return (
      <div className={styles.base}>
        <Carousel
          elementWidthMobile={85}
          elementWidthDesktop={33.333}
          header={feedComponentData.title}
        >
          {feedComponentData.data.items &&
            (feedComponentData.data.items.length > 0 &&
              feedComponentData.data.items.map(datum => {
                return (
                  <NewBrand
                    image={datum.imageURL}
                    logo={datum.brandLogo}
                    label={datum.title}
                    follow={datum.isFollowing}
                    key={datum.id}
                  />
                );
              }))}
        </Carousel>
      </div>
    );
  }
}

FollowBase.propTypes = {
  feedComponentData: PropTypes.shape({
    title: PropTypes.string,
    data: PropTypes.shape({
      items: PropTypes.arrayOf(
        PropTypes.shape({
          imageURL: PropTypes.string,
          brandLogo: PropTypes.string,
          title: PropTypes.string,
          id: PropTypes.string,
          isFollowing: PropTypes.bool
        })
      )
    })
  })
};
