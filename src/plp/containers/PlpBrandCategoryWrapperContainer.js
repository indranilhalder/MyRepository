import { connect } from "react-redux";
import {
  setFilters,
  setSort,
  setSearchString
} from "../../search/actions/search.actions.js";
import { getProductListings, setPage } from "../actions/plp.actions.js";
import PlpBrandCategoryWrapper from "../components/PlpBrandCategoryWrapper";
const mapDispatchToProps = dispatch => {
  return {
    getProductListings: (search: null, filters, suffix, page) => {
      console.log("GET PRODUCT LISTINGS");
      console.log(search);
      if (search) {
        dispatch(setSearchString(search));
      }
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
