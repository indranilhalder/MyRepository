import React from "react";
import NewBrand from "../../general/components/NewBrand.js";
import Carousel from "../../general/components/Carousel.js";
import PropTypes from "prop-types";
import styles from "./FollowBase.css";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class FollowBase extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      items: this.props.feedComponentData.items
        ? this.props.feedComponentData.items
        : null
    };
  }
  handleClick = data => {
    this.props.showStory({
      positionInFeed: this.props.positionInFeed,
      ...data
    });
  };

  render() {
    let { feedComponentData, ...rest } = this.props;
    feedComponentData = feedComponentData.data;

    return (
      <div
        className={
          this.props.positionInFeed === 1 ? styles.firstItemBase : styles.base
        }
      >
        <div className={styles.header}>
          {this.props.feedComponentData.title
            ? this.props.feedComponentData.title
            : "Fresh from Brands"}
        </div>
        <Carousel elementWidthMobile={85} elementWidthDesktop={33.333}>
          {feedComponentData &&
            (feedComponentData.length > 0 &&
              feedComponentData.map(datum => {
                return (
                  <NewBrand
                    image={datum.imageURL}
                    logo={datum.brandLogo}
                    label={datum.title}
                    follow={datum.isFollowing}
                    key={datum.id}
                    webUrl={datum.webURL}
                    brandId={datum.id}
                    isFollowing={datum.isFollowing}
                    onClick={() =>
                      this.handleClick({
                        itemIds: datum.itemIds,
                        image: datum.imageURL,
                        title: datum.title,
                        brandName: datum.brandName,
                        history: this.props.history
                      })
                    }
                    {...rest}
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
