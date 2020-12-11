import React, {Component, Fragment} from "react";
import PropTypes from "prop-types";
import SearchBar from "material-ui-search-bar";
import {css} from "aphrodite";

import $ from "jquery";

import SavePlace from "./place/SavePlace";
import PlacesTable from "./place/PlacesTable";

import {templateAjaxGet} from "../../utils/api";
import {onSuccessAlert} from "../../utils/react/alert";
import {sortBy, filterBy} from "../../utils/core/collection";

import stylesLink from "../../../styles/link";

class Places extends Component {
  state = {
      places: [],
      filteredPlaces: [],
      selectedEntity: null,
      searchTerm: ""
  }

  search = (searchTerm) => this.setState({
      searchTerm: searchTerm,
      filteredPlaces: filterBy(this.state.places, searchTerm.toLowerCase())
  });

  onEdit = (entity) => {
      this.setState({ selectedEntity: entity });
  }

  onSuccess = (response, nameMessage) => {
      this.componentDidMount();
      onSuccessAlert(response, this.props.translate(nameMessage));
  }

  sortBy = (sortTerm) => {
      this.search(this.state.searchTerm);

      if (!sortTerm) return;

      this.setState({
          filteredPlaces: sortBy(this.state.filteredPlaces, sortTerm)
      });
  }

  clearForm = () => this.setState({selectedEntity: null});

  componentDidMount() {
      $.get(templateAjaxGet("/legal-person/", (response) =>
          this.setState({ places: response.places, filteredPlaces: response.places, selectedEntity: null }),
      (xhr) => this.props.onError(xhr, this.props.translate("cannot-retrieve-company"))));
  }

  render() {
      return <Fragment>
          <SavePlace entity={ this.state.selectedEntity } onSuccess={ this.onSuccess } { ...this.props } />
          <h6>{this.props.translate("sorted-by")}&nbsp;
              <a className={ css(stylesLink.default) } onClick={ () => this.sortBy("realAddress") }>
                  {this.props.translate("real-address2")}
              </a>,&nbsp;
              <a className={ css(stylesLink.default) } onClick={ () => this.sortBy("name") }>
                  {this.props.translate("name2")}
              </a>,&nbsp;
              <a className={ css(stylesLink.default) } onClick={ () => this.sortBy("timeWork") }>
                  {this.props.translate("time-work2")}
              </a>&nbsp;
              (<a className={ css(stylesLink.default) } onClick={ () => this.sortBy(null) }>
                  {this.props.translate("reset-sorting")}
              </a>)
          </h6>
          <SearchBar onChange={ this.search } onRequestSearch={ this.search } style={ {
              marginBottom: 20
          } }
          />
          <PlacesTable places={ this.state.filteredPlaces } onEdit={ this.onEdit }
              onSuccess={ this.onSuccess } { ...this.props } clearForm={ this.clearForm }/>
      </Fragment>;
  }
}

Places.propTypes = {
    onError: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired
};

export default Places;
