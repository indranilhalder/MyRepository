import { connect } from "react-redux";
import { getOrderSummary } from "../actions/cart.actions.js";
import { withRouter } from "react-router-dom";
import DisplayOrderSummary from "../components/DisplayOrderSummary";
const mapDispatchToProps = dispatch => {
  return {
    getOrderSummary: () => {
      dispatch(getOrderSummary());
    }
  };
};

const mapStateToProps = state => {
  return {
    orderSummary: state.cart.orderSummary,
    orderSummaryError: state.cart.orderSummaryError,
    orderSummaryStatus: state.cart.orderSummaryStatus
  };
};

const DisplayOrderSummaryContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(DisplayOrderSummary)
);

export default DisplayOrderSummaryContainer;
