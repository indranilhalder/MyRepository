import React, { Component } from "react";
import SecondaryLoader from "../../general/components/SecondaryLoader.js";
import Feed from "../../home/components/Feed";
import styles from "./StaticPage.css";
import {
  BLP_OR_CLP_FEED_TYPE,
  NOT_FOUND,
  REQUESTING,
  SUCCESS
} from "../../lib/constants";
import { Redirect } from "react-router-dom";

export default class StaticPage extends Component {
  componentDidMount() {
    const slug = this.props.match.params.slug;
    this.props.getStaticPage(slug);
  }

  componentDidUpdate(prevProps) {
    if (this.props.match.params.slug !== prevProps.match.params.slug) {
      const slug = this.props.match.params.slug;
      this.props.getStaticPage(slug);
    }
  }
  navigateTo404() {
    return <Redirect to={NOT_FOUND} />;
  }
  render() {
    if (
      this.props.feedType === BLP_OR_CLP_FEED_TYPE &&
      this.props.data &&
      this.props.data.length === 0 &&
      this.props.status === SUCCESS
    ) {
      return this.navigateTo404();
    }
    if (this.props.loading || !this.props.data) {
      return <SecondaryLoader />;
    } else {
      return (
        <div className={styles.base}>
          <Feed
            homeFeedData={this.props.data}
            setHeaderText={this.props.setHeaderText}
          />
        </div>
      );
    }
  }
}
