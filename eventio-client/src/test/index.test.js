import { expect } from "chai";

function addTwoNumbers(a, b) {
    return a + b;
}

describe("addTwoNumbers", () => {
    it("should add two numbers", () => {
        expect(addTwoNumbers(2, 3)).to.equal(5);
    });
});
