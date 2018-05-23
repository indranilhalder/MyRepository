import { connect } from "react-redux";
import Button from "../../general/components/Button";
import { followAndUnFollowBrand } from "../../account/actions/account.actions";
import { FOLLOW, FOLLOWING } from "../../lib/constants";

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    onClick: () => {
      dispatch(
        followAndUnFollowBrand(
          ownProps.brandId,
          ownProps.isFollowing,
          ownProps.pageType,
          ownProps.positionInFeed // it will use if pageType comes homeFeed
        )
      );
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    label:
      ownProps.isFollowing === "true" || ownProps.isFollowing === true
        ? FOLLOWING
        : FOLLOW,
    type: "hollow",
    color: ownProps.color ? ownProps.color : "#fff"
  };
};

// common Props for following and un follow button container .
// 1. pageType. ["HOME_FEED","PDP_PAGE","MY_ACCOUNT"];
// 2. isFollowing. [true,false];
// 3. brandId.

// in home feed page extra props.
// 1. positionInFeed.

export const FollowUnFollowButtonContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(Button);
