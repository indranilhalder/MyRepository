import { connect } from "react-redux";
import { fetchOrderDetails, sendInvoice } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import OrderDetails from "../components/OrderDetails";
const mapDispatchToProps = dispatch => {
  return {
    fetchOrderDetails: orderId => {
      dispatch(fetchOrderDetails(orderId));
    },
    sendInvoice: (ussid, sellerOrderNo) => {
      dispatch(sendInvoice(ussid, sellerOrderNo));
    }
  };
};
const mapStateToProps = state => {
  return {
    orderDetails: state.profile.fetchOrderDetails,
  };
};

const OrderDetailsContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(OrderDetails)
);

export default OrderDetailsContainer;
