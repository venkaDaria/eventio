import React, { Component } from "react";
import PropTypes from "prop-types";
import Geosuggest from "react-geosuggest";
import {css} from "aphrodite";

import LoadImage from "../image/LoadImage";

import {onSave, save, add, getStart, getEnd} from "../../../utils/react/saver";
import {handleChange, handleSimple} from "../../../utils/react/state";
import {hideEdit, clearImage} from "../../../utils/image";
import {loadImage, loadTemplate} from "../../../utils/image/file";

import stylesFlex from "../../../../styles/flex";
import stylesLink from "../../../../styles/link";

const initialState = {
    name: "",
    realAddress: "",
    timeWork: "",
    image: "",
    svgChanged: false
};

class SavePlace extends Component {
  state = initialState

  static getDerivedStateFromProps(nextProps) {
      return nextProps.entity || initialState;
  }

  onChangeAddress = (e) => handleSimple(this, e.label ? e.label : e, "realAddress");

  onChangeStart = (e) =>
      this.onChangeTime(e.target.value, getEnd(this.state.timeWork))

  onChangeEnd = (e) =>
      this.onChangeTime(getStart(this.state.timeWork), e.target.value)

  onChangeTime = (start, end) => handleSimple(this, start + "-" + end, "timeWork");

  onSave = (args) => (this.props.entity ? save : add)(...args);

  onClick = (entity) => onSave(entity, this.onSave, (resp, nameMessage) => {
      this.props.onSuccess(resp, nameMessage);
      this.setState(initialState);
      hideEdit(this);
  }, this.onErrorWithTranslate);

  onErrorWithTranslate = (xhr, nameMessage) => this.props.onError(xhr, this.props.translate(nameMessage));

  render() {
      return <div className={ `${css(stylesFlex.bordered)} form-group` }>
          <div>
              <label htmlFor="name">{this.props.translate("name")}:</label>
              <input className="form-control" value={ this.state.name } name="name"
                  onChange={ (e) => handleChange(this, e) }
              />
              <br/>
              <label>{this.props.translate("real-address")}:</label>
              <Geosuggest inputClassName="form-control"
                  onChange={ this.onChangeAddress }
                  onSuggestSelect={ this.onChangeAddress }
                  initialValue={ this.state.realAddress }
              />
          </div>
          <hr/>

          <div className={ `${css(stylesFlex.container)} w-75` }>
              <div>
                  <label>{this.props.translate("time-work")}:</label>
                  <div className={ `${css(stylesFlex.container)} w-50` }>
                      <div>
                          <label>{this.props.translate("from")}</label>
                          <input className="form-control" type="time"
                              onChange={ this.onChangeStart }
                          />
                      </div>
                  &nbsp;
                      <div>
                          <label>{this.props.translate("to")}</label>
                          <input className="form-control" type="time"
                              onChange={ this.onChangeEnd }
                          />
                      </div>
                  </div>
              </div>
              <div>
                  <label>{this.props.translate("image")}:</label>
                  <LoadImage data={ this.state.image }
                      onChange={ () => {
                          this.setState({svgChanged: true});
                          loadImage(this);
                      } } onClear={ this.state.image ? () => {
                          this.setState({svgChanged: true});
                          clearImage(this);
                      } : null } translate={ this.props.translate }
                  />
              </div>
          </div>
          <br/>
          <input type="button" className="btn btn-success"
              onClick={ () => this.onClick(this.state) }
              value={ this.props.translate("save") }
          />
          &nbsp;&nbsp;&nbsp;
          <a className={ css(stylesLink.default) }
              onClick={ () => loadTemplate(require("../../../../assets/stub.svg")) }>
              {this.props.translate("download-svg")}
          </a>
      </div>;
  }
}

SavePlace.propTypes = {
    onError: PropTypes.func.isRequired,
    onSuccess: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired,
    entity: PropTypes.object
};

export default SavePlace;
