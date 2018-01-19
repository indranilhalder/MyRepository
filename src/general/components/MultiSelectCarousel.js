import React from "react";
import Carousel from "./Carousel";
import styles from "./MultiSelectCarousel.css";
export default class MuliSelectCarousel extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selected: this.props.selected ? this.props.selected : []
    };
  }
  selectItem(val) {
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
    return (
      <div className={styles.base}>
        <Carousel
          headerComponent={
            <div className={styles.header}>
              <div className={styles.headerText}>{this.props.header}</div>
              <div className={styles.subheader}>{this.props.subheader}</div>
              {this.state.selected.length > 0 && (
                <div
                  className={styles.button}
                  onClick={() => {
                    this.handleApply();
                  }}
                >
                  Apply
                </div>
              )}
            </div>
          }
        >
          {childrenWithProps}
        </Carousel>
      </div>
    );
  }
}
