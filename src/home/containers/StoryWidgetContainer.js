import { connect } from "react-redux";
import { getItems, clearItemsSuccess } from "../actions/home.actions";
import { withRouter } from "react-router-dom";
import StoryModal from "../components/StoryModal";
const mapDispatchToProps = dispatch => {
  return {
    getItems: (positionInFeed, itemIds) => {
      dispatch(getItems(positionInFeed, itemIds));
    },
    clearItems: positionInFeed => {
      dispatch(clearItemsSuccess(positionInFeed));
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  const positionInFeed = ownProps.positionInFeed;
  const feedComponentData = state.feed.homeFeed[ownProps.positionInFeed];
  return {
    ownProps: ownProps,
    feedComponentData: feedComponentData,
    positionInFeed,
    loading: feedComponentData.loading
  };
};

const StoryWidgetContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(StoryModal)
);

export default StoryWidgetContainer;
