import { connect } from "react-redux";
import {
  getUserAddress,
  checkPinCodeServiceAvailability
} from "../../cart/actions/cart.actions";
import AddressModal from "../components/AddressModal";
const mapDispatchToProps = dispatch => {
  return {
    getUserAddress: () => {
      dispatch(getUserAddress());
    },
    checkPinCodeServiceAvailability: pincode => {
      dispatch(checkPinCodeServiceAvailability(pincode));
    }
  };
};
const mapStateToProps = (state, ownProps) => {
  return {
    userAddress: state.cart.userAddress,
    ...ownProps
  };
};
const AddressModalContainer = connect(mapStateToProps, mapDispatchToProps)(
  AddressModal
);
export default AddressModalContainer;
