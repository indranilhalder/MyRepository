import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import MyAccountBrands from "../components/MyAccountBrands";
import {
  getFollowedBrands,
  followAndUnFollowBrandInCommerce,
  followAndUnFollowBrandInFeedBackInCommerceApi
} from "../actions/account.actions";

const mapDispatchToProps = dispatch => {
  return {
    getFollowedBrands: () => {
      dispatch(getFollowedBrands());
    },
    followAndUnFollowBrand: (brandId, followStatus) => {
      dispatch(followAndUnFollowBrandInCommerce(brandId, followStatus))
        .then(() =>
          dispatch(
            followAndUnFollowBrandInFeedBackInCommerceApi(brandId, followStatus)
          )
        )
        .then(() => dispatch(getFollowedBrands()));
    }
  };
};
const mapStateToProps = state => {
  return {
    followedBrands: state.profile.followedBrands,
    loading: state.profile.loadingForFollowedBrands
  };
};
const MyAccountBrandsContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(MyAccountBrands)
);
export default MyAccountBrandsContainer;
