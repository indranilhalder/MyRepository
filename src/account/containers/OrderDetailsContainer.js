import { connect } from "react-redux";
import { fetchOrderDetails, sendInvoice } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import OrderDetails from "../components/OrderDetails";
import { displayToast } from "../../general/toast.actions";
import { setHeaderText } from "../../general/header.actions";
import {
  UPDATE_REFUND_DETAILS_POPUP,
  ORDER_DETAILS_MODAL,
  showModal
} from "../../general/modal.actions";
import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions";
import { setUrlToRedirectToAfterAuth } from "../../auth/actions/auth.actions.js";

const mapDispatchToProps = dispatch => {
  return {
    fetchOrderDetails: orderId => {
      dispatch(fetchOrderDetails(orderId));
    },
    displayToast: toastMessage => {
      dispatch(displayToast(toastMessage));
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    },
    sendInvoice: (ussid, sellerOrderNo) => {
      dispatch(sendInvoice(ussid, sellerOrderNo));
    },
    showModal: orderDetails => {
      dispatch(showModal(UPDATE_REFUND_DETAILS_POPUP, orderDetails));
    },
    showSecondaryLoader: () => {
      dispatch(showSecondaryLoader());
    },
    hideSecondaryLoader: () => {
      dispatch(hideSecondaryLoader());
    },
    setUrlToRedirectToAfterAuth: url => {
      dispatch(setUrlToRedirectToAfterAuth(url));
    },
    showShippingDetails: data => {
      dispatch(showModal(ORDER_DETAILS_MODAL, data));
    }
  };
};
const mapStateToProps = state => {
  return {
    orderDetails: state.profile.fetchOrderDetails,
    loadingForFetchOrderDetails: state.profile.loadingForFetchOrderDetails,
    sendInvoiceSatus: state.profile.sendInvoiceStatus
  };
};

const OrderDetailsContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(OrderDetails)
);

export default OrderDetailsContainer;
