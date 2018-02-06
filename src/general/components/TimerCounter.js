import React from "react";
import ReactDOM from "react-dom";
import Countdown from "react-countdown-now";

const renderTimer = ({ hours, minutes, seconds }) => {
  return (
    <span>
      {hours}:{minutes}:{seconds}
    </span>
  );
};
export default class Counter extends React.Component {
  render() {
    let endTime = this.props.endTime;
    let finalTime = new Date(endTime).getTime();
    return <Countdown date={Date.now() + finalTime} renderer={renderTimer} />;
  }
}
