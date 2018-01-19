import React from "react";
import Carousel from "../../general/components/Carousel";
import SingleSelect from "../../general/components/SingleSelect";
export default class SingleQuestion extends React.Component {
  handleClick(val) {
    if (this.props.onApply) {
      this.props.onApply(val);
    }
  }
  render() {
    return (
      <Carousel
        header={this.props.header}
        elementWidthDesktop={20}
        elementWidthMobile={30}
      >
        {this.props.data &&
          this.props.data.map((datum, i) => {
            return (
              <SingleSelect
                key={i}
                text={datum.text}
                value={datum.value}
                image={datum.image}
                onClick={val => {
                  this.handleClick(val);
                }}
              />
            );
          })}
      </Carousel>
    );
  }
}
