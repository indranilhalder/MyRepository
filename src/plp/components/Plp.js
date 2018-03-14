import React from "react";
import FilterContainer from "../containers/FilterContainer";
import ProductGrid from "./ProductGrid";
import PlpMobileFooter from "./PlpMobileFooter";
import InformationHeader from "../../general/components/InformationHeader";
import styles from "./Plp.css";
import MDSpinner from "react-md-spinner";

export default class Plp extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showFilter: props.showFilter
    };
  }

  toggleFilter = () => {
    this.setState({ showFilter: !this.state.showFilter });
  };

  componentWillReceiveProps(nextProps) {
    this.setState({ showFilter: nextProps.showFilter });
  }
  onApply = val => {
    this.toggleFilter();
    if (this.props.onApply) {
      this.props.onApply(val);
    }
  };
  handleBackClick = () => {
    this.props.history.goBack();
    if (this.props.onBack) {
      this.props.onBack();
    }
  };
  backPage() {
    this.setState({ showFilter: !this.state.showFilter });
  }
  onSortClick = () => {
    if (this.props.showSort) {
      this.props.showSort();
    }
  };

  renderLoader() {
    return (
      <div>
        <MDSpinner />
      </div>
    );
  }

  render() {
    let filterClass = styles.filter;

    if (this.props.loading && !this.props.isFilter) {
      return this.renderLoader();
    }
    if (this.state.showFilter) {
      filterClass = styles.filterOpen;
    }

    return (
      this.props.productListings && (
        <div className={styles.base}>
          <div className={styles.pageHeader}>
            <InformationHeader
              onClick={this.handleBackClick}
              text="Product listing"
            />
          </div>
          <div className={styles.main}>
            <ProductGrid
              history={this.props.history}
              data={this.props.productListings.searchresult}
            />
          </div>
          <div className={filterClass}>
            <FilterContainer backPage={() => this.backPage()} />
          </div>
          <div className={styles.footer}>
            <PlpMobileFooter
              onFilter={this.toggleFilter}
              onSort={this.onSortClick}
            />
          </div>
        </div>
      )
    );
  }
}
