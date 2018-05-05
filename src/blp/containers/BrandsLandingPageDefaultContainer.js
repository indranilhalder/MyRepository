import { connect } from "react-redux";
import BrandsLandingPageDefault from "../components/BrandsLandingPageDefault";
import { getAllBrands } from "../actions/blp.actions";
import { setHeaderText } from "../../general/header.actions";
import { getFollowedBrands } from "../../account/actions/account.actions";
const mapDispatchToProps = dispatch => {
  return {
    getAllBrands: () => {
      dispatch(getAllBrands());
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    },
    getFollowedBrands: () => {
      dispatch(getFollowedBrands());
    }
  };
};
const mapStateToProps = state => {
  return {
    followedBrands: state.profile.followedBrands,
    brandsStores: state.brandDefault.brandsStores,
    loading: state.brandDefault.loading
  };
};
const BrandsLandingPageDefaultContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(BrandsLandingPageDefault);
export default BrandsLandingPageDefaultContainer;
