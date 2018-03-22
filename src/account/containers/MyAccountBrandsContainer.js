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
      try {
        dispatch(followAndUnFollowBrandInCommerce(brandId, followStatus))
          .then((err, data) => {
            if (err) {
              throw new Error(err);
            }
            dispatch(
              followAndUnFollowBrandInFeedBackInCommerceApi(
                brandId,
                followStatus
              )
            );
          })
          .then((err, res) => {
            if (err) {
              throw new Error(err);
            }
            dispatch(getFollowedBrands());
          });
      } catch (e) {
        console.log(e.message);
      }
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
