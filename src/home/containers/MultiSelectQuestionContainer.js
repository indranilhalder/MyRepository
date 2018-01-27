import { connect } from "react-redux";
import { multiSelectSubmit } from "../actions/home.actions.js";
import MultiSelectQuestion from "../components/MultiSelectQuestion.js";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {
    onApply: (values, questionId, positionInFeed) => {
      dispatch(multiSelectSubmit(values, questionId, positionInFeed));
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  const feedComponentData = state.home.homeFeed[ownProps.positionInFeed];
  return {
    feedComponentData,
    loading: feedComponentData.submitLoading,
    positionInFeed: ownProps.positionInFeed
  };
};

const MultiSelectQuestionContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(MultiSelectQuestion)
);

export default MultiSelectQuestionContainer;
