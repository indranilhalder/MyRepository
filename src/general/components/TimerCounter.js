import React from "react";
import Countdown from "react-countdown-now";
import PropTypes from "prop-types";
const renderTimer = ({ days, hours, minutes, seconds }) => {
  // const finalHour = days * 24 + parseInt(hours, 10);
  return (
    <span>
      {days ? `${days} : ` : null}
      {hours}:{minutes}:{seconds}
    </span>
  );
};
export default class Counter extends React.Component {
  render() {
    let endTime = this.props.endTime;
    return <Countdown date={new Date(endTime)} renderer={renderTimer} />;
  }
}
Counter.propTypes = {
  endTime: PropTypes.string
};
