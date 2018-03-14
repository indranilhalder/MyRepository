import { connect } from "react-redux";
import BrandsLandingPageDefault from "../components/BrandsLandingPageDefault";
import { getAllBrands } from "../actions/brand.actions";
const mapDispatchToProps = dispatch => {
  return {
    getAllBrands: () => {
      dispatch(getAllBrands());
    }
  };
};
const mapStateToProps = state => {
  return {
    brandsStores: state.brand.brandsStores,
    loading: state.brand.loading
  };
};
const BrandsLandingPageDefaultContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(BrandsLandingPageDefault);
export default BrandsLandingPageDefaultContainer;
