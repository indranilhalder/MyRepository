import React from "react";
export default function withMultiSelect(Component, ownProps) {
  return class extends React.Component {
    constructor(props) {
      super(props);
      this.state = {
        selected: this.props.selected ? this.props.selected : []
      };
    }
    selectItem(val, i) {
      let selected = this.state.selected;
      if (selected.includes(val)) {
        selected = selected.filter(label => val !== label);
      } else {
        if (this.props.limit && this.props.limit <= selected.length) {
          selected.shift();
        }

        selected.push(val);
      }
      this.setState({ selected }, () => {
        if (this.props.onSelect) {
          this.props.onSelect(this.state.selected);
        }
      });
    }
    handleApply() {
      if (this.props.onApply) {
        this.props.onApply(this.state.selected);
      }
    }
    componentWillReceiveProps(props) {
      if (props.selected !== this.props.selected) {
        this.setState({ selected: props.selected });
      }
    }
    render() {
      console.log(this.props);
      const children = this.props.children;
      const childrenWithProps = React.Children.map(children, (child, i) => {
        return React.cloneElement(child, {
          selected: this.state.selected.includes(child.props.value),

          selectItem: () => {
            this.selectItem(child.props.value, i);
          }
        });
      });
      return (
        <Component selected={this.state.selected} {...this.props}>
          {childrenWithProps}
        </Component>
      );
    }
  };
}
