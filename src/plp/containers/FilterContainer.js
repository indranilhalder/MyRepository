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
  console.log("FILTER CONTAINER");
  console.log(state.productListings.productListings.facetdatacategory);
  return {
    ...ownProps,
    facetData: state.productListings.productListings.facetdata,
    facetdatacategory: state.productListings.productListings.facetdatacategory
  };
};

const FilterContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(FilterMobile)
);

export default FilterContainer;
