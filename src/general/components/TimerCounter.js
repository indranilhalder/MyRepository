import React from "react";
import Countdown from "react-countdown-now";
import PropTypes from "prop-types";
import moment from "moment";
const renderTimer = ({ days, hours, minutes, seconds }) => {
  const finalHour = days * 24 + parseInt(hours, 10);
  return (
    <span>
      {finalHour}:{minutes}:{seconds}
    </span>
  );
};
export default class Counter extends React.Component {
  render() {
    // the expectation is that the endTime comes in the American date format
    // MM/DD/YYYY HH:mm:ss
    let endTime = new Date(this.props.endTime);
    return endTime ? <Countdown date={endTime} renderer={renderTimer} /> : null;
  }
}
Counter.propTypes = {
  endTime: PropTypes.string
};
