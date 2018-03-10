import { connect } from "react-redux";
import { setSearchString } from "../../search/actions/search.actions.js";
import { getProductListings, setPage } from "../actions/plp.actions.js";
import PlpBrandCategoryWrapper from "../components/PlpBrandCategoryWrapper";
const mapDispatchToProps = dispatch => {
  return {
    getProductListings: (search: null, suffix, page, isFilter) => {
      dispatch(setSearchString(search));
      dispatch(setPage(page));
      dispatch(getProductListings(suffix, false, isFilter));
    },
    paginate: (page, suffix) => {
      dispatch(setPage(page));
      dispatch(getProductListings(suffix, true, false));
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
