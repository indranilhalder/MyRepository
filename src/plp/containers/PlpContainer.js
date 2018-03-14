import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import Plp from "../components/Plp";

const mapStateToProps = (state, ownProps) => {
  return {
    isFilter: ownProps.isFilter,
    showFilter: ownProps.showFilter,
    productListings: state.productListings.productListings,
    loading: state.productListings.loading,
    searchresult: state.productListings.searchresult
  };
};

const PlpContainer = withRouter(connect(mapStateToProps, null)(Plp));
export default PlpContainer;
