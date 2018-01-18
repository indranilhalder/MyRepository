export function transformData(datum) {
  return {
    image: datum.imageURL,
    title: datum.title,
    description: datum.description,
    price: datum.mrpPrice && datum.mrpPrice.formattedValue,
    discountPrice: datum.discountedPrice && datum.discountedPrice.formattedValue
  };
}
