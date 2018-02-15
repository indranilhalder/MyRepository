import React from "react";
import Countdown from "react-countdown-now";
import PropTypes from "prop-types";
const renderTimer = ({ days, hours, minutes, seconds }) => {
  return (
    <span>
      {days}:{hours}:{minutes}:{seconds}
    </span>
  );
};
export default class Counter extends React.Component {
  render() {
    let endTime = this.props.endTime;
    let endDate = new Date(endTime).getDate();
    let finalTime = new Date().getDate();
    let differDate = endDate - finalTime;
    let dateObj = differDate * 3600000 * 24;
    let hours = new Date(endTime).getHours();
    let finalHour = hours * 3600000;
    let getMinutes = new Date(endTime).getMinutes();
    let minutes = getMinutes * 60000;
    let seconds = new Date(endTime).getSeconds();
    let calculatedSeconds = seconds * 1000;
    let finalEndDate = finalHour + minutes + dateObj + calculatedSeconds;
    return (
      <Countdown date={Date.now() + finalEndDate} renderer={renderTimer} />
    );
  }
}
Counter.propTypes = {
  endTime: PropTypes.string
};
