import { connect } from "react-redux";
import { getAllOrdersDetails } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import AllOrderDetails from "../components/AllOrderDetails";
import { setHeaderText } from "../../general/header.actions";
const mapDispatchToProps = dispatch => {
  return {
    getAllOrdersDetails: () => {
      dispatch(getAllOrdersDetails());
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
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
