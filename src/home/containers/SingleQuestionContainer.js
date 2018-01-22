import { connect } from "react-redux";
import { selectSingleSelectResponse } from "../actions/home.actions";
import SingleQuestion from "../components/SingleQuestion.js";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {
    onApply: (val, questionId, positionInFeed) => {
      dispatch(selectSingleSelectResponse(val, questionId, positionInFeed));
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  const feedComponentData = state.home.homeFeed[ownProps.positionInFeed];
  return {
    feedComponentData: feedComponentData,
    loading: feedComponentData.submitLoading,
    positionInFeed: ownProps.positionInFeed
  };
};

const SingleQuestionContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(SingleQuestion)
);

export default SingleQuestionContainer;
