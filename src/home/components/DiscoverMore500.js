import React from "react";
import Grid from "../../general/components/Grid";
import CategoryWithName from "../../general/components/CategoryWithName";
import PropTypes from "prop-types";
import styles from "./DiscoverMore500.css";
export default class DiscoverMore500 extends React.Component {
  render() {
    const componentData = this.props.feedComponentData.data;

    return (
      <div className={styles.base}>
        <div className={styles.header}>
          {this.props.feedComponentData.title}
        </div>
        <Grid elementWidthMobile={33.33} offset={20}>
          {componentData &&
            componentData.data &&
            componentData.data.map((datum, i) => {
              return (
                <CategoryWithName
                  image={datum.imageURL}
                  label={datum.title}
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
  feedComponentData: PropTypes.shape({
    title: PropTypes.string,
    data: PropTypes.shape({
      data: PropTypes.arrayOf(
        PropTypes.shape({
          imageURL: PropTypes.string,
          title: PropTypes.string
        })
      )
    })
  })
};
