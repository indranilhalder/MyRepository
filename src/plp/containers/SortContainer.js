import { connect } from "react-redux";
import { getProductListings } from "../actions/plp.actions";
import { setSort } from "../../search/actions/search.actions.js";
import { hideModal } from "../../general/modal.actions";
import Sort from "../components/Sort";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {
    onCloseSort: () => {
      dispatch(hideModal());
    },
    onClick: sortBy => {
      dispatch(setSort(sortBy));
      dispatch(getProductListings()).then(dispatch(hideModal()));
    }
  };
};

const mapStateToProps = state => {
  return {
    sortList: state.productListings.productListings.sorts
  };
};

const SortContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Sort)
);

export default SortContainer;
