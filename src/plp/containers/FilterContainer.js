import { connect } from "react-redux";
import FilterMobile from "../components/FilterMobile.js";
import { withRouter } from "react-router-dom";
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    backPage: () => {
      ownProps.backPage();
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  let facetData = null;
  let facetdatacategory = null;
  if (state.productListings && state.productListings.productListings) {
    facetData = state.productListings.productListings.facetdata;
    facetdatacategory = state.productListings.productListings.facetdatacategory;
  }

  return {
    ...ownProps,
    facetData: facetData,
    facetdatacategory: facetdatacategory
  };
};

const FilterContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(FilterMobile)
);

export default FilterContainer;
