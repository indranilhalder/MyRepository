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
  console.log(state);
  return {
    brandDetails: state.brandDetails.brandDetails
  };
};
const BrandLandingPageContainer = connect(mapStateToProps, mapDispatchToProps)(
  BrandsTotal
);
export default BrandLandingPageContainer;
