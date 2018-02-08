import React from "react";
import Grid from "../../general/components/Grid.js";
export default class SellerSelection extends React.Component {
  render() {
    return (
      <Grid elementWidthDesktop={100} elementWidthMobile={100} offset={0}>
        {this.props.children}
      </Grid>
    );
  }
}
