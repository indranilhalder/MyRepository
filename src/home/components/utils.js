export function transformData(datum) {
  return {
    image: datum.imageURL,
    title: datum.title,
    description: datum.title,
    price: datum.mrpPrice && datum.mrpPrice.formattedValue,
    discountedPrice:
      datum.discountedPrice && datum.discountedPrice.formattedValue
  };
}
