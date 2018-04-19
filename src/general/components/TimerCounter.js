import React from "react";
import Countdown from "react-countdown-now";
import PropTypes from "prop-types";
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
    return endTime ? (
      <Countdown
        date={endTime}
        renderer={renderTimer}
        onComplete={this.props.onComplete}
      />
    ) : null;
  }
}
Counter.propTypes = {
  endTime: PropTypes.string,
  onComplete: PropTypes.func
};
