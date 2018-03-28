import { connect } from "react-redux";
import { fetchOrderDetails, sendInvoice } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import OrderDetails from "../components/OrderDetails";
import {
  UPDATE_REFUND_DETAILS_POPUP,
  showModal
} from "../../general/modal.actions";
const mapDispatchToProps = dispatch => {
  return {
    fetchOrderDetails: orderId => {
      dispatch(fetchOrderDetails(orderId));
    },
    sendInvoice: (ussid, sellerOrderNo) => {
      dispatch(sendInvoice(ussid, sellerOrderNo));
    },
    showModal: orderDetails => {
      dispatch(showModal(UPDATE_REFUND_DETAILS_POPUP, orderDetails));
    }
  };
};
const mapStateToProps = state => {
  return {
    orderDetails: state.profile.fetchOrderDetails
  };
};

const OrderDetailsContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(OrderDetails)
);

export default OrderDetailsContainer;
