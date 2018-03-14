import React from "react";
import map from "lodash/map";
import groupBy from "lodash/groupBy";
import filter from "lodash/filter";
import MDSpinner from "react-md-spinner";
import BrandHeader from "./BrandHeader";
import BrandsCategory from "./BrandsCategory";
import BrandsSubCategory from "./BrandsSubCategory";
import BrandBanner from "./BrandBanner";
import BrandImage from "../../general/components/BrandImage";
import BannerMobile from "../../general/components/BannerMobile";
import Carousel from "../../general/components/Carousel";
import MobileFooter from "../../general/components/MobileFooter";
import BrandsSelect from "./BrandsSelect";
import Input2 from "../../general/components/Input2";
import { Icon } from "xelpmoc-core";
import BrandsItem from "./BrandsItem";
import styles from "./BrandsLandingPageDefault.css";
import arrowIcon from "../../general/components/img/down-arrow.svg";
import searchIcon from "../../general/components/img/Search.svg";
export default class BrandsLandingPageDefault extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showFollowing: false,
      currentActiveBrandType: 0,
      searchBy: null
    };
  }
  componentDidMount() {
    this.props.getAllBrandsStore();
  }
  renderToAnotherURL(webURL) {
    console.log(webURL);
  }
  switchTab(val) {
    this.setState({ currentActiveBrandType: val });
  }
  handleShowFollow() {
    const showFollowing = !this.state.showFollowing;
    this.setState({ showFollowing });
  }
  renderLoader() {
    return (
      <div className={styles.loadingIndicator}>
        <MDSpinner />
      </div>
    );
  }

  render() {
    if (!this.props.brandsStores) {
      return this.renderLoader();
    }

    let brandsStores = this.props.brandsStores[
      this.props.brandsStores.componentName
    ].items;
    let brandList = map(brandsStores, brandName => {
      return brandName.subType;
    });
    let currentActiveHeroBanner =
      brandsStores[this.state.currentActiveBrandType].items[0]
        .heroBannerComponent.items;
    let currentActiveBrandList =
      brandsStores[this.state.currentActiveBrandType].brands;
    if (this.state.searchBy) {
      currentActiveBrandList = filter(currentActiveBrandList, brand => {
        return brand.brandName.toLowerCase().includes(this.state.searchBy);
      });
    }

    currentActiveBrandList = groupBy(currentActiveBrandList, list => {
      return list.brandName[0];
    });
    let parentBrandsLabel = Object.keys(currentActiveBrandList);

    return (
      <div className={styles.base}>
        <div className={styles.header}>
          <BrandHeader text="Brands" />
          <BrandsSelect
            limit={1}
            onSelect={val => this.switchTab(val[0])}
            selected={[]}
          >
            {brandList.map((val, idx) => {
              return <BrandsItem label={val} value={idx} key={idx} />;
            })}
          </BrandsSelect>
        </div>
        {/* we need to show this once api will be work and at that
        time also need some modification in integration */}
        {/* <div className={styles.following}>
          <div
            className={
              this.state.showFollowing
                ? styles.followVisible
                : styles.followingHeader
            }
            onClick={() => this.handleShowFollow()}
          >
            Following Brands
            <div className={styles.arrow}>
              <Icon image={arrowIcon} size={18} />
            </div>
          </div>
          {this.state.showFollowing && (
            <Carousel>
              <BrandImage image="https://i.pinimg.com/originals/96/29/ef/9629efc8a4be4fd1600a9bb3940fd89c.jpg" />
            </Carousel>
          )}
        </div> */}
        <div className={styles.bannerHolder}>
          <BannerMobile bannerHeight="45vw">
            {currentActiveHeroBanner &&
              currentActiveHeroBanner.map(heroBanner => {
                return (
                  <BrandBanner
                    image={heroBanner.imageURL}
                    logo={heroBanner.brandLogo}
                    title={heroBanner.title}
                    onClick={() => this.renderToAnotherURL(heroBanner.webURL)}
                  />
                );
              })}
          </BannerMobile>
        </div>
        <div className={styles.searchInput}>
          <Input2
            placeholder="Search your brand"
            onChange={val => this.setState({ searchBy: val })}
            rightChild={
              <div className={styles.searchIcon}>
                <Icon image={searchIcon} size={15} />
              </div>
            }
            rightChildSize={40}
          />
        </div>
        <div className={styles.following} />
        <div className={styles.category}>
          {parentBrandsLabel &&
            parentBrandsLabel.map((val, i) => {
              return (
                <BrandsCategory index={val} catagory={val} key={i}>
                  {currentActiveBrandList[val] &&
                    currentActiveBrandList[val].map((data, i) => {
                      return (
                        <BrandsSubCategory
                          label={data.brandName}
                          select={data.select}
                          key={i}
                        />
                      );
                    })}
                </BrandsCategory>
              );
            })}
        </div>
        <MobileFooter selected="brands" history={this.props.history} />
      </div>
    );
  }
}
