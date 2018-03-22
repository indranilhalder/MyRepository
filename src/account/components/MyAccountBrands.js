import React from "react";
import MoreBrands from "../../blp/components/MoreBrands";
import BrandEdit from "../../blp/components/BrandEdit";
import ProfilePicture from "../../blp/components/ProfilePicture";
import * as styles from "./MyAccountBrands.css";
import MDSpinner from "react-md-spinner";
import {
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN,
  LOGIN_PATH,
  DEFAULT_BRANDS_LANDING_PAGE
} from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";

export default class MyAccountBrands extends React.Component {
  componentDidMount() {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

    if (userDetails && customerCookie) {
      this.props.getFollowedBrands();
    } else {
      this.props.history.push(LOGIN_PATH);
    }
  }
  renderToBLP() {
    this.props.history.push(DEFAULT_BRANDS_LANDING_PAGE);
  }
  followAndUnFollow(brandId, followStatus) {
    console.log(brandId, followStatus);
    this.props.followAndUnFollowBrand(brandId, followStatus);
  }
  renderLoader() {
    return <MDSpinner />;
  }

  render() {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    if (this.props.loading) {
      return this.renderLoader();
    }
    return (
      <div className={styles.base}>
        {/* we need to show this when we ll state getting profile image */}
        {/* <div className={styles.imageHolder}>
          <ProfilePicture firstName={JSON.parse(userDetails).firstName} lastName="Yadav" edit={false} />
        </div> */}
        <MoreBrands
          width={170}
          type="primary"
          label="More Brands"
          onClick={() => this.renderToBLP()}
        />
        {this.props.followedBrands && (
          <div className={styles.brandsHolder}>
            <BrandEdit
              data={this.props.followedBrands}
              onClick={(brandId, followStatus) =>
                this.followAndUnFollow(brandId, followStatus)
              }
            />
          </div>
        )}
      </div>
    );
  }
}
