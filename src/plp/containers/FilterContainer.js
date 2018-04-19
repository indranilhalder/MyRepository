import { connect } from "react-redux";
import FilterMobile from "../components/FilterMobile.js";
import { withRouter } from "react-router-dom";
import {
  setFilterSelectedData,
  resetFilterSelectedData
} from "../actions/plp.actions.js";
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    onApply: () => {
      ownProps.onApply();
    },
    onClear: function() {
      ownProps.onClear();
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

  return {
    ...ownProps,
    facetData: state.productListings.productListings.facetdata,
    facetdatacategory: state.productListings.productListings.facetdatacategory,
    filterSelectedIndex: state.productListings.filterTabIndex,
    isCategorySelected
  };
};

const FilterContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(FilterMobile)
);

export default FilterContainer;
