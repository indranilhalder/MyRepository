import React from "react";
import Observer from "@researchgate/react-intersection-observer";

export default function withIntersectionObserver(BaseComponent, threshold) {
  console.log("BASE COMPONENT");
  console.log(BaseComponent);
  return class extends React.Component {
    state = {
      isIntersecting: false
    };

    handleChange = ({ isIntersecting, intersectionRatio }) => {
      this.setState({
        isIntersecting: isIntersecting && intersectionRatio >= threshold
      });
    };

    render() {
      return (
        <Observer onChange={this.handleChange} threshold={threshold}>
          <BaseComponent
            {...this.props}
            isVisible={this.state.isIntersecting}
          />
        </Observer>
      );
    }
  };
}
