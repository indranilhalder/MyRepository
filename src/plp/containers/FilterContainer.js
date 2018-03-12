import { connect } from "react-redux";
import { getProductListings } from "../actions/plp.actions.js";
import Filter from "../components/Filter";
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
    filterData: state.productListings.productListings.facetdata,
    categoryData: state.productListings.productListings.facetdatacategory
  };
};

const FilterContainer = connect(mapStateToProps, mapDispatchToProps)(Filter);

export default FilterContainer;
