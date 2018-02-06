import { connect } from "react-redux";
import { setFilters } from "../../search/actions/search.actions.js";
import { getProductListings } from "../actions/plp.actions.js";
import Filter from "../components/Filter";
const mapDispatchToProps = dispatch => {
  return {
    onApply: filters => {
      dispatch(setFilters(filters));
      dispatch(getProductListings());
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    ...ownProps
  };
};

const FilterContainer = connect(mapStateToProps, mapDispatchToProps)(Filter);

export default FilterContainer;
