import { connect } from "react-redux";
import { getAllOrdersDetails } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import AllOrderDetails from "../components/AllOrderDetails";
const mapDispatchToProps = dispatch => {
  return {
    getAllOrdersDetails: () => {
      dispatch(getAllOrdersDetails());
    }
  };
};
const mapStateToProps = state => {
  return {
    profile: state.profile
  };
};

const AllOrderContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AllOrderDetails)
);

export default AllOrderContainer;
