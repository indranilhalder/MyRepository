import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import Plp from "../components/Plp";
import { showModal, SORT } from "../../general/modal.actions.js";

const mapDispatchToProps = dispatch => {
  return {
    showSort: () => {
      dispatch(showModal(SORT));
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    isFilter: ownProps.isFilter,
    showFilter: ownProps.showFilter,
    productListings: state.productListings.productListings,
    loading: state.productListings.loading,
    searchresult: state.productListings.searchresult
  };
};

const PlpContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Plp)
);
export default PlpContainer;
