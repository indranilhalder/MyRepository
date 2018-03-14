import { connect } from "react-redux";
import Button from "../../general/components/Button";
import { followUnFollowBrand } from "../actions/pdp.actions";
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    onClick: () => {
      dispatch(followUnFollowBrand(ownProps.brandId));
    }
  };
};
const mapStateToProps = (state, ownProps) => {
  return {
    label:
      state.productDescription.msdItems.brandDetails &&
      state.productDescription.msdItems.brandDetails.isFollowing
        ? "Following"
        : "Follow",
    type: "tertiary"
  };
};
const FollowUnFollowButtonContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(Button);

export default FollowUnFollowButtonContainer;
