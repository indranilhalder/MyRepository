import { connect } from "react-redux";
import { getProductListings } from "../actions/plp.actions.js";
import FilterMobile from "../components/FilterMobile.js";
import { withRouter } from "react-router-dom";
const mapDispatchToProps = dispatch => {
  return {
    onApply: filters => {
      dispatch(getProductListings());
    }
  };
};

const mapStateToProps = (state, ownProps) => {
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
