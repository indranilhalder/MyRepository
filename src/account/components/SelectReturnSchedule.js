import React from "react";
import styles from "./SelectReturnSchedule.css";
import SelectReturnDate from "./SelectReturnDate.js";
import PropTypes from "prop-types";
import GridSelect from "../../general/components/GridSelect";
export default class SelectReturnSchedule extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>{this.props.header}</div>
        <div className={styles.scheduleHolder}>
          <GridSelect limit={1} offset={0} elementWidthMobile={100}>
            {this.props.dateAndTime.map((val, i) => {
              return <SelectReturnDate label={val.date} value={i} />;
            })}
          </GridSelect>
        </div>
      </div>
    );
  }
}
SelectReturnSchedule.PropTypes = {
  header: PropTypes.string,
  dateAndTime: PropTypes.arrayOf(
    PropTypes.shape({
      date: PropTypes.string
    })
  )
};
