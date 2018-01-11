import React, { Component } from "react";
import PropTypes from "prop-types";
import FeedComponentContainer from "../containers/FeedComponentContainer";
const typeComponentMapping = {
  themeOffers: props => <div {...props}> HELLO WORLD</div>
};
// Forgot password --> shows a modal
// Don't have an account --> sign up --> a route change.

class Feed extends Component {
  renderFeedComponent(feedDatum, i) {
    return (
      typeComponentMapping[feedDatum.type] && (
        <FeedComponentContainer positionInFeed={i}>
          {typeComponentMapping[feedDatum.type] &&
            typeComponentMapping[feedDatum.type]}
        </FeedComponentContainer>
      )
    );
  }

  componentWillMount() {
    this.props.homeFeed();
  }
  render() {
    console.log("FEED");
    return (
      <div>
        {this.props.homeFeedData.map((feedDatum, i) => {
          return this.renderFeedComponent(feedDatum, i);
        })}
      </div>
    );
  }
}

Feed.propTypes = {
  onSubmit: PropTypes.func,
  onForgotPassword: PropTypes.func,
  onChangeEmail: PropTypes.func,
  onChangePassword: PropTypes.func,
  emailValue: PropTypes.string,
  passwordValue: PropTypes.string,
  loading: PropTypes.bool
};

Feed.defaultProps = {
  loading: false
};

export default Feed;
