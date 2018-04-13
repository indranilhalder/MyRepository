import Widget from "./Widget.js";
import withIntersectionObserver from "../../higherOrderComponents/withIntersectionObserver.js";

console.log("WIDGET");
console.log(Widget);

export default withIntersectionObserver(0.3)(Widget);
