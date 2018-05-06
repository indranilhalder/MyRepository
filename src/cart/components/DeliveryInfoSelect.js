import React from "react";
import DeliveryInformations from "../../general/components/DeliveryInformations.js";
export default class DeliveryInfoSelect extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedCode: props.selected
    };
  }
  handleSelect(val) {
    this.setState({ selectedCode: val }, () => {
      if (this.props.onSelect) {
        this.props.onSelect(val);
      }
    });
  }
  componentWillReceiveProps(nextProps) {
    if (nextProps.selected !== this.state.selectedCode) {
      this.setState({ selectedCode: nextProps.selected });
    }
  }
  onPiq = () => {
    this.props.onPiq();
  };
  render() {
    return (
      <div>
        {this.props.deliveryInformation.map((datum, i) => {
          return (
            <DeliveryInformations
              key={i}
              type={datum.code}
              header={datum.name}
              showDeliveryCharge={true}
              placedTime={datum.desc}
              deliveryCharge={datum.deliveryCost}
              selected={datum.code === this.state.selectedCode}
              onSelect={val => this.handleSelect(val)}
              onPiq={val => this.onPiq(val)}
              showCliqAndPiqButton={true}
              available={true}
              isClickable={this.props.isClickable}
            />
          );
        })}
      </div>
    );
  }
}
