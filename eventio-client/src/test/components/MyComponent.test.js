import React, { Component } from "react";
import { expect } from "chai";
import Enzyme, { shallow } from "enzyme";
import Adapter from "enzyme-adapter-react-16";

Enzyme.configure({ adapter: new Adapter() });

class MyComponent extends Component {
  state = {
      counter: 0,
  };

  render() {
      return (
          <div>
              {this.state.counter}
              <button onClick={ () => this.setState({ counter: this.state.counter + 1 }) } />
          </div>
      );
  }
}

describe("MyComponent", () => {
    it("should increment the counter when the button is pressed", () => {
        const wrapper = shallow(<MyComponent />);

        expect(wrapper.text()).to.contain(0);
        wrapper.find("button").simulate("click");
        expect(wrapper.text()).to.contain(1);
    });
});
