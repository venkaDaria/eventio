import React, {Component} from "react";
import PropTypes from "prop-types";
import {css} from "aphrodite";

import $ from "jquery";

import {ProfileWrapper} from "./form";
import {SwitcherWrapper} from "./switcher";
import {signOut} from "../google-api/SignOut";
import BigCalendar, {toEvent} from "../../utils/CustomBigCalendar";

import {sleep} from "../../../utils/core";
import {templateAjaxGet, templateAjaxWithMethod} from "../../../utils/api";
import {onLog, onConfirmAlert} from "../../../utils/react/alert";
import {loginUser, logoutUser} from "../../../reducers/auth/actions";

import stylesLink from "../../../../styles/link";
import stylesFlex from "../../../../styles/flex";

class Profile extends Component {
  state = {
      isCompany: this.props.isCompany,
      currentLanguage: this.props.currentLanguage,
      email: "",
      events: []
  }

  onChangeLabel = () => this.setState({isCompany: !this.state.isCompany});

  onChangeEmail = (credentials) => {
      if (credentials.email === this.state.email)
          return;
      this.reLogin(credentials);
  };

  reLogin = (credentials) => {
      sleep(1000).then(() => {
          this.props.dispatch(logoutUser());
          this.props.dispatch(loginUser({
              email: credentials.email,
              path: `${this.state.currentLanguage}/profile`
          }));
      });
  }

  removeProfile = () => onConfirmAlert(this.props.translate, {
      message: this.props.translate("delete-account-confirm"),
      onConfirm: () => $.ajax(templateAjaxWithMethod("/person/", {}, () => {
          signOut(() => this.props.dispatch(logoutUser()));
      }, (xhr) => this.props.onError(xhr, this.props.translate("cannot-delete-profile")), "DELETE"))
  });

  selectEvent = (event) => document.location.href = `./event/${event.id}`

  componentDidMount() {
      $.get(templateAjaxGet("/person/label", onLog, (xhr) =>
          this.props.onError(xhr, this.props.translate("cannot-retrieve-profile"))))
          .then((res) => this.setState({
              isCompany: res.company,
              email: res.email,
              events: res.events.map(toEvent)
          }), () => onLog(this.state.events));
  }

  render() {
      return <div className={ css(stylesFlex.container) }>
          <div className={ css(stylesFlex.child) }>
              <SwitcherWrapper { ...this.props } isCompany={ this.state.isCompany }
                  onSuccess={ this.onChangeLabel }
              />
              <ProfileWrapper { ...this.props } isCompany={ this.state.isCompany }
                  email={ this.state.email } onChangeEmail={ this.onChangeEmail }
                  reLogin={ this.reLogin }
              />
              <br/>
              <a onClick={ this.removeProfile } className={ css(stylesLink.default) }>
                  {this.props.translate("delete-account")}
              </a>
          </div>

          <BigCalendar
              culture={ this.state.currentLanguage }
              events={ this.state.events }
              onSelectEvent={ this.selectEvent }
          />
      </div>;
  }
}

Profile.propTypes = {
    currentLanguage: PropTypes.string.isRequired,
    dispatch: PropTypes.func.isRequired,
    isCompany: PropTypes.bool.isRequired,
    onError: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired
};

export default Profile;
