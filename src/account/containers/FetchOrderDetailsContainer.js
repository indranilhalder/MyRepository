import { connect } from "react-redux";
import { fetchOrderDetails } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import FetchOrderDetail from "../components/FetchOrderDetail";
const mapDispatchToProps = dispatch => {
  return {
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
  connect(mapStateToProps, mapDispatchToProps)(FetchOrderDetail)
);

export default GetAllOrderContainer;
