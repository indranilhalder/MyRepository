import { connect } from "react-redux";
import { getAllOrdersDetails } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import AllOrderDetails from "../components/AllOrderDetails";
import { setHeaderText } from "../../general/header.actions";
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    getAllOrdersDetails: () => {
      dispatch(getAllOrdersDetails());
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    },
    paginate: suffix => {
      dispatch(getAllOrdersDetails(suffix, true)); //paginated is true
    }
  };
};
const mapStateToProps = (state, ownProps) => {
  return {
    profile: state.profile,
    ...ownProps
  };
};

const AllOrderContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AllOrderDetails)
);

export default AllOrderContainer;
