import React from "react";
import styles from "./BrandsTotal.css";
import BrandCardHeader from "./BrandCardHeader.js";
import AllBrandTypes from "./AllBrandTypes.js";
import LatestCollections from "./LatestCollections.js";
import ProductModule from "../../general/components/ProductModule.js";
import Carousel from "../../general/components/Carousel";
import StoresLocationBanner from "./StoresLocationBanner.js";
import ShopCollection from "../../pdp/components/ShopCollection.js";
import MobileFooterBase from "../../general/components/MobileFooter.js";
import Question from "./Question.js";
export default class BrandsTotal extends React.Component {
  onClickFollow() {
    console.log("test");
  }
  onClickUnfollow() {
    console.log("click follow");
  }
  arrowNextClick() {
    console.log("here");
  }
  seeAllSeller() {
    console.log("see all seller");
  }
  shopeRange() {
    console.log("shope");
  }
  brandsItem(val) {
    console.log(val);
  }
  componentDidMount() {
    this.props.getBrandDetails();
  }
  render() {
    const storeLocation = "adidas® stores near you";
    const data = [
      {
        image:
          "https://sits-pod14-adidas.demandware.net/dw/image/v2/aagl_prd/on/demandware.static/-/Sites-adidas-AME-Library/default/dwa17d4964/brand/images/2018/01/originals-ss18-eqt-bb-hp-mh-d_233727.jpg?sw=1366&sh=830&sm=fit&cx=0&cy=0&cw=1366&ch=831&sfrm=jpg",
        title: "adidas®",
        description: "Originals 3-Stripes Leggings",
        price: "1,295"
      },
      {
        image:
          "https://sits-pod14-adidas.demandware.net/dw/image/v2/aagl_prd/on/demandware.static/-/Sites-adidas-AME-Library/default/dwa17d4964/brand/images/2018/01/originals-ss18-eqt-bb-hp-mh-d_233727.jpg?sw=1366&sh=830&sm=fit&cx=0&cy=0&cw=1366&ch=831&sfrm=jpg",
        title: "adidas®",
        description: "Originals 3-Stripes Leggings",
        price: "1,295"
      },
      {
        image:
          "https://sits-pod14-adidas.demandware.net/dw/image/v2/aagl_prd/on/demandware.static/-/Sites-adidas-AME-Library/default/dwa17d4964/brand/images/2018/01/originals-ss18-eqt-bb-hp-mh-d_233727.jpg?sw=1366&sh=830&sm=fit&cx=0&cy=0&cw=1366&ch=831&sfrm=jpg",
        title: "adidas®",
        description: "Originals 3-Stripes Leggings",
        price: "1,295"
      },
      {
        image:
          "https://sits-pod14-adidas.demandware.net/dw/image/v2/aagl_prd/on/demandware.static/-/Sites-adidas-AME-Library/default/dwa17d4964/brand/images/2018/01/originals-ss18-eqt-bb-hp-mh-d_233727.jpg?sw=1366&sh=830&sm=fit&cx=0&cy=0&cw=1366&ch=831&sfrm=jpg",
        title: "adidas®",
        description: "Originals 3-Stripes Leggings",
        price: "1,295"
      }
    ];
    const data1 = [
      {
        image:
          "https://i2.keller-sports.com/shop/products/1000x1000/475848C11564DAA41C574325CF94D3BC.jpg",
        title: "adidas®",
        description: "Barricade Tank Top by Stella McCartney",
        price: "1,295"
      },
      {
        image: "https://i1.adis.ws/i/jpl/jd_261600_a?qlt=80&w=600&h=765&v=1",
        title: "adidas®",
        description: "Originals 3-Stripes Leggings",
        price: "1,295"
      },
      {
        image:
          "https://sits-pod14-adidas.demandware.net/dw/image/v2/aagl_prd/on/demandware.static/-/Sites-adidas-AME-Library/default/dwa17d4964/brand/images/2018/01/originals-ss18-eqt-bb-hp-mh-d_233727.jpg?sw=1366&sh=830&sm=fit&cx=0&cy=0&cw=1366&ch=831&sfrm=jpg",
        title: "adidas®",
        description: "Originals 3-Stripes Leggings",
        price: "1,295"
      },
      {
        image:
          "https://sits-pod14-adidas.demandware.net/dw/image/v2/aagl_prd/on/demandware.static/-/Sites-adidas-AME-Library/default/dwa17d4964/brand/images/2018/01/originals-ss18-eqt-bb-hp-mh-d_233727.jpg?sw=1366&sh=830&sm=fit&cx=0&cy=0&cw=1366&ch=831&sfrm=jpg",
        title: "adidas®",
        description: "Originals 3-Stripes Leggings",
        price: "1,295"
      }
    ];
    const data2 = [
      {
        image:
          "https://i2.keller-sports.com/shop/products/1000x1000/475848C11564DAA41C574325CF94D3BC.jpg",
        title: "adidas®",
        description: "Barricade Tank Top by Stella McCartney",
        price: "1,295"
      },
      {
        image: "https://i1.adis.ws/i/jpl/jd_261600_a?qlt=80&w=600&h=765&v=1",
        title: "adidas®",
        description: "Originals 3-Stripes Leggings",
        price: "1,295"
      },
      {
        image:
          "https://sits-pod14-adidas.demandware.net/dw/image/v2/aagl_prd/on/demandware.static/-/Sites-adidas-AME-Library/default/dwa17d4964/brand/images/2018/01/originals-ss18-eqt-bb-hp-mh-d_233727.jpg?sw=1366&sh=830&sm=fit&cx=0&cy=0&cw=1366&ch=831&sfrm=jpg",
        title: "adidas®",
        description: "Originals 3-Stripes Leggings",
        price: "1,295"
      },
      {
        image:
          "https://sits-pod14-adidas.demandware.net/dw/image/v2/aagl_prd/on/demandware.static/-/Sites-adidas-AME-Library/default/dwa17d4964/brand/images/2018/01/originals-ss18-eqt-bb-hp-mh-d_233727.jpg?sw=1366&sh=830&sm=fit&cx=0&cy=0&cw=1366&ch=831&sfrm=jpg",
        title: "adidas®",
        description: "Originals 3-Stripes Leggings",
        price: "1,295"
      }
    ];
    console.log(this.props);
    return (
      <div className={styles.base}>
        <div className={styles.bannerHeader}>
          <BrandCardHeader
            onClickFollow={() => this.onClickFollow()}
            onClickUnfollow={() => this.onClickUnfollow()}
            image={
              this.props.brandDetails &&
              this.props.brandDetails.heroSection.backgroundImage
            }
            text={
              this.props.brandDetails &&
              this.props.brandDetails.heroSection.description
            }
            buttonLabel="Unfollow"
            logo={
              this.props.brandDetails &&
              this.props.brandDetails.heroSection.logoImage
            }
          />
        </div>
        <div className={styles.brandsList}>
          <AllBrandTypes
            shopeName="Shop at Adidas"
            brandsItem={val => this.brandsItem(val)}
            brandsTypeList={[
              {
                brandsType: "Womens",
                typeList: [
                  { subList: "Accessories" },
                  { subList: "Coats, Jackets" },
                  { subList: "Hats" },
                  { subList: "Joggers, Leggings" },
                  { subList: "Sneakers, Canvas, Boots" },
                  { subList: "Shorts" },
                  { subList: "Swimwear" },
                  { subList: "T-Shirts" }
                ]
              },
              {
                brandsType: "Mens",
                typeList: [
                  { subList: "Accessories" },
                  { subList: "Coats, Jackets" },
                  { subList: "Hats" },
                  { subList: "Joggers, Leggings" },
                  { subList: "Sneakers, Canvas, Boots" }
                ]
              },
              {
                brandsType: "Kids"
              },
              {
                brandsType: "Lifestyle",
                typeList: [
                  { subList: "Accessories" },
                  { subList: "Coats, Jackets" },
                  { subList: "Hats" },
                  { subList: "Joggers, Leggings" },
                  { subList: "Sneakers, Canvas, Boots" }
                ]
              }
            ]}
          />
        </div>
        <LatestCollections
          arrowNextClick={() => this.arrowNextClick()}
          heading="Shop latest collections from Adidas"
        />
        <div className={styles.productDetailsHolder}>
          <Carousel
            header="Recently viewed adidas®"
            withFooter={true}
            buttonText="See all"
            seeAll={() => this.seeAllSeller()}
          >
            {data &&
              data.map((datum, i) => {
                return (
                  <ProductModule
                    key={i}
                    isWhite={false}
                    productImage={datum.image}
                    title={datum.title}
                    price={datum.price}
                    description={datum.description}
                    onDownload={datum.onDownload}
                  />
                );
              })}
          </Carousel>
        </div>
        <div className={styles.locationHolder}>
          <StoresLocationBanner
            storeHeader="adidas® stores near you"
            brandLocation={[
              {
                headingText: "Andheri West",
                label: "1.2km away",
                image:
                  "http://tong.visitkorea.or.kr/cms/resource/58/1016958_image2_1.jpg"
              },
              {
                headingText: "Navi Mumbai",
                label: "1.9km away",
                image:
                  "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHaY_H8IY79o5Es16qSQA7Q8X6qYBk_z6iZArZn1BOGjS0AchT"
              },
              {
                headingText: "Prithvi Theatre",
                label: "1.8km away",
                image:
                  "http://tong.visitkorea.or.kr/cms/resource/58/1016958_image2_1.jpg"
              },
              {
                headingText: "Navi Mumbai",
                label: "1.9km away",
                image:
                  "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHaY_H8IY79o5Es16qSQA7Q8X6qYBk_z6iZArZn1BOGjS0AchT"
              },
              {
                headingText: "Prithvi Theatre",
                label: "1.8km away",
                image:
                  "http://tong.visitkorea.or.kr/cms/resource/58/1016958_image2_1.jpg"
              }
            ]}
          />
        </div>
        <div className={styles.productSellerHolder}>
          <Carousel
            header="Best sellers"
            withFooter={true}
            buttonText="See all"
            seeAll={() => this.seeAllSeller()}
          >
            {data1 &&
              data1.map((datum, i) => {
                return (
                  <ProductModule
                    key={i}
                    isWhite={false}
                    productImage={datum.image}
                    title={datum.title}
                    price={datum.price}
                    description={datum.description}
                    onDownload={datum.onDownload}
                  />
                );
              })}
          </Carousel>
        </div>
        <div className={styles.shopeRangeHolder}>
          <div className={styles.shopeRangeHedaer}>
            All new Stealla McCartney
          </div>
          <ShopCollection
            image="https://outdoorswimmer.com/assets/site/_1440xAUTO_crop_center-center/DIY-10k-training-camp-main-image.jpg"
            title="A seamless swim. Own the water."
            btnText="Shop the range"
            onClick={() => this.shopeRange()}
          />
        </div>
        <div className={styles.newArrivalHolder}>
          <Carousel
            header="New arrivals"
            withFooter={true}
            buttonText="See all"
            seeAll={() => this.seeAllSeller()}
          >
            {data2 &&
              data2.map((datum, i) => {
                return (
                  <ProductModule
                    key={i}
                    isWhite={false}
                    productImage={datum.image}
                    title={datum.title}
                    price={datum.price}
                    description={datum.description}
                    onDownload={datum.onDownload}
                  />
                );
              })}
          </Carousel>
        </div>
        <div className={styles.questionHolder}>
          <Question
            options={[{ label: "Yes, sure" }, { label: "No thanks" }]}
          />
        </div>
        <div className={styles.footer}>
          <MobileFooterBase />
        </div>
      </div>
    );
  }
}
