import { connect } from "react-redux";
import { setFilters, setSort } from "../../search/actions/search.actions.js";
import { getProductListings, setPage } from "../actions/plp.actions.js";
import PlpBrandCategoryWrapper from "../components/PlpBrandCategoryWrapper";
const mapDispatchToProps = dispatch => {
  return {
    getProductListings: (filters, suffix, page) => {
      dispatch(setSort("relevance"));
      dispatch(setPage(page));
      dispatch(setFilters(filters));
      dispatch(getProductListings(suffix));
    },
    paginate: (page, suffix) => {
      dispatch(setPage(page));
      dispatch(getProductListings(suffix, true));
    }
  };
};

const mapStateToProps = state => {
  return {
    page: state.productListings.pageNumber
  };
};

const FilterContainer = connect(mapStateToProps, mapDispatchToProps)(
  PlpBrandCategoryWrapper
);

export default FilterContainer;
