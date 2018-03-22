import React from "react";
// import ShopByBrandLists from "../../blp/components/ShopByBrandLists";
// import Button from "../../general/components/Button";
import MoreBrands from "../../blp/components/MoreBrands";
import BrandEdit from "../../blp/components/BrandEdit";
import ProfilePicture from "../../blp/components/ProfilePicture";
import * as styles from "./MyAccountBrands.css";
import MDSpinner from "react-md-spinner";
import {
  MY_ACCOUNT_PAGE,
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN,
  LOGIN_PATH
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
  renderLoader() {
    return <MDSpinner />;
  }

  render() {
    if (this.props.loading) {
      return this.renderLoader();
    }
    console.log(this.props);
    return (
      <div className={styles.base}>
        {/* we need to show this when we ll state getting profile image */}
        {/* <div className={styles.imageHolder}>
          <ProfilePicture firstName="aakarsh" lastName="Yadav" edit={false} />
        </div> */}

        <MoreBrands
          height={40}
          width={170}
          type="primary"
          label="More Brands"
        />

        {this.props.followedBrands && (
          <BrandEdit data={this.props.followedBrands} />
        )}
        {/* <div className={styles.followedBrandsTextHolder}>
          <div className={styles.followedBrandsTextHeader}>Follow Brands</div>
          <div className={styles.followedBrandsTextSubHeader}>
            (Long press to visit or remove brands)
          </div>
        </div>
        <div className={styles.followedBrandsHolder}>
          <ShopByBrandLists />
        </div> */}
      </div>
    );
  }
}
