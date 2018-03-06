import { connect } from "react-redux";
import { getProductListings } from "../actions/plp.actions.js";
import Filter from "../components/Filter";
import { withRouter } from "react-router-dom";
const mapDispatchToProps = dispatch => {
  return {
    onApply: filters => {
      dispatch(getProductListings());
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  const facetData = state.productListings.productListings.facetdata;
  const categoryData = state.productListings.productListings.facetdatacategory;
  if (facetData[0].name !== "Category") {
    facetData.unshift(categoryData);
  }
  return {
    ...ownProps,
    filterData: state.productListings.productListings.facetdata
  };
};

const FilterContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Filter)
);

export default FilterContainer;
