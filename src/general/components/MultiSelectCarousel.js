import React from "react";
import Carousel from "./Carousel";
export default class MuliSelectCarousel extends React.Component {
  selectItem(val) {
    console.log(val);
  }
  render() {
    const children = this.props.children;
    const childrenWithProps = React.Children.map(children, (child, i) => {
      return React.cloneElement(child, {
        selected: this.props.selected.includes(child.props.value),

        selectItem: () => {
          this.selectItem(child.props.value);
        }
      });
    });
    return <Carousel>{childrenWithProps}</Carousel>;
  }
}
