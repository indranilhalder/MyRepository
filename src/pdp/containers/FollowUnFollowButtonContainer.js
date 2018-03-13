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
    label: ownProps.isFollowing ? "Unfollow" : "Follow"
  };
};
const FollowUnFollowButtonContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(Button);

export default FollowUnFollowButtonContainer;
