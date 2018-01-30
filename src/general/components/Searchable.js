import React from "react";
export default function Seachable(Component, ownProps) {
  return class extends React.Component {
    // constructor(props){
    //     super(props);
    //     this.state={

    //     }
    // }
    searchInArray = val => {
      let flag = false;

      val.forEach(item => {
        flag = item.indexOf(this.props.search) !== -1 || flag;
      });
      if (flag) {
        return true;
      } else {
        return false;
      }
    };
    render() {
      const children = this.props.children;
      const passedChildren = React.Children.map(children, (child, i) => {
        if (this.searchInArray(child.props.search) || !this.props.search) {
          return child;
        }
      });
      return <Component {...this.props}>{passedChildren}</Component>;
    }
  };
}
