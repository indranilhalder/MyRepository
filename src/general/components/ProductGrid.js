import React from "react";
import Grid from "./Grid";
import ProductModule from "./ProductModule";
import { Icon } from "xelpmoc-core";
import styles from "./ProductGrid.css";
import gridImage from "./img/grid.svg";
import listImage from "./img/list.svg";
const LIST = "list";
const GRID = "grid";
export default class ProductGrid extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      view: GRID
    };
  }
  switchView() {
    if (this.state.view === LIST) {
      this.setState({ view: GRID });
    } else {
      this.setState({ view: LIST });
    }
  }
  changeAddress() {
    if (this.props.changeAddress) {
      this.props.changeAddress();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          <div className={styles.area}>{this.props.area}</div>
          <div
            className={styles.areaChange}
            onClick={() => this.changeAddress()}
          >
            Change
          </div>
          <div className={styles.icon} onClick={() => this.switchView()}>
            {this.state.view === LIST && <Icon image={gridImage} size={20} />}
            {this.state.view === GRID && <Icon image={listImage} size={20} />}
          </div>
        </div>
        <div className={styles.content}>
          <Grid
            search={this.props.search}
            offset={20}
            elementWidthMobile={this.state.view === LIST ? 100 : 50}
          >
            {this.props.data &&
              this.props.data.map((datum, i) => {
                return (
                  <ProductModule
                    search={datum.search}
                    productImage={datum.productImage}
                    title={datum.title}
                    description={datum.description}
                    view={this.state.view}
                    key={i}
                  />
                );
              })}
          </Grid>
        </div>
      </div>
    );
  }
}

ProductGrid.defaultProps = {
  area: "Delhi - 560345"
};
