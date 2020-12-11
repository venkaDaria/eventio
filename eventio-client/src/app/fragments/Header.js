import React from "react";
import {Router} from "react-router";

import {history, routesHeader} from "../../index";

const Header = () => (
    <Router history={ history }>
        {routesHeader}
    </Router>
);

Header.propTypes = {
};

export default Header;
