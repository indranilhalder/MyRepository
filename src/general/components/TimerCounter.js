import React from "react";
import ReactDOM from "react-dom";
import Countdown from "react-countdown-now";

const renderer = ({ hours, minutes, seconds, completed }) => {
  if (completed) {
    return <div>Done</div>;
  } else {
    return (
      <span>
        {hours}:{minutes}:{seconds}
      </span>
    );
  }
};
export default class Counter extends React.Component {
  render() {
    return <Countdown date={Date.now() + 18000000} renderer={renderer} />;
  }
}
