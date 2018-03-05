import BrandsTotal from "../components/BrandsTotal";
import { connect } from "react-redux";
import { getBrandDetails } from "../actions/brand.actions";
const mapDispatchToProps = dispatch => {
  return {
    getBrandDetails: categoryId => {
      dispatch(getBrandDetails(categoryId));
    }
  };
};
const mapStateToProps = state => {
  return {
    brands: state
  };
};
const BrandLandingPageContainer = connect(mapStateToProps, mapDispatchToProps)(
  BrandsTotal
);
export default BrandLandingPageContainer;
