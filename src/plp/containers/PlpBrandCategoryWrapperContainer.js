import { connect } from "react-redux";
import { setSearchString } from "../../search/actions/search.actions.js";
import { getProductListings, setPage } from "../actions/plp.actions.js";
import { homeFeed } from "../../home/actions/home.actions";
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
      dispatch(getProductListings(suffix, true));
    },
    homeFeed: feedType => {
      dispatch(homeFeed(feedType));
    }
  };
};

const mapStateToProps = state => {
  return {
    page: state.productListings.pageNumber,
    homeFeedData: state.home,
    productListings: state.productListings.productListings
  };
};

const FilterContainer = connect(mapStateToProps, mapDispatchToProps)(
  PlpBrandCategoryWrapper
);

export default FilterContainer;
