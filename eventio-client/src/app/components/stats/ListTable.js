import React, {Fragment} from "react";
import PropTypes from "prop-types";
import {css} from "aphrodite";

import hash from "object-hash";
import _ from "lodash";

import Row from "./Row";
import { sortByKeys, sortByValues } from "../../utils/core/collection";

import stylesLink from "../../../styles/link";

const ListTable = ({title, events, sortBy, translate, onEdit, onCancel, isContact, isDelete}) => (
    <Fragment>
        <h4>{title}</h4>
        <h6>{translate("sorted-by")}:&nbsp;
            <a className={ css(stylesLink.default) } onClick={ () => sortBy(sortByKeys) }>
                {translate("title2")}</a>,&nbsp;
            <a className={ css(stylesLink.default) } onClick={ () => sortBy(sortByValues) }>
                {translate("count2")}</a>&nbsp;
            (<a className={ css(stylesLink.default) } onClick={ () => sortBy(null) }>
                {translate("reset-sorting")}</a>)
        </h6>
        <div>
            <table className="table table-hover table-bordered">
                <thead>
                    <tr>
                        <td>{translate("title")}</td>
                        <td>{translate("count")}</td>
                        <td hidden={ !onEdit }>{translate("edit")}</td>
                        <td>{isDelete? translate("delete") : translate("unsubscribe")}</td>
                    </tr>
                </thead>
                <tbody>
                    {
                        _.map(events, (value, key) => {
                            const json = JSON.parse(key);
                            return <Row key={ hash(json) } count={ value } entity={ json } translate={ translate }
                                onEdit={ onEdit } onCancel={ onCancel } isContact={ isContact } isDelete={ isDelete }
                            />;
                        })
                    }
                </tbody>
            </table>
        </div>
    </Fragment>
);

ListTable.propTypes = {
    translate: PropTypes.func.isRequired,
    title: PropTypes.string.isRequired,
    events: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.object,
        PropTypes.array
    ]),
    sortBy: PropTypes.func.isRequired,
    onEdit: PropTypes.func,
    onCancel: PropTypes.func,
    isContact: PropTypes.bool,
    isDelete: PropTypes.bool,
};

export default ListTable;
