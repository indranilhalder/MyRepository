import React from "react";
import SearchableGrid from "./SearchableGrid";
import ProductModule from "./ProductModule";
import { Icon } from "xelpmoc-core";
import styles from "./ProductGrid.css";
import gridImage from "./img/grid.svg";
import listImage from "./img/list.svg";
export default class ProductGrid extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      view: "list"
    };
  }
  switchView() {
    if (this.state.view === "list") {
      this.setState({ view: "grid" });
    } else {
      this.setState({ view: "list" });
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
            {this.state.view === "list" && <Icon image={gridImage} size={20} />}
            {this.state.view === "grid" && <Icon image={listImage} size={20} />}
          </div>
        </div>
        <div className={styles.content}>
          <SearchableGrid
            search={this.props.search}
            offset={20}
            elementWidthMobile={this.state.view === "list" ? 100 : 50}
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
          </SearchableGrid>
        </div>
      </div>
    );
  }
}

ProductGrid.defaultProps = {
  area: "Delhi - 560345"
};
