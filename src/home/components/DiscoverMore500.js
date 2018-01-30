import React from "react";
import Grid from "../../general/components/Grid";
import CategoryWithName from "../../general/components/CategoryWithName";
import PropTypes from "prop-types";
import styles from "./DiscoverMore500.css";
export default class DiscoverMore500 extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>{this.props.headingText}</div>
        <Grid elementWidthMobile={33.33}>
          {this.props.data &&
            this.props.data.map((datum, i) => {
              return (
                <CategoryWithName
                  image={datum.image}
                  label={datum.label}
                  key={i}
                />
              );
            })}
        </Grid>
      </div>
    );
  }
}
DiscoverMore500.propTypes = {
  headingText: PropTypes.string,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      image: PropTypes.string,
      label: PropTypes.string
    })
  )
};
DiscoverMore500.defaultProps = {
  headingText: "Discover more from Tata Cliq"
};
