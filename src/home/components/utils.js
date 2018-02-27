export function transformData(datum) {
  return {
    image: datum.imageURL,
    video: datum.video,
    title: datum.title,
    description: datum.description,
    price: datum.mrpPrice && datum.mrpPrice.formattedValue,
    discountPrice: datum.discountedPrice && datum.discountedPrice.formattedValue
  };
}
export function transformItem(datum) {
  return {
    imageURL: datum.imageUrl,
    discountPrice: datum.winningSellerMOP,
    price: datum.mrp,
    title: datum.productName
  };
}
