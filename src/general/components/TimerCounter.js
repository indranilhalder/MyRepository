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
    let finalEndDate = finalHour + dateObj + minutes + calculatedSeconds;
    return (
      <Countdown date={Date.now() + finalEndDate} renderer={renderTimer} />
    );
  }
}

Counter.propTypes = {
  endTime: PropTypes.string
};
