import { connect } from "react-redux";
import InvalidCouponPopUp from "../components/InvalidCouponPopUp";
import {
  releaseBankOffer,
  releaseUserCoupon,
  removeNoCostEmi
} from "../actions/cart.actions";
const mapDispatchToProps = dispatch => {
  return {
    releaseBankOffer: couponCode => {
      return dispatch(releaseBankOffer(couponCode));
    },
    releaseUserCoupon: couponCode => {
      return dispatch(releaseUserCoupon(couponCode));
    },
    releaseNoCostEmiCoupon: couponCode => {
      return dispatch(removeNoCostEmi(couponCode));
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    ...ownProps
  };
};
const InvalidCouponPopUpContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(InvalidCouponPopUp);
export default InvalidCouponPopUpContainer;
