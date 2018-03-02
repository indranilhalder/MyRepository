import { connect } from "react-redux";
import { setFilters, setSort } from "../../search/actions/search.actions.js";
import { getProductListings } from "../actions/plp.actions.js";
import PlpBrandCategoryWrapper from "../components/PlpBrandCategoryWrapper";
const mapDispatchToProps = dispatch => {
  return {
    getProductListings: (filters, suffix) => {
      dispatch(setSort("relevance"));
      dispatch(setFilters(filters));
      dispatch(getProductListings(suffix));
    }
  };
};

const FilterContainer = connect(null, mapDispatchToProps)(
  PlpBrandCategoryWrapper
);

export default FilterContainer;
