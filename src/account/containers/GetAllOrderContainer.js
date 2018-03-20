import { connect } from "react-redux";
import {
  getAllOrdersDetails,
  fetchOrderDetails
} from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import GetAllOrderDetails from "../components/GetAllOrderDetails";
const mapDispatchToProps = dispatch => {
  return {
    getAllOrdersDetails: () => {
      dispatch(getAllOrdersDetails());
    },
    fetchOrderDetails: orderId => {
      dispatch(fetchOrderDetails(orderId));
    }
  };
};
const mapStateToProps = state => {
  return {
    profile: state.profile
  };
};

const GetAllOrderContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(GetAllOrderDetails)
);

export default GetAllOrderContainer;
