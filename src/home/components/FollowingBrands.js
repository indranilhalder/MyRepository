import React from "react";
import BrandImage from "../../general/components/BrandImage";
import Carousel from "../../general/components/Carousel";
import styles from "./FollowingBrands.css";
import PropTypes from "prop-types";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import plusButton from "../../general/components/img/plusbutton.svg";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  DEFAULT_BRANDS_LANDING_PAGE
} from "../../lib/constants";
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
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

    if (followWidgetData.data && followWidgetData.data) {
      let gotoMoreBands = followWidgetData.data;

      if (gotoMoreBands) {
        gotoMoreBands.push({
          imageURL: plusButton,
          isFollowing: "true",
          fit: "1",
          webURL: DEFAULT_BRANDS_LANDING_PAGE
        });
      }
    }

    return (
      <div>
        {userDetails &&
          customerCookie &&
          followWidgetData &&
          followWidgetData.data &&
          followWidgetData.data.filter(val => {
            return val.isFollowing === "true";
          }).length > 1 && (
            <div className={styles.base}>
              <Carousel
                header={
                  this.props.feedComponentData.title
                    ? this.props.feedComponentData.title
                    : "Following Brands"
                }
                elementWidthMobile={30}
              >
                {followWidgetData.data &&
                  followWidgetData.data
                    .filter(val => {
                      return val.isFollowing === "true";
                    })
                    .map((datum, i) => {
                      return (
                        <BrandImage
                          key={i}
                          image={datum.imageURL}
                          value={datum.webURL}
                          fit={datum.fit}
                          isFollowing={datum.isFollowing}
                          onClick={this.handleBrandImageClick}
                        />
                      );
                    })}
              </Carousel>
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
