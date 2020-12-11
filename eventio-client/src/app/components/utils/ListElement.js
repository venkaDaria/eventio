import React, {Fragment, Component} from "react";
import PropTypes from "prop-types";
import SearchBar from "material-ui-search-bar";

import {css} from "aphrodite";
import $ from "jquery";
import hash from "object-hash";

import CustomDatePicker from "./CustomDatePicker";
import EventInfo from "../events/EventInfo";

import {filterBy, complexFilterBy, filterByDate, getPairs} from "../../utils/core/collection";
import {templateAjaxGet} from "../../utils/api";
import {getData, clearData} from "../../utils/core/storage";

import styles from "../../../styles/card";

class ListElement extends Component {
  state = {
      entities: [],
      filteredEntities: [],
      isChecked: false,
      searchTerm: "",
      searchDate: undefined
  }

  search = () => {
      const searchTerm = this.state.searchTerm;

      if (!searchTerm) {
          return;
      }

      if (!searchTerm.startsWith("!")) {
          this.simpleSearch(searchTerm);
          return;
      }

      this.setState({
          filteredEntities: this.filterBy(this.state.entities, searchTerm)
      });
  }

  filterBy = (entities, searchTerm) => complexFilterBy(entities, getPairs(searchTerm, "!", " ", ":"));

  simpleSearch = (searchTerm) => {
      searchTerm = searchTerm.toLowerCase();

      this.setState({
          filteredEntities: filterBy(this.state.entities, searchTerm),
          searchTerm: searchTerm
      });
  }

  onDateChange = (searchDate) => this.setState({
      filteredEntities: filterByDate(this.state.entities, searchDate),
      searchDate: searchDate
  });

  onClear = (name) => {
      clearData(name);
      this.forceUpdate();
  }

  handleChangeFilter = () => this.setState({isChecked: !this.state.isChecked});

  componentDidMount() {
      $.get(templateAjaxGet(
          `${this.props.requestPath}locationFilter=${this.state.isChecked}`,
          (response) => this.setState({entities: response, filteredEntities: response}),
          (xhr) => this.props.onError(xhr, this.props.translate("cannot-retrieve-entities")))
      );
  }

  render() {
      const eventData = getData("eventData");

      return <Fragment>
          <span>
              {`${this.props.translate("example-complex-query")}: `}
              <span className={ css(styles.grey) }>
                  {`!title:[title] feature:[feature] (${this.props.translate("mandatory-starts-with")})`}
              </span>
          </span>
          <SearchBar onChange={ this.simpleSearch }
              onRequestSearch={ this.search } style={ {
                  marginBottom: 20
              } }
          />
          <CustomDatePicker hidden={ !this.props.dateSearchBar } value={ this.state.searchDate }
              onChange={ this.onDateChange } translate={ this.props.translate } disabled={ false }
          />
          <br/>
          <div hidden={ this.props.isCompany !== false } className="form-check">
              <input className="form-check-input" type="checkbox" id="check"
                  value={ this.state.isChecked } onChange={ this.handleChangeFilter }
              />
              <label className="form-check-label" htmlFor="check">
                &nbsp;{this.props.translate("location-filter")}
              </label>
          </div>
          <div className={ css(styles.scroll) }>
              <EventInfo translate={ this.props.translate } clear={ this.onClear }
                  currentLanguage={ this.props.currentLanguage }/>
              {
                  this.state.filteredEntities.map((entity) =>
                      <this.props.card key={ hash(entity) } entity={ entity }
                          translate={ this.props.translate } onError={ this.props.onError }
                          savedEvent={ eventData } currentLanguage={ this.props.currentLanguage }
                      />
                  )
              }
          </div>
      </Fragment>;
  }
}

ListElement.propTypes = {
    isCompany: PropTypes.bool,
    requestPath: PropTypes.string.isRequired,
    currentLanguage: PropTypes.string.isRequired,
    card: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired,
    onError: PropTypes.func.isRequired,
    dateSearchBar: PropTypes.bool
};

export default ListElement;
