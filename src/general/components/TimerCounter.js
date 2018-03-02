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
    const regex = /(.*)\/(.*)\/(.*) (\d+):(\d+):(\d+)/;
    const match = regex.exec(endTime);
    const newDateStr = `${match[2]}/${match[1]}/${match[3]} ${match[4]}:${
      match[5]
    }:${match[6]}`;

    return <Countdown date={new Date(newDateStr)} renderer={renderTimer} />;
  }
}
Counter.propTypes = {
  endTime: PropTypes.string
};
