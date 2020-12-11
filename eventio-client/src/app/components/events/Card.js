import React, { Component } from "react";
import PropTypes from "prop-types";
import { Link } from "react-router";

import { Card, CardActions, CardHeader, CardMedia, CardTitle, CardText } from "material-ui/Card";
import FlatButton from "material-ui/FlatButton";
import FontIcon from "material-ui/FontIcon";

import { css } from "aphrodite";

import { getStatus, getPlaceHref, Status } from "../../utils/api/status";
import { baseUrl } from "../../utils/core";
import { onSuccessAlert } from "../../utils/react/alert";
import { subscribeEvent, unsubscribeEvent, removeEvent } from "../../utils/api/events";
import { options } from "../../utils/image";

import styles from "../../../styles/card";

class CardElement extends Component {
  state = {
      status: "",
      link: ""
  };

  onSuccess = (response, nameMessage) =>
      onSuccessAlert(response, this.props.translate(nameMessage));

  subscribe = () => subscribeEvent(this.props.entity, this.onSuccess, this.props.onError);

  unsubscribe = () => unsubscribeEvent(this.props.entity, this.onSuccess, this.props.onError);

  remove = () => removeEvent(this.props.entity, this.onSuccess, this.props.onError);

  isOwner = () => this.state.status === Status.created;

  isChosen = () => this.isOwner() || this.state.status === Status.subscribed;

  isNotChosen = () => !this.isChosen();

  componentDidMount() {
      getStatus(this.props.entity, (response) => this.setState({status: response}),
          (xhr) => this.props.onError(xhr, this.props.translate("cannot-retrieve-owner")));
      getPlaceHref(this.props.entity.location.id, (response) => this.setState({link: response}),
          (xhr) => this.props.onError(xhr, this.props.translate("cannot-retrieve-place")));
  }

  render() {
      return <Card className={ css(styles.card, this.props.entity.mode === "PUBLIC" && styles.isPublic) }>
          <CardHeader
              title={ this.props.entity.title }
              subtitle={ `${this.props.entity.start} - ${this.props.entity.end != null ?
                  this.props.entity.end : "?"}` }
              showExpandableButton={ true }
          />
          <CardMedia expandable={ true }
              overlay={ <CardTitle title={ this.props.entity.title } /> }>
              <img alt={ this.props.entity.title } src={
                  this.props.entity.image || options.defaultImage
              }
              />
          </CardMedia>
          <CardText className={ this.state.link ? "" : css(styles.hidden) }>
              <Link to={ `${this.state.link}` }>
                  {this.props.entity.location.name}
              </Link>
              <br/><br/>
              Invitation link:&nbsp;
              <Link to={ `${baseUrl}/event/${this.props.entity.id}` }>
                  {`${baseUrl}/event/${this.props.entity.id}`}
              </Link>
          </CardText>
          <CardText expandable={ true }>
              {this.props.entity.description}
          </CardText>
          <CardActions className={ this.state.link ? "" : css(styles.hidden) }>
              <FlatButton onClick={ this.subscribe }
                  className={ this.isChosen() ? css(styles.hidden) : "" }
                  primary={ true }
                  label={ this.props.translate("subscribe") }
              />
              <FlatButton onClick={ this.subscribe }
                  className={ this.isNotChosen() ? css(styles.hidden) : "" }
                  backgroundColor={ this.isOwner() ? "#17a2b8" : "#a4c639" }
                  icon={ <FontIcon className="fas fa-check-circle" /> }
                  label={ this.isOwner() ? this.props.translate("created") : this.props.translate("subscribed") }
                  disabled={ true }
              />
              <FlatButton onClick={ this.isOwner() ? this.remove : this.unsubscribe }
                  className={ this.isNotChosen() ? css(styles.hidden) : "" }
                  secondary={ true }
                  label={ this.isOwner() ? this.props.translate("delete") : this.props.translate("unsubscribe") }
              />
          </CardActions>
      </Card>;
  }
}

CardElement.propTypes = {
    entity: PropTypes.object.isRequired,
    translate: PropTypes.func.isRequired,
    onError: PropTypes.func.isRequired,
};

export default CardElement;
