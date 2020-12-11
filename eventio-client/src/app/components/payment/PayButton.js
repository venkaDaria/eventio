import React, { Component } from "react";
import PropTypes from "prop-types";

import $ from "jquery";

import { templateAjaxGet } from "../../utils/api";
import { baseUrl } from "../../utils/core";

class PayButton extends Component {
  state = { form: "" };

  componentDidMount() {
      $.get(templateAjaxGet(`/payment-form?amount=${this.props.amount}` +
      `&text=${this.props.text}` +
      `&resultUrl=${baseUrl}/profile?reload=true`,
      (response) => this.setState({form: response}), (xhr) =>
          this.props.onError(xhr, this.props.translate("cannot-get-form"))));
  }

  render() {
      return <div dangerouslySetInnerHTML={ {__html: this.state.form} } />;
  }
}

PayButton.propTypes = {
    text: PropTypes.string.isRequired,
    amount: PropTypes.number.isRequired,
    onError: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired
};

export default PayButton;
