import React from "react";
import NewBrand from "../../general/components/NewBrand.js";
import Carousel from "../../general/components/Carousel.js";
import PropTypes from "prop-types";
import styles from "./FollowBase.css";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class FollowBase extends React.Component {
  handleClick = webUrl => {
    const urlSuffix = webUrl.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
  };
  render() {
    let { feedComponentData, ...rest } = this.props;
    feedComponentData = feedComponentData.data;

    return (
      <div className={styles.base}>
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
                    onClick={this.handleClick}
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
