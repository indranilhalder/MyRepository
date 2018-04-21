import { connect } from "react-redux";
import FilterMobile from "../components/FilterMobile.js";
import { withRouter } from "react-router-dom";
import {
  setFilterSelectedData,
  resetFilterSelectedData
} from "../actions/plp.actions.js";
import findIndex from "lodash.findindex";
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    onApply: () => {
      ownProps.onApply();
    },
    setFilterSelectedData: (isCategorySelected, filterTabIndex) => {
      dispatch(setFilterSelectedData(isCategorySelected, filterTabIndex));
    },
    resetFilterSelectedData: () => {
      dispatch(resetFilterSelectedData());
    },
    onL3CategorySelect: () => {
      ownProps.onL3CategorySelect();
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  let isCategorySelected;
  if (!state.productListings.productListings.facetdatacategory) {
    isCategorySelected = false;
  } else {
    isCategorySelected = state.productListings.isCategorySelected;
  }
  const selectedFacetKey = state.productListings.selectedFacetKey;
  // cases

  // The key is there and it's position has changed
  // Key is there and it's position has not changed
  // key is no longer there.

  const facetData = state.productListings.productListings.facetdata;
  let filterSelectedIndex = state.productListings.filterTabIndex;
  if (facetData) {
    let indexOfKey = findIndex(facetData, datum => {
      return datum.key === selectedFacetKey;
    });
    if (indexOfKey === -1) {
      indexOfKey = 0;
    }
    filterSelectedIndex = indexOfKey;
  }

  return {
    ...ownProps,
    facetData: state.productListings.productListings.facetdata,
    facetdatacategory: state.productListings.productListings.facetdatacategory,
    filterSelectedIndex,
    isCategorySelected
  };
};

const FilterContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(FilterMobile)
);

export default FilterContainer;
