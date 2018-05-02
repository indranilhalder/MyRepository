import React from "react";
import PropTypes from "prop-types";
import { Redirect } from "react-router-dom";
import MoreBrands from "../../blp/components/MoreBrands";
import BrandEdit from "../../blp/components/BrandEdit";
import ProfilePicture from "../../blp/components/ProfilePicture";
import * as styles from "./MyAccountBrands.css";
import Loader from "../../general/components/Loader";
import {
  TRUE,
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN,
  LOGIN_PATH,
  DEFAULT_BRANDS_LANDING_PAGE,
  BRANDS
} from "../../lib/constants";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import * as Cookie from "../../lib/Cookie";

export default class MyAccountBrands extends React.Component {
  componentDidMount() {
    this.props.setHeaderText(BRANDS);
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

    if (userDetails && customerCookie) {
      this.props.getFollowedBrands();
    }
  }
  componentDidUpdate() {
    this.props.setHeaderText(BRANDS);
  }
  navigateToLogin() {
    return <Redirect to={LOGIN_PATH} />;
  }
  navigateToBLP() {
    this.props.history.push(DEFAULT_BRANDS_LANDING_PAGE);
  }
  followAndUnFollow(brandId, followStatus) {
    if (this.props.followAndUnFollowBrand) {
      this.props.followAndUnFollowBrand(brandId, followStatus);
    }
  }
  onRedirectToBrandPage(webURL) {
    const urlSuffix = webURL.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
  }
  renderLoader() {
    return <Loader />;
  }

  render() {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

    if (this.props.loading) {
      return this.renderLoader();
    }
    if (!userDetails || !customerCookie) {
      return this.navigateToLogin();
    }
    let followedBrands = [];
    if (this.props.followedBrands) {
      followedBrands = this.props.followedBrands.filter(
        brand => brand.isFollowing === "true"
      );
    }

    return (
      <div className={styles.base}>
        <MoreBrands
          width={170}
          type="primary"
          label="More Brands"
          onClick={() => this.navigateToBLP()}
        />
        {followedBrands && (
          <div className={styles.brandsHolder}>
            <BrandEdit
              data={followedBrands}
              onClick={(brandId, followStatus) =>
                this.followAndUnFollow(brandId, followStatus)
              }
              onRedirectToBrandPage={webURL =>
                this.onRedirectToBrandPage(webURL)
              }
            />
          </div>
        )}
      </div>
    );
  }
}
MyAccountBrands.propTypes = {
  loading: PropTypes.bool,
  followedBrands: PropTypes.array
};
