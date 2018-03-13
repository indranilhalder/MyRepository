import React from "react";
import BrandsCategory from "./BrandsCategory";
import BrandsSubCategory from "./BrandsSubCategory";
import BrandBanner from "./BrandBanner";
import BrandImage from "../../general/components/BrandImage";
import BannerMobile from "../../general/components/BannerMobile";
import Carousel from "../../general/components/Carousel";
import MobileFooter from "../../general/components/MobileFooter";
import BrandsSelect from "./BrandsSelect";
import BrandHeader from "./BrandHeader";
import { Icon } from "xelpmoc-core";
import BrandsItem from "./BrandsItem";
import styles from "./BrandsLandingPageDefault.css";
import arrowIcon from "../../general/components/img/down-arrow.svg";
export default class BrandsLandingPageDefault extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showFollowing: false
    };
  }
  switchTab(val) {
    console.log(val);
  }
  handleShowFollow() {
    const showFollowing = !this.state.showFollowing;
    this.setState({ showFollowing });
  }
  render() {
    const catagory = [
      {
        index: "1",
        catagory: "109 F",
        subCatagory: [{ list: "11cent" }]
      },
      {
        index: "9",
        catagory: "9rasa",
        subCatagory: [{ list: "9teenAGAIN", select: true }]
      },
      {
        index: "A",
        catagory: "AND",
        subCatagory: [
          { list: "Amante" },
          { list: "Avengers", select: true },
          { list: "Arkham Asylum" }
        ]
      }
    ];
    const brandList = [
      { brands: "Womens" },
      { brands: "Mens" },
      { brands: "Kids" },
      { brands: "Child" },
      { brands: "House Wife" },
      { brands: "Party Wear" }
    ];
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          <BrandHeader />
          <BrandsSelect
            limit={1}
            onSelect={val => this.switchTab(val)}
            selected={[]}
          >
            {brandList.map((val, i) => {
              return <BrandsItem label={val.brands} value={val.brands} />;
            })}
          </BrandsSelect>
        </div>
        <div className={styles.following}>
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
              <BrandImage image="https://i.pinimg.com/originals/96/29/ef/9629efc8a4be4fd1600a9bb3940fd89c.jpg" />
              <BrandImage image="https://i.pinimg.com/originals/96/29/ef/9629efc8a4be4fd1600a9bb3940fd89c.jpg" />
              <BrandImage image="https://i.pinimg.com/originals/96/29/ef/9629efc8a4be4fd1600a9bb3940fd89c.jpg" />
              <BrandImage image="https://i.pinimg.com/originals/96/29/ef/9629efc8a4be4fd1600a9bb3940fd89c.jpg" />
              <BrandImage image="https://i.pinimg.com/originals/96/29/ef/9629efc8a4be4fd1600a9bb3940fd89c.jpg" />
              <BrandImage image="https://i.pinimg.com/originals/96/29/ef/9629efc8a4be4fd1600a9bb3940fd89c.jpg" />
            </Carousel>
          )}
        </div>
        <div className={styles.bannerHolder}>
          <BannerMobile bannerHeight="45vw">
            <BrandBanner
              image="https://dtpmhvbsmffsz.cloudfront.net/brands/2017/06/21/53d96e455632a02800000012/m_594ab8fc5632a002ab00023e.jpg"
              logo="https://i.pinimg.com/originals/96/29/ef/9629efc8a4be4fd1600a9bb3940fd89c.jpg"
            />
            <BrandBanner
              image="https://dtpmhvbsmffsz.cloudfront.net/brands/2017/06/21/53d96e455632a02800000012/m_594ab8fc5632a002ab00023e.jpg"
              logo="https://i.pinimg.com/originals/96/29/ef/9629efc8a4be4fd1600a9bb3940fd89c.jpg"
            />
            <BrandBanner
              image="https://dtpmhvbsmffsz.cloudfront.net/brands/2017/06/21/53d96e455632a02800000012/m_594ab8fc5632a002ab00023e.jpg"
              logo="https://i.pinimg.com/originals/96/29/ef/9629efc8a4be4fd1600a9bb3940fd89c.jpg"
            />
          </BannerMobile>
        </div>
        <div className={styles.following} />
        <div className={styles.category}>
          {catagory.map((val, i) => {
            return (
              <BrandsCategory index={val.index} catagory={val.catagory}>
                {val.subCatagory &&
                  val.subCatagory.map((data, i) => {
                    return (
                      <BrandsSubCategory
                        label={data.list}
                        select={data.select}
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
