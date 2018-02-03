import { connect } from "react-redux";
import { hideModal } from "../../general/modal.actions";
import Sort from "../components/Sort";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {
    onCloseSort: () => {
      dispatch(hideModal());
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
