import React from "react";
import styles from "./RattingHolder.css";
import HorizontalRatting from "./HorizontalRatting.js";
import PropTypes from "prop-types";
export default class RattingHolder extends React.Component {
  render() {
    let calculateWidth = 0;
    this.props.rattingData.map((data, i) => {
      calculateWidth += data.label;
    });
    return (
      <div className={styles.base}>
        {this.props.rattingData.map((data, i) => {
          return (
            <HorizontalRatting
              label={data.label}
              header={data.indexNumber}
              width={data.label / calculateWidth * 100}
            />
          );
        })}
      </div>
    );
  }
}
RattingHolder.propTypes = {
  rattingData: PropTypes.arrayOf(
    PropTypes.shape({
      label: PropTypes.number,
      indexNumber: PropTypes.number
    })
  )
};
