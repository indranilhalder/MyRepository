import { connect } from "react-redux";
import BrandsLandingPageDefault from "../components/BrandsLandingPageDefault";
import { getAllBrandsStore } from "../actions/brand.actions";
const mapDispatchToProps = dispatch => {
  return {
    getAllBrandsStore: () => {
      dispatch(getAllBrandsStore());
    }
  };
};
const mapStateToProps = state => {
  return {
    brandsStores: state.brand.brandsStores
  };
};
const BrandsLandingPageDefaultContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(BrandsLandingPageDefault);
export default BrandsLandingPageDefaultContainer;
