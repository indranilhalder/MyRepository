import React from "react";
import Carousel from "./Carousel";
export default class MuliSelectCarousel extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selected: this.props.selected ? this.props.selected : []
    };
  }
  selectItem(val) {
    let selected = [];
    selected.push(val);
    this.setState({ selected });
  }
  render() {
    const children = this.props.children;
    const childrenWithProps = React.Children.map(children, (child, i) => {
      return React.cloneElement(child, {
        selected: this.state.selected.includes(child.props.value),

        selectItem: () => {
          this.selectItem(child.props.value);
        }
      });
    });
    return <Carousel>{childrenWithProps}</Carousel>;
  }
}
