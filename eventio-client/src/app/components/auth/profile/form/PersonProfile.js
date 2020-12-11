import React, {Component} from "react";
import PropTypes from "prop-types";
import Geosuggest from "react-geosuggest";

import Profile from "./Profile";
import {handleChange} from "../../../../utils/react/state";

class PersonProfile extends Component {
  state = {
      location: "",
      phohe: ""
  };

  changeLocation = (event) => this.setState({location: event});

  selectLocation = (event) => this.setState({location: event && event.label});

  render() {
      return <div>
          <Profile { ...this.props }  reLogin={ this.props.reLogin }
              path={ "/natural-person" } data={ {
                  email: this.props.email,
                  location: this.state.location,
                  phone: this.state.phone
              } } setState={ (res) => this.setState({
                  location: res.location || this.state.location,
                  phone: res.phone || this.state.phone
              }) }>

              <label>{this.props.translate("location")}</label><br/>
              <Geosuggest inputClassName="form-control" onChange={ this.changeLocation }
                  onSuggestSelect={ this.selectLocation } initialValue={ this.state.location }
              />
              <label htmlFor="phone">{this.props.translate("phone")}</label><br/>
              <input name="phone" value={ this.state.phone } onChange={ (e) => handleChange(this, e) }
                  className="form-control" type="telephone"
              /><br/>
          </Profile>
      </div>;
  }
}

PersonProfile.propTypes = {
    email: PropTypes.string.isRequired,
    onChangeEmail: PropTypes.func.isRequired,
    onError: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired,
    reLogin: PropTypes.func.isRequired
};

export default PersonProfile;
