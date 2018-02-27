export function transformData(datum) {
  return {
    image: datum.imageUrl,
    video: datum.video,
    title: datum.title,
    description: datum.description,
    price: datum.mrp,
    discountPrice: datum.winningSellerMOP
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
