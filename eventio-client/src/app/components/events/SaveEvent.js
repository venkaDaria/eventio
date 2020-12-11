import React, {Component} from "react";
import PropTypes from "prop-types";
import { Link } from "react-router";

import {css} from "aphrodite";
import moment from "moment";
import $ from "jquery";

import PlaceSuggest from "./PlaceSuggest";
import LoadImage from "../places/image/LoadImage";
import RoomInfo from "../places/room/RoomInfo";
import CustomDatePicker from "../utils/CustomDatePicker";

import {validate} from "../../utils/react/saver";
import {getStatus, Status} from "../../utils/api/status";
import {handleChange, handleSimple, handleChecked} from "../../utils/react/state";
import {getDate, changeHref} from "../../utils/core";
import {onSuccessAlert} from "../../utils/react/alert";
import {clearImage} from "../../utils/image";
import {loadImage} from "../../utils/image/file";
import {templateAjaxGet} from "../../utils/api";
import * as storage from "../../utils/core/storage";

import stylesFlex  from "../../../styles/flex";
import stylesLink from "../../../styles/link";

class SaveEvent extends Component {
  state = {
      id: "",
      title: "",
      description: "",
      image: "",
      start: moment(),
      end: undefined,
      mode: "LINK",
      location: "",
      isOwner: false,
      onlyMine: false,
      rendered: false
  }

  RadioButton = ({ value, label }) => (
      <div className="radio">
          <input type="radio" id={ value } name="mode" className="form-control-input"
              value={ value } checked={ this.state.mode === value }
              onChange={ (e) => handleChecked(this, e) } />
          &nbsp;
          <label className="form-control-label" htmlFor={ value }>
              { label }
          </label>
      </div>
  );

  save = () => {
      if (!validate(this.state.title, this.props.onError, this.props.translate("invalid-title")))
          return;
      if (!validate(this.state.location, this.props.onError, this.props.translate("invalid-location")))
          return;

      const onSuccess = (response, nameMessage) => {
          storage.clearData("slotInfo");
          storage.clearData("roomData");

          changeHref("/stats", () => this.props.go("3"));

          onSuccessAlert(response, this.props.translate(nameMessage));
      };

      this.props.onClick(this.state, onSuccess, (xhr, message) =>
          this.props.onError(xhr, this.props.translate(message)));
  }

  placeSuggest = () => {
      storage.setData("eventData", this.state);
      document.location.href = "/places";
  }

  onClear = (name) => {
      storage.clearData(name);
      this.setState({location: ""});
  }

  setStateWithOwner = (response) => getStatus(response, (answer) => this.setState({...response,
      start: getDate(response.start),
      end: getDate(response.end),
      isOwner: answer === Status.created
  }), (xhr) => this.props.onError(xhr, this.props.translate("cannot-retrieve-owner")));

  setStateBinded = (state) => this.setState({
      ...state,
      start: getDate(state.start),
      end: getDate(state.end)
  });

  createLoad = () => {
      storage.saveEvent(this.setStateBinded);
      storage.saveRoom(this.setStateBinded);
      storage.saveDateTime(this.setStateBinded);
  }

  editLoad = () => $.get(templateAjaxGet(
      `/event/${this.props.id}`,
      (response) => this.setStateWithOwner(response),
      (xhr) => this.props.onError(xhr, this.props.translate("cannot-retrieve-entities")))
  );

  checkRender = () => {
      if (!this.state.rendered) {
          (this.props.isEdit) ? this.editLoad() : this.createLoad();
          this.setState({ rendered: true });
      }
  }

  render() {
      this.checkRender();

      return <div className={ `${css(stylesFlex.bordered)} form-group` }>
          <div>
              <label htmlFor="title">{this.props.translate("title")}</label><br/>
              <input className="form-control" value={ this.state.title }
                  onChange={ (e) => handleChange(this, e) } name="title"
              />
          </div>
          <div>
              <label htmlFor="description">{this.props.translate("description")}</label><br/>
              <textarea className="form-control" value={ this.state.description }
                  onChange={ (e) => handleChange(this, e) } name="description"
              />
          </div>
          <br/>
          <div className="form-check">
              <this.RadioButton value="PUBLIC" label={ this.props.translate("is-public") } />
              <this.RadioButton value="LINK" label={ this.props.translate("is-link") } />
              <this.RadioButton value="PRIVATE" label={ this.props.translate("is-private") } />
          </div>
          <hr/>

          <div className={ `${css(stylesFlex.justFlex)}` }>
              <div>
                  <label>{this.props.translate("date-time")}:</label>
                  <div className={ css(stylesFlex.container) }>
                      <div>
                          <label>{this.props.translate("from")}</label>
                          <CustomDatePicker hidden={ false } disabled={ !storage.noData("slotInfo") }
                              onChange={ (e) => handleSimple(this, e, "start") } translate={ this.props.translate }
                              value={ this.state.start } endDate={ this.state.end }
                          />
                      </div>
                  &nbsp;
                      <div>
                          <label>{this.props.translate("to")}</label>
                          <CustomDatePicker hidden={ false } disabled={ !storage.noData("slotInfo") }
                              onChange={ (e) => handleSimple(this, e, "end") } translate={ this.props.translate }
                              value={ this.state.end } startDate={ this.state.start }
                          />
                      </div>
                  </div>
              </div>
             &nbsp;&nbsp;&nbsp;
              <div>
                  <label>{this.props.translate("image")}:</label>
                  <LoadImage
                      onChange={ (e) => loadImage(this, e) }
                      onClear={ this.state.image ? () => clearImage(this) : null }
                      translate={ this.props.translate }
                      className={ this.state.image ? css(stylesFlex.backWhite) : "" }
                  />
              </div>
          </div>
          <hr/>

          <RoomInfo translate={ this.props.translate } clear={ this.onClear }
              roomData={ this.state.location }/>
          <hr />

          <PlaceSuggest start={ this.state.start } end={ this.state.end }
              onClick={ this.placeSuggest } translate={ this.props.translate }
          />
          <div className="form-check" hidden={ !this.props.isCompany }>
              <input className="form-check-input" type="checkbox" id="onlyMine"
                  name="onlyMine" onChange={ (e) => handleChange(this, e) }/>
              <label className="form-check-label" htmlFor="onlyMine">
                  {this.props.translate("only-my-places")}
              </label>
          </div>
          <br/><br/>

          <input type="button" className="btn btn-success" onClick={ this.save }
              value="Save"
          />
          &nbsp;&nbsp;&nbsp;
          <Link to="/companies" className={ css(stylesLink.default) }>
              {this.props.translate("go-to-companies")}
          </Link>
      </div>;
  }
}

SaveEvent.propTypes = {
    onClick: PropTypes.func.isRequired,
    onError: PropTypes.func.isRequired,
    go: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired,
    isCompany:  PropTypes.bool.isRequired,
    isEdit: PropTypes.bool,
    id: PropTypes.string
};

export default SaveEvent;
