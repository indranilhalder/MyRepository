import React from "react";
import PropTypes from "prop-types";
import styles from "./SearchResult.css";
export default class SearchResult extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        '{this.props.resultCount}' results found for{" "}
        <span className={styles.bold}>"{this.props.searchString}"</span>.
        Showing results for{" "}
        <span className={styles.bold}>"{this.props.resultDefault}"</span>
      </div>
    );
  }
}
SearchResult.propTypes = {
  resultCount: PropTypes.string,
  searchString: PropTypes.string,
  resultDefault: PropTypes.string
};
