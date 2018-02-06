import React from "react";
import Filter from "./Filter";
import ProductGrid from "./ProductGrid";
import PlpMobileFooter from "./PlpMobileFooter";
import InformationHeader from "../../general/components/InformationHeader";
import styles from "./Plp.css";
export default class Plp extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      filterVisible: false
    };
  }
  toggleFilter = () => {
    this.setState({ filterVisible: !this.state.filterVisible });
  };
  handleBackClick = () => {
    if (this.props.onBack) {
      this.props.onBack();
    }
  };
  onSortClick = () => {
    if (this.props.showSort) {
      this.props.showSort();
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.pageHeader}>
          <InformationHeader
            onClick={this.handleBackClick}
            text="Product listing"
          />
        </div>
        <div className={styles.main}>
          <ProductGrid data={this.props.searchresult} />
        </div>
        <div
          className={
            this.state.filterVisible ? styles.filterOpen : styles.filter
          }
        >
          <InformationHeader onClick={this.toggleFilter} text="Refine by" />
          <Filter filterData={this.props.facetData} />
        </div>
        <div className={styles.footer}>
          <PlpMobileFooter
            onFilter={this.toggleFilter}
            onSort={this.onSortClick}
          />
        </div>
      </div>
    );
  }
}
