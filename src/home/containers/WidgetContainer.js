import { connect } from "react-redux";
import { getComponentData, getItems } from "../actions/home.actions";
import { withRouter } from "react-router-dom";
import Widget from "../components/Widget";
import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions";
import { showModal, STORY_MODAL } from "../../general/modal.actions";
const mapDispatchToProps = dispatch => {
  return {
    getComponentData: (
      fetchUrl,
      positionInFeed,
      postParams,
      backUpUrl,
      type
    ) => {
      dispatch(
        getComponentData(positionInFeed, fetchUrl, postParams, backUpUrl, type)
      );
    },
    getItems: (positionInFeed, itemIds) => {
      dispatch(getItems(positionInFeed, itemIds));
    },
    showSecondaryLoader: () => dispatch(showSecondaryLoader()),

    // hideSecondaryLoader: () => {
    //   dispatch(hideSecondaryLoader());
    // },

    showStory: (position, data) => {
      dispatch(showModal(STORY_MODAL, position, data));
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  const positionInFeed = ownProps.positionInFeed;
  const feedComponentData = state.home.homeFeed[ownProps.positionInFeed];
  return {
    feedComponentData: feedComponentData,
    positionInFeed,
    loading: feedComponentData.loading
  };
};

const WidgetContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Widget)
);

export default WidgetContainer;
