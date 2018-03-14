import { connect } from "react-redux";
import BrandsLandingPageDefault from "../components/BrandsLandingPageDefault";
import { getAllBrands } from "../actions/blp.actions";
const mapDispatchToProps = dispatch => {
  return {
    getAllBrands: () => {
      dispatch(getAllBrands());
    }
  };
};
const mapStateToProps = state => {
  return {
    brandsStores: state.brandDefault.brandsStores,
    loading: state.brandDefault.loading
  };
};
const BrandsLandingPageDefaultContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(BrandsLandingPageDefault);
export default BrandsLandingPageDefaultContainer;
