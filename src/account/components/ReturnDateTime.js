import React from "react";
import ReturnsFrame from "./ReturnsFrame";
import SelectReturnDate from "./SelectReturnDate";
import OrderReturnAddressDetails from "./OrderReturnAddressDetails";
import PropTypes from "prop-types";
import styles from "./ReturnDateTime.css";
const PICK_UP_TIME = "Select Pick Up Time";
const PICK_UP_DATE = "Select Pick Up DATE";
export default class ReturnDateTime extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedDate: this.props.selectedDate ? this.props.selectedDate : null,
      selectedTime: this.props.selectedTime ? this.props.selectedTime : null
    };
  }
  handleCancel() {
    if (this.props.onCancel) {
      this.props.onCancel();
    }
  }
  handleDateSelect(val) {
    this.setState({ selectedDate: val }, () => {
      if (this.props.onDateSelect) {
        this.props.onDateSelect(val);
      }
    });
  }
  handleTimeSelect(val) {
    this.setState({ selectedTime: val }, () => {
      if (this.props.onTimeSelect) {
        this.props.onTimeSelect(val);
      }
    });
  }

  render() {
    let header = this.state.selectedDate ? PICK_UP_TIME : PICK_UP_DATE;
    return (
      <ReturnsFrame headerText={header} onCancel={() => this.handleCancel()}>
        <div className={styles.cardOffset}>
          <div className={styles.content}>
            <OrderReturnAddressDetails
              isSelect={true}
              addressType={this.props.selectedAddress.addressType}
              address={this.props.selectedAddress.line1}
              subAddress={`${this.props.selectedAddress.state} ${
                this.props.selectedAddress.city
              } ${this.props.selectedAddress.postalCode}`}
            />
          </div>
        </div>
        <div className={styles.cardOffset}>
          <div className={styles.header}>Select return date</div>
          {this.props.dateSlot.map(val => {
            return (
              <SelectReturnDate
                label={val}
                selectItem={() => {
                  this.handleDateSelect(val);
                }}
                selected={val === this.state.selectedDate}
              />
            );
          })}
        </div>

        <div className={styles.card}>
          <div className={styles.header}>Select return time</div>
          {this.state.selectedDate &&
            this.props.timeSlot.map(val => {
              return (
                <SelectReturnDate
                  label={val}
                  selected={val === this.state.selectedTime}
                  selectItem={() => {
                    this.handleTimeSelect(val);
                  }}
                />
              );
            })}
        </div>
      </ReturnsFrame>
    );
  }
}
ReturnDateTime.propTypes = {
  onCancel: PropTypes.func,
  onDateSelect: PropTypes.func,
  onTimeSelect: PropTypes.func,
  selectedDate: PropTypes.string,
  selectedTime: PropTypes.string
};
