import React from "react";
import MultiSelectCarousel from "../../general/components/MultiSelectCarousel.js";
import CustomerQuestion from "../../general/components/CustomerQuestion.js";

export default class MultiSelectQuestion extends React.Component {
  renderCustomerQuestion(questionItem, i) {
    return (
      <CustomerQuestion
        value={i}
        key={i}
        image={questionItem.imageURL}
        header={questionItem.title}
        description={questionItem.description}
      />
    );
  }
  render() {
    const feedComponentData = this.props.feedComponentData.data;
    const questionItems = feedComponentData.items;
    return (
      <MultiSelectCarousel
        header={feedComponentData.title}
        subheader={feedComponentData.description}
      >
        {questionItems && questionItems.map(this.renderCustomerQuestion)}
      </MultiSelectCarousel>
    );
  }
}
