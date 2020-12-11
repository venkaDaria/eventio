import React, {Fragment, Component} from "react";
import PropTypes from "prop-types";

import $ from "jquery";

import SignInCustom from "../../google-api/SignInCustom";

import {templateAjaxWithMethod, templateAjaxGet} from "../../../../utils/api";
import {onLog, onSuccessAlert, notFound} from "../../../../utils/react/alert";

class Profile extends Component {
  state = { path: "" };

  static getDerivedStateFromProps(nextProps) {
      return {
          path: nextProps.path
      };
  }

  changeEmail = (credentials) => {
      if (credentials.email === this.props.data.email)
          return;

      this.props.data.email = credentials.email;
      this.savePerson(() => this.props.onChangeEmail(credentials));
  }

  getUser = () => $.get(templateAjaxGet(`${this.state.path}/`, onLog, (xhr) => {
      if (!notFound(xhr.responseJson ? xhr.responseJson.message : xhr.responseJson)) {
          this.props.onError(xhr, this.props.translate("cannot-retrieve"));
      } else {
          onLog(xhr);
      }
  }))

  savePerson = (onSuccess) => $.ajax(templateAjaxWithMethod(this.state.path,
      this.props.data, onSuccess, (xhr) => this.props.onError(xhr,
          this.props.translate("cannot-save")), "PUT"))

  onSuccess = () => this.getUserWithCallback((response) => onSuccessAlert(response,
      this.props.translate("changes-saved"))
  )

  getUserWithCallback = (callback) =>
      this.getUser()
          .then(this.props.setState)
          .then(() => this.savePerson(onLog))
          .then(callback);

  componentDidMount() {
      this.getUserWithCallback();
  }

  render() {
      const reload = document.location.href.split("reload=")[1];
      if (reload === "true") {
          this.props.reLogin();
      }

      return <Fragment>
          <fieldset>
              <label htmlFor="email">{this.props.translate("email")}</label><br/>
              <input id="email" type="email" name="email" value={ this.props.data.email }
                  className="form-control" disabled="disabled"
              />
              <SignInCustom onLoginClick={ this.changeEmail } text={ this.props.translate("change-email") }/>

              {this.props.children}
          </fieldset>

          <a className="btn btn-info formControl" onClick={ () => this.savePerson(this.onSuccess) }>
              {this.props.translate("save")}
          </a>
      </Fragment>;
  }
}

Profile.propTypes = {
    data: PropTypes.object.isRequired,
    path: PropTypes.string.isRequired,
    setState: PropTypes.func.isRequired,
    onChangeEmail: PropTypes.func.isRequired,
    onError: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired,
    reLogin: PropTypes.func.isRequired
};

export default Profile;
