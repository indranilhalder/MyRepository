import React from "react";
import MultiSelectCarousel from "../../general/components/MultiSelectCarousel.js";
import CustomerQuestion from "../../general/components/CustomerQuestion.js";
import LoadingScreen from "../../general/components/LoadingScreen.js";
import {
  MULTI_SELECT_DESCRIPTION_COPY,
  MULTI_SELECT_HEADING_COPY
} from "../../lib/constants.js";
export default class MultiSelectQuestion extends React.Component {
  renderCustomerQuestion(questionItem, i) {
    return (
      <CustomerQuestion
        value={questionItem.optionId}
        key={i}
        image={questionItem.imageURL}
        header={questionItem.title}
        description={questionItem.description}
      />
    );
  }

  handleApply(values) {
    this.props.onApply(
      values,
      this.props.feedComponentData.data.questionId,
      this.props.positionInFeed
    );
  }

  render() {
    const feedComponentData = this.props.feedComponentData.data;
    const questionItems = feedComponentData.items;

    if (this.props.loading) {
      return (
        <LoadingScreen
          body={MULTI_SELECT_DESCRIPTION_COPY}
          header={MULTI_SELECT_HEADING_COPY}
        />
      );
    }
    return (
      <MultiSelectCarousel
        header={feedComponentData.title}
        subheader={feedComponentData.description}
        onApply={values => this.handleApply(values)}
      >
        {questionItems && questionItems.map(this.renderCustomerQuestion)}
      </MultiSelectCarousel>
    );
  }
}
