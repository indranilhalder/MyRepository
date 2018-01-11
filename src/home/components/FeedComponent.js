import React from "react";

export default class FeedComponent extends React.Component {
  componentWillMount() {
    this.props.getComponentData(this.props.fetchUrl, this.props.positionInFeed);
  }
  render() {
    return <div>{this.props.children(this.props)}</div>;
  }
}
