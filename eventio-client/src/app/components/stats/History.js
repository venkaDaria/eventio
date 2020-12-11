import React, {Fragment, Component} from "react";
import PropTypes from "prop-types";
import SearchBar from "material-ui-search-bar";

import $ from "jquery";

import ListTable from "./ListTable";

import {templateAjaxGet} from "../../utils/api";
import {removeEvent, unsubscribeEvent} from "../../utils/api/events";
import {onSuccessAlert, onConfirmAlert} from "../../utils/react/alert";
import {isNotEmpty} from "../../utils/core/";
import {filterByMap} from "../../utils/core/collection";

class History extends Component {
  state = {
      createdEvents: {},
      filteredCreatedEvents: {},
      subscribedEvents: {},
      filteredSubscribedEvents: {},
      events: {},
      filteredEvents: {},
      searchTerm: ""
  }

  search = (searchTerm) => this.setState({
      filteredCreatedEvents: filterByMap(this.state.createdEvents, searchTerm),
      filteredSubscribedEvents: filterByMap(this.state.subscribedEvents, searchTerm),
      filteredEvents: filterByMap(this.state.events, searchTerm)
  });

  sortBy = (events, sortFunctionBy) => {
      this.search(this.state.searchTerm);

      if (!sortFunctionBy) {
          return;
      }

      this.setState({
          filteredEvents: this.state.filteredEvents,
          filteredSubscribedEvents: this.state.filteredSubscribedEvents,
          filteredCreatedEvents: this.state.filteredCreatedEvents,
          [events]: sortFunctionBy(this.state[events]),
      });
  }

  onEdit = (entity) => document.location.href = `./event/${entity.id}`

  onRemove = (entity) => onConfirmAlert(this.props.translate, {
      message: `${this.props.translate("event-delete")} '${entity.title}' ` +
        `${this.props.translate("event")}?`,
      onConfirm: () => removeEvent(entity, this.onSuccess, this.props.onError)
  });

  onSuccess = (response, nameMessage) => {
      onSuccessAlert(response, this.props.translate(nameMessage));
      this.componentDidMount();
  }

  onUnsubscribe = (entity) => onConfirmAlert(this.props.translate, {
      message: `${this.props.translate("event-unsubscribe")} '${entity.title}' ` +
        `${this.props.translate("event2")}?`,
      onConfirm: () => unsubscribeEvent(entity, this.onSuccess, this.props.onError)
  });

  componentDidMount() {
      $.get(templateAjaxGet("/event/created", (response) => {
          const createdEvents = response;
          $.get(templateAjaxGet("/event/subscribed", (response) => {
              const subscribedEvents = response;
              $.get(templateAjaxGet("/event/all-created", (response) => {
                  this.setState({
                      createdEvents: createdEvents,
                      filteredCreatedEvents: createdEvents,
                      subscribedEvents: subscribedEvents,
                      filteredSubscribedEvents: subscribedEvents,
                      events: response,
                      filteredEvents: response,
                  });
              }), (xhr) => this.props.onError(xhr, this.props.translate("cannot-retrieve-events-yours-places")));
          }), (xhr) => this.props.onError(xhr, this.props.translate("cannot-retrieve-subscribed-events")));
      }), (xhr) => this.props.onError(xhr, this.props.translate("cannot-retrieve-created-events")));
  }

  render() {
      return <Fragment>
          <SearchBar onChange={ this.search } onRequestSearch={ this.search } style={ {
              marginBottom: 20
          } }
          />
          <ListTable title={ this.props.translate("created-events-history") }
              events={ this.state.filteredCreatedEvents } translate={ this.props.translate }
              sortBy={ (sortTerm, func) => this.sortBy("filteredCreatedEvents", sortTerm, func) }
              onEdit={ this.onEdit } onCancel={ this.onRemove } isDelete={ true }
          />
          <ListTableWrapper title={ this.props.translate("subscribed-events-history") }
              events={ this.state.filteredSubscribedEvents } translate={ this.props.translate }
              sortBy={ (sortTerm, func) => this.sortBy("filteredSubscribedEvents", sortTerm, func) }
              onCancel={ this.onUnsubscribe }
          />
          <ListTableWrapper title={ this.props.translate("events-history") }
              events={ this.state.filteredEvents } translate={ this.props.translate }
              sortBy={ (sortTerm, func) => this.sortBy("filteredEvents", sortTerm, func) }
              isContact={ true }
          />
      </Fragment>;
  }
}

const ListTableWrapper = (props) => (isNotEmpty(props.events)) ?
    <ListTable { ...props }/> : <div />;

ListTableWrapper.propTypes = {
    translate: PropTypes.func.isRequired,
    title: PropTypes.string.isRequired,
    events: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.object,
        PropTypes.array
    ]),
    sortBy: PropTypes.func.isRequired,
    onCancel: PropTypes.func,
    isContact: PropTypes.bool
};

History.propTypes = {
    translate: PropTypes.func.isRequired,
    onError: PropTypes.func.isRequired
};

export default History;
