import React from "react";
import GridSelect from "../../general/components/GridSelect";
import EmiCartSelect from "./EmiCartSelect";
import EmiDisplay from "./EmiDisplay";
import CreditCardForm from "./CreditCardForm";
export default class EmiAccordian extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      planSelected: false,
      selectedEmi: "",
      selectedBank: "",
      selectedEmiRate: "",
      selectedPrice: ""
    };
  }
  handleSelectPlan(val) {
    if (val) {
      this.setState({
        selectedEmiRate: val.interestRate,
        selectedEmi: val.term,
        selectedPrice: val.monthlyInstallment
      });
    }
  }
  handleSelectBank(val) {
    const option = this.props.emiList.filter(data => {
      return data.value === val[0];
    })[0].options[0];
    this.setState({
      selectedBank: this.props.emiList.filter(data => {
        return data.value === val[0];
      })[0].title,
      selectedEmiRate: option.interestRate,
      selectedEmi: option.term,
      selectedPrice: option.monthlyInstallment
    });
  }
  handleConfirmPlan() {
    this.setState({ planSelected: true });
  }
  handleChangePlan() {
    this.setState({ planSelected: false });
  }
  render() {
    return (
      <React.Fragment>
        {!this.state.planSelected && (
          <GridSelect
            elementWidthMobile={100}
            offset={0}
            limit={1}
            onSelect={val => {
              this.handleSelectBank(val);
            }}
          >
            {this.props.emiList &&
              this.props.emiList.map((val, i) => {
                return (
                  <EmiCartSelect
                    key={i}
                    value={val.value}
                    title={val.title}
                    options={val.options}
                    selectPlan={val => this.handleSelectPlan(val)}
                    confirmPlan={() => this.handleConfirmPlan()}
                  />
                );
              })}
          </GridSelect>
        )}
        {this.state.planSelected && (
          <React.Fragment>
            <EmiDisplay
              bankName={this.state.selectedBank}
              term={this.state.selectedEmi}
              emiRate={this.state.selectedEmiRate}
              price={this.state.selectedPrice}
              changePlan={() => this.handleChangePlan()}
            />
            <CreditCardForm />
          </React.Fragment>
        )}
      </React.Fragment>
    );
  }
}
