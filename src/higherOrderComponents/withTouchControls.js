import React from "react";
export default function withTouchControls(Component) {
  return class extends React.Component {
    constructor(props) {
      super(props);
      this.state = {
        position: 0
      };
    }
    forward = () => {
      if (this.state.position < this.props.children.length - 1) {
        const position = this.state.position + 1;
        this.setState({ position: position });
      }
    };
    back = () => {
      if (this.state.position > 0) {
        const position = this.state.position - 1;
        this.setState({ position: position });
      }
    };
    swipeStart(evt) {
      evt.stopPropagation();
      this.setState({ touchStart: evt.touches[0].clientX });
    }
    swipeMove(evt) {
      evt.stopPropagation();
      this.setState({ touchEnd: evt.touches[0].clientX });
    }
    swipeEnd() {
      if (this.state.touchStart < this.state.touchEnd) {
        this.back();
      } else {
        this.forward();
      }
    }
    render() {
      return (
        <Component
          onTouchStart={evt => this.swipeStart(evt)}
          onTouchMove={evt => this.swipeMove(evt)}
          onTouchEnd={evt => this.swipeEnd(evt)}
          {...this.props}
          position={this.state.position}
        >
          {this.props.children}
        </Component>
      );
    }
  };
}
