import React from "react";
import Grid from "../../general/components/Grid";
import ProfileMenu from "./ProfileMenu";
import PropTypes from "prop-types";
import styles from "./ProfileMenuGrid.css";
export default class ProfileMenuGrid extends React.Component {
  render() {
    const data = this.props.data;
    return (
      <div className={styles.base}>
        <Grid elementWidthMobile={33.33}>
          {data.map((datum, i) => {
            return (
              <ProfileMenu image={datum.image} text={datum.text} key={i} />
            );
          })}
        </Grid>
      </div>
    );
  }
}
ProfileMenuGrid.propTypes = {
  image: PropTypes.string,
  text: PropTypes.string
};
