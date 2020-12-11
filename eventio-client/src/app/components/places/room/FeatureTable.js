import React from "react";
import PropTypes from "prop-types";

import hash from "object-hash";

const FeatureTable = ({ features, onDelete }) => (
    <ul>
        {
            features.map((feature) =>
                <li key={ hash(feature) }>
                    { feature.name }&nbsp;&nbsp;
                    <a onClick={ () => onDelete(feature.id) }>
                        <span className="fas fa-times"/>
                    </a>
                </li>
            )
        }
    </ul>
);


FeatureTable.propTypes = {
    features: PropTypes.array.isRequired,
    onDelete: PropTypes.func.isRequired
};

export default FeatureTable;
