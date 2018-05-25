import React from "react";
import BrandImage from "../../general/components/BrandImage";
import styles from "./FollowingBrands.css";
import PropTypes from "prop-types";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import plusButton from "../../general/components/img/plusbutton.svg";
import { DEFAULT_BRANDS_LANDING_PAGE } from "../../lib/constants";
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
    if (this.props.setClickedElementId) {
      this.props.setClickedElementId();
    }
  }

  handleBrandImageClick = url => {
    const urlSuffix = url.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
    if (this.props.setClickedElementId) {
      this.props.setClickedElementId();
    }
  };

  render() {
    const followWidgetData = this.props.feedComponentData;

    return (
      <div>
        {followWidgetData &&
          followWidgetData.data &&
          followWidgetData.data.filter(val => {
            return val.isFollowing === "true";
          }).length > 1 && (
            <div className={styles.base}>
              <div className={styles.header}>
                {this.props.feedComponentData.title
                  ? this.props.feedComponentData.title
                  : "Following Brands"}
              </div>
              <div className={styles.dummyCaurousel}>
                {followWidgetData.data &&
                  followWidgetData.data
                    .filter(val => {
                      return val.isFollowing === "true";
                    })
                    .map((datum, i) => {
                      return (
                        <div className={styles.element}>
                          <BrandImage
                            key={i}
                            image={datum.imageURL}
                            value={datum.webURL}
                            fit={datum.fit}
                            isFollowing={datum.isFollowing}
                            onClick={this.handleBrandImageClick}
                          />
                        </div>
                      );
                    })}
                <React.Fragment>
                  <div className={styles.element}>
                    <BrandImage
                      image={plusButton}
                      value={DEFAULT_BRANDS_LANDING_PAGE}
                      fit="1"
                      isFollowing={true}
                      onClick={this.handleBrandImageClick}
                    />
                  </div>
                </React.Fragment>
              </div>
            </div>
          )}
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
