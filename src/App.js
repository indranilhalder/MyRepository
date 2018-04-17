import React, { Component } from "react";
import { default as AppStyles } from "./App.css";
import FaqPage from "./staticpage/components/FaqPage";
class App extends Component {
  testClick(val) {
    console.log(val);
  }
  render() {
    return (
      <div style={{ background: "#ebeced" }}>
        <FaqPage
          items={[
            {
              cmsParagraphComponent: {
                content: "Frequently Asked Question",
                type: "CMS Paragraph Component"
              },
              componentName: "cmsParagraphComponent"
            },
            {
              accountNavigationComponent: {
                nodeList: [
                  {
                    linkName: "Contact Us",
                    url: "https://uat2.tataunistore.com/contact"
                  },
                  {
                    linkName: "Frequently Asked Questions",
                    url: "https://uat2.tataunistore.com/faq"
                  },
                  {
                    linkName: "Track Orders",
                    url: "https://uat2.tataunistore.com/trackorders"
                  },
                  {
                    linkName: "Shipping",
                    url: "https://uat2.tataunistore.com/shipping"
                  },
                  {
                    linkName: "Cancellation",
                    url: "https://uat2.tataunistore.com/cancellation"
                  },
                  {
                    linkName: "Returns",
                    url: "https://uat2.tataunistore.com/returns"
                  },
                  {
                    linkName: "CLiQ And PIQ",
                    url: "https://uat2.tataunistore.com/faq"
                  }
                ],
                type: "Account Navigation Component"
              },
              componentName: "accountNavigationComponent"
            },
            {
              cmsTextComponent: {
                content:
                  '[{ "question_component": "How do I <b>pay</b>?", "answer": "Use your debit card" }, { "question_component": "Now you use a table", "answer": "<table> <tr> <th>Name</th> <th>Favorite Color</th> </tr> <tr> <td>Bob</td> <td>Yellow</td> </tr> <tr> <td>Michelle</td> <td>Purple</td> </tr> </table>Read more: https://html.com/tables/#ixzz5CuICUAFY" }, { "question_component": "Is shipping free?", "answer": "Depends on the product" }]',
                type: "CMS Text Component"
              },
              componentName: "cmsTextComponent"
            }
          ]}
        />
      </div>
    );
  }
}

export default App;
