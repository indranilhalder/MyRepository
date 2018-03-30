import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import MyAccountBrands from "../components/MyAccountBrands";
import {
  getFollowedBrands,
  followAndUnFollowBrand
} from "../actions/account.actions";
import { SUCCESS } from "../../lib/constants";
const mapDispatchToProps = dispatch => {
  return {
    getFollowedBrands: () => {
      dispatch(getFollowedBrands());
    },
    followAndUnFollowBrand: (brandId, followStatus) => {
      try {
        dispatch(followAndUnFollowBrand(brandId, followStatus)).then(
          response => {
            if (response.status === SUCCESS) {
              return dispatch(getFollowedBrands());
            } else {
              return response;
            }
          }
        );
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
