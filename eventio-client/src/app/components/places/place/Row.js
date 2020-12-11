import React, {Component, Fragment} from "react";
import PropTypes from "prop-types";

import $ from "jquery";

import LoadImage from "../image/LoadImage";

import {handleChange} from "../../../utils/react/state";
import {hideEdit, showEdit} from "../../../utils/image";
import {loadImage} from "../../../utils/image/file";

class Row extends Component {
  state = {
      name: this.props.place.name,
      realAddress: this.props.place.realAddress,
      timeWork: this.props.place.timeWork,
      image: this.props.place.image,
      id: this.props.place.id,
      hidden: true,
      svgChanged: false
  }

  edit = () => {
      showEdit();
      this.props.onEdit(this.state);
  }

  cancel = () => {
      hideEdit(this);
      this.props.clearForm();
  }

  onClickId = () => document.location.href = `/profile/places/${this.state.id}`;

  onClear = () => {
      $("img")[this.props.index + 1].src = "";
      $($("input[type=file]")[this.props.index + 1]).val("");

      this.setState({ svgChanged: true, image: "" });
  }

  onShow = () => {
      const preview = $("img");

      if (preview) {
          preview[this.props.index + 1].src = this.state.image;
          this.setState({hidden: !this.state.hidden});
      }
  }

  onChangeImage = (e) => loadImage(this, e, this.props.index + 1);

  render() {
      return <Fragment>
          <tr>
              <td name="name" onClick={ this.onClickId }
                  onChange={ (e) => handleChange(this, e) }>
                  {this.state.name}
              </td>
              <td name="realAddress" onClick={ this.onClickId }
                  onChange={ (e) => handleChange(this, e) }>
                  {this.state.realAddress}
              </td>
              <td name="timeWork" onClick={ this.onClickId }
                  onChange={ (e) => handleChange(this, e) }>
                  {this.state.timeWork}
              </td>
              <td onClick={ this.onShow }>
                  <span className="fas fa-eye"/>
              </td>
              <td onClick={ this.edit } id="edit">
                  <span className="fa fa-pencil-alt" />
              </td>
              <td onClick={ this.cancel } id="cancel" hidden>
                  <span className="fas fa-times" />
              </td>
              <td onClick={ () => this.props.delete(this.state) }>
                  <span className="fas fa-trash" />
              </td>
          </tr>
          <tr hidden={ this.state.hidden }>
              <td>
                  <LoadImage onChange={ this.onChangeImage } translate={ this.props.translate }
                      data={ this.state.image } onClear={ this.state.image ? this.onClear : null }
                  />
              </td>
          </tr>
      </Fragment>;
  }
}

Row.propTypes = {
    index: PropTypes.number.isRequired,
    place: PropTypes.object.isRequired,
    save: PropTypes.func.isRequired,
    delete: PropTypes.func.isRequired,
    onEdit: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired,
    clearForm:  PropTypes.func.isRequired,
};

export default Row;
