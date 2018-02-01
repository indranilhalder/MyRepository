import { configure } from "enzyme";
import Adapter from "enzyme-adapter-react-16";

configure({ adapter: new Adapter() });

window.matchMedia = query => {
  const queryMap = {
    "(min-device-width: 1025px)": () => window.innerWidth >= 1025,
    "(max-device-width:1024px)": () => window.innerWidth <= 1024
  };

  const queryValue = queryMap[query];
  const matches = queryValue ? queryValue() : false;
  return {
    matches,
    addListener: () => {},
    removeListener: () => {}
  };
};
