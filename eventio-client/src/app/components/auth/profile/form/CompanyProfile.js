import React, {Component} from "react";
import PropTypes from "prop-types";
import {Link} from "react-router";

import {css} from "aphrodite";

import Profile from "./Profile";
import {handleChange} from "../../../../utils/react/state";
import stylesLink from "../../../../../styles/link";

class CompanyProfile extends Component {
  state = {
      name: "",
      info: ""
  };

  render() {
      return <div>
          <Profile { ...this.props } reLogin={ this.props.reLogin }
              path={ "/legal-person" } data={ {
                  email: this.props.email,
                  name: this.state.name,
                  info: this.state.info
              } } setState={ (res) => this.setState({
                  name: res.name || this.state.name,
                  info: res.info || this.state.info
              }) }>

              <label htmlFor="name">{this.props.translate("name")}</label><br/>
              <input name="name" value={ this.state.name } onChange={ (e) => handleChange(this, e) }
                  className="form-control"
              />
              <label htmlFor="url">{this.props.translate("url")}</label><br/>
              <input name="url" value={ `company-${this.state.name}` }
                  className="form-control" disabled
              /><br/>
              <label htmlFor="info">{this.props.translate("info")}</label><br/>
              <input name="info" value={ this.state.info } onChange={ (e) => handleChange(this, e) }
                  className="form-control"
              /><br/>
          </Profile>

      &nbsp;&nbsp;&nbsp;
          <Link to={ `/${this.props.currentLanguage}/profile/places` } className={ css(stylesLink.default) }>
              {this.props.translate("my-places")}
          </Link>
      </div>;
  }
}

CompanyProfile.propTypes = {
    email: PropTypes.string.isRequired,
    currentLanguage: PropTypes.string.isRequired,
    onChangeEmail: PropTypes.func.isRequired,
    onError: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired,
    reLogin: PropTypes.func.isRequired
};

export default CompanyProfile;
