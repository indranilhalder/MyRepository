import { connect } from "react-redux";
import BrandsLandingPageDefault from "../components/BrandsLandingPageDefault";
import { getCategories } from "../actions/brand.actions";
const mapDispatchToProps = dispatch => {
  return {
    getCategories: () => {
      dispatch(getCategories());
    }
  };
};
const mapStateToProps = state => {
  return {
    categories: state.categories.categories
  };
};
const BrandsLandingPageDefaultContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(BrandsLandingPageDefault);
export default BrandsLandingPageDefaultContainer;
